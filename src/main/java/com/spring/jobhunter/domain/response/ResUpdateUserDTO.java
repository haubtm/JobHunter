package com.spring.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.jobhunter.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String username;
    private GenderEnum gender;
    private String address;
    private int age;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GTM+7")
    private Instant updatedAt;

    private CompanyUser company;

    @Getter
    @Setter
    public static class CompanyUser{
        private long id;
        private String name;
    }
}
