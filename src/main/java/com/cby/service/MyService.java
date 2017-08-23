package com.cby.service;

import com.cby.entity.*;
import com.cby.enums.ProductCodeEnum;
import com.cby.repository.*;
import com.cby.utils.DESEncrypt;
import com.cby.utils.IDCardParser;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.ServiceException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ma on 2017/7/6.
 */
@org.springframework.stereotype.Service
public class MyService {

    @Autowired
    private EntityRepository entityRepository;
    @Autowired
    private MobileRepository mobileRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private Entity1Repository entity1Repository;
    @Value("${taiKangUrl}")
    private String taiKangUrl;
    @Value("${metLifeUrl}")
    private String metLifeUrl;


    public Result getcheckInfo(String info) {
        Gson gson = new Gson();
        Entity entity = gson.fromJson(info, Entity.class);
        if (entity.getSex().equals("0"))
            entity.setSex("男");
        if (entity.getSex().equals("1"))
            entity.setSex("女");
        entity.setId(UUID.randomUUID().toString());
        entity.setDate(new Date());

        saveEntity(entity);


        if (entity.getIdentifer() == null || entity.getIdentifer().equals("")) {
            Result result = new Result();
            result.setStatus("N");
            result.setMessage("渠道编码不能为空");
            saveRecord(entity, "渠道编码不能为空", "N");
            return result;
        }
        if (!checkPermission(entity.getCompanyCode())) {
            Result result = new Result();
            result.setStatus("N");
            result.setMessage("公司验证不通过");
            saveRecord(entity, "公司验证不通过", "N");
            return result;
        }


        //请求泰康数据
        if (entity.getIdentifer().equals("01")) {
            //检查年龄信息是否符合要求
            Result result = new Result();
            String message = checkInfoForTaikang(entity);
            if (message != null) {
                result.setStatus("N");
                result.setMessage(message);
                saveRecord(entity, message, "N");
                return result;
            }
            List<Entity> list = entityRepository.findByPhoneAndPolicyFrom(entity.getPhone(), "TaiKang");
            if (list.size() == 0 || list == null) {
                entity.setPolicyFrom("TaiKang");
                entityRepository.save(entity);
                return RequestTaiKang(entity);
            } else {
                if (list.get(0).getPolicyNo() != null) {
                    if (list.get(0).getType().equals("tdb")) {
                        result.setStatus("N");
                        result.setMessage("您已经承过铁定保");
                        saveRecord(entity, "您已经承过铁定保", "N");
                        return result;
                    }
                    if (list.get(0).getType().equals("fcb")) {
                        result.setStatus("N");
                        result.setMessage("您已经承过飞常保");
                        saveRecord(entity, "您已经承过飞常保", "N");
                        return result;
                    }

                } else {
                    entity.setId(list.get(0).getId());
                    entity.setPolicyFrom("TaiKang");
                    entityRepository.save(entity);
                    return RequestTaiKang(entity);
                }
            }
        }
        //请求大都会数据
        if (entity.getIdentifer().equals("02")) {
            Result result = new Result();
            //检查年龄，地域信息是否符合要求
            String message = checkInfoForMetlife(entity);
            if (message != null) {
                result.setStatus("N");
                result.setMessage(message);
                saveRecord(entity, message, "N");
                return result;
            }
            //检查数量限制
            if (!countlimit(entity.getProvince(),entity.getCompanyCode(),entity.getType())){
                result.setStatus("N");
                result.setMessage("超过数量限制，请联系合作商");
                saveRecord(entity,"达到供应量", "N");
                return result;
            }

            List<Record> list = recordRepository.findByPhoneAndTypeAndStatus(entity.getPhone(), entity.getType(),"Y");
            if (list.size() == 0 || list == null) {
                entity.setPolicyFrom("Metlife");
                entityRepository.save(entity);
                return RequestMetlife(entity);
            } else {
//                if (list.get(0).getFreeInsureNo() != null) {
                    result.setStatus("N");
                    result.setMessage("您已经承过保");
                    saveRecord(entity, "已经承过保", "N");
                    return result;
//                }
//                else {
//                    entity.setPolicyFrom("Metlife");
//                    entityRepository.save(entity);
//                    return RequestMetlife(entity);
//                }
            }


        }
        return null;
    }

