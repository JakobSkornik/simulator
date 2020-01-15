package virtual_machine;

public class Flags {

    public static final int NONE         = 0x00;
    // ni flags
    public static final int MASK_NI      = 0x03;
    public static final int SIC          = 0x00;
    public static final int IMMEDIATE    = 0x01;
    public static final int INDIRECT     = 0x02;
    public static final int SIMPLE       = 0x03;
    // xbpe flags
    public static final int MASK_XBPE    = 0xF0;
    public static final int MASK_XBP     = 0xE0;
    public static final int MASK_BP      = 0x60;
    public static final int INDEXED      = 0x80;
    public static final int BASERELATIVE = 0x40;
    public static final int PCRELATIVE   = 0x20;
    public static final int EXTENDED     = 0x10;

    private int ni;
    private int xbpe;
    public boolean is_sic;
    public boolean imm;
    public boolean indir;
    public  boolean simp;
    public  boolean indexd;
    public  boolean based;
    public  boolean pcd;
    public  boolean rel;
    public  boolean abs;
    public  boolean ext;

    public Flags(int opcode, int op) {

        setNI(opcode);
        setXBPE(op);
        setflags();
    }

    public void setNI(int opcode) {

        ni = opcode & MASK_NI;
    }

    public void setXBPE(int op) {

        xbpe = op & MASK_XBPE;
    }

    public void setflags() {

        is_sic  = (ni & MASK_NI) == SIC;
        imm     = (ni & MASK_NI) == IMMEDIATE;
        indir   = (ni & MASK_NI) == INDIRECT;
        simp    = (ni & MASK_NI) == SIMPLE || (ni & MASK_NI) == SIC;
        indexd  = (xbpe & INDEXED) == INDEXED;
        based   = (xbpe & BASERELATIVE) == BASERELATIVE;
        pcd     = (xbpe & PCRELATIVE) == PCRELATIVE;
        rel     = based || pcd;
        abs     = (xbpe & MASK_BP) == NONE;
        ext     = (xbpe & EXTENDED) == EXTENDED;
    }
}
