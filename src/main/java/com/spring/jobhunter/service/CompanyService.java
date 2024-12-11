package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.Company;
import com.spring.jobhunter.domain.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {
    ResultPaginationDTO getAllCompanies(Pageable pageable);
    Company getCompanyById(long id);
    Company saveCompany(Company company);
    Company updateCompany(Company company);
    void deleteCompany(long id);
}
