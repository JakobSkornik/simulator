package virtual_machine.mnemonics;

import virtual_machine.code.InstructionF1;
import virtual_machine.code.Node;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;


/**
 * Directive without operands.
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class MnemonicF1 extends Mnemonic {

    public MnemonicF1(String mnemonic, int opcode) {
        super(mnemonic, opcode);
    }

    @Override
    public Node parse(Parser parser) throws SyntaxError {

        return new InstructionF1(this);
    }

}