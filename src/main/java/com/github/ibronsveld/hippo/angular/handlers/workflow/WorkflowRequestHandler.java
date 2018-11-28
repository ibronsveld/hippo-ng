package com.github.ibronsveld.hippo.angular.handlers.workflow;

import com.github.ibronsveld.hippo.angular.AngularPluginContext;
import com.github.ibronsveld.hippo.angular.behaviors.PluginRequest;
import com.github.ibronsveld.hippo.angular.behaviors.PluginResponse;
import com.github.ibronsveld.hippo.angular.handlers.AbstractPluginRequestHandler;
import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hippoecm.frontend.session.UserSession;
import org.hippoecm.repository.api.HippoWorkspace;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.api.WorkflowManager;
import org.onehippo.repository.documentworkflow.DocumentWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WorkflowRequestHandler extends AbstractPluginRequestHandler {

    private static final String ACTION_PUBLISH_REQUEST = "workflow.publish";
    private static final String ACTION_CANCEL_REQUEST = "workflow.cancel";

    private static final String ACTION_DEPUBLISH_REQUEST = "workflow.depublish";
    private static final String ACTION_REJECT_REQUEST = "workflow.reject";
    private static final String ACTION_WORKFLOW_STATE = "workflow.status";
    private static final String ACTION_APPROVE_REQUEST = "workflow.approve";

    private static final String WORKFLOW_CATEGORY = "default";

    private static final Logger log = LoggerFactory.getLogger(WorkflowRequestHandler.class);

    public WorkflowRequestHandler(AngularPluginContext context) {
        super(context);
    }

    @Override
    public boolean canProcess(PluginRequest request) {
        if (request.getAction() != null && !request.getAction().equals("")) {
            // Validate the action
            switch(request.getAction().toLowerCase()) {
                case WorkflowRequestHandler.ACTION_PUBLISH_REQUEST:
                    return true;
                case WorkflowRequestHandler.ACTION_CANCEL_REQUEST:
                    return true;
                case WorkflowRequestHandler.ACTION_DEPUBLISH_REQUEST:
                    return true;
                case WorkflowRequestHandler.ACTION_REJECT_REQUEST:
                    return true;
                case WorkflowRequestHandler.ACTION_APPROVE_REQUEST:
                    return true;
                case WorkflowRequestHandler.ACTION_WORKFLOW_STATE:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public PluginResponse process(PluginRequest request) {
        if (request.getAction() != null && !request.getAction().equals("")) {
            return doWorkflowAction(request);
        }
        return null;
    }

    public PluginResponse doWorkflowAction(PluginRequest request) {
        Session jcrSession = UserSession.get().getJcrSession();
        //Session s = UserSession.get().getJcrSession();

        String documentId = request.getAsString("documentId");
        String action = request.getAction();
        try {

            Node startingNode = jcrSession.getNodeByIdentifier(documentId);

            WorkflowManager wflManager = ((HippoWorkspace) jcrSession.getWorkspace()).getWorkflowManager();
            DocumentWorkflow workflow = (DocumentWorkflow) wflManager.getWorkflow(WORKFLOW_CATEGORY, startingNode);


            // Check action
            switch(action) {
                case WorkflowRequestHandler.ACTION_APPROVE_REQUEST:
                    return handleRequest(request, workflow, "accept");

                case WorkflowRequestHandler.ACTION_PUBLISH_REQUEST:
                    return publish(request, workflow);

                case WorkflowRequestHandler.ACTION_WORKFLOW_STATE:
                    return getCurrentWorkflowStatus(workflow);

                case WorkflowRequestHandler.ACTION_REJECT_REQUEST:
                    return handleRequest(request, workflow, "reject");

                case WorkflowRequestHandler.ACTION_DEPUBLISH_REQUEST:
                    return depublish(request, workflow);

                case WorkflowRequestHandler.ACTION_CANCEL_REQUEST:
                    return handleRequest(request, workflow, "cancel");
            }

        } catch (RepositoryException | RemoteException | WorkflowException e) {
            log.error("Error executing workflow action", e);
            PluginResponse response = new PluginResponse();
            response.setError(500, e.getMessage());
            return response;
        }

        return null;
    }

    private PluginResponse getCurrentWorkflowStatus(DocumentWorkflow workflow) throws RepositoryException, RemoteException, WorkflowException {
        PluginResponse pluginResponse = new PluginResponse();

        JsonObject jsonObject = new JsonObject();
        // Iterate over the options
        Map<String, Serializable> hints = workflow.hints();

        for (String key : hints.keySet()) {
            jsonObject.addProperty(key, hints.get(key).toString());
        }

        pluginResponse.addResponseBody(jsonObject);
        return pluginResponse;
    }

    private PluginResponse publish(PluginRequest request, DocumentWorkflow workflow){
        // Do something here
        PluginResponse pluginResponse = new PluginResponse();

        try {
            // TODO: Figure out how to do the actual action depending on user context
            // TODO: Handle date
            Map<String, Serializable> hints = workflow.hints();

            if (hints.containsKey("publish") && Boolean.parseBoolean(hints.get("publish").toString())) {
                workflow.publish();
                pluginResponse.addResponseBody("published");
            } else {
                if (hints.containsKey("requestPublication") && Boolean.parseBoolean(hints.get("requestPublication").toString())) {
                    workflow.requestPublication();
                    pluginResponse.addResponseBody("publicationrequested");
                }
            }


        } catch (WorkflowException | RepositoryException | RemoteException e) {
            log.error("Error publishing document", e);
            pluginResponse.setError(500, e.getMessage());
        }

        return pluginResponse;
    }

    private PluginResponse depublish(PluginRequest request, DocumentWorkflow workflow){
        // Do something here
        PluginResponse pluginResponse = new PluginResponse();

        try {
            // TODO: Figure out how to do the actual action depending on user context
            // TODO: Handle date
            Map<String, Serializable> hints = workflow.hints();

            if (hints.containsKey("depublish") && Boolean.parseBoolean(hints.get("depublish").toString())) {
                workflow.publish();
                pluginResponse.addResponseBody("depublished");
            } else {
                if (hints.containsKey("requestDepublication") && Boolean.parseBoolean(hints.get("requestDepublication").toString())) {
                    workflow.requestPublication();
                    pluginResponse.addResponseBody("depublicationrequested");
                }
            }


        } catch (WorkflowException | RepositoryException | RemoteException e) {
            log.error("Error publishing document", e);
            pluginResponse.setError(500, e.getMessage());
        }

        return pluginResponse;
    }

    private PluginResponse handleRequest(PluginRequest request, DocumentWorkflow workflow, String type) {

        PluginResponse pluginResponse = new PluginResponse();

        try {
            // TODO: Figure out how to do the actual action depending on user context
            // TODO: Handle date
            Map<String, Serializable> hints = workflow.hints();

            if (hints.containsKey("requests")) {
                // We have a requests object
                String req = hints.get("requests").toString();
                String reqId = getRequestId(req, type +"Request=true");
                if (reqId != null) {
                    switch(type) {
                        case "cancel":
                            workflow.cancelRequest(reqId);
                            break;
                        case "accept":
                            workflow.acceptRequest(reqId);
                            break;
                        case "reject":
                            String comment = request.getAsString("comment");
                            workflow.rejectRequest(reqId, comment);
                            break;
                    }
                    pluginResponse.addResponseBody("ok");
                }
            }
        } catch (WorkflowException | RepositoryException | RemoteException e) {
            log.error("Error publishing document", e);
            pluginResponse.setError(500, e.getMessage());
        }

        return pluginResponse;
    }

    private String getRequestId(String requestObject, String actionValidation) {
        Pattern p = Pattern.compile("\\{(.+)=\\{(.+)\\}\\}");

        Matcher m = p.matcher(requestObject);

        if (m.matches()) {
            // There is two groups that we need to validate
            if (m.group(1) != null) {
                // We have the ID here
                // Check group 2
                if (m.group(2) != null) {

                    if (m.group(2).toLowerCase().contains(actionValidation.toLowerCase())) {
                        return m.group(1);
                    }
                }
            }
        }
        return null;
    }
}
