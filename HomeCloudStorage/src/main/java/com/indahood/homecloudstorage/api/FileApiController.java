package com.indahood.homecloudstorage.api;

import com.indahood.homecloudstorage.FileStorageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
public class FileApiController {
    @GetMapping("/files")
    public List<String> listFiles() throws IOException {
        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(Path.of(FileStorageService.STORAGE_DIRECTORY))) {

        return StreamSupport.stream(stream.spliterator(), false)
                .map(Path::getFileName)
                .map(Path::toString)
                .toList();
        }
    }



}
