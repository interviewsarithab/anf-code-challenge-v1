package com.anf.core.services.impl;

import com.anf.core.pojo.User;
import com.anf.core.services.ContentService;
import com.anf.core.services.ResolverService;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;

import static com.anf.core.Constant.*;
import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;

@Component(immediate = true, service = ContentService.class)
public class ContentServiceImpl implements ContentService {

    @Reference
    private ResolverService resolverService;

    @Override
    public User getUserFromRequest(SlingHttpServletRequest request) {
        Map parameterMap = request.getParameterMap();
        if (isParameterValid(parameterMap)) {
            User user = new User(request.getParameter(FIRST_NAME)
                    , request.getParameter(LAST_NAME),
                    Integer.parseInt(request.getParameter(AGE)));
            return user;
        }
        return null;
    }

    private boolean isParameterValid(Map parameterMap) {
        return parameterMap.containsKey(FIRST_NAME)
                && parameterMap.containsKey(LAST_NAME)
                && parameterMap.containsKey(AGE)
                && StringUtils.isNotEmpty(parameterMap.get(FIRST_NAME).toString())
                && StringUtils.isNotEmpty(parameterMap.get(LAST_NAME).toString())
                && StringUtils.isNotEmpty(parameterMap.get(AGE).toString());
    }

    @Override
    public void commitUserDetails(User user) throws LoginException, PersistenceException {
        final ResourceResolver resourceResolver = resolverService.getSystemResourceResolver();
        String userDataPath = new StringBuilder(VAR_ANF_CODE_CHALLENGE).append(user.getFirstName()).append(user.getLastName()).append(user.getAge()).toString();
        Resource anfuserGeneratedResource = ResourceUtil.getOrCreateResource(resourceResolver, userDataPath, NT_UNSTRUCTURED,NT_UNSTRUCTURED, true);
        ModifiableValueMap modifiableValueMap = anfuserGeneratedResource.adaptTo(ModifiableValueMap.class);
        modifiableValueMap.put(FIRST_NAME, user.getFirstName());
        modifiableValueMap.put(LAST_NAME, user.getLastName());
        modifiableValueMap.put(AGE, user.getAge());
        resourceResolver.commit();
    }

    @Override
    public boolean isUserAgeValid(int age) throws LoginException {
        Resource ageResource = resolverService.getSystemResourceResolver().resolve(ETC_AGE);
        ValueMap valueMap = ageResource.getValueMap();
        int maxAge = Integer.parseInt(valueMap.get(MAX_AGE).toString());
        int minAge = Integer.parseInt(valueMap.get(MIN_AGE).toString());
        if (age <= maxAge && age >= minAge) {
            return true;
        }
        return false;
    }
}
