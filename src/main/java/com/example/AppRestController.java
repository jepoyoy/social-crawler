package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thakralone.pojo.CrawlerProcessItem;
import com.thakralone.util.FileUtil;
import com.thakralone.util.KeyValue;

@RestController
public class AppRestController {

	
	@RequestMapping("/gettaillog")
	public KeyValue getTailLog(@RequestParam("crawlid") String crawlId){
		String curdir = System.getProperty("user.dir");
		
		File f = new File(curdir + "/crawldata" + "/" + crawlId + "/logn.txt");
		
		return new KeyValue("data", FileUtil.getLastLineFast(f));
	}
	
	@RequestMapping("/geterrorlog")
	public KeyValue getErrorLog(@RequestParam("crawlid") String crawlId) throws IOException{
		String curdir = System.getProperty("user.dir");
		
		FileReader f = new FileReader(curdir + "/crawldata" + "/" + crawlId + "/logn.txt");
		BufferedReader br = new BufferedReader(f);
		
		return new KeyValue("data", FileUtil.grep(br, "ERROR"));
	}
	
	@RequestMapping("/getprocesslist")
	public ArrayList<CrawlerProcessItem> loadProcessList(Model model){
		String curdir = System.getProperty("user.dir");
		
		ArrayList<CrawlerProcessItem> processFolders = FileUtil.getDirContents(curdir + "/crawldata");
		
		//model.addAttribute("processFolders", processFolders);
		
		return processFolders;
	}

}
