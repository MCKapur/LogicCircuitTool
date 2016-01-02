package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean contCreate = true;
        System.out.println("Welcome to LogicGateTool. This program will ask you to build a logic gate and input catalyst (initial) bits, and then will output the propagation result of the circuit.");
        ConsoleCircuitBuilder console = new ConsoleCircuitBuilder();
        while (contCreate) {
            LogicGateCircuit circuit = console.getGateCircuit();
            boolean contUse = true;
            while (contUse) {
                boolean output = console.prettyExecuteCircuit(circuit);
                System.out.println("Final result is " + BoolToInt.boolToInt(output));
                System.out.println("Would you like to continue using this circuit? Y or N.");
                contUse = scanner.next().equals("Y");
            }
            System.out.println("Would you like to create a new circuit? Y or N.");
            contCreate = scanner.next().equals("Y");
        }
    }
}
