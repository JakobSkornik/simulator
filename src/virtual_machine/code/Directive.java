package virtual_machine.code;

import virtual_machine.mnemonics.Mnemonic;

public class Directive extends Node {

    public String symbol;
    public int value;

    public Directive(Mnemonic m) {

        super(m);
    }

    public Directive(Mnemonic m, String symbol) {

        super(m);
        this.symbol = symbol;
    }

    public Directive(Mnemonic m, int value) {

        super(m);
        this.value = value;
    }

    @Override
    public int length() { return 0;}

    @Override
    public void resolve(Code code) {

        String right_symbol = mnemonic.operandToString(this);

        int val;

        if (code.sym_tab.get(right_symbol) != null) val = code.sym_tab.get(right_symbol);
        val = this.value;

        switch(mnemonic.name) {

            case("START"):

                code.start_address = val;
                break;

            case("ORG"):

                //TODO
                break;

            case("END"):

                code.MAX_ADDR = val;
                break;

            case("BASE"):

                code.regs.setB(val);
                break;
        }
    }
}
