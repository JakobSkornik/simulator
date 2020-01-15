package virtual_machine.code;

import virtual_machine.mnemonics.Mnemonic;

public class InstructionF2 extends Node {

    public String register;
    public String register_2;
    public int value;
    private int type;

    public InstructionF2(Mnemonic m, String r) {

        super(m);
        this.register = r;
        this.type = 1;
    }

    public InstructionF2(Mnemonic m, int val) {

        super(m);
        this.value = val;
        this.type = 2;
    }

    public InstructionF2(Mnemonic m, String r, int val) {

        super(m);
        this.register = r;
        this.value = val;
        this.type = 3;
    }

    public InstructionF2(Mnemonic m, String r, String r2) {

        super(m);
        this.register = r;
        this.register_2 = r2;
        this.type = 4;
    }

    @Override
    public int length() { return 2;}

    @Override
    public void emitCode(byte[] data, int pos) {


        switch(type) {

            case 1:

                String opc = Integer.toHexString(this.mnemonic.opcode);
                String hex = Integer.toHexString(Integer.parseInt(register));

                data[pos] = (byte)this.mnemonic.opcode;
                data[pos + 1] = (byte)Integer.parseInt(register, 16);
                break;
        }
    }
}
