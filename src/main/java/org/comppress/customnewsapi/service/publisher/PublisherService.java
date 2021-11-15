package org.comppress.customnewsapi.service.publisher;

import org.comppress.customnewsapi.dto.PublisherDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PublisherService {

    ResponseEntity<List<PublisherDto>> getPublisher();

}
