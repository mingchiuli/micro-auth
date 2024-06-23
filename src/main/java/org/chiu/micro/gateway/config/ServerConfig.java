package org.chiu.micro.gateway.config;

import java.time.Duration;

import org.chiu.micro.gateway.server.BlogServer;
import org.chiu.micro.gateway.server.ExhibitServer;
import org.chiu.micro.gateway.server.SearchServer;
import org.chiu.micro.gateway.server.UserServer;
import org.chiu.micro.gateway.server.WebSocketServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * ServerConfig
 */
@Configuration
public class ServerConfig {

    @Bean
    BlogServer blogServer() {

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        RestClient client = RestClient.builder()
                .baseUrl("http://micro-blog:8081/sys/blog")
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter)
                .build();
        return factory.createClient(BlogServer.class);
    }

    @Bean
    SearchServer searchServer() {

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        RestClient client = RestClient.builder()
                .baseUrl("http://micro-search:8081/search")
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter)
                .build();
        return factory.createClient(SearchServer.class);
    }

    @Bean
    ExhibitServer exhibitServer() {

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        RestClient client = RestClient.builder()
                .baseUrl("http://micro-exbihit:8081/public/blog")
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter)
                .build();
        return factory.createClient(ExhibitServer.class);
    }


    @Bean
    WebSocketServer webSocketServer() {

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        RestClient client = RestClient.builder()
                .baseUrl("http://micro-websocket:8081/edit")
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter)
                .build();
        return factory.createClient(WebSocketServer.class);
    }

    @Bean
    UserServer userServer() {

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        RestClient client = RestClient.builder()
                .baseUrl("http://micro-user:8081/sys")
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter)
                .build();
        return factory.createClient(UserServer.class);
    }
}