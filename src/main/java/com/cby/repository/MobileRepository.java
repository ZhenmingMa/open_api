package com.cby.repository;

import com.cby.entity.Mobile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Ma on 2017/7/17.
 */
public interface MobileRepository extends JpaRepository<Mobile,Integer>{
    Mobile findByPhone(Integer phone);
}
