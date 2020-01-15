package virtual_machine.mnemonics;

import virtual_machine.code.Code;
import virtual_machine.code.Node;
import virtual_machine.code.Storage;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;


/**
 * Directive with one numeric operand.
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class MnemonicSn extends Mnemonic {

    public MnemonicSn(String label, int opcode) {
        super(label, opcode);
    }

    @Override
    public Node parse(Parser parser) throws SyntaxError {

        if (Character.isDigit(parser.lexer.peek()))
            return new Storage(this, parser.parseNumber(0, Code.MAX_ADDR));

        else
            throw new SyntaxError(String.format("Invalid character '%c", parser.lexer.peek()), parser.lexer.row, parser.lexer.col);
    }

    @Override
    public String operandToString(Node instruction) {

        Storage i = ((Storage)instruction);
        return Integer.toString(i.value);
    }
}
