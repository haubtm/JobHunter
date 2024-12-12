package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.Job;
import com.spring.jobhunter.domain.response.job.ResCreateJobDTO;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.domain.response.job.ResUpdateJobDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface JobService {
    public ResultPaginationDTO getAllJobs(Specification<Job> jobSpecification, Pageable pageable);
    public Job getJobById(long id);
    public ResCreateJobDTO saveJob(Job job);
    public ResUpdateJobDTO updateJob(Job job);
    public void deleteJob(long id);
}
