package com.pingjin.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ContentWithTags {
    private String content;
    private String originContent;
    private final List<TagElement> tags = new ArrayList<>();

    @Data
    public static class TagElement {
        //mktsymbol
        private String tagName;
        //{"symbol":"BABA.US"}
        private final Map<String, String> attributes = new HashMap<>();
    }
}
