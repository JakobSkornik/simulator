package virtual_machine.code;

import virtual_machine.mnemonics.Mnemonic;

import javax.swing.*;

/**
 * Abstract class Node.
 * Includes label, mnemonic and comment.
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public abstract class Node {

	public String label;
	public Mnemonic mnemonic;
	public String comment;

	public Node(Mnemonic mnemonic) {
		this.mnemonic = mnemonic;
	}

	public String getLabel() {
		return label == null ? "" : label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Return comment as a string.
	 */
	public String getComment() {
		return comment == null ? "" : comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Return string representation of the node.
	 * Label and comment are not included.
	 */

	public int length() {

		return 0;
	}

	public void resolve(Code c) {

	}

	public void emitCode(byte[] data, int pos) {}

	public void enter(Code c) {

		c.loc = c.nextLoc;
		c.nextLoc += length();
	}

	public void leave(Code c) {}

	public void activate(Code code) {

		if (!getLabel().equals("")) code.sym_tab.put(getLabel(), code.loc);
	}

	@Override
	public String toString() {
		return mnemonic.toString() + " " + operandToString();
	}

	public String operandToString() {
		return mnemonic.operandToString(this);
	}
}
