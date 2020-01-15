package virtual_machine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import virtual_machine.code.Code;
import virtual_machine.code.Node;
import virtual_machine.code.SemanticError;
import virtual_machine.mnemonics.Mnemonic;
import virtual_machine.parsing.Parser;
import virtual_machine.parsing.SyntaxError;

import javax.swing.*;
import javax.swing.plaf.synth.SynthButtonUI;

/**
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class Asm {

	public final Parser parser;
	public Registers regs;

	public Asm(Registers regs) {

		this.regs = regs;
		this.parser = new Parser(regs);
	}

	public static String readFile(File file) {

	    byte[] buf = new byte[(int) file.length()];
	    try {
	    	InputStream s = new FileInputStream(file);
	    	try {
	    		s.read(buf);
			} finally {
	    		s.close();
	    	}
    	} catch (IOException e) {
    		return "";
	    }
	    return new String(buf);
	}

	public Code assemble(String input) throws SemanticError, SyntaxError, IOException {

		Code code = parser.parse(input);

		code.resolve();

		byte[] raw = code.emitCode();



		return code;
	}

}
