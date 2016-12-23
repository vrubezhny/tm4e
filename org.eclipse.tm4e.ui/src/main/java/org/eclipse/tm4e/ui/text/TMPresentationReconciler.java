/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.ui.text;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tm4e.core.TMCorePlugin;
import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.core.model.IModelTokensChangedListener;
import org.eclipse.tm4e.core.model.ITMModel;
import org.eclipse.tm4e.core.model.ModelTokensChangedEvent;
import org.eclipse.tm4e.core.model.Range;
import org.eclipse.tm4e.core.model.TMToken;
import org.eclipse.tm4e.ui.TMUIPlugin;
import org.eclipse.tm4e.ui.internal.model.DocumentHelper;
import org.eclipse.tm4e.ui.internal.model.TMModel;
import org.eclipse.tm4e.ui.internal.themes.ThemeManager;
import org.eclipse.tm4e.ui.model.ITMModelManager;
import org.eclipse.tm4e.ui.themes.ITokenProvider;

/**
 * TextMate presentation reconcilier which must be initialized with:
 * 
 * <ul>
 * <li>a TextMate grammar {@link IGrammar} used to initialize the TextMate model
 * {@link ITMModel}.</li>
 * <li>a token provider {@link ITokenProvider} to retrieve the {@link IToken}
 * from a TextMate token type .</li>
 * </ul>
 * 
 */
public class TMPresentationReconciler implements IPresentationReconciler {

	/**
	 * The default text attribute if non is returned as data by the current
	 * token
	 */
	protected Token defaultToken = new Token(null);

	/** The target viewer. */
	private ITextViewer viewer;
	/** The internal listener. */
	private InternalListener internalListener = new InternalListener();
	private IGrammar grammar;
	private ITokenProvider tokenProvider;

	private TextAttribute fDefaultTextAttribute = new TextAttribute(null);

	private IPreferenceChangeListener e4CSSThemeChangeListener;

	/**
	 * Listener to recolorize editors when E4 Theme from General / Appearance
	 * preferences changed.
	 *
	 */
	private class E4CSSThemeChangeListener implements IPreferenceChangeListener {

		@Override
		public void preferenceChange(PreferenceChangeEvent event) {
			if (ThemeManager.E4_THEME_ID.equals(event.getKey())) {
				IDocument document = viewer.getDocument();
				if (document == null) {
					return;
				}
				ITokenProvider oldTheme = tokenProvider;
				// Select the well TextMate theme from the given E4 theme id.
				ITokenProvider newTheme = ThemeManager.getInstance()
						.getThemeForE4Theme(event.getNewValue() != null ? event.getNewValue().toString() : null);
				if (!newTheme.equals(oldTheme)) {
					// Theme has changed, recolorize
					tokenProvider = newTheme;
					ITMModel model = getTMModelManager().connect(document);
					colorize(0, document.getNumberOfLines() - 1, null, (TMModel) model);
				}
			}
		}
	};

	/**
	 * Internal listener class.
	 */
	class InternalListener implements ITextInputListener, IModelTokensChangedListener, ITextListener {

		@Override
		public void inputDocumentAboutToBeChanged(IDocument oldDocument, IDocument newDocument) {
			if (oldDocument != null) {
				viewer.removeTextListener(this);
				getTMModelManager().disconnect(oldDocument);
			}
		}

