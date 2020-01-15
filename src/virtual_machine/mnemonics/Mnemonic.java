package virtual_machine.mnemonics;

import virtual_machine.code.Node;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;

import java.util.HashMap;


/**
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public abstract class Mnemonic {

	public String name;
	public int opcode;
	public boolean extended;

	public Mnemonic(String name, int opcode) {

		this.name = name;
		this.opcode = opcode;

		if (name.startsWith("+")) this.extended = false;
		else this.extended = true;
	}
	
	public abstract Node parse(Parser parser) throws SyntaxError;

	public void setOperand(Node instruction, int n) {}

	@Override
	public String toString() {
		return String.format(" %-6s", name);
	}

	public String operandToString(Node instruction) {
		return "";
	}
}