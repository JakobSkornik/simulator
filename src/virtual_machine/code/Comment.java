package virtual_machine.code;

/**
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class Comment extends Node {

	public Comment(String comment) {

		super(null);
		setComment(comment);
	}

	@Override
	public String toString() {
		return comment;
	}

	@Override
	public int length() { return 0;}
}