		@Override
		public void inputDocumentChanged(IDocument oldDocument, IDocument newDocument) {
			if (newDocument != null) {
				// Connect a TextModel to the new document.
				ITMModel model = getTMModelManager().connect(newDocument);
				try {
					viewer.addTextListener(this);
					// Update theme + grammar
					IContentType[] contentTypes = DocumentHelper.getContentTypes(newDocument);
					updateTokenProvider(contentTypes);
					model.setGrammar(getGrammar(contentTypes));
					// Add model listener
					model.addModelTokensChangedListener(this);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void textChanged(TextEvent e) {
			if (!e.getViewerRedrawState()) {
				return;
			}
			// textChanged is called in 2 cases:
			// 1) when the content of the document has changed. In this case,
			// colorization must not be done here. The colorization is done with
			// modelTokensChanged listener.
			// 2) when TextViewer#invalidateTextPresentation is called (because
			// of folding, etc)
			if (e.getDocumentEvent() != null) {
				// case 1), ignore colorization
				return;
			}

			// case 2), do the colorization.
			IDocument document = viewer.getDocument();
			if (document != null) {
				IRegion region = null;
				int fromLineNumber = -1;
				int toLineNumber = -1;
				if (e.getOffset() == 0 && e.getLength() == 0 && e.getText() == null) {
					// redraw state change, damage the whole document
					fromLineNumber = 0;
					toLineNumber = document.getNumberOfLines() - 1;
				} else {
					region = widgetRegion2ModelRegion(e);
					if (region != null) {
						try {
							String text = document.get(region.getOffset(), region.getLength());
							DocumentEvent de = new DocumentEvent(document, region.getOffset(), region.getLength(),
									text);
							fromLineNumber = DocumentHelper.getStartLine(de);
							toLineNumber = DocumentHelper.getEndLine(de, false);
						} catch (BadLocationException x) {
						}
					}
				}
				ITMModel model = getTMModelManager().connect(document);
				colorize(fromLineNumber, toLineNumber, region, (TMModel) model);

			}
		}

		/**
		 * Translates the given text event into the corresponding range of the
		 * viewer's document.
		 * 
		 * @param e
		 *            the text event
		 * @return the widget region corresponding the region of the given event
		 *         or <code>null</code> if none
		 * @since 2.1
		 */
		private IRegion widgetRegion2ModelRegion(TextEvent e) {

			String text = e.getText();
			int length = text == null ? 0 : text.length();

			if (viewer instanceof ITextViewerExtension5) {
				ITextViewerExtension5 extension = (ITextViewerExtension5) viewer;
				return extension.widgetRange2ModelRange(new Region(e.getOffset(), length));
			}

			IRegion visible = viewer.getVisibleRegion();
			IRegion region = new Region(e.getOffset() + visible.getOffset(), length);
			return region;
		}

		private IGrammar getGrammar(IContentType[] contentTypes) throws CoreException {
			if (grammar != null) {
				return grammar;
			}
			// Discover the well grammar from the contentTypes
			return TMCorePlugin.getGrammarRegistryManager().getGrammarFor(contentTypes);
		}

		private void updateTokenProvider(IContentType[] contentTypes) throws CoreException {
			if (tokenProvider == null) {
				tokenProvider = TMUIPlugin.getThemeManager().getThemeFor(contentTypes);
			}
		}

		@Override
		public void modelTokensChanged(ModelTokensChangedEvent e) {
			Control control = viewer.getTextWidget();
			if (control != null) {
				control.getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (viewer != null)
							colorize(e);
					}
				});
			}
		}
	}

	public void setGrammar(IGrammar grammar) {
		this.grammar = grammar;
	}

	public IGrammar getGrammar() {
		return grammar;
	}

	public ITokenProvider getTokenProvider() {
		return tokenProvider;
	}

