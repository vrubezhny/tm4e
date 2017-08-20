package org.eclipse.tm4e.languageconfiguration.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.eclipse.core.runtime.IConfigurationElement;

public class LanguageConfigurationDelegate {

	private static final String PLATFORM_PLUGIN = "platform:/plugin/"; //$NON-NLS-1$

	private String contentTypeId;
	private String path;
	private String pluginId;

	public LanguageConfigurationDelegate(IConfigurationElement ce) {
		this.path = ce.getAttribute("path");
		this.contentTypeId = ce.getAttribute("contentTypeId");
		this.pluginId = ce.getNamespaceIdentifier();
	}

	public String getContentTypeId() {
		return contentTypeId;
	}

	public LanguageConfiguration getLanguageConfiguration() {		
		try {
			return LanguageConfiguration.load(new InputStreamReader(getInputStream(), Charset.defaultCharset()));
		} catch (IOException e) {
			// TODO: log it!!!
			return null;
		}
	}

	public InputStream getInputStream() throws IOException {
		if (path == null || path.length() < 0) {
			return null;
		}
		if (pluginId != null) {
			URL url = new URL(new StringBuilder(PLATFORM_PLUGIN).append(pluginId).append("/").append(path).toString());
			return url.openStream();
		}
		return new FileInputStream(new File(path));
	}

}
