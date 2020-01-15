package virtual_machine;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Stack;
import java.util.Timer;

public class Machine {

    public final Registers regs;
    public Memory mem;
    public final Device[] devices;
    public  Opcode opc;
    private Timer timer;
    private long period;
    private int IC;
    private HashMap<Integer, String> command_names;
    private HashMap<Integer, String> reg_names;
    private HashMap<String, Integer> reg_numbers;

    private Stack <Integer> sub_addr;

    public Machine() {

        this.regs = new Registers();
        this.mem = new Memory(1048576);
        this.devices = new Device[256];
        this.opc = new Opcode();
        this.timer = new Timer();
        this.command_names = opc.getNames();
        this.sub_addr = new Stack<>();
        this.period = 10;

        try {

            init_devices();
        }

        catch (IOException e){

            System.out.println("Cannot init machine!");
        }
    }

    public String loaded(String path) throws IOException {

        return loadSection(new FileReader(path));
    }

    public String execute() throws IOException{

        int opcode = fetch();
        if (execF1(opcode)) return command_names.get(opcode);

        int operand = fetch();
        if (execF2(opcode, operand)) return command_names.get(opcode) + " " + operand;

        Flags f = new Flags(opcode, operand);

        int op;

        if (f.is_sic) {

            op = (operand & 0x7F) << 8 | fetch() & 0xFF;
        }

        else if (f.ext) {

            int a = fetch();
            int b = fetch();
            op = (operand & 0x0F) << 16 | a << 8 | b;
        }

        else {

            op = (operand & 0x0F) << 8 | fetch();

            if (f.pcd) {

                int pcr = op >= 2048 ? op - 4096 : op;

                op = pcr + regs.PC();
            }

            else if (f.based) {

                op += regs.getB();
            }

            else if (!f.abs) {

                invalidAddressing();
            }
        }

        if (f.indexd) {

            if (f.simp) op += regs.getX() & 0xFF;
            else invalidAddressing();
        }

        if (execSICF3F4(opcode & 0xFC, f, op)) return command_names.get(opcode & 0xFC) + " " + op;
        invalidOpcode(opcode);
        return "ERROR";
    }

    public boolean execF1(int opcode) {

        switch (opcode) {

            //NOT IMPLEMENTED
            case Opcode.FLOAT:	notImplemented(); break;
            case Opcode.FIX:	notImplemented(); break;
            case Opcode.NORM:	notImplemented(); break;
            case Opcode.SIO:	notImplemented(); break;
            case Opcode.HIO:	notImplemented(); break;
            case Opcode.TIO:	notImplemented(); break;
            default:			return false;
        }

        return true;
    }

    public boolean execF2(int opcode, int operand) { 
        
        int operand_1 = (operand & 0xF0) >> 4;
        int operand_2 = (operand & 0x0F);

        switch (opcode) {

            case Opcode.ADDR:	regs.setReg(operand_2, regs.getReg(operand_2) + regs.getReg(operand_1)); break;
            case Opcode.SUBR:	regs.setReg(operand_2, regs.getReg(operand_2) - regs.getReg(operand_1)); break;
            case Opcode.MULR:	regs.setReg(operand_2, regs.getReg(operand_2) * regs.getReg(operand_1)); break;
            case Opcode.DIVR:	regs.setReg(operand_2, regs.getReg(operand_2) / regs.getReg(operand_1)); break;
            case Opcode.COMPR:	regs.setFlag_CC(regs.getReg(operand_1) - regs.getReg(operand_2)); break;
            case Opcode.SHIFTL:	regs.setReg(operand_1, regs.getReg(operand_1) << (operand_2 + 1) | regs.getReg(operand_1) >> (24 - operand_2 - 1)); break;
            case Opcode.SHIFTR:	regs.setReg(operand_1, regs.getReg(operand_1) >> (operand_2 + 1)); break;
            case Opcode.CLEAR:	regs.setReg(operand_1, 0); break;
            case Opcode.TIXR:	regs.setX(regs.getX()+1);

                regs.setFlag_CC(regs.getX() - regs.getReg(operand_1));
                break;


            //NOT IMPLEMENTED
            case Opcode.RMO:	notImplemented(); break;
            case Opcode.SVC:	notImplemented(); break;
            default: return false;
        }

        return true;
    }

