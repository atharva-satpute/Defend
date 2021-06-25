package com.app.defend.model;

import java.util.ArrayList;

public class Message {
	String UID, encryptedText, encodings;
	ArrayList<Integer> flags;

	public String getUID() {
		return UID;
	}

	public void setUID(String UID) {
		this.UID = UID;
	}

	public String getEncryptedText() {
		return encryptedText;
	}

	public void setEncryptedText(String encryptedText) {
		this.encryptedText = encryptedText;
	}

	public String getEncodings() {
		return encodings;
	}

	public void setEncodings(String encodings) {
		this.encodings = encodings;
	}

	public ArrayList<Integer> getFlags() {
		return flags;
	}

	public void setFlags(ArrayList<Integer> flags) {
		this.flags = flags;
	}
}
