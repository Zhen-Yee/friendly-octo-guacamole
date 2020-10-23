package com.example.demo.service;

import com.example.demo.model.JukeBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class JbApiService {

    public ResponseEntity<JukeBox[]> getJukes() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity entity = new HttpEntity(httpHeaders);

        log.info("Retrieving Jukeboxes from Jukebox API.");
        return restTemplate.exchange("http://my-json-server.typicode.com/touchtunes/tech-assignment/jukes", HttpMethod.GET, entity,  JukeBox[].class);
    }

}
