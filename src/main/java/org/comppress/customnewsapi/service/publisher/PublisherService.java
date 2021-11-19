package org.comppress.customnewsapi.service.publisher;

import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.dto.PublisherDto;
import org.comppress.customnewsapi.dto.PublisherUserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PublisherService {

    ResponseEntity<GenericPage<PublisherDto>> getPublisher(String lang, int page, int size);

    ResponseEntity<GenericPage<PublisherUserDto>> getPublisherUser(String lang, int page, int size);
}
