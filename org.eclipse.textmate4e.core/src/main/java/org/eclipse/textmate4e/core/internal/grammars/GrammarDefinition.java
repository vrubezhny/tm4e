package org.eclipse.textmate4e.core.internal.grammars;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class GrammarDefinition {

	private String path;
	private String scopeName;
	private String pluginId;

	public GrammarDefinition(IConfigurationElement element) {
		this.path = element.getAttribute("path");
		this.scopeName = element.getAttribute("scopeName");
		this.pluginId = element.getNamespaceIdentifier();
	}

	public String getScopeName() {
		return scopeName;
	}

	public String getPath() {
		return path;
	}

	public InputStream getInputStream() throws IOException {
		if (path != null && path.length() > 0) {
			File bundleDir = FileLocator.getBundleFile(Platform.getBundle(pluginId));
			return new FileInputStream(new File(bundleDir, path));
		}
		return null;
	}

}
