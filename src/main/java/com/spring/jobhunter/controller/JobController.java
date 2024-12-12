package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.Job;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.domain.response.job.ResCreateJobDTO;
import com.spring.jobhunter.domain.response.job.ResUpdateJobDTO;
import com.spring.jobhunter.service.JobService;
import com.spring.jobhunter.util.annotation.ApiMessage;
import com.spring.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    @Autowired
    private JobService jobService;

    @GetMapping("/jobs")
    @ApiMessage("Get all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(
            @Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobs(spec, pageable));
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable("id") long id) throws IdInvalidException {
        Job job = jobService.getJobById(id);
        if(job == null) {
            throw new IdInvalidException("Job with id = " + id + " not found");
        }
        return ResponseEntity.ok(job);
    }

    @PostMapping("/jobs")
    @ApiMessage("Create new job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.saveJob(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("Update job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws IdInvalidException {
        if (jobService.getJobById(job.getId()) == null) {
            throw new IdInvalidException("Job with id = " + job.getId() + " not found");
        }
        return ResponseEntity.ok(jobService.updateJob(job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete job")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException {
        if(jobService.getJobById(id) == null) {
            throw new IdInvalidException("Job with id = " + id + " not found");
        }
        jobService.deleteJob(id);
        return ResponseEntity.ok().body(null);
    }
}
