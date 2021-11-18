package org.comppress.customnewsapi.service.fileupload;

import org.comppress.customnewsapi.dto.CriteriaDto;
import org.comppress.customnewsapi.entity.RssFeed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    ResponseEntity<List<RssFeed>> saveRssFeeds(MultipartFile file);
    ResponseEntity<List<CriteriaDto>> saveCriteria(MultipartFile file);

}
