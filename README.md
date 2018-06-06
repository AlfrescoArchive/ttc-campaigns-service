# BluePrint - Trending Topic Campaigns: Infrastructural Gateway
This project is using the Spring Cloud Gateway project along the Spring Cloud Kubernetes project to create dynamic routes
to the services registered inside a Kubernetes namespace. 

The main goal of this project is to provide a single entrypoint for your client applications to interact with a set of backend services.

This service also adds some domain specific filters to find out Services that are labelled to be Marketing Campaigns and 
expose a REST endpoint to get the list of available campaigns.  

# Run

In order to run this project locally, you need to clone the source code and then run inside the root directory

> mvn -Dserver.port=808x spring-boot:run

**Note**: replace "x" for your desired port number


# Endpoints
- GET http://localhost:808x/ -> welcome message
- GET http://localhost:808x/campaigns/ -> returns the list of all the available campaigns
- GET http://localhost:808x/actuator/gateway/routes -> get all the dynamically registered routes

# Campaigns List Example
```json

[
    {
        "name": "activiti",
        "serviceId": "ttc-rb-english-campaign",
        "lang": "en"
    }
]
```


