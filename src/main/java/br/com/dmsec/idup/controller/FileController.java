package br.com.dmsec.idup.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.dmsec.idup.models.ListHashs;
import br.com.dmsec.idup.payload.UploadFileResponse;
import br.com.dmsec.idup.property.FileStorageProperties;
import br.com.dmsec.idup.property.IpfsProperties;
import br.com.dmsec.idup.service.FileStorageService;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import javax.servlet.http.HttpServletRequest;


import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageProperties properties;
    private FileStorageService fileStorageService;
    private boolean createDir = true;
    
    @Autowired
    HashController hashController;
    
    @Autowired
    private IpfsProperties configIPFS;
    
    private IPFS ipfs;
    
    

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("packageName") String packageName) {
            	
    	
    	if(isCreateDir()) {
    		properties.setUploadDir(properties.getUploadDir()+"/"+packageName);
        	fileStorageService = new FileStorageService(properties);
    	}
    	
    	    	    	
    	String fileName = fileStorageService.storeFile(file);
                

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
               
        
        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }
    
    
    
    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("packageName") String packageName) {
    	this.properties.setUploadDir(properties.getUploadDir()+"/"+packageName);
    	this.fileStorageService = new FileStorageService(properties);
    	setCreateDir(false);
    	
    	
    	List<UploadFileResponse> listFileResponse = Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file, packageName))
                .collect(Collectors.toList());
    	 
        try {
        	
        	File f = new File(properties.getUploadDir());
        	NamedStreamable.FileWrapper streamable = new NamedStreamable.FileWrapper(f);
			List<NamedStreamable> list = streamable.getChildren();
			ipfs = new IPFS(configIPFS.getMultiAddr());
			List<MerkleNode> nodes = ipfs.add(list, true,false);
			
			List<String> hashs = nodes.stream().map(node -> node.hash.toString()).collect(Collectors.toList());
			for (String value : hashs)
			{
			  System.out.println("Value of element :"+ value);
			}
			System.out.println(hashs.get(hashs.size()-1));
		    
			ListHashs listHashsModel = new ListHashs();
			listHashsModel.setHash(hashs.get(hashs.size()-1));
			listHashsModel.setHorario(LocalTime.now());
			listHashsModel.setName(packageName);
		
			
			hashController.criarListaRest(listHashsModel);
			
			
			deleteDirectory(f);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	return listFileResponse;
    			    			
    }

    private void deleteDirectory(File f) {
    	String[]entries = f.list();
		for(String s: entries){
		    File currentFile = new File(f.getPath(),s);
		    currentFile.delete();
		}
		f.delete();
	}



	@GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    
    //TO-DO
        


	public boolean isCreateDir() {
		return createDir;
	}




	public void setCreateDir(boolean createDir) {
		this.createDir = createDir;
	}



	

}