    public boolean execSICF3F4(int opcode, Flags f, int operand) throws IOException {

        switch (opcode) {

            case Opcode.STA:    mem.setWord(regs.getA(), storeAddr(f, operand)); break;
            case Opcode.STX:	mem.setWord(regs.getX(), storeAddr(f, operand)); break;
            case Opcode.STL:	mem.setWord(regs.getL(), storeAddr(f, operand)); break;
            case Opcode.STCH:	mem.setByte(regs.getA(), storeAddr(f, operand)); break;
            case Opcode.STB:	mem.setWord(regs.getB(), storeAddr(f, operand)); break;
            case Opcode.STS:	mem.setWord(regs.getS(), storeAddr(f, operand)); break;
            case Opcode.STT:	mem.setWord(regs.getT(), storeAddr(f, operand)); break;
            case Opcode.STSW:	mem.setWord(regs.getCC(), storeAddr(f, operand)); break;
            case Opcode.JEQ:	if (regs.getCC() == 0) regs.setPC(storeAddr(f, operand)); break;
            case Opcode.JGT:	if (regs.getCC() == 1) regs.setPC(storeAddr(f, operand)); break;
            case Opcode.JLT:	if (regs.getCC() == -1) regs.setPC(storeAddr(f, operand)); break;
            case Opcode.J:      regs.setPC(storeAddr(f, operand)); break;
            case Opcode.RSUB:   regs.setPC(regs.getL()); if (!sub_addr.isEmpty()) popsub(); break;
            case Opcode.JSUB:	regs.setL(regs.PC()); pushsub(); regs.setPC(storeAddr(f, operand)); break;
            case Opcode.LDA:	regs.setA(loadWord(f, operand)); break;
            case Opcode.LDX:	regs.setX(loadWord(f, operand)); break;
            case Opcode.LDL:	regs.setL(loadWord(f, operand)); break;
            case Opcode.LDCH:	regs.setAasByte(loadByte(f, operand)); break;
            case Opcode.LDB:	regs.setB(loadWord(f, operand)); break;
            case Opcode.LDS:	regs.setS(loadWord(f, operand)); break;
            case Opcode.LDT:	regs.setT(loadWord(f, operand)); break;
            case Opcode.ADD:	regs.setA(regs.getA() + loadWord(f, operand)); break;
            case Opcode.SUB:	regs.setA(regs.getA() - loadWord(f, operand)); break;
            case Opcode.MUL:	regs.setA(regs.getA() * loadWord(f, operand)); break;
            case Opcode.DIV:	regs.setA(regs.getA() / loadWord(f, operand)); break;
            case Opcode.AND:	regs.setA(regs.getA() & loadWord(f, operand)); break;
            case Opcode.OR:		regs.setA(regs.getA() | loadWord(f, operand)); break;
            case Opcode.COMP:	regs.setFlag_CC(regs.getA() - loadWord(f, operand)); break;
            case Opcode.TIX:	regs.setX(regs.getX() + 1);
                regs.setFlag_CC(regs.getX() - loadWord(f, operand)); break;
            case Opcode.RD:		regs.setAasByte(devices[operand].read(loadByte(f, operand)));  break;
            case Opcode.WD:		devices[operand].write(loadByte(f, operand), regs.getAasByte()); break;
            default: return false;
        }
        return true;
    }

    private int loadWord (Flags f, int op) {

        if(f.imm) return op;
        op = mem.getWord(op);
        if(f.indir) op = mem.getWord(op);
        return op;
    }

    private int loadByte (Flags f, int op) {

        if (f.imm) return op;
        if (f.indir) return mem.getByte(mem.getWord(op));
        return mem.getByte(op);
    }

    private int storeAddr (Flags f, int addr) {

        return f.indir ? mem.getWord(addr) : addr;
    }

    private void pushsub() { this.sub_addr.push(this.regs.PC());}

    private void popsub() { this.sub_addr.pop();}

    public Integer get_sub_addr() {

        if (this.sub_addr.isEmpty()) return null;
        else return this.sub_addr.peek();
    }

    public int fetch() {

        int bajt    = mem.getByte(regs.PC());
        regs.increment_PC();
        return bajt;
    }

    public Device getDevice(int indeks) {

        return devices[indeks];
    }

    public void setDevice(int indeks, Device d) {

        devices[indeks] = d;
    }

    public void init_devices() throws  IOException {

        setDevice(0, new InputDevice(System.in));
        setDevice(1, new OutputDevice(System.out));
        setDevice(2, new OutputDevice(System.err));

        for (int i = 3; i < 3; i++)
            setDevice(i, new FileDevice(toHex(i) + ".dev"));
    }

    private String toHex(int a) {

        return Integer.toHexString(a & 0xFF);
    }

    public void invalidOpcode(int opcode) {

        devices[1].write(1,1);
    }

    public void invalidAddressing() {

        devices[1].write(2, 2);
    }

    public void notImplemented() {

        devices[1].write(3, 2);
    }

    public static String readString(Reader r, int len) throws IOException {

        StringBuilder buf = new StringBuilder();
        while (len-- > 0) buf.append((char)r.read());
        return buf.toString();
    }

    public static int readByte(Reader r) throws IOException { return 0xFF & Integer.parseInt(readString(r, 2), 16);}

    public static int readWord(Reader r) throws IOException { return 0xFFFFFF & Integer.parseInt(readString(r, 6), 16);}

    public String loadSection(Reader r) {

        try {

            if (r.read() != 'H') return "failed4";
            String titula = readString(r, 6);

            int absoulute_address = readWord(r);
            int end = readWord(r) + absoulute_address;
            r.read();

            int k = r.read();

            while(k == 'T') {

                int location    = readWord(r);
                int length_2    = readByte(r);

                while (length_2-- > 0) {

                    if (location < absoulute_address || location >= end) return Integer.toString(absoulute_address);

                    byte b = (byte)readByte(r);
                    mem.setByte(b, location++);
                }

                r.read();
                k = r.read();
            }

            while(k == 'M') {


                readWord(r); readByte(r); r.read();
                k = r.read();
            }


            if(k != 'E') return "-" + k;
            regs.setPC(readWord(r));
            return titula;
        }

        catch(IOException e){

            return "failed";
        }
    }
}