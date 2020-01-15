package virtual_machine.parsing;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import virtual_machine.Opcode;
import virtual_machine.Registers;
import virtual_machine.code.*;
import virtual_machine.mnemonics.*;

import javax.swing.*;

/**
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class Parser {
	
	public Lexer lexer;
	public Registers regs;

	public String parseLabel() {
		if (lexer.col == 1 && Character.isLetter(lexer.peek()))
			return lexer.readAlphanumeric();
		return null;
	}

	public Mnemonic parseMnemonic() throws SyntaxError {
		boolean isExtended = lexer.advanceIf('+');
		String name = lexer.readAlphanumeric();
		Mnemonic mnemonic = get(isExtended ? "+" + name : name);
		if (mnemonic == null)
			throw new SyntaxError(String.format("Invalid mnemonic '%s'", name), lexer.row, lexer.col);

		return mnemonic;
	}

	public String parseSymbol() {
		return lexer.readAlphanumeric();
	}

	public int parseRegister() throws SyntaxError {
		int ch = lexer.advance();
		int reg = "AXLBSTF".indexOf(ch);
		if (reg < 0) throw new SyntaxError(String.format("Invalid register '%c'", ch), lexer.row, lexer.col);
		return reg;
	}

	public void parseComma() throws SyntaxError {
		lexer.skipWhitespace();
		lexer.advance(',');
		lexer.skipWhitespace();
	}

	public boolean parseHash() throws SyntaxError {
		lexer.skipWhitespace();
		if (lexer.advanceIf('#')) {
			lexer.skipWhitespace();
			return  true;
		}
		return false;
	}

	public boolean parseIndirect() throws SyntaxError {
		lexer.skipWhitespace();
		if (lexer.advanceIf('@')) {
			lexer.skipWhitespace();
			return  true;
		}
		return false;
	}

	public boolean parseIndexed() throws SyntaxError {
		lexer.skipWhitespace();
		if (lexer.advanceIf(',')) {
			lexer.skipWhitespace();
			lexer.advance('X');
			return  true;
		}
		return false;
	}

	public int parseNumber(int lo, int hi) throws SyntaxError {
		int num;
		if (lexer.peek() == '0') {
			int r = -1;
			switch (lexer.peek(1)) {
			case 'b': r = 2; break;
			case 'o': r = 8; break;
			case 'x': r = 16; break;
			}
			if (r != -1) {
				lexer.advance();
				lexer.advance();
				try {
					num = Integer.parseInt(lexer.readDigits(r), r);
				} catch (NumberFormatException e) {
					throw new SyntaxError("Invalid number", lexer.row, lexer.col);					
				}
			} else
				// fallback to decimal base
				try {
					num = Integer.parseInt(lexer.readDigits(10));
				} catch (NumberFormatException e) {
					throw new SyntaxError("Invalid number", lexer.row, lexer.col);					
				}			
		} else if (Character.isDigit(lexer.peek()))
			try {
				num = Integer.parseInt(lexer.readDigits(10));
			} catch (NumberFormatException e) {
				throw new SyntaxError("Invalid number", lexer.row, lexer.col);					
			}
		else
			throw new SyntaxError("Number expected", lexer.row, lexer.col);
		// number must not be followed by letter or digit
		if (Character.isLetterOrDigit(lexer.peek()))
			throw new SyntaxError(String.format("invalid digit '%c'", lexer.peek()), lexer.row, lexer.col);
		// check range
		if (num < lo || num > hi)
			throw new SyntaxError(String.format("Number '%d' out of range [%d..%d]", num, lo, hi), lexer.row, lexer.col);
		return num;
	}

	public byte[] parseData() throws SyntaxError {
		// C'chars' or X'hex' or number
		if (lexer.advanceIf('C')) {
			// C'<chars>'
			lexer.advance('\'');
			return lexer.readTo('\'').getBytes();
		} else if (lexer.advanceIf('X')) {
			// X'<hex>'
			lexer.advance('\'');
			String s = lexer.readTo('\'');
			byte[] data = new byte[s.length() / 2];
			for (int i = 0; i < data.length; i++)
				data[i] = (byte) Integer.parseInt(s.substring(2*i, 2*i+2), 16);
			return data;
		} else if (Character.isDigit(lexer.peek())) {
			// number, represented by word
			int num = parseNumber(0, Code.MAX_WORD);
			byte[] data = new byte[3];
			data[2] = (byte) num;
			data[1] = (byte) (num >> 8); 
			data[0] = (byte) (num >> 16);
			return data;
		}
		throw new SyntaxError(String.format("Invalid storage specifier '%s'", lexer.peek()), lexer.row, lexer.col);
	}

	// instruction parser

	public Node parseInstruction() throws SyntaxError {
		// check for comment
		if (lexer.col == 1 && lexer.peek() == '.')
			return new Comment(lexer.readTo('\n'));
		// check for label
		String label = parseLabel();
		// skip whitespace: if EOL and without label then continue (i.e. skip empty lines)
		if (lexer.skipWhitespace() && label == null) {
			lexer.advance();  // skip EOL
			return null;
		}

		// parse mnemonic name
		Mnemonic mnemonic = parseMnemonic();
		// skip whitespace
		lexer.skipWhitespace();
		// parse any operands and obtain instruction
		Node node = mnemonic.parse(this);

		// set label and comment
		node.setLabel(label);
		node.setComment(lexer.readTo('\n'));
		return node;
	}

	public Code parseCode() throws SyntaxError, SemanticError {
		Code code = new Code(regs);

		while (lexer.peek() > 0) {
			// skip comments / skip to the beginning of line
			while (lexer.peek() > 0 && lexer.col > 1)
				lexer.readTo('\n');

			// parse instruction
			Node instruction = parseInstruction();

			if (instruction != null) {

				code.append(instruction);
				instruction.enter(code);
				instruction.activate(code);
				instruction.leave(code);
			}
		}

		return code;
	}

	public Code parse(String input) throws SyntaxError, SemanticError {
		lexer = new Lexer(input);

		return parseCode();
	}
	
	// ***** mnemonics *****

	public Map<String, Mnemonic> mnemonics;

	Mnemonic get(String name) {
		if (mnemonics.containsKey(name)) return mnemonics.get(name);
		return null;
	}

	void put(Mnemonic mnemonic) {
		mnemonics.put(mnemonic.name, mnemonic);
	}

	public void put34(String name, int opcode) {
		put(new MnemonicF3m(name, opcode));
		put(new MnemonicF4m("+" + name, opcode));
	}

	void initMnemonics() {

		this.mnemonics = new HashMap<>();
		// Directives
		put(new MnemonicD ("NOBASE",	Opcode.NOBASE));
		put(new MnemonicD ("LTORG",	Opcode.LTORG));
		put(new MnemonicDn("START",		Opcode.START));
		put(new MnemonicDn("END",			Opcode.END));
		put(new MnemonicDn("BASE",		Opcode.BASE));
		put(new MnemonicDn("EQU",			Opcode.EQU));
		put(new MnemonicDn("ORG",			Opcode.ORG));
		// Storage directives
		put(new MnemonicSn("RESB",		Opcode.RESB));
		put(new MnemonicSn("RESW",		Opcode.RESW));
		put(new MnemonicSd("BYTE",		Opcode.BYTE));
		put(new MnemonicSd("WORD",		Opcode.WORD));
		// Format 1 mnemonics, no operand
		put(new MnemonicF1("FIX",		Opcode.FIX));
		put(new MnemonicF1("FLOAT", 	Opcode.FLOAT));
		put(new MnemonicF1("NORM",	Opcode.NORM));
		put(new MnemonicF1("SIO",		Opcode.SIO));
		put(new MnemonicF1("HIO",		Opcode.HIO));
		put(new MnemonicF1("TIO",		Opcode.TIO));
		// Format 2 mnemonics, one or two operands
		put(new MnemonicF2n("SVC",		Opcode.SVC));
		put(new MnemonicF2rn("SHIFTL",	Opcode.SHIFTL));
		put(new MnemonicF2rn("SHIFTR",	Opcode.SHIFTR));
		put(new MnemonicF2rr("ADDR",		Opcode.ADDR));
		put(new MnemonicF2rr("SUBR",		Opcode.SUBR));
		put(new MnemonicF2rr("MULR",		Opcode.MULR));
		put(new MnemonicF2rr("DIVR",		Opcode.DIVR));
		put(new MnemonicF2rr("COMPR",		Opcode.COMPR));
		put(new MnemonicF2rr("RMO",		Opcode.RMO));
		put(new MnemonicF2r("CLEAR",		Opcode.CLEAR));
		put(new MnemonicF2r("TIXR",		Opcode.TIXR));
		// Load and store
		put34("LDA",	Opcode.LDA);
		put34("LDCH",	Opcode.LDCH);
		put34("LDB",	Opcode.LDB);
		put34("LDF",	Opcode.LDF);
		put34("LDL",	Opcode.LDL);
		put34("LDS",	Opcode.LDS);
		put34("LDT",	Opcode.LDT);
		put34("LDX",	Opcode.LDX);
		put34("LPS",	Opcode.LPS);
		put34("STA",	Opcode.STA);
		put34("STCH",	Opcode.STCH);
		put34("STB",	Opcode.STB);
		put34("STF",	Opcode.STF);
		put34("STL",	Opcode.STL);
		put34("STS",	Opcode.STS);
		put34("STT",	Opcode.STT);
		put34("STX",	Opcode.STX);
		put34("STI",	Opcode.STI);
		put34("STSW",	Opcode.STS);
		// fixed point operations, register-memory
		put34("ADD",	Opcode.ADD);
		put34("SUB",	Opcode.SUB);
		put34("MUL",	Opcode.MUL);
		put34("DIV",	Opcode.DIV);
		put34("COMP",	Opcode.COMP);
		put34("AND",	Opcode.AND);
		put34("OR",	Opcode.OR);
		put34("TIX",	Opcode.TIX);
		// floating point arithmetic
//		put34("ADDF",	Opcode.ADDF);
//		put34("SUBF",	Opcode.SUBF);
//		put34("MULF",	Opcode.MULF);
//		put34("DIVF",	Opcode.DIVF);
//		put34("COMPF",	Opcode.COMPF);
		// jumps
		put34("J",	Opcode.J);
		put34("JEQ",	Opcode.JEQ);
		put34("JGT",	Opcode.JGT);
		put34("JLT",	Opcode.JLT);
		put34("JSUB",	Opcode.JSUB);
		put(new MnemonicF3("RSUB", Opcode.RSUB));
		// IO
		put34("RD",	Opcode.RD);
		put34("WD",	Opcode.WD);
		put34("TD",	Opcode.TD);
		// System
		put34("SSK",	Opcode.SSK);
	}

	public Parser(Registers regs) {

		initMnemonics();
		this.regs = regs;
	}

}
