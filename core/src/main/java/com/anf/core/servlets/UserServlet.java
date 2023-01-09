/***Begin Code - Saritha Bommakanti ***/
package com.anf.core.servlets;

import com.anf.core.pojo.User;
import com.anf.core.services.ContentService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@Component(service = {Servlet.class})
@SlingServletPaths(
        value = "/bin/saveUserDetails"
)
public class UserServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServlet.class);

    private static final long serialVersionUID = 1L;

    @Reference
    private ContentService contentService;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        User user = contentService.getUserFromRequest(req);
        if (Objects.isNull(user)) {
            writer.print("Invalid form data submitted.");
            return;
        }

        try {
            if (contentService.isUserAgeValid(user.getAge())) {
                contentService.commitUserDetails(user);
                writer.print("User data saved successfully.");
            } else {
                writer.print("You are not eligible");
            }
        } catch (LoginException e) {
            LOGGER.error("Save user details failed with exception : {0}", e);
        }
    }
}

/***END Code*****/
