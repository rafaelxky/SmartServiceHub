package org.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.util.stream.Stream;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;


    @GetMapping("/")
    @ResponseBody
    public String listAllEndpoints() {
        Set<RequestMappingInfo> mappings = handlerMapping.getHandlerMethods().keySet();

        String endpoints = mappings.stream()
                .flatMap(mapping -> {
                    if (mapping.getPatternsCondition() != null) {
                        return mapping.getPatternsCondition().getPatterns().stream();
                    } else if (mapping.getPathPatternsCondition() != null) {
                        return mapping.getPathPatternsCondition().getPatterns().stream()
                                .map(Object::toString);
                    } else {
                        return Stream.<String>empty();
                    }
                })
                
                .sorted()
                .collect(Collectors.joining("<br>"));

        return endpoints;
    }
}
