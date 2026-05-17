package com.github.sanchezih.shopping.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/meta")
public class MetadataController {

    @Value("${meta.application}")
    private String application;

    @Value("${meta.environment}")
    private String environment;

    @Value("${meta.version}")
    private String version;

    @GetMapping
    public Map<String, String> getMetadata() {
        Map<String, String> meta = new HashMap<>();
        meta.put("application", application);
        meta.put("environment", environment);
        meta.put("version", version);
        return meta;
    }
}