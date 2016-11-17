package org.eclipse.language.textmate.eclipse.internal.themes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.language.textmate.eclipse.themes.ITokenProvider;
import org.eclipse.language.textmate.eclipse.themes.css.CSSTokenProvider;

public class Theme {

	private final IConfigurationElement ce;
	private String id;
	private String name;
	private String path;

	private ITokenProvider tokenProvider;

	public Theme(IConfigurationElement ce) {
		this.ce = ce;
		this.id = ce.getAttribute("id");
		this.name = ce.getAttribute("name");
		this.path = ce.getAttribute("path");
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ITokenProvider getTokenProvider() {
		if (tokenProvider == null) {
			if (path != null && path.length() > 0) {
				String pluginId = ce.getNamespaceIdentifier();
				try {
					File bundleDir = FileLocator.getBundleFile(Platform.getBundle(pluginId));
					InputStream in = new FileInputStream(new File(bundleDir, path));
					tokenProvider = new CSSTokenProvider(in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return tokenProvider;
	}

}
