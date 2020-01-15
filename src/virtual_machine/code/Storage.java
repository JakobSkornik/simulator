package virtual_machine.code;

import virtual_machine.mnemonics.Mnemonic;

import javax.swing.*;

public class Storage extends Node {

    public byte[] data;
    public int value;

    public Storage(Mnemonic m, int value) {

        super(m);
        this.value = value;
    }

    public Storage(Mnemonic m, byte[] symbol) {

        super(m);
        this.data = symbol;
    }

    @Override
    public int length() {

        if (this.data == null) {

            if (this.mnemonic.opcode == 4) return 3;
            else if (this.mnemonic.opcode == 3) return 1;
            else if (this.mnemonic.opcode == 1) return 3 * value;
            else return value;
        }

        return data.length;
    }
}
