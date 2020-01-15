package virtual_machine.mnemonics;

import virtual_machine.code.InstructionF2;
import virtual_machine.code.Node;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;

public class MnemonicF2rr extends Mnemonic {

    public MnemonicF2rr(String label, int opcode) {
        super(label, opcode);
    }

    @Override
    public Node parse(Parser parser) throws SyntaxError {

        if (Character.isLetter(parser.lexer.peek())) {
            String r = Integer.toString(parser.parseRegister());

            parser.parseComma();

            if (Character.isLetter(parser.lexer.peek()))
                return new InstructionF2(this, r, Integer.toString(parser.parseRegister()));

            else
                throw new SyntaxError(String.format("Invalid character '%c", parser.lexer.peek()), parser.lexer.row, parser.lexer.col);
        }

        else
            throw new SyntaxError(String.format("Invalid character '%c", parser.lexer.peek()), parser.lexer.row, parser.lexer.col);
    }

    @Override
    public String operandToString(Node instruction) {

        InstructionF2 i = (InstructionF2)instruction;

        return i.register + ", " + i.register_2;
    }
}