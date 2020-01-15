package virtual_machine;

import java.util.HashMap;

public class Registers {

    //DECLARATIONS
    private int register_A = 0;

    private int register_X = 0;

    private int register_L = 0;

    private int register_B = 0;

    private int register_S = 0;

    private int register_T = 0;

    private double register_F = 0;

    private int register_PC = 0;

    private int flag_CC;


    //FUNCTIONS
    //getters:
    public int getA() { return register_A; }

    public int getX() { return register_X; }

    public int getL() { return register_L; }

    public int getB() { return register_B; }

    public int getS() { return register_S; }
    
    public int getT() { return register_T; }
    
    public double getF() { return register_F; }

    public int getCC(){

        return flag_CC;
    }

    public int PC() {
        return register_PC; 
    }

    public void increment_PC(){ register_PC++;}

    //setters:
    public void setA(int val)       { register_A = val;}

    public void setX(int val)       { register_X = val;}

    public void setB(int val)       { register_B = val;}

    public void setL(int val)       { register_L = val;}

    public void setS(int val)       { register_S = val;}

    public void setT(int val)       { register_T = val;}

    public void setF(double val)    { register_F = val;}

    public void setSW(int val)      { flag_CC = val;}

    public void setPC(int val)      { register_PC = val;}

    public void setFlag_CC(int val) {

        if (val < 0) flag_CC = -1;
        else if (val > 0) flag_CC = 1;
        else flag_CC = 0;
    }

    public int getReg(int indeks) {

        switch(indeks) {

            case 0:
                
                return getA();

            case 1:
                
                return getX();

            case 2:
                
                return getL();

            case 3:

                return getB();

            case 4:

                return getS();
            
            case 5:

                return getT();
        }

        return -1;
    }

    public void setReg(int indeks, int val) {

        switch(indeks) {

            case 0:

                setA(val);
                break;

            case 1:

                setX(val);
                break;

            case 2:

                setL(val);
                break;

            case 3:

                setB(val);
                break;

            case 4:

                setS(val);
                break;

            case 5:

                setT(val);
                break;
        }
    }

    public void setAasByte(int value) {

        register_A = register_A & 0xFFFF00 | value & 0xFF;
    }

    public void reset() {

        setA(0);
        setB(0);
        setX(0);
        setS(0);
        setL(0);
        setT(0);
    }

    public int getAasByte() {
        return register_A & 0xFF;
    }
}