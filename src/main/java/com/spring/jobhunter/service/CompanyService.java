package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.Company;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CompanyService {
    ResultPaginationDTO getAllCompanies(Specification<Company> companySpecification, Pageable pageable);
    Company getCompanyById(long id);
    Company saveCompany(Company company);
    Company updateCompany(Company company);
    void deleteCompany(long id);
}
