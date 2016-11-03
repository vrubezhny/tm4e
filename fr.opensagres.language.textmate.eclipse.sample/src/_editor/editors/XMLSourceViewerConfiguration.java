package _editor.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import _editor.MyTextMateTokenScanner;
import _editor.TextMateTokenScanner;
import _editor.editors.tm.TMPresentationReconciler;
import _editor.editors.tm.TMReconcileStrategy;

public class XMLSourceViewerConfiguration extends SourceViewerConfiguration {
	
	private XMLDoubleClickStrategy doubleClickStrategy;
	//private XMLTagScanner tagScanner;
	private TextMateTokenScanner scanner;
	private ColorManager colorManager;

	public XMLSourceViewerConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			XMLPartitionScanner.XML_COMMENT,
			XMLPartitionScanner.XML_TAG };
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new XMLDoubleClickStrategy();
		return doubleClickStrategy;
	}

//	protected TextMateTokenScanner getTextMateScanner() {
//		if (scanner == null) {
//			scanner = new MyTextMateTokenScanner(colorManager);
//			scanner.setDefaultReturnToken(
//				new Token(
//					new TextAttribute(
//						colorManager.getColor(IXMLColorConstants.DEFAULT))));
//		}
//		return scanner;
//	}
	
	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		IReconcilingStrategy strategy= new TMReconcileStrategy();
		MonoReconciler reconciler= new MonoReconciler(strategy, true);
		reconciler.setDelay(500);
		return reconciler;
	}
	
//	protected XMLTagScanner getXMLTagScanner() {
//		if (tagScanner == null) {
//			tagScanner = new XMLTagScanner(colorManager);
//			tagScanner.setDefaultReturnToken(
//				new Token(
//					new TextAttribute(
//						colorManager.getColor(IXMLColorConstants.TAG))));
//		}
//		return tagScanner;
//	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		//MyPresentationReconciler reconciler = new MyPresentationReconciler();

//		DefaultDamagerRepairer dr =
//			new DefaultDamagerRepairer(getXMLTagScanner());
//		reconciler.setDamager(dr, XMLPartitionScanner.XML_TAG);
//		reconciler.setRepairer(dr, XMLPartitionScanner.XML_TAG);
//
//		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getTextMateScanner());
//		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
//		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

//		NonRuleBasedDamagerRepairer ndr =
//			new NonRuleBasedDamagerRepairer(
//				new TextAttribute(
//					colorManager.getColor(IXMLColorConstants.XML_COMMENT)));
//		reconciler.setDamager(ndr, XMLPartitionScanner.XML_COMMENT);
//		reconciler.setRepairer(ndr, XMLPartitionScanner.XML_COMMENT);

		TMPresentationReconciler reconciler = new TMPresentationReconciler(colorManager);
		return reconciler;
	}

}