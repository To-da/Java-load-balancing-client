package com.toda.javaloadbalancerclient.config.clientsconfig;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ClientConfigEnabledRoundRobinRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.toda.javaloadbalancerclient.config.ExcludeFromComponentScan;

@Configuration
@ExcludeFromComponentScan
public class LoadBalancerConfig {

    private static Logger log = getLogger(LoadBalancerConfig.class);

//    @Bean
//    public IClientConfig ribbonClientConfig() {
//        DefaultClientConfigImpl config = new DefaultClientConfigImpl();
//        config.loadProperties(JavaLoadBalancerClientApplication.UPSTREAM_SERVER_CLIENT);
//        return config;
//    }

    @Bean
    public IRule ribbonRule(IClientConfig clientConfig) {
        final ClientConfigEnabledRoundRobinRule clientConfigEnabledRoundRobinRule = new ClientConfigEnabledRoundRobinRule();
        clientConfigEnabledRoundRobinRule.initWithNiwsConfig(clientConfig);
        log.info("Initializing {} with {}", clientConfigEnabledRoundRobinRule, clientConfig.getProperties().get("listOfServers"));
        return clientConfigEnabledRoundRobinRule;
    }

    @Bean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig clientConfig, IPing iPing, IRule iRule, ServerList<Server> serverServerList) {
        final BaseLoadBalancer iLoadBalancer = LoadBalancerBuilder.newBuilder()
                .withDynamicServerList(serverServerList)
                .withClientConfig(clientConfig)
                .withPing(iPing)
                .withRule(iRule)
                .buildDynamicServerListLoadBalancer();

        iLoadBalancer.setPingInterval(15);

        log.info("Initializing {} with {}", iLoadBalancer, clientConfig.getProperties().get("listOfServers"));
        return iLoadBalancer;
    }

    @Bean
    public IPing ribbonPing() {
        return new PingUrl(false, "/ping");
    }
}

