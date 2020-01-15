package virtual_machine;

import java.util.HashMap;

public class Opcode {

    HashMap<Integer, String> names = new HashMap<>();

    public Opcode() {

        names.put(0x00, "LDA");
        names.put(0x04, "LDX");
        names.put(0x08, "LDL");
        names.put(0x0C, "STA");
        names.put(0x10, "STX");
        names.put(0x14, "STL");
        names.put(0x18, "ADD");
        names.put(0x1C, "SUB");
        names.put(0x20, "MUL");
        names.put(0x24, "DIV");
        names.put(0x28, "COMP");
        names.put(0x2C, "TIX");
        names.put(0x30, "JEQ");
        names.put(0x34, "JGT");
        names.put(0x38, "JLT");
        names.put(0x3C, "J");
        names.put(0x40, "AND");
        names.put(0x44, "OR");
        names.put(0x48, "JSUB");
        names.put(0x4C, "RSUB");
        names.put(0x50, "LDCH");
        names.put(0x54, "STCH");
        names.put(0x58, "ADDF");
        names.put(0x5C, "SUBF");
        names.put(0x60, "MULF");
        names.put(0x64, "DIVF");
        names.put(0x68, "LDB");
        names.put(0x6C, "LDS");
        names.put(0x70, "LDF");
        names.put(0x74, "LDT");
        names.put(0x78, "STB");
        names.put(0x7C, "STS");
        names.put(0x80, "STF");
        names.put(0x84, "STT");
        names.put(0x88, "COMPF");
        names.put(0x90, "ADDR");
        names.put(0x94, "SUBR");
        names.put(0x98, "MULR");
        names.put(0x9C, "DIVR");
        names.put(0xA0, "COMPR");
        names.put(0xA4, "SHIFTL");
        names.put(0xA8, "SHIFTR");
        names.put(0xAC, "RMO");
        names.put(0xB0, "SVC");
        names.put(0xB4, "CLEAR");
        names.put(0xB8, "TIXR");
        names.put(0xC0, "FLOAT");
        names.put(0xC4, "FIX");
        names.put(0xC8, "NORM");
        names.put(0xD0, "LPS");
        names.put(0xD4, "STI");
        names.put(0xD8, "RD");
        names.put(0xDC, "WD");
        names.put(0xE0, "TD");
        names.put(0xE8, "STSW");
        names.put(0xEC, "SSK");
        names.put(0xF0, "SIO");
        names.put(0xF4, "HIO");
        names.put(0xF8, "TIO");
    }

    public HashMap<Integer, String> getNames() {

        return names;
    }

    public static final int LDA     = 0x00;
    public static final int LDX     = 0x04;
    public static final int LDL     = 0x08;
    public static final int STA     = 0x0C;
    public static final int STX     = 0x10;
    public static final int STL     = 0x14;
    public static final int ADD     = 0x18;
    public static final int SUB     = 0x1C;
    public static final int MUL     = 0x20;
    public static final int DIV     = 0x24;
    public static final int COMP    = 0x28;
    public static final int TIX     = 0x2C;
    public static final int JEQ     = 0x30;
    public static final int JGT     = 0x34;
    public static final int JLT     = 0x38;
    public static final int J       = 0x3C;
    public static final int AND     = 0x40;
    public static final int OR      = 0x44;
    public static final int JSUB    = 0x48;
    public static final int RSUB    = 0x4C;
    public static final int LDCH    = 0x50;
    public static final int STCH    = 0x54;
    public static final int ADDF    = 0x58;
    public static final int SUBF    = 0x5C;
    public static final int MULF    = 0x60;
    public static final int DIVF    = 0x64;
    public static final int LDB     = 0x68;
    public static final int LDS     = 0x6C;
    public static final int LDF     = 0x70;
    public static final int LDT     = 0x74;
    public static final int STB     = 0x78;
    public static final int STS     = 0x7C;
    public static final int STF     = 0x80;
    public static final int STT     = 0x84;
    public static final int COMPF   = 0x88;
    public static final int ADDR    = 0x90;
    public static final int SUBR    = 0x94;
    public static final int MULR    = 0x98;
    public static final int DIVR    = 0x9C;
    public static final int COMPR   = 0xA0;
    public static final int SHIFTL  = 0xA4;
    public static final int SHIFTR  = 0xA8;
    public static final int RMO     = 0xAC;
    public static final int SVC     = 0xB0;
    public static final int CLEAR   = 0xB4;
    public static final int TIXR    = 0xB8;
    public static final int FLOAT   = 0xC0;
    public static final int FIX     = 0xC4;
    public static final int NORM    = 0xC8;
    public static final int LPS     = 0xD0;
    public static final int STI     = 0xD4;
    public static final int RD      = 0xD8;
    public static final int WD      = 0xDC;
    public static final int TD      = 0xE0;
    public static final int STSW    = 0xE8;
    public static final int SSK     = 0xEC;
    public static final int SIO     = 0xF0;
    public static final int HIO     = 0xF4;
    public static final int TIO     = 0xF8;

    public static final int START       = 0;
    public static final int END         = 1;

    public static final int CSECT       = 2;
    public static final int USE         = 3;
    public static final int EXTREF      = 4;
    public static final int EXTDEF      = 5;

    public static final int ORG         = 6;
    public static final int LTORG       = 7;

    public static final int BASE        = 8;
    public static final int NOBASE      = 9;

    public static final int EQU         = 10;

    // Storage directives

    public static final int RESB        = 0;
    public static final int RESW        = 1;
    public static final int RESF        = 2;
    public static final int BYTE        = 3;
    public static final int WORD        = 4;
    public static final int FLOT        = 5;

}