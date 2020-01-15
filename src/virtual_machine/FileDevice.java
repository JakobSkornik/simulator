package virtual_machine;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileDevice extends Device {
    
    public String fname;
    public RandomAccessFile file;

    public FileDevice(String fname) throws IOException { file = new RandomAccessFile(fname, "rw");}
    /*
    @Override
    public int read() {

        try {

            return file.read();
        }

        catch (IOException e) {

            return -1;
        }
    }

    @Override
    public void write(int val) {

        try {

            file.write(val);
        }

        catch(IOException e) {

            return;
        }
    }*/
}