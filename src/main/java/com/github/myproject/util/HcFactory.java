package com.github.myproject.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * httpClient请求工具类,将cookie仓库保存到redis，便于保存cookie
 * Created by madong on 2018/03/17.
 */
@Component
public class HcFactory {

    private final static Logger logger = LoggerFactory.getLogger(HcFactory.class);
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private static final String REDIS_NAMESPACE = "plum";

    // defines the overal connection limit for a conneciton pool.
    @Value("${hc.maxTotal:200}")
    private int maxTotal = 200;
    // defines a connection limit per one HTTP route
    @Value("${hc.defaultMaxPerRoute:50}")
    private int defaultMaxPerRoute = 50;
    // A socket timeout is dedicated to monitor the continuous incoming data flow.
    @Value("${hc.socketTimeout:30000}")
    private int socketTimeout = 30000;
    // A connection timeout occurs only upon starting the TCP connection. Connection timeout is the timeout until a connection with the server is established.
    @Value("${hc.connectTimeout:30000}")
    private int connectTimeout = 30000;
    // ConnectionRequestTimeout used when requesting a connection from the connection manager.
    @Value("${hc.connectionRequestTimeout:30000}")
    private int connectionRequestTimeout = 30000;

    // 代理服务器
    @Value("${proxyserver.host:none}")
    private String proxyHost;
    @Value("${proxyserver.port:0}")
    private int proxyPort;
    @Value("${isProxy:false}")
    private boolean isProxy;

    private static final List<Header> DEFAULT_HEADERS = new ArrayList<>(Arrays.asList(
            new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
            new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"),
            new BasicHeader("Accept-Encoding", "gzip, deflate"),
            new BasicHeader("Connection", "keep-alive"),
            new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36")
    ));
    private PoolingHttpClientConnectionManager connManager;
    private RequestConfig requestConfig;
    private DefaultHttpRequestRetryHandler retryHandler;

