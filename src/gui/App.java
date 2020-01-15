package gui;

import virtual_machine.*;
import virtual_machine.code.Code;
import virtual_machine.code.Node;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class App extends JFrame {

    private JPanel panel;
    private JTable table_regs;
    private JButton stepButton;
    private JButton startButton;
    private JButton stopButton;
    private JComboBox comboBox1;
    private JTable table_commands;
    private JTable table_mem;
    private JButton reloadButton;
    private JButton buttonRefresh;
    private JPanel re;
    private JLabel program_counter;
    private JLabel t;
    private JTextField memSize;
    private JButton buttonPrint;
    private Machine machine;
    private File file;
    private Timer timer;
    private int period;
    private int ctr;
    private String prev;
    public Code code;

    private String toHex(int a) {

        return Integer.toHexString(a & 0xFF);
    }

    private String toHexWord(int a) {

        return Integer.toHexString(a & 0xFFFFFF);
    }

    public void populate_registers() {

        table_regs.setValueAt(toHexWord(machine.regs.getA()), 0, 1);
        table_regs.setValueAt(toHexWord(machine.regs.getB()), 1, 1);
        table_regs.setValueAt(toHexWord(machine.regs.getL()), 2, 1);
        table_regs.setValueAt(toHexWord(machine.regs.getS()), 3, 1);
        table_regs.setValueAt(toHexWord(machine.regs.getT()), 4, 1);
        table_regs.setValueAt(toHexWord(machine.regs.getX()), 5, 1);
        table_regs.setSize(new Dimension(2, 6));
        table_regs.revalidate();
    }

    public void display_memory() {

        for (int i = 0; i < Integer.parseInt(memSize.getText()); i++) {

            for (int j = 0; j < 16; j++) {

                table_mem.setValueAt(toHex(machine.mem.memory[i * 16 + j]), i, j);
            }
        }

        int a = machine.regs.PC();
        int b = a / 16;
        int c = a % 16;

        table_mem.setRowSelectionInterval(b, b);
        table_mem.setColumnSelectionInterval(c, c);
    }

    public void add_command(String oc) {


        String[] s = oc.split(" ");
        String d = toHex(Integer.parseInt(s[1]));
        String c = s[0].join(" ", s[0], d);

        DefaultTableModel model = (DefaultTableModel) table_commands.getModel();
        model.addRow(new Object[]{c});
    }

    public void set_regs() {

        String[] cols = {"Register", "Value"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(cols);
        model.setRowCount(6);
        table_regs.setModel(model);

        table_regs.setValueAt("A", 0, 0);
        table_regs.setValueAt("B", 1, 0);
        table_regs.setValueAt("L", 2, 0);
        table_regs.setValueAt("S", 3, 0);
        table_regs.setValueAt("T", 4, 0);
        table_regs.setValueAt("X", 5, 0);

        program_counter.setText(toHex(machine.regs.PC()));

        populate_registers();
    }

    public void set_mem() {

        DefaultTableModel model_mem = new DefaultTableModel();
        String[] cols_mem = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F"};
        model_mem.setColumnIdentifiers(cols_mem);

        model_mem.setRowCount(Integer.parseInt(memSize.getText()));
        table_mem.setModel(model_mem);
        table_mem.setCellSelectionEnabled(true);

        display_memory();
    }

    public void init(Machine m) {

        machine = m;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setSize(1042, 768);

        //REGISTERS
        set_regs();

        //MEMORY
        set_mem();

        //COMMANDS
        DefaultTableModel model_commands = new DefaultTableModel();
        model_commands.setColumnCount(1);
        table_commands.setModel(model_commands);
        table_commands.setTableHeader(null);
        this.setVisible(true);
    }

    public void refresh() {

        populate_registers();
        display_memory();
        program_counter.setText(toHex(machine.regs.PC()));
    }

    public void reload(Machine m) throws IOException {

        m.mem = new Memory(m.mem.MAX_ADDRESS);
        m.loaded(file.getAbsolutePath());
        m.regs.reset();
    }

    public boolean isRunning() {

        return (timer != null);
    }

    public void stop() {

        if (isRunning()) timer = null;
    }

    public void start() throws Exception {

        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

                try {

                    if (ctr < 200) {

                        String opcode = machine.execute();

                        if (opcode.equals(prev)) {

                            timer.stop();
                            ctr = 0;
                        }

                        add_command(opcode);
                        refresh();
                        ctr++;
                    } else {

                        timer.stop();
                        ctr = 0;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR WHILE RUNNING");
                }
            }
        };

        timer = new Timer(0, taskPerformer);
        timer.setRepeats(true);
        timer.start();
    }

    public App(String title, Machine m) {

        super(title);
        init(m);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    start();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR ON START");
                }
            }
        });

        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {

                    String opcode = machine.execute();
                    add_command(opcode);
                    refresh();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR ON STEP");
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    stop();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR ON STOP");
                }
            }
        });

        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {

                    init(m);
                    reload(m);
                    refresh();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR ON RELOAD");
                }
            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                int s = comboBox1.getSelectedIndex();
                final JFileChooser fc = new JFileChooser();
                switch (s) {

                    case 0:

                        fc.showOpenDialog(App.this);
                        File asm = fc.getSelectedFile();
                        Asm a = new Asm(machine.regs);

                        try {

                            t.setText(asm.getName());
                            String tekst = a.readFile(asm);
                            code = a.assemble(tekst);
                            code.name = asm.getName();
                        }

                        catch (Exception e) { JOptionPane.showMessageDialog(null, "ERROR OPENING ASM");}
                        break;

                    case 1:

                        fc.showOpenDialog(App.this);
                        file = fc.getSelectedFile();

                        try {

                            t.setText(m.loaded(file.getAbsolutePath()));
                            refresh();
                        }

                        catch (Exception e) { JOptionPane.showMessageDialog(null, "ERROR OPENING OBJ");}
                        break;
                }
            }
        });

        buttonRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                refresh();
            }
        });

        memSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                set_mem();
                refresh();
            }
        });

        buttonPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {

                    PrintWriter p = new PrintWriter("pretty_print.txt");

                    for (Node node : code.program) {

                        if (node.mnemonic != null) p.printf("%-15s      %-15s         %-15s\n", node.getLabel(), node.mnemonic.name, node.operandToString());
                    }

                    p.close();

                    JOptionPane.showMessageDialog(null, "Code has been printed to pretty_print.txt!");
                }

                catch(Exception e) {}
            }
        });
    }

}

