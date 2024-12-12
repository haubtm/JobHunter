package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Job;
import com.spring.jobhunter.domain.Skill;
import com.spring.jobhunter.domain.response.job.ResCreateJobDTO;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.domain.response.job.ResUpdateJobDTO;
import com.spring.jobhunter.repository.JobRepository;
import com.spring.jobhunter.repository.SkillRepository;
import com.spring.jobhunter.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public ResultPaginationDTO getAllJobs(Specification<Job> jobSpecification, Pageable pageable) {
        Page<Job> pageJob = jobRepository.findAll(jobSpecification, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageJob.getTotalPages());
        meta.setTotal(pageJob.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageJob.getContent());

        return rs;
    }

    @Override
    public Job getJobById(long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public ResCreateJobDTO saveJob(Job job) {
        // Giu lai cac skill ton tai trong db
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x-> x.getId())
                    .collect(Collectors.toList());
            List<Skill> skills = skillRepository.findByIdIn(reqSkills);
            job.setSkills(skills);
        }

        // Luu job
        Job currentJob = jobRepository.save(job);

        // Tao response
        ResCreateJobDTO res = new ResCreateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLocation(currentJob.getLocation());
        res.setLevel(currentJob.getLevel());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setCreatedBy(currentJob.getCreatedBy());

        if(currentJob.getSkills() != null){
            List<String> skills = currentJob.getSkills()
                    .stream().map(item-> item.getName())
                    .collect(Collectors.toList());
            res.setSkills(skills);
        }

        return res;
    }

    @Override
    public ResUpdateJobDTO updateJob(Job job) {
        // Giu lai cac skill ton tai trong db
        if(job.getSkills() != null){
            List<Long> reqSkills = job.getSkills()
                    .stream().map(x-> x.getId())
                    .collect(Collectors.toList());
            List<Skill> skills = skillRepository.findByIdIn(reqSkills);
            job.setSkills(skills);
        }

        // Luu job
        Job currentJob = jobRepository.save(job);

        // Tao response
        ResUpdateJobDTO res = new ResUpdateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLocation(currentJob.getLocation());
        res.setLevel(currentJob.getLevel());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setCreatedBy(currentJob.getCreatedBy());

        if(currentJob.getSkills() != null){
            List<String> skills = currentJob.getSkills()
                    .stream().map(item-> item.getName())
                    .collect(Collectors.toList());
            res.setSkills(skills);
        }

        return res;
    }

    @Override
    public void deleteJob(long id) {
        jobRepository.deleteById(id);
    }
}
