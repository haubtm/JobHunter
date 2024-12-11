package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Company;
import com.spring.jobhunter.repository.CompanyRepository;
import com.spring.jobhunter.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(Company company) {
        Optional<Company> companyOptional = companyRepository.findById(company.getId());
        if(companyOptional.isPresent()) {
            Company companyToUpdate = companyOptional.get();
            companyToUpdate.setName(company.getName());
            companyToUpdate.setAddress(company.getAddress());
            companyToUpdate.setDescription(company.getDescription());
            companyToUpdate.setLogo(company.getLogo());
            return companyRepository.save(companyToUpdate);
        }
        return null;
    }

    @Override
    public void deleteCompany(long id) {
        companyRepository.deleteById(id);
    }
}
