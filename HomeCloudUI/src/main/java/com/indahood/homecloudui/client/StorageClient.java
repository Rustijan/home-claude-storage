package com.indahood.homecloudui.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.List;


@Service
public class StorageClient {
//    A built-in Spring class used to make HTTP requests
    private final RestTemplate restTemplate = new RestTemplate();

    // URL of your HomecloudStorage app
    @Value("${storage.url}")
    private String STORAGE_URL;

//    private final String STORAGE_URL = "http://localhost:8080";
//    private final String STORAGE_URL = "https://dispiteously-overelliptical-brady.ngrok-free.dev";

    // Call: GET http://localhost:8080/list-files
    public List<String> getFiles() {
        return restTemplate.getForObject(
                STORAGE_URL + "/api/files",
                List.class
        );
    }

    // Call: GET http://localhost:8080/Download?filename=x
    public byte[] downloadFile(String filename) {
        return restTemplate.getForObject(
                STORAGE_URL + "/Download?filename=" + filename,
                byte[].class
        );
    }
    public void uploadFile(byte[] data, String filename) {

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("file", new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return filename;
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        restTemplate.postForEntity(
                STORAGE_URL + "/upload-file",
                request,
                String.class
        );
    }







    }




