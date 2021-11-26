package org.comppress.customnewsapi.service.publisher;

import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.dto.PublisherDto;
import org.springframework.http.ResponseEntity;

public interface PublisherService {

    ResponseEntity<GenericPage<PublisherDto>> getPublisher(String lang, int page, int size);

    ResponseEntity<GenericPage> getPublisherUser(String lang, int page, int size);
}
