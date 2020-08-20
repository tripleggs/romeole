package org.romeole.data.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auth gongzhou
 * @CreateTime 2020-08-13
 */
public class HttpUtil {

    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    private static final Integer DEFAULT_POOL_NUM = 5;

    private static final Integer DEFAULT_CON_TIMEOUT = 1000;

    private static final Integer DEFAULT_REQ_TIMEOUT = 2000;

    private static final Integer DEFAULT_SOC_TIMEOUT = 2000;

    private static final int SUCCESS_CODE = 200;

    private static CloseableHttpClient httpClient;

    private static PoolingHttpClientConnectionManager cm;

    static {
        initHttpClient();
    }

    public static JSONObject doGet(String url) {
        return doGet(url, new ArrayList<>());
    }

    public static JSONObject doPost(String url) {
        return doPost(url, new ArrayList<>());
    }

    public static JSONObject doGet(String url, Map<String, Object> params) {
        return doGet(url, getParams(params));
    }

    public static JSONObject doPost(String url, Map<String, Object> params) {
        return doPost(url, getParams(params));
    }

    private static void initHttpClient() {
        cm = new PoolingHttpClientConnectionManager();
        // 设置连接池数量
        cm.setMaxTotal(DEFAULT_POOL_NUM);
        // 为域名设置单独的连接池
        /*if (HttpUtil.httpHost != null && !"".equals(HttpUtil.httpHost)) {
            HttpHost httpHost = new HttpHost(HttpUtil.httpHost);
            cm.setMaxPerRoute(new HttpRoute(httpHost), DEFAULT_POOL_NUM);
        }*/
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CON_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_REQ_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOC_TIMEOUT).build();
        // 请求重试处理(可自定义处理类)
        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();
        httpClient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler)
//                .setDefaultSocketConfig(socketConfig)
                .build();
    }

    /**
     * 关闭空闲连接，节省性能（适合连接池较大的情况下使用）
     */
    public static void closeIdle() {
        try {
            // 关闭失效连接并从连接池中移除
            cm.closeExpiredConnections();
            // 关闭30秒钟内不活动的连接并从连接池中移除，空闲时间从交还给连接管理器时开始
            cm.closeIdleConnections(20, TimeUnit.SECONDS);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static String doGetString(String url) {
        Long start = System.currentTimeMillis();
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE != statusCode) {
                log.error("GET请求失败,状态码:{}", statusCode);
                return null;
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            response.close();
            log.info("GET返回数据:{}", result);
            log.info("GET请求用时:{}ms", System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            log.error("GET请求异常,EXCEPTION:{}", e);
            return null;
        }
    }

    public static JSONObject doGet(String url, List<NameValuePair> nameValuePairList) {
        Long start = System.currentTimeMillis();
        try {
            log.info("GET请求地址:{}", url);
            log.info("GET请求参数:{}", nameValuePairList.toString());
            URIBuilder uriBuilder = new URIBuilder(url);
            if (null != nameValuePairList && !nameValuePairList.isEmpty()) {
                uriBuilder.addParameters(nameValuePairList);
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE != statusCode) {
                log.error("GET请求失败,状态码:{}", statusCode);
                return null;
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            response.close();
            log.info("GET返回数据:{}", result);
            log.info("GET请求用时:{}ms", System.currentTimeMillis() - start);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("GET请求异常,EXCEPTION:{}", e);
            return null;
        }
    }

    public static JSONObject doPost(String url, List<NameValuePair> nameValuePairList) {
        Long start = System.currentTimeMillis();
        try {
            log.info("POST请求地址:{}", url);
            log.info("POST请求参数:{}", nameValuePairList.toString());
            HttpPost post = new HttpPost(url);
            if (null != nameValuePairList && !nameValuePairList.isEmpty()) {
                StringEntity entity = new UrlEncodedFormEntity(nameValuePairList, StandardCharsets.UTF_8);
                post.setEntity(entity);
            }
            CloseableHttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE != statusCode) {
                log.error("POST请求失败, 状态码:{}", statusCode);
                return null;
            }
            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            response.close();
            log.info("POST返回数据:{}", result);
            log.info("POST请求用时:{}ms", System.currentTimeMillis() - start);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            log.error("POST请求异常, EXCEPTION:{}", e);
            return null;
        }
    }

    public static String doPostXml(String url, String xml) {
        Long start = System.currentTimeMillis();
        try {
            log.debug("POST_Xml请求地址:{}", url);
            log.debug("POST_Xml请求参数:{}", xml);
            HttpPost post = new HttpPost(url);
            if (null != xml && !xml.isEmpty()) {
                StringEntity entity = new StringEntity(xml, StandardCharsets.UTF_8);
                post.setEntity(entity);
            }
            CloseableHttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE != statusCode) {
                log.error("POST_Xml失败, 状态码:{}", statusCode);
                return null;
            }
            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            response.close();
            log.debug("POST_Xml返回数据:{}", result);
            log.debug("POST_Xml请求用时:{}ms", System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            log.error("POST请求异常, EXCEPTION:{}", e);
            return null;
        }
    }

    public static String doPostXmlSSL(String url, String xml, KeyStore keyStore, String secrit) {
        Long start = System.currentTimeMillis();
        try {
            log.debug("POST_Xml_SSL请求地址:{}", url);
            log.debug("POST_Xml_SSL请求参数:{}", xml);
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, secrit.toCharArray())//这里也是写密码的
                    .build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            HttpPost post = new HttpPost(url);
            if (null != xml && !xml.isEmpty()) {
                StringEntity entity = new StringEntity(xml, StandardCharsets.UTF_8);
                post.setEntity(entity);
            }
            CloseableHttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE != statusCode) {
                log.error("POST_Xml_SSL失败, 状态码:{}", statusCode);
                return null;
            }
            String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            response.close();
            httpClient.close();
            log.debug("POST_Xml_SSL返回数据:{}", result);
            log.debug("POST_Xml_SSL请求用时:{}ms", System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            log.error("POST请求异常, EXCEPTION:{}", e);
            return null;
        }
    }

    /**
     * 组织请求参数
     *
     * @param params 请求参数
     * @return 参数对象
     */
    public static List<NameValuePair> getParams(Map<String, Object> params) {
        if (params == null || params.size() == 0) {
            return null;
        }
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        params.forEach((key, val) -> nameValuePairList.add(new BasicNameValuePair(key, val.toString())));
        return nameValuePairList;
    }

}
