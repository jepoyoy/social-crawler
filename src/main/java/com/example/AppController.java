package com.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thakralone.pojo.CrawlerProcessItem;
import com.thakralone.util.FileUtil;

@Controller
public class AppController {
	
	
	@RequestMapping(value="/runcrawlerapp", method=RequestMethod.POST)
	public String runCrawlerApp(@RequestParam("keyword") String keyword, @RequestParam("searchOption") String searchOption, RedirectAttributes redirectAttributes, HttpServletResponse response) throws IOException{

		try {
			Process ps=Runtime.getRuntime().exec(new String[]{"java","-jar","crawler.jar", keyword, searchOption});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "error");
		}
		redirectAttributes.addFlashAttribute("message", "done");
		redirectAttributes.addAttribute("_D", "5646456346");
	    return "redirect";
	}
	
	@RequestMapping("/viewdata")
	public String viewData(@RequestParam("crawlid") String crawlId, Model model){
		model.addAttribute("crawlid", crawlId);
		return "viewdata";
	}
	
	@RequestMapping("/processlist")
	public String loadProcessList(Model model){
		String curdir = System.getProperty("user.dir");
		ArrayList<CrawlerProcessItem> processFolders = FileUtil.getDirContents(curdir + "/crawldata");
		
		model.addAttribute("processFolders", processFolders);
		
		return "processlist";
	}
	
	@RequestMapping("/installcheck")
	public String installcheck(Model model) throws IOException{
		
		boolean installStatus = false;
		String curdir = System.getProperty("user.dir");
		
		model.addAttribute("userDir", curdir);
		
		File f = new File(curdir + "/crawldata");
		if (!f.exists()) {
		   model.addAttribute("folderStatus", "crawldata folder not yet existing. created one");
		   f.mkdir();
		}else{
		   model.addAttribute("folderStatus", "crawldata folder already exists");
		}
		
		installStatus = true;
		
		model.addAttribute("installStatus", installStatus);
		
		return "installcheck";
	}

}
