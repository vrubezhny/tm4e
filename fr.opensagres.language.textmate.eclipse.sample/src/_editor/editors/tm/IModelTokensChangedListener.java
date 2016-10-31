package _editor.editors.tm;

public interface IModelTokensChangedListener {

	void modelTokensChanged(int fromLineNumber, Integer toLineNumber, TMModel model);

}
