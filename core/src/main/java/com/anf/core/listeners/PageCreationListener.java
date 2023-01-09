package com.anf.core.listeners;

import com.anf.core.Constant;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import static com.anf.core.Constant.ANF_CONTENT;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class PageCreationListener implements EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageCreationListener.class);

    private ObservationManager observationManager;
    private Session repositorySession;

    @Reference
    private SlingRepository repository;

    @Activate
    @Modified
    public void activate() {
        try {
            repositorySession = repository.loginService(Constant.ANF_SYSTEM_USER, null);
            final Workspace workSpace = repositorySession.getWorkspace();
            if (null != workSpace) {
                observationManager = workSpace.getObservationManager();
                observationManager.addEventListener(this, Event.NODE_ADDED
                        , ANF_CONTENT
                        , true
                        , null
                        , new String[]{"cq:Page"}
                        , false);
                LOGGER.info("Page Creation Listener is Registered at {} for the event type {}."
                        , ANF_CONTENT
                        , Event.NODE_ADDED);
            }
        } catch (RepositoryException e) {
            LOGGER.error("An error occurred while getting session", e);
        }
    }

    @Override
    public void onEvent(final EventIterator eventIterator) {
        while (eventIterator.hasNext()) {
            final Event event = eventIterator.nextEvent();
            try {
                repositorySession.getNode(event.getPath()).getNode(Constant.JCR_CONTENT).setProperty(Constant.PAGE_CREATED, true);
                repositorySession.save();
            } catch (RepositoryException e) {
                LOGGER.error("An error occurred while getting event path", e);
            }
        }
    }

    @Deactivate
    protected void deactivate() {
        try {
            if (null != observationManager) {
                observationManager.removeEventListener(this);
                LOGGER.info("The Page Event Listener is removed.");
            }
        } catch (RepositoryException e) {
            LOGGER.error("An error occurred while removing event listener", e);
        } finally {
            if (null != repositorySession) {
                repositorySession.logout();
            }
        }
    }

}
