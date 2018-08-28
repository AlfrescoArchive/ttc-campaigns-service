package org.activiti.cloud.campaigns.controller;

import java.util.ArrayList;
import java.util.List;

import org.activiti.cloud.campaigns.model.Campaign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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


    @RequestMapping(path = "/")
    public String helloFromCampaigns() {
        return "{ \"welcome\" : \"Hello from the Trending Topic Campaigns Service \" }";
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
