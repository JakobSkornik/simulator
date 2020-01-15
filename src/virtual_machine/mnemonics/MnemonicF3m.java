package virtual_machine.mnemonics;

import virtual_machine.code.*;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;

public class MnemonicF3m extends Mnemonic {

    public MnemonicF3m(String label, int opcode)  {
        super(label, opcode);
    }

    @Override
    public Node parse(Parser parser) throws SyntaxError {


        boolean n = true;
        boolean i = true;

        if (parser.parseIndirect()) i = false;
        if (parser.parseHash()) n = false;

        if (Character.isDigit(parser.lexer.peek())) return new InstructionF3(this, parser.parseNumber(0, Code.MAX_WORD), n, i, parser.parseIndexed());
        else if (Character.isLetter(parser.lexer.peek())) return new InstructionF3(this, parser.parseSymbol(), n, i, parser.parseIndexed());
        else
            throw new SyntaxError(String.format("Invalid character '%c", parser.lexer.peek()), parser.lexer.row, parser.lexer.col);
    }

    @Override
    public String operandToString(Node instruction) {

        InstructionF3 i = ((InstructionF3)instruction);
        return i.symbol != null ? i.symbol : Integer.toString(i.value);
    }
}
