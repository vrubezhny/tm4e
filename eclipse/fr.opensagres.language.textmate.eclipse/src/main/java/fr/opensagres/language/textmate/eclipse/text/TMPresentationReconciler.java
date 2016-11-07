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
package fr.opensagres.language.textmate.eclipse.text;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Display;

import fr.opensagres.language.textmate.core.grammar.IGrammar;
import fr.opensagres.language.textmate.core.model.IModelTokensChangedListener;
import fr.opensagres.language.textmate.core.model.ITMModel;
import fr.opensagres.language.textmate.core.model.TMToken;
import fr.opensagres.language.textmate.eclipse.TMPlugin;
import fr.opensagres.language.textmate.eclipse.internal.model.DocumentHelper;
import fr.opensagres.language.textmate.eclipse.internal.model.TMModel;
import fr.opensagres.language.textmate.eclipse.model.ITMModelManager;
import fr.opensagres.language.textmate.eclipse.text.styles.ITokenProvider;

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

	/**
	 * Internal listener class.
	 */
	class InternalListener implements ITextInputListener, IModelTokensChangedListener {

		// @Override
		// public void textChanged(TextEvent event) {
		// System.err.println(event);
		// }

		@Override
		public void inputDocumentAboutToBeChanged(IDocument oldDocument, IDocument newDocument) {
			if (oldDocument != null) {
				getTMModelManager().disconnect(oldDocument);
				// try {

				// viewer.removeTextListener(this);
				// oldDocument.removeDocumentListener(this);
				// oldDocument.removeDocumentPartitioningListener(this);

				// oldDocument.removePositionUpdater(fPositionUpdater);
				// oldDocument.removePositionCategory(fPositionCategory);

				// } catch (BadPositionCategoryException x) {
				// // should not happened for former input documents;
				// }
			}
		}

		/*
		 * @see ITextInputListener#inputDocumenChanged(IDocument, IDocument)
		 */
		@Override
		public void inputDocumentChanged(IDocument oldDocument, IDocument newDocument) {
			if (newDocument != null) {

				ITMModel model = getTMModelManager().connect(newDocument);
				model.setGrammar(TMPresentationReconciler.this.grammar);
				model.addModelTokensChangedListener(this);
				//
				// newDocument.addPositionCategory(fPositionCategory);
				// newDocument.addPositionUpdater(fPositionUpdater);

				// newDocument.addDocumentPartitioningListener(this);
				// newDocument.addDocumentListener(this);
				// viewer.addTextListener(this);

				// setDocumentToDamagers(newDocument);
				// setDocumentToRepairers(newDocument);
				// processDamage(new Region(0, newDocument.getLength()),
				// newDocument);
			}
		}

		@Override
		public void modelTokensChanged(final int fromLineNumber, final int toLineNumber, final ITMModel model) {

			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					// viewer.setEditable(false);
					colorize(fromLineNumber, toLineNumber, model);
					// fViewer.setEditable(true);
				}
			});

		}
	}

	public IGrammar getGrammar() {
		return grammar;
	}

	public void setGrammar(IGrammar grammar) {
		this.grammar = grammar;
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
	}

	@Override
	public void uninstall() {
		viewer.removeTextInputListener(internalListener);

		// Ensure we uninstall all listeners
		internalListener.inputDocumentAboutToBeChanged(viewer.getDocument(), null);
		getTMModelManager().disconnect(viewer.getDocument());
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
		return TMPlugin.getDefault().getTMModelManager();
	}

	private void colorize(int fromLineNumber, Integer toLineNumber, ITMModel model) {
		// Refresh the UI Presentation
		System.err.println("Render from: " + fromLineNumber + " to: " + toLineNumber);
		try {
			IDocument document = ((TMModel) model).getDocument();
			IRegion damage = DocumentHelper.getRegion(document, fromLineNumber, toLineNumber);
			TextPresentation presentation = new TextPresentation(damage, 1000);

			int lastStart = damage.getOffset();
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
						lastStart = t.startIndex + startLineOffset; // fScanner.getTokenOffset();
						length = getTokenLengh(t.startIndex, tokens, i, line, document);
					}
					i++;
				}
			}

			addRange(presentation, lastStart, length, lastAttribute);
			applyTextRegionCollection(presentation);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private IToken toToken(TMToken t) {
		IToken token = tokenProvider.getToken(t.type);
		if (token != null) {
			return token;
		}
		return defaultToken;
	}

	private int getTokenLengh(int startOffset, List<TMToken> tokens, int i, int line,
			IDocument document) throws BadLocationException {
		TMToken next = (i + 1 < tokens.size()) ? tokens.get(i + 1) : null;
		if (next != null) {
			return next.startIndex - startOffset;
		}
		String delim = document.getLineDelimiter(line);
		return document.getLineLength(line) /*- (delim != null ? delim.length() : 0)*/ - startOffset;
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
		return fDefaultTextAttribute ;
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
