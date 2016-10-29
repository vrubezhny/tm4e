package _editor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

import fr.opensagres.language.textmate.grammar.StackElement;

public class TMModel implements IDocumentListener {

	private Map<Integer, List<StackElement>> lines = new HashMap<>();

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void documentChanged(DocumentEvent event) {
		//IDocument document
		//event.getDocument().getLineOfOffset(offset)

	}

	public void setLineContext(int i, List<StackElement> prevState) {
		lines.put(i, prevState);
	}

	public List<StackElement> getLineContext(int line) {
		return lines.get(line);
	}

}
