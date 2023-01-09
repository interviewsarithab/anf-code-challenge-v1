package com.anf.core.services.impl;

import com.anf.core.services.ResolverService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

import static com.anf.core.Constant.ANF_SYSTEM_USER;
@Component(
        immediate = true,
        service = ResolverService.class
)
public class ResolverServiceImpl implements ResolverService {

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public ResourceResolver getSystemResourceResolver() throws LoginException {
        Map<String, Object> resolverProps = new HashMap<>();
        resolverProps.put(ResourceResolverFactory.SUBSERVICE, ANF_SYSTEM_USER);
        return resolverFactory.getServiceResourceResolver(resolverProps);
    }

}
