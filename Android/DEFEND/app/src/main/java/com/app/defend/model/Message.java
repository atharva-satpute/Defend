package com.app.defend.model;

import java.util.ArrayList;
import java.util.Date;

public class Message {
	String UID, encryptedText, encodings, to, from;
	ArrayList<Integer> flags;
	Date date;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

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