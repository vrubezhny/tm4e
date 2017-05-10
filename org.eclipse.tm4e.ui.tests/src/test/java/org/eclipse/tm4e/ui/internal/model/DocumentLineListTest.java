package org.eclipse.tm4e.ui.internal.model;

import org.eclipse.jface.text.Document;
import org.junit.Test;

public class DocumentLineListTest {

	@Test
	public void test() {
		Document document = new Document();
		DocumentLineList lineList = new DocumentLineList(document);
		
		System.err.println(lineList.getSize());
		document.set("var");
		//document.set("var\nv");
		System.err.println(lineList.getSize());
	}
}
