package org.comppress.customnewsapi.service.topic;

import org.comppress.customnewsapi.dto.GenericPage;
import org.springframework.http.ResponseEntity;

public interface TopicService {
    ResponseEntity<GenericPage> getTopic(int page, int size, Long topicId, String lang, String fromDate, String toDate);
}
