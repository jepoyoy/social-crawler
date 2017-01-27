package com.thakralone.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.thakralone.pojo.CrawlerProcessItem;

public class FileUtil {
	
	
	
	public static ArrayList<CrawlerProcessItem> getDirContents(String crawlFolder){
		
		ArrayList<CrawlerProcessItem> processFolders = new ArrayList<CrawlerProcessItem>();
		
		File folder = new File(crawlFolder);
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	    	
	       String folderData = listOfFiles[i].getName();
	       
	       if(folderData.equals(".DS_Store")){continue;}
	        
	       CrawlerProcessItem cpi = new CrawlerProcessItem(folderData);
	       processFolders.add(cpi);
	    }
	    
	    return processFolders;
	}
	
	public static String grep(Reader inReader, String searchFor) throws IOException {
	    BufferedReader reader = null;
	    try {
	        reader = new BufferedReader(inReader);
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.contains(searchFor)) {
	                return line;
	            }
	        }
	    } finally {
	        if (reader != null) {
	            reader.close();
	        }
	    }
		return "";
	}
	
	public static String getLastLineFast(final File file) {
        // file needs to exist
        if (file.exists() == false || file.isDirectory()) {
                return "";
        }

        // avoid empty files
        if (file.length() <= 2) {
                return "";
        }

        // open the file for read-only mode
        try {
            RandomAccessFile fileAccess = new RandomAccessFile(file, "r");
            char breakLine = '\n';
            // offset of the current filesystem block - start with the last one
            long blockStart = (file.length() - 1) / 4096 * 4096;
            // hold the current block
            byte[] currentBlock = new byte[(int) (file.length() - blockStart)];
            // later (previously read) blocks
            List<byte[]> laterBlocks = new ArrayList<byte[]>();
            while (blockStart >= 0) {
                fileAccess.seek(blockStart);
                fileAccess.readFully(currentBlock);
                // ignore the last 2 bytes of the block if it is the first one
                int lengthToScan = currentBlock.length - (laterBlocks.isEmpty() ? 2 : 0);
                for (int i = lengthToScan - 1; i >= 0; i--) {
                    if (currentBlock[i] == breakLine) {
                        // we found our end of line!
                        StringBuilder result = new StringBuilder();
                        // RandomAccessFile#readLine uses ISO-8859-1, therefore
                        // we do here too
                        result.append(new String(currentBlock, i + 1, currentBlock.length - (i + 1), "ISO-8859-1"));
                        for (byte[] laterBlock : laterBlocks) {
                                result.append(new String(laterBlock, "ISO-8859-1"));
                        }
                        // maybe we had a newline at end of file? Strip it.
                        if (result.charAt(result.length() - 1) == breakLine) {
                                // newline can be \r\n or \n, so check which one to strip
                                int newlineLength = result.charAt(result.length() - 2) == '\r' ? 2 : 1;
                                result.setLength(result.length() - newlineLength);
                        }
                        return result.toString();
                    }
                }
                // no end of line found - we need to read more
                laterBlocks.add(0, currentBlock);
                blockStart -= 4096;
                currentBlock = new byte[4096];
            }
        } catch (Exception ex) {
                ex.printStackTrace();
        }
        // oops, no line break found or some exception happened
        return "";
    }

}
