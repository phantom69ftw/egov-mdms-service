package org.egov.wf.web.models;

import lombok.Data;

@Data
public class ProcessStateAndAction {
/*Contains the action object to be performed, the currentState and resultantState
   to avoid multiple iterations*/

    private ProcessInstance processInstance = null;

    private Action action = null;

    private State currentState = null;

    private State resultantState = null;

}