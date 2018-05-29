package org.activiti.cloud.gateway;

import java.util.ArrayList;
import java.util.List;

import org.activiti.cloud.gateway.model.Campaign;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class GatewayApplication {

    private static final Log log = LogFactory.getLog(GatewayApplication.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public DiscoveryClientRouteDefinitionLocator discoveryClientRouteLocator(DiscoveryClient discoveryClient,
                                                                             DiscoveryLocatorProperties properties) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient,
                                                         properties);
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,
                              args);
    }

    @RequestMapping(path = "/")
    public String helloFromGateway() {
        return "Hello from the Trending Topic Campaigns Gateway";
    }

    @RequestMapping(path = "/campaigns")
    public List<Campaign> getCampaigns() {
        List<Campaign> campaigns = new ArrayList<>();
        try {
            List<String> services = discoveryClient.getServices();
            for (String serviceId : services) {
                for (ServiceInstance si : discoveryClient.getInstances(serviceId)) {
                    String type = si.getMetadata().get("type");
                    String campaignName = si.getMetadata().get("campaign.name");
                    String campaignLang = si.getMetadata().get("campaign.lang");
                    if (type != null && type.equals("campaign")) {
                        campaigns.add(new Campaign(campaignName,
                                                   si.getServiceId(),
                                                   campaignLang));
                    }
                }
            }
        } catch (Exception ex) {

            // if discovery is not possible don't do anything
            log.error(">>> Service Discovery wasn't possible. Check your environment configuration to make sure that you have a Service Registry available.");
        }
        return campaigns;
    }
}
