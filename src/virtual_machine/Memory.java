package virtual_machine;

public class Memory {

    public final byte[] memory;

    public Memory(int max_address) {

        this.memory = new byte[max_address];
    }

    public final int MAX_ADDRESS = 1048575;

    public int getByte(int address) {

        if(valid(address))
            return (int)memory[address] & 0xFF;
        
        return 0;
    }
    
    public void setByte(int val, int address) {

        if(valid(address)) memory[address] = (byte)(val & 0xFF);
    }

    public int getWord(int address) {

        if(valid(address)) return getByte(address + 2) | getByte(address + 1) << 8 | getByte(address) << 16;

        return 0;
    }

    public void setWord(int val, int address) {

        setByte(val >> 16, address);
        setByte(val >> 8, address + 1);
        setByte(val,address + 2);
    }

    public boolean valid(int address) { return (address >= 0 && address < MAX_ADDRESS);}
}