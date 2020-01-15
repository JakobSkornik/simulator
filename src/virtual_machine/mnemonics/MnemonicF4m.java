package virtual_machine.mnemonics;

import virtual_machine.code.*;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;

public class MnemonicF4m extends Mnemonic {

    public MnemonicF4m(String label, int opcode)  {
        super(label, opcode);
    }

    @Override
    public Node parse(Parser parser) throws SyntaxError {

        boolean n = true;
        boolean i = true;parser.parseHash();

        if (parser.parseIndirect()) i = false;
        if (parser.parseHash()) n = false;

        if (Character.isDigit(parser.lexer.peek())) return new InstructionF4(this, parser.parseNumber(0, Code.MAX_WORD), n, i, parser.parseIndexed());
        else if (Character.isLetter(parser.lexer.peek())) return new InstructionF4(this, parser.parseSymbol(), n, i, parser.parseIndexed());
        else
            throw new SyntaxError(String.format("Invalid character '%c", parser.lexer.peek()), parser.lexer.row, parser.lexer.col);
    }

    @Override
    public String operandToString(Node instruction) {

        InstructionF4 i = ((InstructionF4)instruction);
        return i.symbol != null ? i.symbol : Integer.toString(i.value);
    }
}
