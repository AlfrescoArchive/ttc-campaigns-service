package org.activiti.cloud.gateway.controller;

import java.util.ArrayList;
import java.util.List;

import org.activiti.cloud.gateway.model.Campaign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CampaignsRegistryController {

    private Logger logger = LoggerFactory.getLogger(CampaignsRegistryController.class);

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public DiscoveryClientRouteDefinitionLocator discoveryClientRouteLocator(DiscoveryClient discoveryClient,
                                                                             DiscoveryLocatorProperties properties) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient,
                                                         properties);
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
                        logger.info(">>> New Campaign Found: " + campaignName + " - Lang: " + campaignLang);
                        campaigns.add(new Campaign(campaignName,
                                                   si.getServiceId(),
                                                   campaignLang));
                    }
                }
            }
        } catch (Exception ex) {

            // if discovery is not possible don't do anything
            logger.info(">>> Service Discovery wasn't possible. Check your environment configuration to make sure that you have a Service Registry available.");
        }
        return campaigns;
    }
}
