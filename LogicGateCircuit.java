package com.company;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rohankapur on 4/12/15.
 */
public class LogicGateCircuit {

    protected ArrayList<LogicGate> logicGates;

    // Init
    LogicGateCircuit() {
        logicGates = new ArrayList<LogicGate>();
    }

    // Searching
    public ArrayList<LogicGate> catalystGates() {
        ArrayList<LogicGate> catalystGates = new ArrayList<LogicGate>();
        for (int i = 0; i < this.logicGates.size(); i++) {
            LogicGate gate = this.logicGates.get(i);
            for (int j = 0; j < gate.inputPorts.length; j++) {
                if (gate.inputPorts[j].isCatalyst) {
                    catalystGates.add(gate);
                    break;
                }
            }
        }
        return catalystGates;
    }
    public ArrayList<LogicGate> tailDisconnectedLogicGates() {
        ArrayList<LogicGate> tailDisconnectedLogicGates = new ArrayList<LogicGate>();
        for (int i = 0; i < this.logicGates.size(); i++) {
            LogicGate gate = this.logicGates.get(i);
            if (gate.outputPort == null || (gate.outputPort != null && gate.outputPort.connection() == null))
                tailDisconnectedLogicGates.add(gate);
        }
        return tailDisconnectedLogicGates;
    }
    public LogicGate gateWithArbitraryID(int aID) {
        for (int i = 0; i < this.logicGates.size(); i++) {
            LogicGate gate = this.logicGates.get(i);
            if (gate.arbitraryID == aID)
                return gate;
        }
        return null;
    }
    public LogicGate circuitExitPoint() { // Only accurate when readyForExecution() is true
        return this.tailDisconnectedLogicGates().get(0);
    }
    public int numberOfCatalystBits() {
        int sum = 0;
        for (int i = 0; i < this.logicGates.size(); i++) {
            LogicGate gate = this.logicGates.get(i);
            for (int j = 0; j < gate.inputPorts.length; j++) {
                if (gate.inputPorts[j].isCatalyst)
                    sum++;
            }
        }
        return sum;
    }

    // Execution
    public boolean readyForExecution() {
        return (this.tailDisconnectedLogicGates().size() == 1);
    }
    public ArrayList<FunctionalLogicGate> recursivelyComputeFunctionalLogicGates(HashMap<Integer, ArrayList<Boolean>> catalystBits, ArrayList<FunctionalLogicGate> functionalLogicGates, FunctionalLogicGate currentPoint) {
        if (functionalLogicGates == null)
            functionalLogicGates = new ArrayList<FunctionalLogicGate>();
        ArrayList<Boolean> bits = catalystBits.get(currentPoint.arbitraryID);
        for (int i = 0; i < currentPoint.inputPorts.length; i++) {
            LogicGatePort port = currentPoint.inputPorts[i];
            if (port.isCatalyst) {
                currentPoint.absoluteParameters.add(bits.get(i));
            } else {
                FunctionalLogicGate attachment = new FunctionalLogicGate(port.connection().attachment);
                currentPoint.paramaterSources.add(attachment);
                functionalLogicGates = recursivelyComputeFunctionalLogicGates(catalystBits, functionalLogicGates, attachment);
            }
        }
        functionalLogicGates.add(currentPoint);
        return functionalLogicGates;
    }
    public boolean[] booleanObjArrayListToPrimitive(ArrayList<Boolean> BooleanArr) {
        boolean[] booleanArr = new boolean[BooleanArr.size()];
        for (int i = 0; i < BooleanArr.size(); i++)
            booleanArr[i] = BooleanArr.get(i);
        return booleanArr;
    }
    public boolean execute(HashMap<Integer, ArrayList<Boolean>> catalystBits) {
        ArrayList<FunctionalLogicGate> functionalLogicGates = recursivelyComputeFunctionalLogicGates(catalystBits, null, new FunctionalLogicGate(this.circuitExitPoint()));
        FunctionalLogicGate currentPoint = functionalLogicGates.get(0);
        FunctionalLogicGate nextPoint = null;
        boolean output = false;
        for (int i = 0; i < functionalLogicGates.size(); i++) {
            boolean medOutput = currentPoint.openGate(booleanObjArrayListToPrimitive(currentPoint.absoluteParameters));
            if (i == functionalLogicGates.size() - 1)
                output = medOutput;
            else {
                nextPoint = functionalLogicGates.get(1);
                for (int j = 0; j < nextPoint.paramaterSources.size(); j++) {
                    if (nextPoint.paramaterSources.get(j).arbitraryID == currentPoint.arbitraryID) {
                        nextPoint.absoluteParameters.add(new Boolean(medOutput));
                        break;
                    }
                }
            }
            currentPoint = nextPoint;
        }
        return output;
    }

}
