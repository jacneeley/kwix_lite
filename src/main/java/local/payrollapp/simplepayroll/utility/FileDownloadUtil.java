package local.payrollapp.simplepayroll.utility;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import local.payrollapp.simplepayroll.GlobalConstants;

@RestController
public class FileDownloadUtil {
	private static final Logger log = LoggerFactory.getLogger(FileDownloadUtil.class);
	private String path = GlobalConstants.TMP;
	private final Path fileStorageLocation = Paths.get(path);
	
	@GetMapping("/download/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename){
		try {
			Path filePath = this.fileStorageLocation.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if(!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.notFound().build();
			}
			
			log.info(String.format("Downloaded %s -- ", filename.toString()) + LocalDateTime.now().toString());
			return ResponseEntity.ok()
					.header("Content-Disposition", "attatchment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} catch (Exception e) {
			log.error("Download failed -- " + LocalDateTime.now().toString());
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}