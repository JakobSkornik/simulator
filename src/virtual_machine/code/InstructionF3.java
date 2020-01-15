package virtual_machine.code;

import virtual_machine.mnemonics.Mnemonic;
import javax.swing.*;

public class InstructionF3 extends Node {

    public String symbol;
    public int value;
    public boolean n;
    public boolean i;
    public boolean x;

    public InstructionF3(Mnemonic m) {

        super(m);
    }

    public InstructionF3(Mnemonic m, String symbol, boolean n, boolean i, boolean x) {

        super(m);
        this.symbol = symbol;
        this.n = n;
        this.i = i;
        this.x = x;
    }

    public InstructionF3(Mnemonic m, int value, boolean n, boolean i, boolean x) {

        super(m);
        this.value = value;
        this.n = n;
        this.i = i;
        this.x = x;
    }

    @Override
    public int length() { return 3;}

    @Override
    public void resolve(Code code) {

        String right_symbol = mnemonic.operandToString(this);
        int val = this.value;

        if (code.sym_tab.get(right_symbol) != null) val = code.sym_tab.get(right_symbol);

        int valPC = val - code.regs.PC();
        int valB = val - code.regs.getB();

        if (valPC >= -2048 && valPC <= 2047) val = val & 0x2FFF;
        else if (valB >= 0 && valB <= 4095) val = val & 0x4FFF;

        this.value = val;

        if(this.value >= Code.MAX_ADDR || this.value < 0) {

            JOptionPane.showMessageDialog(null, "INVALID ADDRESS" + value);
        }
    }

    @Override
    public String operandToString() {

        return Integer.toString(this.value);
    }
}
