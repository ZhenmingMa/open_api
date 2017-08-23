package com.cby.repository;

import com.cby.entity.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * Created by Ma on 2017/7/6.
 */
public interface EntityRepository extends JpaRepository<Entity,String>{
    List<Entity> findByPhoneAndPolicyFrom(String phone,String policyFrom);
    List<Entity> findByCompanyCode(String companyCode);
}
