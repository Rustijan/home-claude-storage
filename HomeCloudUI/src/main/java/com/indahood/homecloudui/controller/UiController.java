package com.indahood.homecloudui.controller;

import com.indahood.homecloudui.client.StorageClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UiController {

    private final StorageClient storageClient;

    public UiController(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    // UI page for listing files
    @GetMapping("/list-files")
    public String listFiles(Model model) {
        model.addAttribute("files", storageClient.getFiles());
        return "list_files"; // list-files.html
    }

    // UI download endpoint (browser calls UI, UI calls storage)
    @GetMapping("/Download")
    public ResponseEntity<byte[]> download(@RequestParam String filename) {
        byte[] data = storageClient.downloadFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(data);
    }

    // UI upload page
    @GetMapping("/uploader")
    public String uploaderPage() {
        return "uploader"; // uploader.html
    }

    // UI upload handler (forwards to storage)
    @PostMapping("/upload-file")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        storageClient.uploadFile(file.getBytes(), file.getOriginalFilename());
        return "redirect:/list-files";
    }
}