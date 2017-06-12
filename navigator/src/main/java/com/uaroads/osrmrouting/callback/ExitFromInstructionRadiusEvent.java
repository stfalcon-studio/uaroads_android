package com.uaroads.osrmrouting.callback;

public class ExitFromInstructionRadiusEvent {

//    public Instruction instruction;
    public int position;

    public ExitFromInstructionRadiusEvent(int position) {
        this.position = position;
    }
}
