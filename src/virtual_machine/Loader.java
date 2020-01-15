package virtual_machine;

import java.io.IOException;
import java.io.Reader;

public class Loader {

    public static String readString(Reader r, int len) throws IOException {

        StringBuilder buf = new StringBuilder();
        while (len-- > 0) buf.append((char)r.read());
        return buf.toString();
    }

    public static int readByte(Reader r) throws IOException { return Integer.parseInt(readString(r, 2), 16);}

    public static int readWord(Reader r) throws IOException { return Integer.parseInt(readString(r, 6), 16);}

    public static String loadSection(Machine machine, Reader r) {

        try {

            if (r.read() != 'H') return null;
            String titula = readString(r, 6);

            int absoulute_address = readWord(r);
            int end = readWord(r) + absoulute_address;
            r.read();

            Memory memory = machine.mem;

            int k = r.read();

            while(k == 'T') {

                int location    = readWord(r);
                int length_2    = readByte(r);

                while (length_2 > 0) {

                    length_2--;

                    if (location >= absoulute_address && location < end) {

                        byte b = (byte)readByte(r);
                        memory.setByte(b, location);
                        location++;
                    }
                    else { return null;}
                }
            }

            while(k == 'M') {

                readWord(r); readByte(r); r.read();
                k = r.read();
            }

            if(k != 'E') return null;
            return titula;
        }

        catch(IOException e){

            return null;
        }
    }
}
