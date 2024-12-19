package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.Resume;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.domain.response.resume.ResCreateResumeDTO;
import com.spring.jobhunter.domain.response.resume.ResResumeDTO;
import com.spring.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ResumeService {
    ResultPaginationDTO getAllResume(Specification<Resume> specification, Pageable pageable);
    Resume getResumeById(long id);
    ResCreateResumeDTO saveResume(Resume resume);
    ResUpdateResumeDTO updateResume(Resume resume);
    void deleteResume(long id);
    ResResumeDTO convertToResResumeDTO(Resume resume);
    ResultPaginationDTO getResumeByUser(Pageable pageable);
}
