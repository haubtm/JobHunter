package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.Skill;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.service.SkillService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    @Autowired
    private SkillService skillService;

    @GetMapping("/skills")
    @ApiMessage("Get all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(
            @Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(skillService.getAllSkills(spec, pageable));
    }

    @GetMapping("/skills/{id}")
    @ApiMessage("Get skill by id")
    public ResponseEntity<Skill> getSkillById(@PathVariable("id") Long id) throws IdInvalidException {
        Skill skill = skillService.getSkillById(id);
        if(skill == null) {
            throw new IdInvalidException("Skill with id = " + id + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(skill);
    }

    @PostMapping("/skills")
    @ApiMessage("Create new skill")
    public ResponseEntity<Skill> saveSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        boolean isSkillExist = skillService.existsByName(skill.getName());
        if (isSkillExist) {
            throw new IdInvalidException("Skill " + skill.getName() + " da ton tai");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.save(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Skill currentSkill = skillService.getSkillById(skill.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill with id = " + skill.getId() + " not found");
        }
        if(skillService.existsByName(skill.getName())) {
            throw new IdInvalidException("Skill " + skill.getName() + " da ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(skillService.updateSkill(skill));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") Long id) throws IdInvalidException {
        Skill skill = skillService.getSkillById(id);
        if (skill == null) {
            throw new IdInvalidException("Skill with id = " + id + " not found");
        }
        skillService.deleteSkill(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
