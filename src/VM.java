import gui.App;
import virtual_machine.*;

public class VM {

    public static void main(String[] args) {

        Machine machine = new Machine();

        App frame = new App("SIC/XE Simulator", machine);
        frame.display_memory();
    }
}