	public void setTokenProvider(ITokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void install(ITextViewer viewer) {
		Assert.isNotNull(viewer);

		this.viewer = viewer;
		viewer.addTextInputListener(internalListener);

		IDocument document = viewer.getDocument();
		if (document != null) {
			internalListener.inputDocumentChanged(null, document);
		}
		IEclipsePreferences preferences = ThemeManager.getInstance().getPreferenceE4CSSTheme();
		if (preferences != null) {
			e4CSSThemeChangeListener = new E4CSSThemeChangeListener();
			preferences.addPreferenceChangeListener(e4CSSThemeChangeListener);
		}
	}

	@Override
	public void uninstall() {
		viewer.removeTextInputListener(internalListener);
		// Ensure we uninstall all listeners
		internalListener.inputDocumentAboutToBeChanged(viewer.getDocument(), null);
		getTMModelManager().disconnect(viewer.getDocument());
		if (e4CSSThemeChangeListener != null) {
			ThemeManager.getInstance().getPreferenceE4CSSTheme()
					.removePreferenceChangeListener(e4CSSThemeChangeListener);
		}
		e4CSSThemeChangeListener = null;
	}

	@Override
	public IPresentationDamager getDamager(String contentType) {
		return null;
	}

	@Override
	public IPresentationRepairer getRepairer(String contentType) {
		return null;
	}

	private ITMModelManager getTMModelManager() {
		return TMUIPlugin.getTMModelManager();
	}

	private void colorize(ModelTokensChangedEvent e) {
		for (Range range : e.getRanges()) {
			colorize(range.fromLineNumber - 1, range.toLineNumber - 1, null, ((TMModel) e.getModel()));
		}
	}

	private void colorize(int fromLineNumber, int toLineNumber, IRegion damage, TMModel model) {
		// Refresh the UI Presentation
		System.err.println("Render from: " + fromLineNumber + " to: " + toLineNumber);
		try {
			IDocument document = model.getDocument();
			TextPresentation presentation = new TextPresentation(
					damage != null ? damage : DocumentHelper.getRegion(document, fromLineNumber, toLineNumber), 1000);

			int lastStart = presentation.getExtent().getOffset();
			int length = 0;
			boolean firstToken = true;
			IToken lastToken = Token.UNDEFINED;
			TextAttribute lastAttribute = getTokenTextAttribute(lastToken);

			List<TMToken> tokens = null;
			for (int line = fromLineNumber; line <= toLineNumber; line++) {
				tokens = model.getLineTokens(line);
				int i = 0;
				int startLineOffset = document.getLineOffset(line);
				for (TMToken t : tokens) {
					if (damage != null) {
						// Damage region is setted (this case comes from when
						// hyperlink, occurences, folding are processed and call
						// TextViewer#invalidateTextPresentation)
						if (isBeforeRegion(t, startLineOffset, damage)) {
							// The token is before the damage region, ignore it
							i++;
							continue;
						} else if (isAfterRegion(t, startLineOffset, damage)) {
							// The token is after the damage region, stop the
							// colorization process
							break;
						}
					}
					IToken token = toToken(t);
					TextAttribute attribute = getTokenTextAttribute(token);
					if (lastAttribute != null && lastAttribute.equals(attribute)) {
						length += getTokenLengh(t.startIndex, tokens, i, line, document);
						firstToken = false;
					} else {
						if (!firstToken)
							addRange(presentation, lastStart, length, lastAttribute);
						firstToken = false;
						lastToken = token;
						lastAttribute = attribute;
						lastStart = t.startIndex + startLineOffset;
						length = getTokenLengh(t.startIndex, tokens, i, line, document);
					}
					i++;
				}
			}

			addRange(presentation, lastStart, length, lastAttribute);
			applyTextRegionCollection(presentation);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return true if the given token is before the given region and false
	 * otherwise.
	 * 
	 * @param token
	 * @param startLineOffset
	 * @param damage
	 * @return
	 */
	private boolean isBeforeRegion(TMToken token, int startLineOffset, IRegion damage) {
		return token.startIndex + startLineOffset < damage.getOffset();
	}

	/**
	 * Return true if the given token is after the given region and false
	 * otherwise.
	 * 
	 * @param t
	 * @param startLineOffset
	 * @param damage
	 * @return
	 */
	private boolean isAfterRegion(TMToken t, int startLineOffset, IRegion damage) {
		return t.startIndex + startLineOffset >= damage.getOffset() + damage.getLength();
	}

	private IToken toToken(TMToken t) {
		IToken token = getTokenProvider().getToken(t.type);
		if (token != null) {
			return token;
		}
		return defaultToken;
	}

	private int getTokenLengh(int startOffset, List<TMToken> tokens, int i, int line, IDocument document)
			throws BadLocationException {
		TMToken next = (i + 1 < tokens.size()) ? tokens.get(i + 1) : null;
		if (next != null) {
			return next.startIndex - startOffset;
		}
		return document.getLineLength(line) - startOffset;
	}

	/**
	 * Returns a text attribute encoded in the given token. If the token's data
	 * is not <code>null</code> and a text attribute it is assumed that it is
	 * the encoded text attribute. It returns the default text attribute if
	 * there is no encoded text attribute found.
	 *
	 * @param token
	 *            the token whose text attribute is to be determined
	 * @return the token's text attribute
	 */
	protected TextAttribute getTokenTextAttribute(IToken token) {
		Object data = token.getData();
		if (data instanceof TextAttribute)
			return (TextAttribute) data;
		return fDefaultTextAttribute;
	}

	/**
	 * Adds style information to the given text presentation.
	 *
	 * @param presentation
	 *            the text presentation to be extended
	 * @param offset
	 *            the offset of the range to be styled
	 * @param length
	 *            the length of the range to be styled
	 * @param attr
	 *            the attribute describing the style of the range to be styled
	 * @param lastLineStyleRanges
	 */
	protected void addRange(TextPresentation presentation, int offset, int length, TextAttribute attr) {
		if (attr != null) {
			int style = attr.getStyle();
			int fontStyle = style & (SWT.ITALIC | SWT.BOLD | SWT.NORMAL);
			StyleRange styleRange = new StyleRange(offset, length, attr.getForeground(), attr.getBackground(),
					fontStyle);
			styleRange.strikeout = (style & TextAttribute.STRIKETHROUGH) != 0;
			styleRange.underline = (style & TextAttribute.UNDERLINE) != 0;
			styleRange.font = attr.getFont();
			presentation.addStyleRange(styleRange);
		}
	}

	/**
	 * Applies the given text presentation to the text viewer the presentation
	 * reconciler is installed on.
	 *
	 * @param presentation
	 *            the text presentation to be applied to the text viewer
	 */
	private void applyTextRegionCollection(TextPresentation presentation) {
		viewer.changeTextPresentation(presentation, false);
	}
}
