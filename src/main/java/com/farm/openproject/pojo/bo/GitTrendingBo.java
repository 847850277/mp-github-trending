package com.farm.openproject.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author zhengpeng
 * @date 2021-09-21 09:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GitTrendingBo {

    private String url;

    //private String author;

    private String language;

    private String starts;

    private String todayStars;

    private String forks;

    private String description;



    public String getLanguage() {
        if(Objects.nonNull(language)){
            if(StringUtils.EMPTY.equals(StringUtils.trim(language))){
                return null;
            }
        }
        return language;
    }



    public String getDescription() {
        if(Objects.nonNull(description)){
            if(StringUtils.EMPTY.equals(StringUtils.trim(description))){
                return null;
            }
        }
        return description;
    }
}
