package com.spring.jobhunter.domain.response.resume;

import com.spring.jobhunter.util.constant.ResumeStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateResumeDTO {
    private Long id;
    private ResumeStatusEnum status;
    private Instant updatedAt;
    private String updatedBy;
}
