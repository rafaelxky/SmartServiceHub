package org.example.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.models.ServicePost;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AppServicePublicDto {
    public Long id;
    public String title;
    public String content;
    public Long creatorId;
    public LocalDateTime timestamp;

    public static AppServicePublicDto fromAppService(ServicePost servicePost){
        return new AppServicePublicDto(servicePost.getId(), servicePost.getTitle(), servicePost.getContent(), servicePost.getCreatorId(), servicePost.getTimestamp());
    }

    public static List<AppServicePublicDto> fromAppServiceList(List<ServicePost> servicePost) {
        List<AppServicePublicDto> appServiceList = new ArrayList<>();
        for (ServicePost service : servicePost){
            appServiceList.add(AppServicePublicDto.fromAppService(service));
        }
        return appServiceList;
    }
}