    //请求大都会接口
    private Result RequestMetlife(Entity entity) {
//        Mobile mobile = getCityByMobile(entity);
//        entity.setProvince(mobile.getProvince());
//        entity.setCity(mobile.getCity());
        Result result = new Result();
        Service service = new Service();
        try {
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(metLifeUrl);
            call.setOperationName("doRequest");
            String strXml = appendReqMetLifeXml(entity);
            String data1 = (String) call.invoke(new Object[]{strXml});
            String data = data1.replace("\n", "").replace("\t", "").replace("\r", "");
            int index = data.indexOf("<paq>");
            String string = data.subSequence(index, data.length()).toString().replaceAll(" ", "");
            String str1 = data.substring(0, index);
            String content = str1 + string;
            System.out.println(content);
            try {
                StringReader sr = new StringReader(content);
                InputSource is1 = new InputSource(sr);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(is1);
                Element element = doc.getDocumentElement();
                String flag = element.getElementsByTagName("Flag").item(0).getFirstChild().getNodeValue();
                if ("TRUE".equals(flag)) {
                    result.setStatus("success");
                }
                if ("FALSE".equals(flag)) {
                    result.setStatus("error");
                    entity.setSuccess(false);
                }
                String message = element.getElementsByTagName("Message").item(0).getFirstChild().getNodeValue();
                result.setMessage(message);
                if ("TRUE".equals(flag)) {
                    String FreeInsureNo = element.getElementsByTagName("FreeInsureNo").item(0).getFirstChild().getNodeValue();
                    result.setPolicyNo(FreeInsureNo);
                    entity.setSuccess(true);
                    entity.setPolicyNo(FreeInsureNo);

                    if (entity.getSex().equals("0"))
                        entity.setSex("男");
                    if (entity.getSex().equals("1"))
                        entity.setSex("女");
                    entityRepository.save(entity);
                    //保存记录
                    saveRecord(entity, message, "Y");
                    return result;
                } else {
                    entity.setSuccess(false);
                    if (entity.getSex().equals("0"))
                        entity.setSex("男");
                    if (entity.getSex().equals("1"))
                        entity.setSex("女");
                    entityRepository.save(entity);
                    //保存记录
                    saveRecord(entity, message, "N");
                    return result;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return result;
    }

    //请求泰康接口
    public Result RequestTaiKang(Entity entity) {

        Result result = new Result();
        String str = appendReqTaiKangXml(entity);

        //身份证号获取信息
        String sex = IDCardParser.getGenderByIdCard(entity.getIdCard());
        if (sex.equals("M"))
            entity.setSex("男");
        if (sex.equals("F"))
            entity.setSex("女");
        Mobile mobile = getCityByMobile(entity);
        entity.setProvince(mobile.getProvince());
        entity.setCity(mobile.getCity());
        entity.setBirthday(IDCardParser.getBirthByIdCard(entity.getIdCard()));

        String requestXml = null;
        try {
            requestXml = "reqXml=" + DESEncrypt.encrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStream is = null;
        BufferedReader br = null;
        InputStreamReader il = null;
        HttpURLConnection conn = null;
        try {

            java.net.URL urll = new URL(
                    taiKangUrl);

            conn = (HttpURLConnection) urll.openConnection();
            conn.setConnectTimeout(50000);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            DataOutputStream out = new
                    DataOutputStream(conn.getOutputStream());
            out.writeBytes(requestXml);
            out.flush();
            out.close();
            is = conn.getInputStream();
            il = new InputStreamReader(is, "gbk");
            br = new BufferedReader(il);

            String temp;
            StringBuffer sb1 = new StringBuffer();
            while ((temp = br.readLine()) != null) {
                sb1.append(temp).append("\n");
            }
            try {
                StringReader sr = new StringReader(sb1.toString());
                InputSource is1 = new InputSource(sr);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(is1);
                Element element = doc.getDocumentElement();
                String type = element.getElementsByTagName("type").item(0)
                        .getFirstChild().getNodeValue();
                if (type.equals("N"))
                    result.setStatus("error");
                if (type.equals("Y"))
                    result.setStatus("success");
                String desc = element.getElementsByTagName("desc").item(0)
                        .getFirstChild().getNodeValue();
                result.setMessage(desc);
                if (type.equals("Y")) {
                    String policyNo = element.getElementsByTagName("policyNo").item(0)
                            .getFirstChild().getNodeValue();
                    result.setPolicyNo(policyNo);
                    entity.setPolicyNo(policyNo);
                    entity.setSuccess(true);
                    entity.setPolicyFrom("TaiKang");
                    entityRepository.save(entity);
                    //保存记录
                    saveRecord(entity, desc, "Y");
                } else {
                    entity.setSuccess(false);
                    entityRepository.save(entity);
                    //保存记录
                    saveRecord(entity, desc, "N");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            try {

                if (br != null) {
                    br.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (il != null) {
                    il.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (is != null) {
                    is.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (conn != null) {
                    conn.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    //拼接请求大都会的xml
    private String appendReqMetLifeXml(Entity entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        sb.append("<Records>");
        sb.append("<Record>");
        sb.append("<Customer>");
        sb.append("<Key>" + "CBYI" + System.currentTimeMillis() + "</Key>");
        sb.append("<FromSystem>CBYI</FromSystem>");
        sb.append("<Name>" + entity.getName() + "</Name>");
        if (entity.getSex().trim().equals("男")) {
            sb.append("<Sex>" + "Male" + "</Sex>");
        }
        if (entity.getSex().trim().equals("女")) {
            sb.append("<Sex>" + "Female" + "</Sex>");
        }

        sb.append("<Birthday>" + entity.getBirthday() + "</Birthday>");
        sb.append("<Document></Document>");
        sb.append("<DocumentType></DocumentType>");
        sb.append("<Email></Email>");
        sb.append("<Mobile>" + entity.getPhone() + "</Mobile>");
        sb.append("<ContactState><Name>" + entity.getProvince() + "</Name></ContactState>");
        sb.append("<ContactCity><Name>" + entity.getCity() + "</Name></ContactCity>");
        sb.append("<ContactAddress>" + entity.getAddress() + "</ContactAddress>");
        sb.append("<Occupation><Code>0001001</Code></Occupation>");
        sb.append("<Description />");
        sb.append("</Customer>");
        sb.append("<Task><CallList><Name></Name></CallList><Campaign><Name></Name></Campaign></Task>");
        sb.append("<Activity>");
        sb.append("<Code></Code>");
        if (entity.getType().equals("PC001"))
            sb.append("<Present><Code>" + ProductCodeEnum.PRODUCT_01.getMessage() + "</Code></Present>");
        if (entity.getType().equals("PC002"))
            sb.append("<Present><Code>" + ProductCodeEnum.PRODUCT_02.getMessage() + "</Code></Present>");
        sb.append("<TSR><Code>805095</Code></TSR>");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sb.append("<DonateTime>" + sdf.format(date) + "</DonateTime>");
        sb.append("<SMS>0</SMS>");
        sb.append("<FlghtNo />");
        sb.append("<ValidTime></ValidTime>");
        sb.append("</Activity>");
        sb.append("</Record>");
        sb.append("</Records>");
        System.out.println(sb.toString());
        return sb.toString();
    }

    //拼接请求泰康的xml
    private String appendReqTaiKangXml(Entity entity) {
        StringBuffer sb1 = new StringBuffer();
        sb1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb1.append("<requst>");
        sb1.append("<name>" + entity.getName() + "</name>");
        sb1.append("<idNo>" + entity.getIdCard() + "</idNo>");
        sb1.append("<phoneNum>" + entity.getPhone() + "</phoneNum>");
        sb1.append("<type>" + entity.getType() + "</type>");
        sb1.append("<email></email>");
        sb1.append("<fromId>52245</fromId>");
        sb1.append("<ssid>00000</ssid>");
        sb1.append("<gender></gender>");
        sb1.append("</requst>");
        return sb1.toString();
    }


    public String getCount(String CompanyCode) {
        Company company = companyRepository.findByCompanyCode(CompanyCode);
        if (company == null) {
            return "公司不存在";
        } else {

            List<Entity> list = entityRepository.findByCompanyCode(CompanyCode);
            List<Entity> list1 = new ArrayList<>();
            List<Entity> list2 = new ArrayList<>();
            List<Entity> list3 = new ArrayList<>();
            for (Entity entity : list) {
                if (entity.isSuccess()) {
                    list1.add(entity);
                    if ("TaiKang".equals(entity.getPolicyFrom())) {
                        list2.add(entity);
                    }
                    if ("Metlife".equals(entity.getPolicyFrom())) {
                        list3.add(entity);
                    }
                }
            }

            return "公司名称：" + company.getCompanyName() + "\n合计数量：" + list1.size() + "\n泰康数量：" + list2.size() + "\n大都会数量：" + list3.size();
        }
    }

    private String checkInfoForMetlife(Entity entity) {
        String[] limit = {"北京市", "上海市", "重庆市", "辽宁省", "江苏省", "浙江省", "福建省", "广东省", "四川省", "湖北省", "天津市"};
        boolean hasLimit = true; //是否有限制，默认为true
        for (int i = 0; i < limit.length; i++) {
            if (entity.getProvince().equals(limit[i])) {
                //验证手机号与地址是否匹配
                Mobile mobile = getCityByMobile(entity);
                String city = mobile.getProvince() + mobile.getCity();
                String temp = limit[i].substring(0, limit[i].length() - 1);
                String temp2 = entity.getCity().substring(0, entity.getCity().length() - 1);
                if (city.contains(temp) && city.contains(temp2)) {
                    hasLimit = false;
                    break;
                }

            }

        }
        if (entity.getProvince().equals("福建省") && entity.getCity().contains("厦门")) {
            hasLimit = true;
        }
        if (hasLimit) {
            return "所在地区不在活动范围之内或手机号不匹配";
        }

        try {
            if (!ifGrown_up(entity.getBirthday(), 25, 50)) {
                return "年龄不符合要求";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String checkInfoForTaikang(Entity entity) {
        if (entity.getName() == null)
            return "姓名不能为空";
        if (entity.getIdCard() == null)
            return "身份证号不能为空";
        if (entity.getPhone() == null)
            return "手机号不能为空";
        if (!IDCardParser.validateIdCard18(entity.getIdCard())) {
            return "身份证不合法";
        }
        int y = IDCardParser.getYearByIdCard(entity.getIdCard());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String nowYear = sdf.format(date);
        int ny = Integer.parseInt(nowYear);
        if (!(ny - y >= 0 && ny - y <= 80)) {
            return "年龄不符合要求";
        }
        return null;
    }

    //通过手机号得到归属地
    private Mobile getCityByMobile(Entity entity) {
        String mobile = entity.getPhone().substring(0, 7);
        Mobile mobile1 = mobileRepository.findByPhone(Integer.parseInt(mobile));
        return mobile1;
    }

    //检查公司权限
    private boolean checkPermission(String companyCode) {
        Company company = companyRepository.findByCompanyCode(companyCode);
        if (company != null) {
            return true;
        } else {
            return false;
        }
    }

    //判断年龄是否符合要求
    public static boolean ifGrown_up(String num, int minAge, int maxAge) throws ParseException {
        int year = Integer.parseInt(num.substring(0, 4));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date update = sdf.parse(String.valueOf(year + minAge) + num.substring(5, 7) + num.substring(8, 10));
        Date today = new Date();
        if (today.after(update)) {
            Date update1 = sdf.parse(String.valueOf(year + maxAge) + num.substring(5, 7) + num.substring(8, 10));
            if (today.before(update1)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void saveRecord(Entity entity, String message, String status) {
        Record record = new Record();
        record.setCompanyCode(entity.getCompanyCode());
        if (entity.getIdentifer().equals("01")) {
            record.setIdentifer("TaiKang");
        }
        if (entity.getIdentifer().equals("02")) {
            record.setIdentifer("MetLife");
        }
        record.setType(entity.getType());
        record.setStatus(status);
        record.setMessage(message);
        record.setPhone(entity.getPhone());
        record.setName(entity.getName());
        record.setSex(entity.getSex());
        record.setBirthday(entity.getBirthday());
        record.setFreeInsureNo(entity.getPolicyNo());
        record.setProvince(entity.getProvince());
        record.setCity(entity.getCity());
        record.setAddress(entity.getAddress());

        record.setDate(new Date());
        recordRepository.save(record);
    }

    public void saveEntity(Entity entity) {
        Entity1 entity1 = new Entity1();
        entity1.setId(entity.getId());
        entity1.setName(entity.getName());
        entity1.setIdCard(entity.getIdCard());
        entity1.setPhone(entity.getPhone());
        entity1.setEmail(entity.getEmail());
        entity1.setIdentifer(entity.getIdentifer());
        entity1.setCompanyCode(entity.getCompanyCode());
        entity1.setType(entity.getType());
        entity1.setSex(entity.getSex());
        entity1.setBirthday(entity.getBirthday());
        entity1.setProvince(entity.getProvince());
        entity1.setCity(entity.getCity());
        entity1.setAddress(entity.getAddress());
        entity1.setDate(entity.getDate());
        entity1Repository.save(entity1);
    }


    //限制数量
    private boolean countlimit(String province, String companyCode,String type) {
        Company company = companyRepository.findByCompanyCode(companyCode);


        List<Record> list = recordRepository.findByCompanyCodeAndProvinceAndStatusAndType(companyCode, province, "Y",type);

        if (province.contains("北京市")) {
            if (list.size() <= company.getM_beijing_count())
                return true;
            else
                return false;

        } else if (province.equals("上海市")) {
            if (list.size() <= company.getM_shanghai_count())
                return true;
            else
                return false;
        } else if (province.equals("重庆市")) {
            if (list.size() <= company.getM_chongqing_count())
                return true;
            else
                return false;
        } else if (province.equals("辽宁省")) {
            if (list.size() <= company.getM_liaoning_count())
                return true;
            else
                return false;
        } else if (province.equals("江苏省")) {
            if (list.size() <= company.getM_jiangsu_count())
                return true;
            else
                return false;
        } else if (province.equals("福建省")) {
            if (list.size() <= company.getM_fujian_count())
                return true;
            else
                return false;
        } else if (province.equals("广东省")) {
            if (list.size() <= company.getM_guangdong_count())
                return true;
            else
                return false;
        } else if (province.equals("四川省")) {
            if (list.size() <= company.getM_sichuang_count())
                return true;
            else
                return false;
        } else if (province.equals("湖北省")) {
            if (list.size() <= company.getM_hubei_count())
                return true;
            else
                return false;
        } else if (province.equals("天津市")) {
            if (list.size() <= company.getM_tianjin_count())
                return true;
            else
                return false;
        } else if (province.equals("浙江省")) {
            if (list.size() <= company.getM_zhejiang_count())
                return true;
            else
                return false;
        }else
            return false;
    }

}
