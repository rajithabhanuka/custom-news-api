package org.comppress.customnewsapi.service.publisher;

import org.comppress.customnewsapi.dto.GenericPage;
import org.comppress.customnewsapi.dto.PublisherDto;
import org.comppress.customnewsapi.entity.Publisher;
import org.comppress.customnewsapi.mapper.MapstructMapper;
import org.comppress.customnewsapi.repository.PublisherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GenericPage<PublisherDto>> getPublisher(String lang, int page, int size) {
        Page<Publisher> publisherPage = publisherRepository.findByLang(lang, PageRequest.of(page, size));

        GenericPage<PublisherDto> genericPage = new GenericPage<>();
        genericPage.setData(publisherPage.stream().map(publisher -> mapstructMapper.publisherToPublisherDto(publisher)).collect(Collectors.toList()));
        BeanUtils.copyProperties(publisherPage, genericPage);

        return ResponseEntity.status(HttpStatus.OK).body(genericPage);
    }
}
