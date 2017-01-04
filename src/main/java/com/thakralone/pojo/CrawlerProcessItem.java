package com.thakralone.pojo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thakralone.util.DateUtil;

public class CrawlerProcessItem implements Serializable{
	
	String crawlId;
	String elapsedDate;
	String searchTerm;
	String searchOption;
	String searchOptionAbbr;
	String status;
	String latestLog;
	
	public CrawlerProcessItem(String folderData) {
		super();
		
		String foldername = folderData.split("-")[0];
	       
        String crawlId = foldername;	       
        String startDateStr = foldername.substring(0,14);
       
        @SuppressWarnings("unused")
	    Date startDate = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
       
        try {
    	  startDate = df.parse(startDateStr);
	    } catch (ParseException e) {
		  e.printStackTrace();
	    }
       
        String elapsedDate = DateUtil.printElapsed(startDate, new Date());
       
        String searchTerm = folderData.split("-")[1];
        String status = foldername.substring(foldername.length() - 1);
        String latestLog = "latestlog";
        String searchOption = foldername.substring(foldername.length()-3,foldername.length()-1);
		
		this.crawlId = crawlId;
		this.elapsedDate = elapsedDate;
		this.searchTerm = searchTerm;
		this.searchOptionAbbr = searchOption;
		this.searchOption = ApiEnum.findByAbbr(searchOption).getName();
		this.status = (status.equalsIgnoreCase("N") ? "In Progress" : "Done");
		this.latestLog = latestLog;
	}
	
	public String getCrawlId() {
		return crawlId;
	}
	public String getStartDate() {
		if(this.getStatus().equals("Done")){ return "-"; }
		return elapsedDate;
	}
	public String getSearchTerm() {
		return searchTerm;
	}
	public String getSearchOption(){
		return searchOption;
	}
	public String getStatus() {
		return status;
	}
	public String getLatestLog() {
		if(this.getStatus().equals("Done")){ return "-"; }
		return latestLog;
	}
	public String getSearchAbbr(){
		return this.searchOptionAbbr;
	}
	
	

}
