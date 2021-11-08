package org.comppress.customnewsapi.service.fileupload;

import org.comppress.customnewsapi.entity.RssFeed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    ResponseEntity<List<RssFeed>> save(MultipartFile file);

}
