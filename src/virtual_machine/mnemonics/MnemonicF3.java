package virtual_machine.mnemonics;

import virtual_machine.code.InstructionF3;
import virtual_machine.code.Node;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;

public class MnemonicF3 extends Mnemonic {

    public MnemonicF3(String mnemonic, int opcode) {
        super(mnemonic, opcode);
    }

    @Override
    public Node parse(Parser parser) throws SyntaxError {

        return new InstructionF3(this);
    }
}
