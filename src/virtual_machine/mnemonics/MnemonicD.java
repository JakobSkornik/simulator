package virtual_machine.mnemonics;

import virtual_machine.code.Directive;
import virtual_machine.code.Node;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;


/**
 * Directive without operands.
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class MnemonicD extends Mnemonic {

	public MnemonicD(String mnemonic, int opcode) {
		super(mnemonic, opcode);
	}

	@Override
	public Node parse(Parser parser) throws SyntaxError {

		return new Directive(this);
	}

	@Override
	public String operandToString(Node instruction) {

		return "";
	}
}