    public HcFactory() {
    }

    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("HcFactory-pool-%d").build();
    Executor executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), namedThreadFactory);

    @PostConstruct
    public void init() {
        logger.info("HcFactory init begin");
        SSLConnectionSocketFactory sslConnectionFactory;
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            }).build();
            sslConnectionFactory = new SSLConnectionSocketFactory(sslContext.getSocketFactory(), new NoopHostnameVerifier());
        } catch (Exception e) {
            throw new RuntimeException("HcFactory init ssl fail!!!");
        }
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslConnectionFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        connManager = new PoolingHttpClientConnectionManager(registry);
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setRedirectsEnabled(true)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
        executor.execute(new IdleConnectionMonitorThread(connManager));
        logger.info("HcFactory init end");
    }

    /**
     * 重试3次
     *
     * @param requestId
     * @param hrb
     * @return
     */
    public HttpData try3TimeExecute(String requestId, HttpRequestBase hrb, boolean isSaveCookie) {
        TryBase<HttpData> ofc = TryBase.of(3, (times) -> {
            logger.info("try times | requestId={},times={}", requestId, times);
            HttpData execute = execute(requestId, hrb);
            if (execute.getStatusCode() / 100 >= 4 && times < 2) {
                logger.info("request status is error | requestId={},status={}", requestId, execute.getStatusCode());
                throw new Exception("response status greater than or equals to 400");
            }
            return execute;
        });
        if (ofc.isSuccess()) {
            logger.info("request success | requestId={},result={}", requestId, ofc.get());
            return ofc.get();
        } else {
            logger.info("try request fail | requestId={},exception={}", requestId, ofc.getCause());
        }
        return null;
    }


    /**
     * 创建http客户端
     *
     * @param cookieStore cookie仓库
     * @return http客户端
     */
    private CloseableHttpClient createHttpClient(BasicCookieStore cookieStore) {
        HttpClientBuilder builder = HttpClients.custom();
        builder.setConnectionManager(connManager);
        builder.setDefaultHeaders(DEFAULT_HEADERS);
        builder.setDefaultRequestConfig(requestConfig);
        builder.setRedirectStrategy(new LaxRedirectStrategy());
        if (retryHandler != null) {
            builder.setRetryHandler(retryHandler);
        }
        if (cookieStore != null) {
            builder.setDefaultCookieStore(cookieStore);
        }
        if (isProxy) {
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            builder.setProxy(proxy);
        }
        return builder.build();
    }

    /**
     * @param hrb http请求参数
     * @return 响应数据
     */
    public HttpData execute(HttpRequestBase hrb) {
        return execute("", hrb);
    }

    /**
     * 执行请求
     *
     * @param requestId 请求标号
     * @param hrb       http请求参数
     * @return 响应数据
     */
    public HttpData execute(String requestId, HttpRequestBase hrb) {
        BasicCookieStore cookieStore = null;
        CloseableHttpClient httpClient = createHttpClient(cookieStore);
        HttpData httpData = new HttpData();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(hrb);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String data = EntityUtils.toString(httpResponse.getEntity());
            httpData.setStatusCode(statusCode);
            httpData.setData(data);
            httpData.setHeaders(httpResponse.getAllHeaders());
            httpData.setCookieStore(cookieStore);
        } catch (Exception e) {
            logger.error("http request excetpion|requestId:{}", requestId, e);
        } finally {
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                } catch (IOException ignored) {
                }
            }
            if (hrb != null) {
                hrb.releaseConnection();
            }
        }
        logger.info("request result requestId:{}", requestId);
        return httpData;
    }

    /**
     * 下载图片
     *
     * @param requestId 请求编号
     * @param url       图片地址
     * @return 图片字节数组
     */
    public byte[] downloadImage(String requestId, String url) throws Exception {
        byte[] bytes;
        BasicCookieStore cookieStore = null;
        CloseableHttpClient httpClient = createHttpClient(cookieStore);
        HttpGet httpGet = null;
        HttpResponse httpResponse = null;
        try {
            httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);
            bytes = EntityUtils.toByteArray(httpResponse.getEntity());

        } catch (Exception e) {
            logger.error("http request excetpion|requestId:{}", requestId, e);
            throw new Exception(e.getMessage());
        } finally {
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                } catch (IOException ignored) {
                }
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return bytes;
    }

    /**
     * 序列化对象
     *
     * @param object 对象
     * @return 对象字节数组
     */
    private static byte[] serialize(Object object) {
        try {
            //序列化
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error("serialize exception", e);
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param bytes 对象字节数组
     * @param <T>   返回类型
     * @return 返回类
     */
    @SuppressWarnings("unchecked")
    private static <T> T deserialize(byte[] bytes) {
        try {
            //反序列化
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            logger.error("deserialize exception", e);
        }
        return null;
    }

    /**
     * 生成redis的键
     *
     * @param requestId 请求编号
     * @return redis的键
     */
    private static String generateRedisKey(String requestId) {
        return REDIS_NAMESPACE + ":hc_factory:requestId:" + requestId;
    }

    public HttpData postJson(String url, Object data, String tagInfo) {
        return postJson(url, data, tagInfo, null);
    }

    public int head(String url) {
        int code = 0;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpHead httpHead = new HttpHead(url);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpHead);
            code = httpResponse.getStatusLine().getStatusCode();
        } catch (IOException e) {
        } finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return code;
    }

    public HttpData postJson(String url, Object data, String tagInfo, Header[] headers) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(toJson(data), ContentType.APPLICATION_JSON));
        if (headers != null) {
            httpPost.setHeaders(headers);
        }
        return execute(tagInfo, httpPost);
    }

    public static String toJson(Object data) {
        String json;
        if (data instanceof String) {
            json = (String) data;
        } else {
            json = GSON.toJson(data);
        }
        return json;
    }

    /**
     * 请求无用的连接,避免内存泄漏
     */
    private static class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        private IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(60 * 1000);
                        connMgr.closeExpiredConnections();
                        connMgr.closeIdleConnections(2 * 60, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ignored) {
                // terminate
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * 请求返回的数据
     */
    public static class HttpData {

        private int statusCode;
        private String data;
        private BasicCookieStore cookieStore;
        private Header[] headers;

        public int getStatusCode() {
            return statusCode;
        }

        public boolean isSuccess() {
            return this.statusCode == 200;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getData() {
            return data;
        }

        public boolean isStatusCodeOk() {
            return this.statusCode == HttpStatus.SC_OK;
        }

        public void setData(String data) {
            this.data = data;
        }

        public BasicCookieStore getCookieStore() {
            return cookieStore;
        }

        public void setCookieStore(BasicCookieStore cookieStore) {
            this.cookieStore = cookieStore;
        }

        public Header[] getHeaders() {
            return headers;
        }

        public void setHeaders(Header[] headers) {
            this.headers = headers;
        }

        @Override
        public String toString() {
            return "HttpData{" +
                    "statusCode=" + statusCode +
                    ", data='" + data + '\'' +
                    '}';
        }

        public Header[] getHeaders(final String name) {
            List<Header> headersFound = null;
            for (Header header : headers) {
                if (header.getName().equalsIgnoreCase(name)) {
                    if (headersFound == null) {
                        headersFound = new ArrayList<>();
                    }
                    headersFound.add(header);
                }
            }
            return headersFound != null ? headersFound.toArray(new Header[headersFound.size()]) : new Header[]{};
        }


        public Header getFirstHeader(final String name) {
            for (Header header : headers) {
                if (header.getName().equalsIgnoreCase(name)) {
                    return header;
                }
            }
            return null;
        }

    }
}