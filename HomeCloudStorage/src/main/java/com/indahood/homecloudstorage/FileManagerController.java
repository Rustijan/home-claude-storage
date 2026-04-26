package com.indahood.homecloudstorage;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// hands the api responses

import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class FileManagerController {
    @Autowired
    private FileStorageService fileStorageService;
    private static final Logger log = Logger.getLogger(FileManagerController.class.getName());


    @PostMapping("/upload-file")
    public boolean uploadfile(@RequestParam("file") MultipartFile file, @org.springframework.web.bind.annotation.RequestHeader("X-User-Name") String username) {
        try {
            fileStorageService.saveFile(file, username);
            return true;
        } catch (IOException e) {
//            throw new RuntimeException(e);
            log.log(Level.SEVERE, "Exception during upload", e);
        }
        return false;
    }

    @GetMapping("/Download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename, @org.springframework.web.bind.annotation.RequestHeader("X-User-Name") String username) {
        try {
            
            var fileToDownload = fileStorageService.getDownloadFile(filename, username);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= \"" + filename + "\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(Files.newInputStream(fileToDownload.toPath())));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception during download", e);
            return ResponseEntity.notFound().build();
        }
    }

    //        multipart download
    @GetMapping("/Download-faster")
    public ResponseEntity<Resource> downloadFileFaster(@RequestParam("filename") String filename, @org.springframework.web.bind.annotation.RequestHeader("X-User-Name") String username) {
        try {
            var fileToDownload = fileStorageService.getDownloadFile(filename, username);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= \"" + filename + "\"")
                    .contentLength(fileToDownload.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(fileToDownload));
        } catch (Exception e) {
//            throw new RuntimeException;
            return ResponseEntity.notFound().build();

        }
    }








}