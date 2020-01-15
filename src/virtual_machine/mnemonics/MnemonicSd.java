package virtual_machine.mnemonics;

import virtual_machine.code.Code;
import virtual_machine.code.Node;
import virtual_machine.code.Storage;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;

import java.io.UnsupportedEncodingException;


/**
 * Directive with one numeric operand.
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class MnemonicSd extends Mnemonic {

    public MnemonicSd(String label, int opcode) {
        super(label, opcode);
    }

    @Override
    public Node parse(Parser parser) throws SyntaxError {

        return new Storage(this, parser.parseData());
    }

    @Override
    public String operandToString(Node instruction) {

        Storage j = ((Storage)instruction);

        if (j.data == null) return Integer.toString(j.value);

        StringBuilder b = new StringBuilder();

        for (int i = 0; i < 3; i++)
            b.append(String.format("%02X", j.data[i]));

        return b.toString();
    }
}
