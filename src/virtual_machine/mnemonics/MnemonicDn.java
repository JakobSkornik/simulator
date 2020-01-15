package virtual_machine.mnemonics;

import virtual_machine.code.Code;
import virtual_machine.code.Directive;
import virtual_machine.code.Node;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;


/**
 * Directive with one numeric operand.
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class MnemonicDn extends Mnemonic {

	public MnemonicDn(String label, int opcode) {
		super(label, opcode);
	}
	
	@Override
	public Node parse(Parser parser) throws SyntaxError {
		// number
		if (Character.isDigit(parser.lexer.peek()))
			return new Directive(this, parser.parseNumber(0, Code.MAX_ADDR));
		// symbol
		else if (Character.isLetter(parser.lexer.peek()))
			return new Directive(this, parser.parseSymbol());
		// otherwise: error
		else
			throw new SyntaxError(String.format("Invalid character '%c", parser.lexer.peek()), parser.lexer.row, parser.lexer.col);
	}

	@Override
	public String operandToString(Node instruction) {

		Directive i = ((Directive)instruction);
		return i.symbol != null ? i.symbol : Integer.toString(i.value);
	}
}
