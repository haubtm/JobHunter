package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Company;
import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.dto.Meta;
import com.spring.jobhunter.domain.dto.ResultPaginationDTO;
import com.spring.jobhunter.repository.CompanyRepository;
import com.spring.jobhunter.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Override
    public ResultPaginationDTO getAllCompanies(Pageable pageable) {
        Page<Company> pageCompany = companyRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageCompany.getNumber() + 1);
        meta.setPageSize(pageCompany.getSize());

        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageCompany.getContent());
        return rs;
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
