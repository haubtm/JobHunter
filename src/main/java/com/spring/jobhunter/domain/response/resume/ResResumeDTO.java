package com.spring.jobhunter.domain.response.resume;

import com.spring.jobhunter.util.constant.ResumeStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResResumeDTO {
    private long id;

    private String email;
    private String url;
    private ResumeStatusEnum status;

    private Instant createdAt;
    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;
    private UserResume user;
    private JobResume job;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResume{
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobResume{
        private long id;
        private String name;
    }
}
