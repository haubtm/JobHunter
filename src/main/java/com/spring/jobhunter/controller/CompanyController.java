package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.Company;
import com.spring.jobhunter.domain.dto.ResultPaginationDTO;
import com.spring.jobhunter.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @RequestParam("current") Optional<String> current,
            @RequestParam("pageSize") Optional<String> pageSize
    ) {
        String sCurrent = current.orElse("1");
        String sPageSize = pageSize.orElse("10");
        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));
        ResultPaginationDTO rs = companyService.getAllCompanies(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> saveCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.saveCompany(company));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.updateCompany(company));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
