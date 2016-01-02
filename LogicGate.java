package com.company;

/**
 * Created by rohankapur on 31/12/15.
 */
public class LogicGate {
    public int arbitraryID;
    public String GATE_ID;
    public LogicGatePort inputPorts[];
    public LogicGatePort outputPort;

    // Convenience
    public int numberOfParams() {
        return nParamsFromGateID(this.GATE_ID);
    }
    public int numberOfCatalystParams() {
        int sum = 0;
        for (int i = 0; i < this.inputPorts.length; i++) {
            if (this.inputPorts[i].isCatalyst)
                sum++;
        }
        return sum;
    }
    private static int nParamsFromGateID(String GATE_ID) {
        if (GATE_ID.equals("NOT") || GATE_ID.equals(("ID")))
            return 1;
        return 2;
    }

    // Init
    LogicGate(String GATE_ID, int arbitraryID) {
        this.GATE_ID = GATE_ID;
        this.arbitraryID = arbitraryID;
        this.inputPorts = new LogicGatePort[this.numberOfParams()];
    }

    // Execution
    public boolean openGate(boolean[] bitParams) {
        if (bitParams.length != this.numberOfParams())
            return false;
        switch (this.GATE_ID) {
            case "AND":
                return AND(bitParams);
            case "OR":
                return OR(bitParams);
            case "XOR":
                return XOR(bitParams);
            case "NOT":
                return NOT(bitParams);
            case "NAND":
                return NAND(bitParams);
            case "NOR":
                return NOR(bitParams);
            case "XNOR":
                return XNOR(bitParams);
            case "ID":
                return ID(bitParams);
            default:
                return false;
        }
    }

    // Individual Logic Gates
    private static boolean AND(boolean[] bitParams) { return (bitParams[0] && bitParams[1]); }
    private static boolean OR(boolean[] bitParams) {
        return (bitParams[0] || bitParams[1]);
    }
    private static boolean XOR(boolean[] bitParams) { return (bitParams[0] && !bitParams[1]) || (bitParams[0] && !bitParams[1]); }
    private static boolean NOT(boolean[] bitParams) { return !bitParams[0]; }
    private static boolean NAND(boolean[] bitParams) { return !AND(bitParams); }
    private static boolean NOR(boolean[] bitParams) { return !OR(bitParams); }
    private static boolean XNOR(boolean[] bitParams) { return !XOR(bitParams); }
    private static boolean ID(boolean[] bitParams) { return bitParams[0]; }
}
