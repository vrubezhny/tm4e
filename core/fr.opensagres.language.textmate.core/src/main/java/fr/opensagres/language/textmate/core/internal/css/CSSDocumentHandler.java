package fr.opensagres.language.textmate.core.internal.css;

import java.util.ArrayList;
import java.util.List;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

import fr.opensagres.language.textmate.core.css.CSSStyle;

public class CSSDocumentHandler implements DocumentHandler {

	private final List<CSSStyle> list;
	private CSSStyle currentStyle;

	public CSSDocumentHandler() {
		list = new ArrayList<>();
	}

	@Override
	public void comment(String arg0) throws CSSException {

	}

	@Override
	public void endDocument(InputSource arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endFontFace() throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endMedia(SACMediaList arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endPage(String arg0, String arg1) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endSelector(SelectorList selector) throws CSSException {
		currentStyle = null;
	}

	@Override
	public void ignorableAtRule(String arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void importStyle(String arg0, SACMediaList arg1, String arg2) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void namespaceDeclaration(String arg0, String arg1) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void property(String name, LexicalUnit lexicalUnit, boolean arg2) throws CSSException {
		if (currentStyle != null && "color".equals(name)) {
			currentStyle.setColor(new RGBColorImpl(lexicalUnit));
		}

	}

	@Override
	public void startDocument(InputSource arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startFontFace() throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startMedia(SACMediaList arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPage(String arg0, String arg1) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startSelector(SelectorList selector) throws CSSException {
		currentStyle = new CSSStyle(selector);
		list.add(currentStyle);
	}

	public List<CSSStyle> getList() {
		return list;
	}
}
