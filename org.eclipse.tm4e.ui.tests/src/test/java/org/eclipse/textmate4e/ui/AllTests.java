package org.eclipse.textmate4e.ui;

import org.eclipse.textmate4e.ui.text.typescript.TMPresentationReconcilerTypeScriptTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
		TMPresentationReconcilerTypeScriptTest.class,
		RegistryTest.class,
		TMinGenericEditorTest.class
})
public class AllTests {

}
