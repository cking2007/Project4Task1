package com.example.project4task1;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @author caixiao
 * @date 2019-8-11 18:08
 */
@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // @Autowired
    // private UserService userService;

    @GetMapping (value = "/getSourceData")
    public String get1(@RequestParam("countryCode") String countryCode) {
        if (StringUtils.hasLength(countryCode)) {
            String url = "https://api.coronatracker.com/v3/stats/worldometer/country?countryCode=" + countryCode;
            String result = "";
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
                    logger.info("收到的数据 :" + result);
                }
            } catch (Exception e) {
                logger.error("error :" + e.getMessage());
            }
            return result;
        }
        return "";
    }

}
