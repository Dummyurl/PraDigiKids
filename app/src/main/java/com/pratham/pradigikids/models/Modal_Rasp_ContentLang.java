package com.pratham.pradigikids.models;

import com.google.gson.annotations.SerializedName;

public class Modal_Rasp_ContentLang {

	@SerializedName("lang_name")
	private String langName;

	@SerializedName("lang_code")
	private String langCode;

	@SerializedName("id")
	private String id;

	@SerializedName("lang_subcode")
	private Object langSubcode;

	@SerializedName("lang_direction")
	private String langDirection;

	public void setLangName(String langName){
		this.langName = langName;
	}

	public String getLangName(){
		return langName;
	}

	public void setLangCode(String langCode){
		this.langCode = langCode;
	}

	public String getLangCode(){
		return langCode;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setLangSubcode(Object langSubcode){
		this.langSubcode = langSubcode;
	}

	public Object getLangSubcode(){
		return langSubcode;
	}

	public void setLangDirection(String langDirection){
		this.langDirection = langDirection;
	}

	public String getLangDirection(){
		return langDirection;
	}
}