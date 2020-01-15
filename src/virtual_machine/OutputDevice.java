package virtual_machine;

import java.io.OutputStream;

public class OutputDevice extends Device {

    private OutputStream out;

    public OutputDevice(OutputStream out) {

        this.out = out;
    }
    /*
    @Override
    public void write(int val) { 
        try {

            out.write(val);
            out.flush();
        }
        catch(IOException e) {}
    }*/
}