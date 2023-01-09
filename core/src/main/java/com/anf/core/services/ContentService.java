package com.anf.core.services;

import com.anf.core.pojo.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;

public interface ContentService {
	User getUserFromRequest(SlingHttpServletRequest request);

	void commitUserDetails(User user) throws LoginException, PersistenceException;

	boolean isUserAgeValid(int age) throws LoginException;
}
