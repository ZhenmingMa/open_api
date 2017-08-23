package com.cby;

import com.cby.entity.Entity;
import com.cby.repository.MobileRepository;
import com.cby.utils.IDCardParser;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Ma on 2017/7/14.
 */

public class Test {
    public static void main(String[] args) {
        String birthday = "1952-08-17";
        System.out.printf(birthday.substring(0, 4));
        System.out.printf(birthday.substring(5, 7));
        System.out.printf(birthday.substring(8, 10));

        try {
            System.out.printf(String.valueOf(ifGrown_up(birthday)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean ifGrown_up(String num) throws ParseException {
        int year = Integer.parseInt(num.substring(0, 4));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date update = sdf.parse(String.valueOf(year + 25) + num.substring(5, 7) + num.substring(8, 10));
        Date today = new Date();
        if (today.after(update)) {
            Date update1 = sdf.parse(String.valueOf(year + 50) + num.substring(5, 7) + num.substring(8, 10));
            if (today.before(update1)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
