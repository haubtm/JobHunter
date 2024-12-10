package com.spring.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.jobhunter.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String address;
    private String logo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GTM+7")
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeInsert() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().orElse("system");
        this.createdAt = Instant.now();
    }
}
