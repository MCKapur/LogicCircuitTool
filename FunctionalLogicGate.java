package com.company;

import java.util.ArrayList;

/**
 * Created by rohankapur on 1/1/16.
 */
public class FunctionalLogicGate extends LogicGate {

    public ArrayList<Boolean> absoluteParameters;
    public ArrayList<FunctionalLogicGate> paramaterSources;

    // Init
    private void commonInit() {
        this.paramaterSources = new ArrayList<FunctionalLogicGate>();
        this.absoluteParameters = new ArrayList<Boolean>();
    }
    FunctionalLogicGate(String GATE_ID, int arbitraryID) {
        super(GATE_ID, arbitraryID);
        commonInit();
    }
    FunctionalLogicGate(LogicGate logicGate) {
        super(logicGate.GATE_ID, logicGate.arbitraryID);
        this.inputPorts = logicGate.inputPorts;
        this.outputPort = logicGate.outputPort;
        commonInit();
    }

}
