package org.comppress.customnewsapi.service.fileupload;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.comppress.customnewsapi.dto.CategoryDto;
import org.comppress.customnewsapi.dto.CriteriaDto;
import org.comppress.customnewsapi.dto.PublisherDto;
import org.comppress.customnewsapi.entity.Category;
import org.comppress.customnewsapi.entity.Criteria;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.exceptions.FileImportException;
import org.comppress.customnewsapi.repository.CategoryRepository;
import org.comppress.customnewsapi.repository.CriteriaRepository;
import org.comppress.customnewsapi.repository.PublisherRepository;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.springframework.beans.BeanUtils;
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

    private final int RSS_FEED_SOURCE = 0;
    private final int RSS_FEED_CATEGORY = 1;
    private final int RSS_FEED_LINK = 2;
    private final int RSS_FEED_LANG = 3;

    private final int CRITERIA_ID = 0;
    private final int CRITERIA_NAME = 1;

    private final int CATEGORY_NAME = 0;
    private final int CATEGORY_SVG_URL = 1;

    private final int PUBLISHER_NAME = 0;
    private final int PUBLISHER_SVG_URL = 1;

    private final RssFeedRepository rssFeedRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final CriteriaRepository criteriaRepository;

    @Autowired
    public FileUploadServiceImpl(RssFeedRepository rssFeedRepository, PublisherRepository publisherRepository, CategoryRepository categoryRepository, CriteriaRepository criteriaRepository) {
        this.rssFeedRepository = rssFeedRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
        this.criteriaRepository = criteriaRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<List<RssFeed>> saveRssFeeds(MultipartFile file) {

        log.info("LINKS IMPORT CSV IS PROCESSING {}", file.getName());

        List<CSVRecord> csvRecordList = getRecords(file);
        List<RssFeed> rssFeedList = getRssFeeds(csvRecordList);
        List<RssFeed> finalRssFeedList = new ArrayList<>();
        // TODO Also update Feeds
        for (RssFeed rssFeed : rssFeedList) {
            if (rssFeedRepository.findByUrl(rssFeed.getUrl()).isEmpty()) finalRssFeedList.add(rssFeed);
        }
        try {
            rssFeedRepository.saveAll(finalRssFeedList);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO Logging
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(finalRssFeedList);
    }

    private List<CSVRecord> getRecords(MultipartFile file) {
        try {
            CSVParser csvParser = new CSVParser(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8), CSVFormat.DEFAULT.withDelimiter(';'));
            return csvParser.getRecords();
        } catch (IOException e) {
            throw new FileImportException("Failed to import csv file", file.getName());
        }
    }

    private List<RssFeed> getRssFeeds(List<CSVRecord> csvRecordList) {
        List<RssFeed> rssFeedList = new ArrayList<>();

        for (CSVRecord record : csvRecordList) {
            Publisher publisher = publisherRepository.findByName(record.get(RSS_FEED_SOURCE));
            if (publisher == null) {
                publisher = new Publisher(record.get(RSS_FEED_SOURCE),record.get(RSS_FEED_LANG),"");
                publisherRepository.save(publisher);
            }
            Category category = categoryRepository.findByName(record.get(RSS_FEED_CATEGORY));
            if (category == null) {
                category = new Category(record.get(RSS_FEED_CATEGORY), record.get(RSS_FEED_LANG),"");
                categoryRepository.save(category);
            }
            rssFeedList.add(RssFeed.builder()
                    .publisherId(publisher.getId())
                    .categoryId(category.getId())
                    .lang(record.get(RSS_FEED_LANG))
                    .url(record.get(RSS_FEED_LINK)).build());
        }

        return rssFeedList;
    }

    @Override
    public ResponseEntity<List<CriteriaDto>> saveCriteria(MultipartFile file) {
        log.info("LINKS IMPORT CSV IS PROCESSING {}", file.getName());

        List<CSVRecord> csvRecordList = getRecords(file);
        List<CriteriaDto> criteriaDtoList = new ArrayList<>();
        for(CSVRecord csvRecord:csvRecordList){
            if(criteriaRepository.existsById(Long.parseLong(csvRecord.get(CRITERIA_ID)))){
                continue;
            }
            Criteria criteria = new Criteria();
            criteria.setId(Long.parseLong(csvRecord.get(CRITERIA_ID)));
            criteria.setName(csvRecord.get(CRITERIA_NAME));
            criteriaRepository.save(criteria);
            CriteriaDto criteriaDto = new CriteriaDto();
            BeanUtils.copyProperties(criteria,criteriaDto);
            criteriaDtoList.add(criteriaDto);
        }
        return ResponseEntity.ok().body(criteriaDtoList);
    }

    @Override
    @Transactional
    public ResponseEntity<List<CategoryDto>> saveCategorySVGs(MultipartFile file) {
        log.info("LINKS IMPORT CSV IS PROCESSING {}", file.getName());

        List<CSVRecord> csvRecordList = getRecords(file);
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for(CSVRecord csvRecord:csvRecordList){
            Category category = categoryRepository.findByName(csvRecord.get(CATEGORY_NAME));
            if(category != null){
                category.setUrlToImage(csvRecord.get(CATEGORY_SVG_URL));
                CategoryDto categoryDto = new CategoryDto();
                BeanUtils.copyProperties(category, categoryDto);
                categoryDtoList.add(categoryDto);
            }
        }
        return ResponseEntity.ok().body(categoryDtoList);
    }

    @Override
    public ResponseEntity<List<PublisherDto>> savePublisherSVGs(MultipartFile file) {
        log.info("LINKS IMPORT CSV IS PROCESSING {}", file.getName());

        List<CSVRecord> csvRecordList = getRecords(file);
        List<PublisherDto> publisherDtoList = new ArrayList<>();
        for(CSVRecord csvRecord:csvRecordList){
            Publisher publisher = publisherRepository.findByName(csvRecord.get(PUBLISHER_NAME));
            if(publisher != null){
                publisher.setUrlToImage(csvRecord.get(PUBLISHER_SVG_URL));
                PublisherDto publisherDto = new PublisherDto();
                BeanUtils.copyProperties(publisher, publisherDto);
                publisherDtoList.add(publisherDto);
            }
        }
        return ResponseEntity.ok().body(publisherDtoList);
    }
}
