package org.comppress.customnewsapi.service.publisher;

import org.comppress.customnewsapi.dto.PublisherDto;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final MapstructMapper mapstructMapper;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository, MapstructMapper mapstructMapper) {
        this.publisherRepository = publisherRepository;
        this.mapstructMapper = mapstructMapper;
    }

    @Override
    public ResponseEntity<List<PublisherDto>> getPublisher() {
        List<Publisher> publisherList = publisherRepository.findAll();
        List<PublisherDto> publisherDtoList = publisherList.stream().map(publisher -> mapstructMapper.publisherToPublisherDto(publisher)).collect(Collectors.toList());
        return ResponseEntity.ok().body(publisherDtoList);
    }
}
