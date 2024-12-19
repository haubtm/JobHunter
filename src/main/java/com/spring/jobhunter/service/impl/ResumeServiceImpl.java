package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Resume;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.domain.response.resume.ResCreateResumeDTO;
import com.spring.jobhunter.domain.response.resume.ResResumeDTO;
import com.spring.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import com.spring.jobhunter.repository.ResumeRepository;
import com.spring.jobhunter.service.ResumeService;
import com.spring.jobhunter.util.SecurityUtil;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    @Override
    public ResultPaginationDTO getAllResume(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> pageResume = resumeRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());

        rs.setMeta(meta);

        List<ResResumeDTO> listRes = pageResume.stream()
                .map(item -> convertToResResumeDTO(item)).collect(Collectors.toList());
        rs.setResult(listRes);
        return rs;
    }

    @Override
    public Resume getResumeById(long id) {
        return resumeRepository.findById(id).orElse(null);
    }

    @Override
    public ResCreateResumeDTO saveResume(Resume resume) {
        Resume resume1 = resumeRepository.save(resume);
        ResCreateResumeDTO res = new ResCreateResumeDTO();
        res.setId(resume1.getId());
        res.setCreatedDate(resume1.getCreatedAt());
        res.setCreatedBy(resume1.getCreatedBy());
        return res;
    }

    @Override
    public ResUpdateResumeDTO updateResume(Resume resume) {
        Resume currentResume = resumeRepository.findById(resume.getId()).orElse(null);
        if (currentResume != null) {
            currentResume.setStatus(resume.getStatus());

            resumeRepository.save(currentResume);
            ResUpdateResumeDTO res = new ResUpdateResumeDTO();
            res.setId(currentResume.getId());
            res.setStatus(currentResume.getStatus());
            res.setUpdatedAt(currentResume.getUpdatedAt());
            res.setUpdatedBy(currentResume.getUpdatedBy());
            return res;
        }
        return null;
    }

    @Override
    public void deleteResume(long id) {
        resumeRepository.deleteById(id);
    }

    @Override
    public ResResumeDTO convertToResResumeDTO(Resume resume) {
        ResResumeDTO res = new ResResumeDTO();
        ResResumeDTO.JobResume resJob = new ResResumeDTO.JobResume();
        ResResumeDTO.UserResume resUser = new ResResumeDTO.UserResume();

        resJob.setId(resume.getId());
        resJob.setName(resume.getJob().getName());

        resUser.setId(resume.getId());
        resUser.setName(resume.getUser().getUsername());

        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedBy(resume.getUpdatedBy());

        res.setJob(resJob);
        res.setUser(resUser);

        return res;
    }

    @Override
    public ResultPaginationDTO getResumeByUser(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        FilterNode filterNode = filterParser.parse("email ='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(filterNode);
        Page<Resume> pageResume = resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());

        rs.setMeta(meta);

        // remove sensitive data
        List<ResResumeDTO> listResume = pageResume.getContent()
                .stream().map(item -> convertToResResumeDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;

    }
}
