package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thakralone.pojo.CrawlerProcessItem;
import com.thakralone.util.FileUtil;

@RestController
public class AppRestController {

	
	@RequestMapping("/gettaillog")
	public String loadProcessList(@RequestParam("crawlid") String crawlId){
		String curdir = System.getProperty("user.dir");
		
		File f = new File(curdir + "/crawldata" + "/" + crawlId + "/log.txt");
		
		return FileUtil.getLastLineFast(f);
	}
	
	@RequestMapping("/getdatafromfile")
	public Object getDataFromFileToJson(@RequestParam("crawlid") String crawlId) throws FileNotFoundException, IOException, ParseException{
		
		String curdir = System.getProperty("user.dir");
		
		JSONParser parser = new JSONParser();
		return parser.parse(new FileReader(curdir + "/crawldata" + "/" + crawlId + "/data.txt"));
	}
	
	@RequestMapping("/getprocesslist")
	public ArrayList<CrawlerProcessItem> loadProcessList(Model model){
		String curdir = System.getProperty("user.dir");
		
		ArrayList<CrawlerProcessItem> processFolders = FileUtil.getDirContents(curdir + "/crawldata");
		
		//model.addAttribute("processFolders", processFolders);
		
		return processFolders;
	}

}
