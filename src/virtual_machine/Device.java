package virtual_machine;

import java.io.IOException;

public abstract class Device {

    public boolean test() { return true;}

    public int read(int where) throws IOException { return -1;}

    public void write(int value, int where) {}
}