package org.eclipse.textmate4e.core.grammar;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import junit.framework.TestSuite;

@RunWith(AllTests.class)
public class VSCodeTextMateTests {

	private static final File REPO_ROOT = new File("src/test/resources");

	public static TestSuite suite() throws Exception {
		TestSuite rootTestSuite = new TestSuite();

		// Tokenization /first-mate/
		TestSuite firstMateTestSuite = createTestSuite("Tokenization /first-mate/", rootTestSuite);
		createVSCodeTestSuite("test-cases/first-mate/tests.json", firstMateTestSuite);

		// Tokenization /suite1/
		TestSuite suite1TestSuite = createTestSuite("Tokenization /suite1/", rootTestSuite);
		createVSCodeTestSuite("test-cases/suite1/tests.json", suite1TestSuite);
		createVSCodeTestSuite("test-cases/suite1/whileTests.json", suite1TestSuite);

		return rootTestSuite;
	}

	private static TestSuite createTestSuite(String name, TestSuite parentTestSuite) {
		TestSuite testSuite = new TestSuite(name);
		parentTestSuite.addTest(testSuite);
		return testSuite;
	}

	private static TestSuite createVSCodeTestSuite(String name, TestSuite parentTestSuite) throws Exception {
		TestSuite vscodeTestSuite = createTestSuite(name, parentTestSuite);
		addVSCodeTestSuite(new File(REPO_ROOT, name), vscodeTestSuite);
		return vscodeTestSuite;
	}

	private static void addVSCodeTestSuite(File testLocation, TestSuite suite) throws Exception {
		Type listType = new TypeToken<ArrayList<RawTest>>() {
		}.getType();
		List<RawTest> tests = new GsonBuilder().create().fromJson(new FileReader(testLocation), listType);
		for (RawTest test : tests) {
			test.setTestLocation(testLocation);
			suite.addTest(test);
		}
	}

}
