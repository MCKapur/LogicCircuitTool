package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by rohankapur on 4/12/15.
 */
public class ConsoleCircuitBuilder {
    private Scanner scanner;

    // Init
    ConsoleCircuitBuilder() {
        Scanner sc = new Scanner(System.in);
        this.scanner = sc;
    }

    // Bits
    private boolean getBit() {
        System.out.print("Input a bit (0 or 1):\n");
        int bit = this.scanner.nextInt();
        if (bit == 1)
            return true;
        else
            return false;
    }

    // Circuit Input
    public LogicGate getGate(int arbitraryID) {
        System.out.print("Please input a new logic gate: AND, OR, NOT, XOR, NOR, NAND, XNOR\n");
        String GATE_ID = this.scanner.next();
        return new LogicGate(GATE_ID, arbitraryID);
    }
    public LogicGatePort getConnectedPortForGateInCircuit(LogicGateCircuit circuit, LogicGate gate, int prettyPortNumber, boolean isCompleteEntry) {
        LogicGatePort port = new LogicGatePort(true, isCompleteEntry /* Don't know yet */, gate);
        if (!isCompleteEntry) {
            System.out.println("Will input port #" + prettyPortNumber + " be an entry point into the circuit? Y or N.");
            port.isCatalyst = this.scanner.next().equals("Y");
            if (!port.isCatalyst) {
                System.out.print("Then you will need to connect input port #" + prettyPortNumber + " of this gate to the output port of another gate.\n");
                ArrayList<LogicGate> availableGates = circuit.tailDisconnectedLogicGates();
                if (availableGates.size() > 0) {
                    System.out.print("Here are all available ports:\n");
                    for (int i = 0; i < availableGates.size(); i++) {
                        LogicGate availableGate = availableGates.get(i);
                        System.out.println("• " + availableGate.GATE_ID + " gate with an ID of " + availableGate.arbitraryID);
                    }
                    System.out.print("Please type in the ID of the gate you would like to connect to:\n");
                    LogicGatePort portToConnect = circuit.gateWithArbitraryID(this.scanner.nextInt()).outputPort;
                    port.connectWithPort(portToConnect);
                    portToConnect.connectWithPort(port);
                }
                else {
                    System.out.println("There are no available output ports. Would you like to make this port an entry to the circuit? Y or N (cancel).");
                    port.isCatalyst = this.scanner.next().equals("Y");
                    return port.isCatalyst ? port : null;
                }
            }
        }
        return port;
    }
    public LogicGate getConnectedGate(LogicGateCircuit circuit) {
        LogicGate gate = this.getGate(circuit.logicGates.size());
        boolean isCompleteEntry = false;
        if (circuit.logicGates.size() == 0) {
            System.out.println("Each port for this gate will be an entry point to the circuit as it is the first gate you inputted.");
            isCompleteEntry = true;
        }
        if (!isCompleteEntry)
            System.out.println("A(n) " + gate.GATE_ID + " gate has " + gate.numberOfParams() + " ports. You will need to connect them.");
        for (int i = 0; i < gate.numberOfParams(); i++) {
            LogicGatePort port = this.getConnectedPortForGateInCircuit(circuit, gate, (i + 1), isCompleteEntry);
            if (port != null)
                gate.inputPorts[i] = port;
            else
                return null;
        }
        gate.outputPort = new LogicGatePort(false, false, gate);
        return gate;
    }
    public LogicGateCircuit getGateCircuit() {
        System.out.print("I will allow you to build a logic gate circuit. Each logic gate can be connected to another logic gate via its ports. A logic gate may have 1 or more input ports (number of bits going in), and just one output port (one output bit). A connection can only be established between an input and output port -- same type ports will experience a connection rejection.");
        System.out.print("The first gate you add will be an entry point to the circuit.\n");
        LogicGateCircuit circuit = new LogicGateCircuit();
        boolean cont = true;
        while (cont) {
            LogicGate newConnectedGate = getConnectedGate(circuit);
            if (newConnectedGate != null) {
                circuit.logicGates.add(newConnectedGate);
                System.out.print("A(n) " + newConnectedGate.GATE_ID + " gate" + " with an ID of " + newConnectedGate.arbitraryID + " has been connected and added to the circuit.\n");
            }
            System.out.print("Would you like to add a new gate? Y to add or N to complete the circuit.\n");
            if (this.scanner.next().equals("N")) {
                if (circuit.readyForExecution())
                    cont = false;
                else {
                    System.out.println("Sorry, but your circuit is not valid for execution. You still have multiple output ports that are disconnected. To execute, only one output port can be disconnected.\n");
                    System.out.println("You will need to add more gates to resolve this issue.\n");
                }
            }
        }
        return circuit;
    }

    // Circuit Output
    public boolean prettyExecuteCircuit(LogicGateCircuit circuit) {
        System.out.print("You will need to feed the initial catalyst bits to the circuit. Starting:\n");
        ArrayList<LogicGate> catalystGates = circuit.catalystGates();
        HashMap<Integer, ArrayList<Boolean>> catalystBits = new HashMap<Integer, ArrayList<Boolean>>();
        for (int i = 0; i < catalystGates.size(); i++) {
            LogicGate gate = catalystGates.get(i);
            System.out.println(gate.GATE_ID + " gate with ID of " + gate.arbitraryID);
            ArrayList<Boolean> bits = new ArrayList<Boolean>();
            for (int j = 0; j < gate.numberOfCatalystParams(); j++)
                bits.add(new Boolean(this.getBit()));
            catalystBits.put(new Integer(gate.arbitraryID), bits);
        }
        boolean output = circuit.execute(catalystBits);
        return output;
    }
}
