package com.cby.repository;

import com.cby.entity.Record;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Ma on 2017/7/24.
 */
public interface RecordRepository extends JpaRepository<Record,Integer> {
    List<Record> findByPhoneAndTypeAndStatus(String phone,String type,String status);
    List<Record> findByCompanyCodeAndProvinceAndStatusAndType(String companyCode,String province,String status,String type);
}
