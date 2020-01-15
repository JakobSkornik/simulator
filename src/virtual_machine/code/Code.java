package virtual_machine.code;

import virtual_machine.Registers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Podporni razred za predmet Sistemska programska oprema.
 * @author jure
 */
public class Code {

    public static int MAX_ADDR = 1048575;
    public static final int MAX_WORD = 16777215;
    public static List<Node> program;
    public String name;
    public int start_address;
    public HashMap<String, Integer> sym_tab;
    public int loc;
    public int nextLoc;
    public Registers regs;
    public int length;

    public Code(Registers regs) {

        this.regs = regs;
        this.program = new ArrayList<>();
        this.sym_tab = new HashMap<>();
    }

    public void begin() {

        this.loc = 0;
        this.nextLoc = loc + program.get(0).length();
    }

    public void end() {

        this.length = this.loc;
    }

    public void resolve()  {

        this.loc = start_address;

        for (Node node : program) {

            node.enter(this);
            node.resolve(this);
            node.leave(this);
        }

        end();
    }

    public byte[] emitCode() {

        byte[] rawCode = new byte[this.length];

        begin();

        for (Node node : program) {

            node.enter(this);
            node.emitCode(rawCode, loc);
            node.leave(this);
        }

        return rawCode;
    }

    public void append(Node instruction) {

        program.add(instruction);
    }
}
