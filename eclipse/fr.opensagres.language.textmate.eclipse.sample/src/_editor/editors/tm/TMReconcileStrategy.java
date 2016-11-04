package _editor.editors.tm;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

public class TMReconcileStrategy implements IReconcilingStrategy {

	private TMModel model;
	
	@Override
	public void setDocument(IDocument document) {
		model = TMModelManager.getInstance().connect(document, false);
	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		//System.err.println(dirtyRegion);
	}

	@Override
	public void reconcile(IRegion partition) {
		// TODO Auto-generated method stub

	}

}
