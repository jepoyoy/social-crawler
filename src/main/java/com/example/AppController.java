package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thakralone.pojo.CrawlerProcessItem;
import com.thakralone.util.FileUtil;

@Controller
public class AppController {
	
	@RequestMapping(value="/getdatafromfile", method=RequestMethod.GET,produces="application/vnd.ms-excel")
	@ResponseBody
	public void getDataFromFileToJson(@RequestParam("crawlid") String crawlId, Model model, HttpServletResponse response) throws FileNotFoundException, IOException, JSONException, ParseException{
		
		String curdir = System.getProperty("user.dir");
		Object obj = new JSONParser().parse(new FileReader(curdir + "/crawldata" + "/" + crawlId + "/data.txt"));
		JSONObject jObj = (JSONObject) obj;
		
		
		response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + crawlId +"\".xls"));


		Map<Integer, Object[]> excelData = new HashMap<Integer, Object[]>();

		Iterator<JSONObject> iterator = jObj.entrySet().iterator();
		int ctr = 1;

		while (iterator.hasNext()) {
			Map.Entry pair = (Map.Entry)iterator.next();
			JSONObject objj = (JSONObject) pair.getValue();
			JSONObject dateCre = (JSONObject) objj.get("dateCreated");
			excelData.put(ctr, new Object[] {   pair.getKey(), 
					  						    objj.get("type"), 
					  						    objj.get("hashtag"), 
					  						    dateCre.get("value").toString(), 
					  						    objj.get("text"), 
					  						    objj.get("creator")});
			
			ctr++;
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");
		
		Row rowH = sheet.createRow(0);
		Cell cellH1 = rowH.createCell(0);
		cellH1.setCellValue("ID");
		Cell cellH2 = rowH.createCell(1);
		cellH2.setCellValue("Type");
		Cell cellH3 = rowH.createCell(2);
		cellH3.setCellValue("Hashtag");
		Cell cellH4 = rowH.createCell(3);
		cellH4.setCellValue("Date Created");
		Cell cellH5 = rowH.createCell(4);
		cellH5.setCellValue("Text");
		Cell cellH6 = rowH.createCell(5);
		cellH6.setCellValue("Created");
		
		
		Set<Integer> keyset = excelData.keySet();
		int rownum = 1;
		for (Integer key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object [] objArr = excelData.get(key);
			int cellnum = 0;
			for (Object obj2 : objArr) {
				Cell cell = row.createCell(cellnum++);
				if(obj2 instanceof Date) 
					cell.setCellValue((Date)obj2);
				else if(obj2 instanceof Boolean)
					cell.setCellValue((Boolean)obj2);
				else if(obj2 instanceof String)
					cell.setCellValue((String)obj2);
				else if(obj2 instanceof Double)
					cell.setCellValue((Double)obj2);
			}
		}

		workbook.write(response.getOutputStream()); // Write workbook to response.
		workbook.close();


	}
	
	
	@RequestMapping(value="/runcrawlerapp", method=RequestMethod.POST)
	public String runCrawlerApp(@RequestParam("keyword") String keyword, @RequestParam("searchOption") String searchOption, RedirectAttributes redirectAttributes, HttpServletResponse response) throws IOException{

		try {
			Process ps=Runtime.getRuntime().exec(new String[]{"java","-jar","crawler.jar", keyword, searchOption});
			 final BufferedReader outputReader = new BufferedReader(new InputStreamReader(ps
			            .getInputStream()));
			    final BufferedReader errorReader = new BufferedReader(new InputStreamReader(ps
			            .getErrorStream()));

			    String line;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "error");
			return "redirect:/processlist"; 
		}
		redirectAttributes.addFlashAttribute("message", "Query submitted, reloading table..");
		redirectAttributes.addAttribute("_D", "5646456346");
		return "redirect:/processlist"; 
	}
	
	@RequestMapping("/viewdata")
	public String viewData(@RequestParam("crawlid") String crawlId, Model model){
		model.addAttribute("crawlid", crawlId);
		String curdir = System.getProperty("user.dir");
		File f = new File(curdir + "/crawldata" + "/" + crawlId + "/logn.txt");
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
