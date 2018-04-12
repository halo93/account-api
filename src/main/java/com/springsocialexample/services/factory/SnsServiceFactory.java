package com.springsocialexample.services.factory;

import com.springsocialexample.services.IBaseProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SnsServiceFactory {
    private final List<IBaseProviderService> serviceList;

    @Autowired
    public SnsServiceFactory(List<IBaseProviderService> serviceList) {
        this.serviceList = serviceList;
    }

    public IBaseProviderService get(String snsName) {
        return serviceList
            .stream()
            .filter(service -> service.supports(snsName))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
