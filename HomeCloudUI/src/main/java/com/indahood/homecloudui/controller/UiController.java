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
    public String listFiles(Model model, java.security.Principal principal) {
        model.addAttribute("files", storageClient.getFiles(principal.getName()));
        return "list_files"; // list-files.html
    }

    // UI download endpoint (browser calls UI, UI calls storage)
    @GetMapping("/Download")
    public void download(@RequestParam String filename, jakarta.servlet.http.HttpServletResponse response, java.security.Principal principal) throws java.io.IOException {
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        storageClient.downloadFile(filename, principal.getName(), response.getOutputStream());
    }

    // UI upload page
    @GetMapping("/uploader")
    public String uploaderPage() {
        return "uploader"; // uploader.html
    }

    @PostMapping("/upload-file")
    public String upload(@RequestParam("file") MultipartFile file, java.security.Principal principal) throws Exception {
        storageClient.uploadFile(file.getResource(), file.getOriginalFilename(), principal.getName());
        return "redirect:/list-files";
    }
}