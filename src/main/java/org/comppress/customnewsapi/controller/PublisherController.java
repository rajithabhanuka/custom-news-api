package org.comppress.customnewsapi.controller;

import org.comppress.customnewsapi.dto.CategoryDto;
import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.dto.PublisherDto;
import org.comppress.customnewsapi.service.publisher.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<GenericPage<PublisherDto>> getPublisher(
            @RequestParam(value = "lang", required = false, defaultValue = "en") String lang,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    ){
        return publisherService.getPublisher(lang, page, size);
    }

}
