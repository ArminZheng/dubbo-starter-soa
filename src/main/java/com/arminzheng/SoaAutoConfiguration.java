package com.arminzheng;

import com.arminzheng.config.SoaProperties;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ConsumerConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.dubbo.spring.boot.util.DubboUtils.BASE_PACKAGES_PROPERTY_NAME;
import static org.apache.dubbo.spring.boot.util.DubboUtils.DUBBO_SCAN_PREFIX;

/**
 * Configuration
 *
 * @author ape
 * @since 2022.01.08
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
        prefix = DUBBO_SCAN_PREFIX,
        name = BASE_PACKAGES_PROPERTY_NAME,
        havingValue = "foo",
        matchIfMissing = true)
@EnableConfigurationProperties(SoaProperties.class)
@EnableDubbo(scanBasePackages = "com.arminzheng.application")
public class SoaAutoConfiguration {

    private final SoaProperties soaProperties;

    @Value("${spring.application.name}")
    private String name;

    @Value("${server.port}")
    private Integer port;

    public SoaAutoConfiguration(SoaProperties soaProperties) {
        this.soaProperties = soaProperties;
    }

    @Bean
    public ApplicationConfig application() {
        ApplicationConfig application = new ApplicationConfig();
        application.setName(name);
        return application;
    }

    @Bean
    public RegistryConfig registry() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        if (soaProperties.getAddress() == null) {
            if ("dev".equals(soaProperties.getActive()))
                soaProperties.setAddress("10.8.206.109:2181");
            else if ("test".equals(soaProperties.getActive()))
                soaProperties.setAddress("116.62.110.70:2181");
            else soaProperties.setAddress("127.0.0.1:2181");
        }
        registryConfig.setAddress(soaProperties.getAddress());
        return registryConfig;
    }

    @Bean
    public ProtocolConfig protocol() {
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(20880 + (port - 9080));
        return protocol;
    }

    @Bean
    public ConsumerConfig consumer() {
        ConsumerConfig consumer = new ConsumerConfig();
        consumer.setCheck(false);
        consumer.setRetries(3);
        consumer.setTimeout(6000);
        return consumer;
    }

    @Bean
    public ProviderConfig provider() {
        ProviderConfig provider = new ProviderConfig();
        provider.setTimeout(3000);
        return provider;
    }
}
