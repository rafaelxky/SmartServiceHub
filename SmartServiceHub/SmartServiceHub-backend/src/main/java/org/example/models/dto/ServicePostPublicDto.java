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
public class ServicePostPublicDto {
    public Long id;
    public String title;
    public String content;
    public Long creatorId;
    public LocalDateTime timestamp;

    public static ServicePostPublicDto fromAppService(ServicePost servicePost){
        return new ServicePostPublicDto(servicePost.getId(), servicePost.getTitle(), servicePost.getContent(), servicePost.getCreatorId(), servicePost.getTimestamp());
    }

    public static List<ServicePostPublicDto> fromAppServiceList(List<ServicePost> servicePost) {
        List<ServicePostPublicDto> appServiceList = new ArrayList<>();
        for (ServicePost service : servicePost){
            appServiceList.add(ServicePostPublicDto.fromAppService(service));
        }
        return appServiceList;
    }
}
