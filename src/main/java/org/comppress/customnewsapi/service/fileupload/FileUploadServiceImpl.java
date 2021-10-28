package org.comppress.customnewsapi.service.fileupload;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.exceptions.FileImportException;
import org.comppress.customnewsapi.repository.CategoryRepository;
import org.comppress.customnewsapi.repository.PublisherRepository;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final int SOURCE = 0;
    private final int CATEGORY = 1;
    private final int LINK = 2;

    private final RssFeedRepository rssFeedRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public FileUploadServiceImpl(RssFeedRepository rssFeedRepository, PublisherRepository publisherRepository, CategoryRepository categoryRepository) {
        this.rssFeedRepository = rssFeedRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<List<RssFeed>> save(MultipartFile file) {
        
        log.info("LINKS IMPORT CSV IS PROCESSING {}", file.getName());
        
        List<CSVRecord> csvRecordList = getRecords(file);
        List<RssFeed> rssFeedList = getRssFeeds(csvRecordList);

        rssFeedRepository.saveAll(rssFeedList);

        return ResponseEntity.status(HttpStatus.CREATED).body(rssFeedList);
    }

    private List<CSVRecord> getRecords(MultipartFile file) {
        try{
            CSVParser csvParser = new CSVParser(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8), CSVFormat.DEFAULT.withDelimiter(';'));
            return csvParser.getRecords();
        } catch (IOException e) {
           throw new FileImportException("Failed to import csv file", file.getName());
        }
    }

    private List<RssFeed> getRssFeeds(List<CSVRecord> csvRecordList) {
        List<RssFeed> rssFeedList = new ArrayList<>();

        csvRecordList.forEach(record -> {
            Publisher publisher = publisherRepository.findByName(record.get(SOURCE));
            if(publisher == null){
                publisher = new Publisher(record.get(SOURCE));
            }
            Category category = categoryRepository.findByName(record.get(CATEGORY));

            rssFeedList.add(RssFeed.builder()
                    .publisherId(publisher.getId())
                    .categoryId(category.getId())
                    .url(record.get(LINK)).build());
        });

        return rssFeedList;
    }
}
