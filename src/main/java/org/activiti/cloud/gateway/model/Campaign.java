package org.activiti.cloud.gateway.model;

public class Campaign {

    private String name;
    private String serviceId;
    private String lang;

    public Campaign() {
    }

    public Campaign(String name) {
        this.name = name;
    }

    public Campaign(String name,
                    String serviceId) {
        this.name = name;
        this.serviceId = serviceId;
    }

    public Campaign(String name,
                    String serviceId,
                    String lang) {
        this.name = name;
        this.serviceId = serviceId;
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
