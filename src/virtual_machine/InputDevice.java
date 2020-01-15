package virtual_machine;

import java.io.InputStream;

public class InputDevice extends Device {

    InputStream in;

    public InputDevice(InputStream in) {

        this.in = in;
    }
    /*
    @Override
    public int read() {

        try {

            return in.read();
        }
        catch(IOException e) {

            return -1;
        }
    }*/
}