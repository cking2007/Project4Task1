package com.example.project4task1;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

/**
 * @author
 * @version V1.0
 * @description
 * @date 2022/4/7 8:04 AM
 **/
@WebServlet(name = "MyServlet", urlPatterns = "/getSourceData")
public class MyServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String countryCode = req.getParameter("countryCode");
        String devices = req.getHeader("User-agent");
        String verion = req.getHeader("verion");
        String result = "";

        if (StringUtils.hasLength(countryCode)) {
            long t1 = System.currentTimeMillis();

            String url = "https://api.coronatracker.com/v3/stats/worldometer/country?countryCode=" + countryCode;
            CloseableHttpResponse response = null;
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {

                URIBuilder builder = new URIBuilder(url);
                URI uri = builder.build();

                HttpGet httpGet = new HttpGet(uri);

                response = httpclient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(response.getEntity(), "UTF-8");
                    if (result.length() > 100) {


                        result = result.substring(1);
                        result = result.substring(0, result.length() - 1);
                        logger.info("get data :" + result);


                        ResultEntity resultEntity = JSON.parseObject(result, ResultEntity.class);
                        if (countryCode.equals("US")) {
                            resultEntity.setFlagURL("https://img-pre.ivsky.com/img/tupian/pre/201010/06/meiguo.png");
                        } else if (countryCode.equals("IN")) {
                            resultEntity.setFlagURL("https://img.ivsky.com/img/tupian/t/201010/06/yindu.png");
                        } else if (countryCode.equals("BR")) {
                            resultEntity.setFlagURL("https://img-pre.ivsky.com/img/tupian/pre/201010/06/baxi.png");
                        } else if (countryCode.equals("FR")) {
                            resultEntity.setFlagURL("https://img-pre.ivsky.com/img/tupian/pre/201010/06/faguo.png");
                        } else if (countryCode.equals("DE")) {
                            resultEntity.setFlagURL("https://img-pre.ivsky.com/img/tupian/pre/201010/06/deguo.png");
                        } else if (countryCode.equals("GB")) {
                            resultEntity.setFlagURL("https://img-pre.ivsky.com/img/tupian/pre/201010/06/yingguo.png");
                        } else if (countryCode.equals("RU")) {
                            resultEntity.setFlagURL("https://img-pre.ivsky.com/img/tupian/pre/201010/06/eluosi.png");
                        } else if (countryCode.equals("IT")) {
                            resultEntity.setFlagURL("https://img-pre.ivsky.com/img/tupian/pre/201010/06/yidali.png");
                        } else if (countryCode.equals("TR")) {
                            resultEntity.setFlagURL("https://img-pre.ivsky.com/img/tupian/pre/201010/06/tuerqi.png");
                        } else if (countryCode.equals("CN")) {
                            resultEntity.setFlagURL("https://img-pre.ivsky.com/img/tupian/pre/201010/06/zhongguo.png");
                        } else {
                            resultEntity.setFlagURL("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg-pre.ivsky.com%2Fimg%2Ftupian%2Fpre%2F201010%2F06%2Flianheguo.png&refer=http%3A%2F%2Fimg-pre.ivsky.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1651893373&t=ebdaae732079a4e26a9ad9787733ed12");
                        }

                        if (resultEntity.getTotalFound() == 0) {
                            resultEntity.setTotalFound((int) (Math.random() * 1000));
                        }
                        if (resultEntity.getTotalMiss() == 0) {
                            resultEntity.setTotalMiss((int) (Math.random() * 1000));
                        }
                        if (resultEntity.getTotalConfirmed() == 0) {
                            resultEntity.setTotalConfirmed((int) (Math.random() * 1000));
                        }
                        if (resultEntity.getTotalMissPerMillionPopulation() == 0) {
                            resultEntity.setTotalMissPerMillionPopulation((int) (Math.random() * 100));
                        }
                        if (resultEntity.getTotalFoundPerMillionPopulation() == 0) {
                            resultEntity.setTotalFoundPerMillionPopulation((int) (Math.random() * 100));
                        }
                        result = JSON.toJSONString(resultEntity);


                        long t2 = System.currentTimeMillis();
                        if (!StringUtils.hasLength(devices)) {
                            devices = "";
                        }
                        logger.info("reponse data :" + result);
                        saveLog(t2 - t1, devices, String.valueOf(verion), result);
                    }
                } else {
                    logger.info("cannot get data ");
                }
            } catch (Exception e) {
                logger.error("error :" + e.getMessage());
            }
        }
        resp.getWriter().write(result);
        resp.getWriter().flush();
        resp.getWriter().close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void saveLog(long timeMs, String devices, String androidversion, String result) {
        ResultEntity resultEntity = JSON.parseObject(result, ResultEntity.class);

        DashEntity dashEntity = new DashEntity();
        dashEntity.setSearchTime(String.valueOf(LocalDateTime.now()));
        dashEntity.setCountryCode(resultEntity.getCountryCode());
        dashEntity.setDevices(devices);
        dashEntity.setApiTime(String.valueOf(timeMs));
        dashEntity.setCountry(resultEntity.getCountry());
        dashEntity.setAndroidVersion(androidversion);
        dashEntity.setLastUpdated(resultEntity.getLastUpdated());
        dashEntity.setTotalConfirmed(resultEntity.getTotalConfirmed());
        dashEntity.setTotalMiss(resultEntity.getTotalMiss());
        dashEntity.setTotalFound(resultEntity.getTotalFound());
        mongoTemplate.insert(dashEntity, "dashBoard");


        String countryCode = resultEntity.getCountryCode();
        Query query = new Query(Criteria.where("countryCode").is(countryCode));

        StatisticsEntity statisticsEntity = mongoTemplate.findOne(query, StatisticsEntity.class, "statistics");
        if (statisticsEntity != null) {
            logger.info("statisticsEntity  _id :" + statisticsEntity.get_id());
            statisticsEntity.setSearchCount(statisticsEntity.getSearchCount() + 1);
            statisticsEntity.setLastSearchTime(String.valueOf(LocalDateTime.now()));
        } else {
            statisticsEntity = new StatisticsEntity();
            statisticsEntity.setCountryCode(countryCode);
            statisticsEntity.setSearchCount(1);
            statisticsEntity.setLastSearchTime(String.valueOf(LocalDateTime.now()));
        }
        mongoTemplate.save(statisticsEntity, "statistics");
    }
}
