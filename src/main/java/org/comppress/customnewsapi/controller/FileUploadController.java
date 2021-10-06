package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping(value = "files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/links", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<RssFeed>> save(@RequestParam("file") MultipartFile file) {
        return fileUploadService.save(file);
    }
}
