package com.kirby.languagetester.audio.extractor;

public class AudioCreationResponse {

	private String id;
	private String status;
	private String fileURL;
	private String fileCors;
	private int parts;
	private int parts_done;
	private int duration;
	private String format;
	private String error;
	private String balans;
	private int cost;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public String getFileCors() {
		return fileCors;
	}

	public void setFileCors(String fileCors) {
		this.fileCors = fileCors;
	}

	public int getParts() {
		return parts;
	}

	public void setParts(int parts) {
		this.parts = parts;
	}

	public int getParts_done() {
		return parts_done;
	}

	public void setParts_done(int parts_done) {
		this.parts_done = parts_done;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getBalans() {
		return balans;
	}

	public void setBalans(String balans) {
		this.balans = balans;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}
