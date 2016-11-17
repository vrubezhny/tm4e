package org.eclipse.language.textmate.core.internal.oniguruma;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class OnigString {

	private final String str;
	private byte[] value;
	private UUID uniqueId;

	public OnigString(String str) {
		this.str = str;
		try {
			this.value = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.uniqueId = UUID.randomUUID();
	}

	public UUID uniqueId() {
		return uniqueId;
	}

	public byte[] utf8_value() {
		return value;
	}

	public int utf8_length() {
		return str.length();
	}

	public String getString() {
		return str;
	}
}
