package _editor.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import _editor.TMModel;

public class XMLDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
//			IDocumentPartitioner partitioner =
//				new FastPartitioner(
//					new XMLPartitionScanner(),
//					new String[] {
//						XMLPartitionScanner.XML_TAG,
//						XMLPartitionScanner.XML_COMMENT });
//			partitioner.connect(document);
			//document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
	
//	@Override
//	protected IDocument createEmptyDocument() {
//		Document document = new Document() {
//			protected void setLineTracker(ILineTracker tracker) {
//				super.setLineTracker(new TMModel(tracker));
//			};
//		};
//		return document;
//	}
}