package com.spring.jobhunter.domain.response.resume;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateResumeDTO {
    private Long id;
    private Instant createdDate;
    private String createdBy;
}
