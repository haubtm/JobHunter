package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Company;
import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.repository.CompanyRepository;
import com.spring.jobhunter.repository.UserRepository;
import com.spring.jobhunter.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ResultPaginationDTO getAllCompanies(Specification<Company> companySpecification, Pageable pageable) {
        Page<Company> pageCompany = companyRepository.findAll(companySpecification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

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
        Optional<Company> companyOptional = companyRepository.findById(id);
        if(companyOptional.isPresent()) {
            Company company = companyOptional.get();
            List<User> users = userRepository.findByCompany(company);
            userRepository.deleteAll(users);
        }
        companyRepository.deleteById(id);
    }
}
