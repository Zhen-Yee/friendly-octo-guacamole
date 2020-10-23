package com.example.demo.service;

import com.example.demo.model.Settings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SettingApiService {

    public ResponseEntity<Settings> getSettings() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity entity = new HttpEntity(httpHeaders);

        log.info("Retrieving settings from Setting API.");
        return restTemplate.exchange("http://my-json-server.typicode.com/touchtunes/tech-assignment/settings", HttpMethod.GET, entity,  Settings.class);
    }

}
