package com.indahood.homecloudstorage;

//makes the download stufff
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {
//    for uploading
    public static final String STORAGE_DIRECTORY= System.getProperty("user.home") + "/My_files/storage";

    public FileStorageService() {
        try {
            Files.createDirectories(Path.of(STORAGE_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    public void saveFile(MultipartFile fileToSave, String username) throws IOException {
//        in case error
        if (fileToSave ==null){
            throw new NullPointerException("fileToSave is null");
        }
        Path storagePath = Path.of(STORAGE_DIRECTORY, username).toAbsolutePath().normalize();
        Files.createDirectories(storagePath); // Ensure user directory exists
        Path destinationFile = storagePath.resolve(Path.of(Objects.requireNonNull(fileToSave.getOriginalFilename()))).normalize();

//        security problem
        if (!destinationFile.startsWith(storagePath)){
            throw new SecurityException("Unsupported filename");
        }
//        copying the content of the multiple file(filetosave) into the target file
        Files.copy(fileToSave.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

    }


//    for downloading
    public File getDownloadFile(String fileName, String username) throws  Exception{
        if (fileName == null){
            throw new NullPointerException("fileName is null");
        }
        Path storagePath = Path.of(STORAGE_DIRECTORY, username).toAbsolutePath().normalize();
        Path fileToDownload = storagePath.resolve(Path.of(fileName)).normalize();

//        security layer
        if(!fileToDownload.startsWith(storagePath)){
            throw  new SecurityException("Unsupported filename!");
        }

        File targetFile = fileToDownload.toFile();
        if(!targetFile.exists()){
            throw new FileNotFoundException("No file named" +fileName);
        }

        return targetFile;
    }





}
