package de.app.infoapp;

public class Losung {
	private String date;
	private String losungsText;
	private String losungsVers;
	private String lehrText;
	private String lehrtextVers;
	
	
	public String getLehrText() {
		return lehrText;
	}
	public void setLehrText(String lehrText) {
		this.lehrText = lehrText;
	}
	public String getLehrtextVers() {
		return lehrtextVers;
	}
	public void setLehrtextVers(String lehrtextVers) {
		this.lehrtextVers = lehrtextVers;
	}
	public Losung(String date, String losungsText, String losungsVers, String lehrText, String lehrVers) {
		super();
		this.date = date;
		this.losungsText = losungsText;
		this.losungsVers = losungsVers;
		this.lehrText = lehrText;
		this.lehrtextVers = lehrVers;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLosungsText() {
		return losungsText;
	}
	public void setLosungsText(String losungsText) {
		this.losungsText = losungsText;
	}
	public String getLosungsVers() {
		return losungsVers;
	}
	public void setLosungsVers(String losungsVers) {
		this.losungsVers = losungsVers;
	}

}
