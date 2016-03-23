package fr.opensagres.language.textmate.oniguruma;

import java.util.UUID;

public class OnigString {

	private final String str;
	private byte[] value;
	private UUID uniqueId;

	public OnigString(String str) {
		this.str = str;
		this.value = str.getBytes();
		this.uniqueId = UUID.randomUUID();
	}

	public UUID uniqueId() {
		return uniqueId;
	}

	public byte[] utf8_value() {
		return value;
	}

	public int utf8_length() {
		return value.length;
	}

	public String getString() {
		return str;
	}
}
