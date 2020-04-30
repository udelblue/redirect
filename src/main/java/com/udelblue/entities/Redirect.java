package com.udelblue.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

@Entity
public class Redirect {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotBlank(message = "URL is mandatory.")
	@URL(regexp = "^(www|http|https|ftp|scp|ws).*", message = "Not a valid URL.")
	private String url;

	private String guid;

	private boolean appendQueryString;

	private boolean recordQueryString;

	public Redirect() {
	}

	public Redirect(String url, boolean appendQueryString) {
		this.url = url;
		this.appendQueryString = appendQueryString;
	}

	public Redirect(String url, boolean appendQueryString, boolean recordQueryString) {
		this.url = url;
		this.appendQueryString = appendQueryString;
		this.recordQueryString = recordQueryString;
	}

	public boolean isRecordQueryString() {
		return recordQueryString;
	}

	public void setRecordQueryString(boolean recordQueryString) {
		this.recordQueryString = recordQueryString;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public boolean isAppendQueryString() {
		return appendQueryString;
	}

	public void setAppendQueryString(boolean appendQueryString) {
		this.appendQueryString = appendQueryString;
	}

	@Override
	public String toString() {
		return "Redirect [id=" + id + ", url=" + url + ", guid=" + guid + ", appendQueryString=" + appendQueryString
				+ ", recordQueryString=" + recordQueryString + "]";
	}

}
