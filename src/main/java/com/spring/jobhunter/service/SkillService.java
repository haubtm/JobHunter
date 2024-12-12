package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.Skill;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SkillService {
    public ResultPaginationDTO getAllSkills(Specification<Skill> skillSpecification, Pageable pageable);
    public Skill getSkillById(long id);
    public boolean existsByName(String name);
    public Skill save(Skill skill);
    public Skill updateSkill(Skill skill);
    public void deleteSkill(long id);
}
