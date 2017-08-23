package com.cby.repository;

import com.cby.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Ma on 2017/7/18.
 */
public interface CompanyRepository extends JpaRepository<Company,Integer>{
    Company findByCompanyCode(String company);
}
