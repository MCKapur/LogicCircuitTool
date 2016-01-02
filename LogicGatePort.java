package com.company;

/**
 * Created by rohankapur on 31/12/15.
 */
public class LogicGatePort {

    public boolean isInputPort; // A port type... in a sense
    public boolean isCatalyst; // Entry point to circuit
    public LogicGate attachment;
    private LogicGatePort connection;

    // Init
    LogicGatePort(boolean isInput, boolean catalyst, LogicGate _attachment) {
        if (!isInput)
            catalyst = false;
        this.isInputPort = isInput;
        this.isCatalyst = catalyst;
        this.attachment = _attachment;
    }

    // Connecting
    public LogicGatePort connection() {
        return this.connection;
    }
    public boolean connectWithPort(LogicGatePort port) {
        if (this.isInputPort != port.isInputPort) {
            this.connection = port;
            return true;
        }
        return false;
    }

}
