package org.activiti.cloud.campaigns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CampaignsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampaignsApplication.class,
                              args);
    }
}
