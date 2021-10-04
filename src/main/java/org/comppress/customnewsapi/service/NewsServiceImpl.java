package org.comppress.customnewsapi.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.comppress.customnewsapi.dto.RssDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsServiceImpl implements NewsService {

    @Override
    public ResponseEntity<String> getNews() {

        try {
            RestTemplate restTemplate = new RestTemplate();

            String rssFeedUrl = "https://www.spiegel.de/sport/fussball/index.rss";

            ResponseEntity<String> response
                    = restTemplate.getForEntity(rssFeedUrl, String.class);

            if (response == null) {
                // TODO
            }

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            RssDto rssDto = xmlMapper.readValue(response.getBody(), RssDto.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
