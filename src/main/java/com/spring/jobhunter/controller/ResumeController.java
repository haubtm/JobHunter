package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.Company;
import com.spring.jobhunter.domain.Job;
import com.spring.jobhunter.domain.Resume;
import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.domain.response.resume.ResCreateResumeDTO;
import com.spring.jobhunter.domain.response.resume.ResResumeDTO;
import com.spring.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import com.spring.jobhunter.service.JobService;
import com.spring.jobhunter.service.ResumeService;
import com.spring.jobhunter.service.UserService;
import com.spring.jobhunter.util.SecurityUtil;
import com.spring.jobhunter.util.annotation.ApiMessage;
import com.spring.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private FilterBuilder filterBuilder;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    @GetMapping("/resumes")
    @ApiMessage("Get all resumes")
    public ResponseEntity<ResultPaginationDTO> getAllResume(
            @Filter Specification<Resume> spec, Pageable pageable) {
        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        User user = userService.getUserByUsername(email);
        if(user != null) {
            Company userCompany = user.getCompany();
            if(userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if(companyJobs != null && companyJobs.size() > 0)
                    arrJobIds = companyJobs.stream().map(x -> x.getId()).collect(Collectors.toList());
            }
        }
        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job")
                .in(filterBuilder.input(arrJobIds)).get());

        Specification<Resume> finalSpec = jobInSpec.and(spec);
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.getAllResume(finalSpec, pageable));
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get resume by id")
    public ResponseEntity<ResResumeDTO> getResumeById(@PathVariable("id") long id) throws IdInvalidException {
        Resume resume = resumeService.getResumeById(id);
        if(resume == null) {
            throw new IdInvalidException("Resume with id = " + id + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.convertToResResumeDTO(resume));
    }

    @PostMapping("/resumes")
    @ApiMessage("Create new resume")
    public ResponseEntity<ResCreateResumeDTO> saveResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        User user = userService.getUserById(resume.getUser().getId());
        if(user == null) {
            throw new IdInvalidException("User with id = " + resume.getUser().getId() + " not found");
        }

        Job job = jobService.getJobById(resume.getJob().getId());
        if(job == null) {
            throw new IdInvalidException("Job with id = " + resume.getJob().getId() + " not found");
        }
        resume.setUser(user);
        resume.setJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.saveResume(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.updateResume(resume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws IdInvalidException {
        if(resumeService.getResumeById(id) == null) {
            throw new IdInvalidException("Resume with id = " + id + " not found");
        }
        resumeService.deleteResume(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get resumes by user")
    public ResponseEntity<ResultPaginationDTO> getAllResumesByUser(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.getResumeByUser(pageable));
    }
}
