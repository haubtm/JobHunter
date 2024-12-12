package com.spring.jobhunter.domain.dto;

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
    private Instant updatedAt;
}
