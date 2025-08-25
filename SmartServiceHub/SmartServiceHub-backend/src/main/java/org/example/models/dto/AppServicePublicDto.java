package org.example.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.models.AppService;

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

    public static AppServicePublicDto fromAppService(AppService appService){
        return new AppServicePublicDto(appService.getId(), appService.getTitle(), appService.getContent(), appService.getCreatorId(), appService.getTimestamp());
    }

    public static List<AppServicePublicDto> fromAppServiceList(List<AppService> appService) {
        List<AppServicePublicDto> appServiceList = new ArrayList<>();
        for (AppService service : appService){
            appServiceList.add(AppServicePublicDto.fromAppService(service));
        }
        return appServiceList;
    }
}
