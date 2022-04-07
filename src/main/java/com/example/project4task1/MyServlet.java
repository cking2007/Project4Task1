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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author
 * @version V1.0
 * @description
 * @date 2022/4/7 8:04 AM
 **/
@WebServlet(name = "MyServlet", urlPatterns = "/getSourceData")//表示请求路径
public class MyServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String countryCode = req.getParameter("countryCode");
        String devices = req.getParameter("devices");
        String result = "";

        if (StringUtils.hasLength(countryCode)) {
            long t1 = System.currentTimeMillis();

            String url = "https://api.coronatracker.com/v3/stats/worldometer/country?countryCode=" + countryCode;
            CloseableHttpResponse response = null;
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                // 创建uri
                URIBuilder builder = new URIBuilder(url);
                URI uri = builder.build();
                // 创建http GET请求
                HttpGet httpGet = new HttpGet(uri);
                // 执行请求
                response = httpclient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(response.getEntity(), "UTF-8");
                    result = result.substring(1);
                    result = result.substring(0, result.length() - 1);
                    logger.info("收到的数据 :" + result);
                }

                long t2 = System.currentTimeMillis();
                if (devices == null) {
                    devices = "";
                }

                saveLog(t2 - t1, devices, result);

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
        super.doPost(req, resp);
    }

    public void saveLog(long timeMs, String devices, String result) {
        ResultEntity resultEntity = JSON.parseObject(result, ResultEntity.class);

        DashEntity dashEntity = new DashEntity();
        dashEntity.setDateTime(String.valueOf(LocalDateTime.now()));
        dashEntity.setCountry(resultEntity.getCountry());
        dashEntity.setDevices(devices);
        dashEntity.setApiTime(String.valueOf(timeMs));
        dashEntity.setCountry(resultEntity.getCountry());
        dashEntity.setTotalConfirmed(resultEntity.getTotalConfirmed());
        dashEntity.setTotalDeaths(resultEntity.getTotalDeaths());
        dashEntity.setTotalRecovered(resultEntity.getTotalRecovered());
        dashEntity.setDailyConfirmed(resultEntity.getDailyConfirmed());
        dashEntity.setDailyDeaths(resultEntity.getDailyDeaths());
        dashEntity.setActiveCases(resultEntity.getActiveCases());
        dashEntity.setLastUpdated(resultEntity.getLastUpdated());
        mongoTemplate.insert(dashEntity, "dashBoard");


        String countryCode = resultEntity.getCountryCode();
        Query query = new Query(Criteria.where("countryCode").is(countryCode));

        StatisticsEntity statisticsEntity = mongoTemplate.findOne(query, StatisticsEntity.class,"statistics");
        if (statisticsEntity != null) {
            logger.info("statisticsEntity  _id :" + statisticsEntity.get_id());
            statisticsEntity.setSearchCount(statisticsEntity.getSearchCount() + 1);
        } else {
            statisticsEntity = new StatisticsEntity();
            statisticsEntity.setCountryCode(countryCode);
            statisticsEntity.setSearchCount(1);
        }
        mongoTemplate.save(statisticsEntity, "statistics");
    }
}
