/***Begin Code - Saritha Bommakanti ***/
package com.anf.core.services;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;

public interface ResolverService {
    ResourceResolver getSystemResourceResolver() throws LoginException;
}

/***END Code*****/
