package com.cby.controller;

import com.cby.entity.Entity;
import com.cby.entity.Result;
import com.cby.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Ma on 2017/7/6.
 */

@Controller
@SpringBootApplication
public class LLYController {
    @Autowired
    private MyService myService;

    @ResponseBody
    @PostMapping(value = "/CommitInfo")
    public Result getcheckInfo(@RequestBody String info){
        return myService.getcheckInfo(info);
    }

    @ResponseBody
    @PostMapping(value = "/getCount")
    public String getCount(String CompanyCode){
        return myService.getCount(CompanyCode);
    }

    @RequestMapping("/admin")
    public String index(){
        return "index";
    }

//    @RequestMapping("/admin/getCount")
//    public String getCount(Model model){
//
//        return "index";
//    }
}
