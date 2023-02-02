package com.atguigu.gulimall.search.config;

import com.atguigu.common.utils.R;
import org.apache.http.HttpHost;
import org.bouncycastle.cert.ocsp.Req;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhp
 * @date 2023-02-01 15:14
 */
@Configuration
public class GulimallElasticSearchConfig {

    //请求选项
    public static final RequestOptions COMMENT_OPTIONS;

    static{
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMENT_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient(){
        RestClientBuilder builder = null;
        builder = RestClient.builder(new HttpHost("192.168.56.10",9200));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
