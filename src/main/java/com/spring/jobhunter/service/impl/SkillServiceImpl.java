package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Skill;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.repository.SkillRepository;
import com.spring.jobhunter.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {
    @Autowired
    private SkillRepository skillRepository;

    @Override
    public ResultPaginationDTO getAllSkills(Specification<Skill> skillSpecification, Pageable pageable) {
        Page<Skill> pageSkill = skillRepository.findAll(skillSpecification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageSkill.getTotalPages());
        meta.setTotal(pageSkill.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageSkill.getContent());
        return rs;
    }

    @Override
    public Skill getSkillById(long id) {
        return skillRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsByName(String name) {
        return skillRepository.existsByName(name);
    }

    @Override
    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public Skill updateSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public void deleteSkill(long id) {
        Skill currentSkill = skillRepository.findById(id).orElse(null);
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));
        skillRepository.delete(currentSkill);
    }
}
