package com.thakralone.pojo;

public enum ApiEnum {
	
	FACEBOOK("FB","Facebook"), 
	TWITTER("TW", "Twitter"), 
	INSTAGRAM("IG", "Instagram"), 
	GPLUS("GP","Google Plus"), 
	YOUTUBE("YT","Youtube"), 
	WORDPRESS("WP", "Wordpress"),
	ALL("AL", "All");
	
	private String abbr;
	private String name;

	private ApiEnum(String abbr, String name) {
		this.abbr = abbr;
		this.name = name;
	}

	public String getAbbr() {
		return abbr;
	}

	public String getName() {
		return name;
	}
	
	public static ApiEnum findByAbbr(String abbr){
	    for(ApiEnum ae : values()){
	        if( ae.getAbbr().equals(abbr)){
	            return ae;
	        }
	    }
	    return null;
	}
}
