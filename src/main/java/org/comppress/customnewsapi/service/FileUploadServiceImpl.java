package org.comppress.customnewsapi.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.comppress.customnewsapi.entity.RssFeed;
import org.comppress.customnewsapi.exceptions.FileImportException;
import org.comppress.customnewsapi.repository.RssFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
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

    @Autowired
    public FileUploadServiceImpl(RssFeedRepository rssFeedRepository) {
        this.rssFeedRepository = rssFeedRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<List<RssFeed>> save(MultipartFile file) {

        log.info("LINKS IMPORT CSV IS PROCESSING {}", file.getName());

        List<CSVRecord> records = getRecords(file);
        List<RssFeed> rssFeeds = getRssFeeds(records);

        rssFeedRepository.saveAll(rssFeeds);

        return ResponseEntity.status(HttpStatus.CREATED).body(rssFeeds);
    }

    // Getting all the records
    private List<CSVRecord> getRecords(MultipartFile file) {

        try (
//                BufferedReader reader = new BufferedReader();
                CSVParser csvParser = new CSVParser(
                        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8),
                        CSVFormat.DEFAULT.withDelimiter(';'))
        ) {
            return csvParser.getRecords();
        } catch (IOException e) {
            throw new FileImportException("Failed to import csv file", file.getName());
        }
    }

    // Converting csv records to entities
    private List<RssFeed> getRssFeeds(List<CSVRecord> records) {

        List<RssFeed> rssFeeds = new ArrayList<>();

        records.forEach(record -> rssFeeds.add(RssFeed.builder()
                .source(record.get(SOURCE))
                .category(record.get(CATEGORY))
                .url(record.get(LINK)).build()));

        return rssFeeds;
    }
}
