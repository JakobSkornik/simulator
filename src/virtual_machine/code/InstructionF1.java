package virtual_machine.code;

import virtual_machine.mnemonics.Mnemonic;

public class InstructionF1 extends Node{

    public InstructionF1(Mnemonic m) {

        super(m);
    }

    @Override
    public int length() { return 1;}
}
