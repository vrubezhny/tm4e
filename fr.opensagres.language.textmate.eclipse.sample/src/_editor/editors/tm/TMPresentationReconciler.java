package _editor.editors.tm;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Display;

import _editor.editors.ColorManager;
import _editor.editors.IXMLColorConstants;

public class TMPresentationReconciler implements IPresentationReconciler {

	/**
	 * Prefix of the name of the position category for tracking damage regions.
	 */
	protected final static String TRACKED_PARTITION = "__reconciler_tracked_partition"; //$NON-NLS-1$

	/**
	 * Internal listener class.
	 */
	class InternalListener
			implements ITextInputListener, IDocumentListener, ITextListener, IModelTokensChangedListener {

		/**
		 * Set to <code>true</code> if between a document about to be changed
		 * and a changed event.
		 */
		private boolean fDocumentChanging = false;
		/**
		 * The cached redraw state of the text viewer.
		 * 
		 * @since 3.0
		 */
		private boolean fCachedRedrawState = true;

		@Override
		public void inputDocumentAboutToBeChanged(IDocument oldDocument, IDocument newDocument) {
			if (oldDocument != null) {
				try {

					fViewer.removeTextListener(this);
					oldDocument.removeDocumentListener(this);
					// oldDocument.removeDocumentPartitioningListener(this);

					oldDocument.removePositionUpdater(fPositionUpdater);
					oldDocument.removePositionCategory(fPositionCategory);

				} catch (BadPositionCategoryException x) {
					// should not happened for former input documents;
				}
			}
		}

		/*
		 * @see ITextInputListener#inputDocumenChanged(IDocument, IDocument)
		 */
		@Override
		public void inputDocumentChanged(IDocument oldDocument, IDocument newDocument) {

			fDocumentChanging = false;
			fCachedRedrawState = true;

			if (oldDocument != null) {
				TMModelManager.getInstance().disconnect(oldDocument);
			}

			if (newDocument != null) {

				TMModel model = TMModelManager.getInstance().connect(newDocument, false);
				model.addModelTokensChangedListener(this);
				model.load();

				newDocument.addPositionCategory(fPositionCategory);
				newDocument.addPositionUpdater(fPositionUpdater);

				// newDocument.addDocumentPartitioningListener(this);
				newDocument.addDocumentListener(this);
				fViewer.addTextListener(this);

				// setDocumentToDamagers(newDocument);
				// setDocumentToRepairers(newDocument);
				processDamage(new Region(0, newDocument.getLength()), newDocument);
			}
		}

		@Override
		public void documentAboutToBeChanged(DocumentEvent e) {

			fDocumentChanging = true;
			if (fCachedRedrawState) {
				try {
					int offset = e.getOffset() + e.getLength();
					ITypedRegion region = getPartition(e.getDocument(), offset);
					fRememberedPosition = new TypedPosition(region);
					e.getDocument().addPosition(fPositionCategory, fRememberedPosition);
				} catch (BadLocationException x) {
					// can not happen
				} catch (BadPositionCategoryException x) {
					// should not happen on input elements
				}
			}
		}

		@Override
		public void documentChanged(DocumentEvent e) {
			if (fCachedRedrawState) {
				try {
					e.getDocument().removePosition(fPositionCategory, fRememberedPosition);
				} catch (BadPositionCategoryException x) {
					// can not happen on input documents
				}
			}
			fDocumentChanging = false;
		}

		@Override
		public void textChanged(TextEvent e) {

			fCachedRedrawState = e.getViewerRedrawState();
			if (!fCachedRedrawState)
				return;

			IRegion damage = null;
			IDocument document = null;

			if (e.getDocumentEvent() == null) {
				document = fViewer.getDocument();
				if (document != null) {
					if (e.getOffset() == 0 && e.getLength() == 0 && e.getText() == null) {
						// redraw state change, damage the whole document
						damage = new Region(0, document.getLength());
					} else {
						IRegion region = widgetRegion2ModelRegion(e);
						if (region != null) {
							try {
								String text = document.get(region.getOffset(), region.getLength());
								DocumentEvent de = new DocumentEvent(document, region.getOffset(), region.getLength(),
										text);
								damage = getDamage(de, false);
							} catch (BadLocationException x) {
							}
						}
					}
				}
			} else {
				DocumentEvent de = e.getDocumentEvent();
				document = de.getDocument();
				damage = getDamage(de, true);
			}

			// if (/*damage != null && */document != null)
			// processDamage(damage, document);

			// fDocumentPartitioningChanged= false;
			// fChangedDocumentPartitions= null;
		}

		private IRegion getDamage(DocumentEvent e, boolean b) {
			int length = e.getText() == null ? 0 : e.getText().length();

			// if (fDamagers == null || fDamagers.isEmpty()) {
			length = Math.max(e.getLength(), length);
			length = Math.min(e.getDocument().getLength() - e.getOffset(), length);
			return new Region(e.getOffset(), length);
			// }
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
		protected IRegion widgetRegion2ModelRegion(TextEvent e) {

			String text = e.getText();
			int length = text == null ? 0 : text.length();

			if (fViewer instanceof ITextViewerExtension5) {
				ITextViewerExtension5 extension = (ITextViewerExtension5) fViewer;
				return extension.widgetRange2ModelRange(new Region(e.getOffset(), length));
			}

			IRegion visible = fViewer.getVisibleRegion();
			IRegion region = new Region(e.getOffset() + visible.getOffset(), length);
			return region;
		}

		@Override
		public void modelTokensChanged(final int fromLineNumber, final Integer toLineNumber, final TMModel model) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					colorize(fromLineNumber, toLineNumber, model);
				}
			});
			// colorize(fromLineNumber, toLineNumber, model);
			// Job j = new Job("Syntax color") {
			//
			// @Override
			// protected IStatus run(IProgressMonitor monitor) {
			//
			// Display.getDefault().asyncExec(new Runnable() {
			//
			// @Override
			// public void run() {
			// colorize(fromLineNumber, toLineNumber, model);
			// }
			// });
			//
			// return Status.OK_STATUS;
			// }
			// };
			// j.schedule();
		}

		private void colorize(int fromLineNumber, Integer toLineNumber, TMModel model) {
			// Refresh the UI Presentation
			System.err.println("Render from: " + fromLineNumber + " to: " + toLineNumber);
			try {
				IRegion damage = model.getRegion(fromLineNumber, toLineNumber);
				TextPresentation presentation = new TextPresentation(damage, 1000);

				int lastStart = damage.getOffset();
				int length = 0;
				boolean firstToken = true;
				IToken lastToken = Token.UNDEFINED;
				TextAttribute lastAttribute = getTokenTextAttribute(lastToken);

				List<_editor.editors.tm.Token> tokens = null;
				for (int line = fromLineNumber; line <= toLineNumber; line++) {
					tokens = model.getLineTokens(line);
					int i = 0;
					int startLineOffset = model.getDocument().getLineOffset(line);
					for (_editor.editors.tm.Token t : tokens) {
						IToken token = toToken(t);
						TextAttribute attribute = getTokenTextAttribute(token);
						if (lastAttribute != null && lastAttribute.equals(attribute)) {
							length += getTokenLengh(t.startIndex, tokens, i, line, model.getDocument());
							firstToken = false;
						} else {
							if (!firstToken)
								addRange(presentation, lastStart, length, lastAttribute);
							firstToken = false;
							lastToken = token;
							lastAttribute = attribute;
							lastStart = t.startIndex + startLineOffset; // fScanner.getTokenOffset();
							length = getTokenLengh(t.startIndex, tokens, i, line, model.getDocument());
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

		private int getTokenLengh(int startOffset, List<_editor.editors.tm.Token> tokens, int i, int line,
				IDocument document) throws BadLocationException {
			_editor.editors.tm.Token next = (i + 1 < tokens.size()) ? tokens.get(i + 1) : null;
			if (next != null) {
				return next.startIndex - startOffset;
			}
			String delim = document.getLineDelimiter(line);
			return document.getLineLength(line) /*- (delim != null ? delim.length() : 0)*/ - startOffset;
		}

		private IToken toToken(_editor.editors.tm.Token token) {
			// if ("comment.block.documentation.ts".equals(token.type) ) {
			// return commentsToken;
			// }
			// if ("comment.block.ts".equals(token.type)) {
			// return commentsToken;
			// }

			if (token.type != null) {
				if (token.type.startsWith("comment.")) {
					return commentsToken;
				}
				if (token.type.contains("meta") && token.type.contains("type") && token.type.contains("parameter") && token.type.contains("variable")) {
					return metaParameterTypeVariableToken;
				}
				if (token.type.contains("meta") && token.type.contains("type") && token.type.contains("annotation")) {
					return metaTypeAnnotationToken;
				}
				if (token.type.contains("keyword") && token.type.contains("control")) {
					return keywordControlToken;
				}
				if (token.type.contains("function") && token.type.contains("entity") && token.type.contains("name")) {
					return entityNameFunctionToken;
				}
				if (token.type.contains("string")) {
					return stringToken;
				}
				if (token.type.contains("numeric")) {
					return numericToken;
				}
				if (token.type.contains("storage")) {
					return wordToken;
				}
				
//				if ("meta.function.ts.storage.type".equals(token.type)
//						|| "ts.meta.function.storage.type".equals(token.type)
//						|| "meta.ts.storage.type.function".equals(token.type)
//						|| "meta.ts.storage.type.function.decl.block".equals(token.type)) {
//					return wordToken;
//				}

			}
			//uSystem.err.println("Not found:" + token.type);
			return defaultToken;
		}
	}

	/**
	 * The default text attribute if non is returned as data by the current
	 * token
	 */
	protected TextAttribute fDefaultTextAttribute;

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

	/** The target viewer. */
	private ITextViewer fViewer;
	/** The internal listener. */
	private InternalListener fInternalListener = new InternalListener();
	/** The name of the position category to track damage regions. */
	private String fPositionCategory;
	/** The position updated for the damage regions' position category. */
	private IPositionUpdater fPositionUpdater;
	/** The positions representing the damage regions. */
	private TypedPosition fRememberedPosition;

	private Token commentsToken;
	private Token wordToken;
	private Token defaultToken;
	private Token entityNameFunctionToken;
	private Token stringToken;
	private Token numericToken;
	private Token metaTypeAnnotationToken;
	private Token metaParameterTypeVariableToken;
	private Token keywordControlToken;

	/**
	 * Creates a new presentation reconciler. There are no damagers or repairers
	 * registered with this reconciler by default. The default partitioning
	 * <code>IDocumentExtension3.DEFAULT_PARTITIONING</code> is used.
	 */
	public TMPresentationReconciler(ColorManager manager) {
		super();
		fPositionCategory = TRACKED_PARTITION + hashCode();
		fPositionUpdater = new DefaultPositionUpdater(fPositionCategory);

		commentsToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.KEY_COMMENTS)));
		wordToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.KEY_WORD)));
		stringToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.STRING)));
		numericToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.NUMERIC)));
		entityNameFunctionToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.NAME_FUNCTION)));
		metaTypeAnnotationToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.META_TYPE_ANNOTATION)));
		metaParameterTypeVariableToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.META_PARAMETER_TYPE_VARIABLE)));
		keywordControlToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.KEYWORD_CONTROL)));
		defaultToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.DEFAULT)));
	}

	@Override
	public void install(ITextViewer viewer) {
		Assert.isNotNull(viewer);

		fViewer = viewer;
		fViewer.addTextInputListener(fInternalListener);

		IDocument document = viewer.getDocument();
		if (document != null) {
			fInternalListener.inputDocumentChanged(null, document);
		}
	}

	@Override
	public void uninstall() {
		fViewer.removeTextInputListener(fInternalListener);

		// Ensure we uninstall all listeners
		fInternalListener.inputDocumentAboutToBeChanged(fViewer.getDocument(), null);
		TMModelManager.getInstance().disconnect(fViewer.getDocument());
	}

	@Override
	public IPresentationDamager getDamager(String contentType) {
		return null;
	}

	@Override
	public IPresentationRepairer getRepairer(String contentType) {
		return null;
	}

	/**
	 * Constructs a "repair description" for the given damage and returns this
	 * description as a text presentation. For this, it queries the partitioning
	 * of the damage region and asks the appropriate presentation repairer for
	 * each partition to construct the "repair description" for this partition.
	 *
	 * @param damage
	 *            the damage to be repaired
	 * @param document
	 *            the document whose presentation must be repaired
	 * @return the presentation repair description as text presentation or
	 *         <code>null</code> if the partitioning could not be computed
	 */
	protected TextPresentation createPresentation(IRegion damage, IDocument document) {
		// try {

		TextPresentation presentation = new TextPresentation(damage, 1000);

		// ITypedRegion[] partitioning=
		// TextUtilities.computePartitioning(document,
		// getDocumentPartitioning(), damage.getOffset(), damage.getLength(),
		// false);
		// for (int i= 0; i < partitioning.length; i++) {
		// ITypedRegion r= partitioning[i];
		// IPresentationRepairer repairer= getRepairer(r.getType());
		// if (repairer != null)
		// repairer.createPresentation(presentation, r);
		// }

		return presentation;

		// } catch (BadLocationException x) {
		// return null;
		// }
	}

	/**
	 * Processes the given damage.
	 * 
	 * @param damage
	 *            the damage to be repaired
	 * @param document
	 *            the document whose presentation must be repaired
	 */
	private void processDamage(IRegion damage, IDocument document) {
		// if (damage != null && damage.getLength() > 0) {
		TextPresentation p = createPresentation(damage, document);
		if (p != null)
			applyTextRegionCollection(p);
		// }
	}

	/**
	 * Applies the given text presentation to the text viewer the presentation
	 * reconciler is installed on.
	 *
	 * @param presentation
	 *            the text presentation to be applied to the text viewer
	 */
	private void applyTextRegionCollection(TextPresentation presentation) {
		fViewer.changeTextPresentation(presentation, false);
	}

	/**
	 * Returns the partition for the given offset in the given document.
	 *
	 * @param document
	 *            the document
	 * @param offset
	 *            the offset
	 * @return the partition
	 * @throws BadLocationException
	 *             if offset is invalid in the given document
	 * @since 3.0
	 */
	private ITypedRegion getPartition(IDocument document, int offset) throws BadLocationException {
		return TextUtilities.getPartition(document, IDocumentExtension3.DEFAULT_PARTITIONING, offset, false);
	}
}
