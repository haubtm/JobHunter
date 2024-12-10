package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Company;
import com.spring.jobhunter.repository.CompanyRepository;
import com.spring.jobhunter.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Override
    public Company saveOrUpdateCompany(Company company) {
        return companyRepository.save(company);
    }
}
