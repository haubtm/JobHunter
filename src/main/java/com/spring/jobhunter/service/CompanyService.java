package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.Company;

import java.util.List;

public interface CompanyService {
    List<Company> getAllCompanies();
    Company getCompanyById(long id);
    Company saveCompany(Company company);
    Company updateCompany(Company company);
    void deleteCompany(long id);
}
