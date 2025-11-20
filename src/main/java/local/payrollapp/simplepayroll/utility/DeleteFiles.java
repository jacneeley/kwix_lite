package local.payrollapp.simplepayroll.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import local.payrollapp.simplepayroll.GlobalConstants;

@Service
public class DeleteFiles {
	private static final Logger log = LoggerFactory.getLogger(DeleteFiles.class);
	
	public void deleteFile(String filePath, String fileName) {
		String fPath = String.format("%s/%s", filePath, fileName);
		Path path = FileSystems.getDefault().getPath(fPath);
		try {
			Files.delete(path);
		}
		catch(NoSuchFileException e) {
			log.error(System.err.format("%s: no such file or dir...", filePath + fileName).toString());
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFiles() {
		log.info("Checking for stale files...");
		String path = GlobalConstants.TMP;
		File dir = new File(path);
		if(dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (Integer.valueOf(files.length).equals(0)) {
				log.warn("tmp folder was empty...");
				log.warn("'dummy' file was missing, this may cause 'export' function to fail.");
				return;
			}
			
			if (Integer.valueOf(files.length).equals(1)){
				log.info("No files to delete.");
				return;
			}
			
			if (Integer.valueOf(files.length) > 1){
				for (File f : files) {
					if(!f.getName().equals("dummy.txt")) {
						deleteFile(dir.toString(), f.getName());
						log.info("Files Cleared!");
					}	
				}
			}
		}
		else {
			log.warn("data/sheets/ dir could not be found...");
		}
	}
}