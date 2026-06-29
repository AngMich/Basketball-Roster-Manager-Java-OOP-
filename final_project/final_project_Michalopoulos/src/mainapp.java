import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class mainapp implements ActionListener {
    JFrame frame;
    JTable table;
    DefaultTableModel tableModel;
    JButton addButton;
    JButton editButton;
    JButton deleteButton;
    JButton saveButton;
    JButton loadButton;
    JButton statsButton;

    JComboBox<String> filterButton;
    JComboBox<String> sortButton;

    HashSet<player> players = new HashSet<>();
    //from line 14 to 28 shows the components that i used in this class

    public mainapp(){
        //this is the main cass (as well as the constructor or this class that sets upp the application
        // and adds all the buttons and ComboBoxes
        // as well as creates the frame and the table
        frame = new JFrame("final project");
        frame.setSize(1100,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();

        addButton = new JButton("ADD Player");
        editButton = new JButton("EDIT Players");
        deleteButton = new JButton("DELETE Player");
        saveButton = new JButton("Save File");
        loadButton = new JButton("Load File");
        statsButton = new JButton("STATS Summary");

        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        editButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);
        statsButton.addActionListener(this);
        //this gets a source for the different buttons

        topPanel.add(addButton);
        topPanel.add(deleteButton);
        topPanel.add(editButton);
        topPanel.add(saveButton);
        topPanel.add(loadButton);
        topPanel.add(statsButton);
        //this adds the different buttons to the panel


        filterButton = new JComboBox<>(new String[]{"ALL", "GUARD", "FORWARD", "CENTER"});
        sortButton = new JComboBox<>(new String[]{"NA","PPG","RPG","APG","MPG","EFFICIENCY"});

        filterButton.addActionListener(this);
        sortButton.addActionListener(this);

        topPanel.add(new JLabel("  Filter:"));
        topPanel.add(filterButton);

        topPanel.add(new JLabel("  Sort:"));
        topPanel.add(sortButton);

        frame.add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"NAME","POSITION","PPG","RPG","APG","MPG","EFFICIENCY"},0
        );

        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        //this sets the table rows and adds it to the frame

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //this sets the frame to be visible when we run the program
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == addButton){
            add();
        }
        if(e.getSource() == deleteButton){
            delete();
        }
        if(e.getSource() == editButton){
            edit();
        }
        if(e.getSource() == saveButton){
            save();
        }
        if(e.getSource() == loadButton){
            load();
        }
        if(e.getSource() == statsButton){
            stats();
        }

        if(e.getSource() == sortButton || e.getSource() == filterButton){
            refreshTable();
        }
    }// this code is responsible for making sure that when a specific button is pressed that the correct action is taken

    public void add(){//this function is run when the user presses the add button
        try {
            String name = JOptionPane.showInputDialog(frame,"enter players name: ");
            if(name == null){
                return;
            }
            String posName = JOptionPane.showInputDialog(frame, "Position (G/F/C):");
            if (posName == null) return;

            position pos;

            if (posName.equalsIgnoreCase("G")) {
                pos = position.GUARD;
            } else if (posName.equalsIgnoreCase("F")) {
                pos = position.FORWARD;
            } else if (posName.equalsIgnoreCase("C")) {
                pos = position.CENTER;
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid position. Use G, F, or C.");
                return;
            }//this is done to get the correct enum based on the position the user wrote

            double ppg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter PPG (points per game): "));
            double rpg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter RPG (rebounds per game): "));
            double apg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter APG (assists per game): "));
            double mpg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter MPG (minutes per game): "));

            player p = null;

            if(pos == position.GUARD){
                double tpg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter TPG (turnovers per game): "));
                p = new Guard(name, ppg, rpg, apg, mpg, pos, tpg);
            } else if (pos == position.FORWARD) {
                double fgpg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter FGPG " +
                        "(Field goals per game): "));
                double ftpg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter FTPG " +
                        "(Free throws per game): "));
                p = new Forward(name, ppg, rpg, apg, mpg, pos, fgpg, ftpg);
            } else if (pos == position.CENTER) {
                double bpg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter BPG " +
                        "(blocks per game): "));
                double fpg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter FPG " +
                        "(fouls per game): "));
                p = new Center(name, ppg, rpg, apg, mpg, pos, bpg, fpg);

            }//this is done to create the correct object for the correct class based on the info

            if(p == null){
                return;
            }

            if(!players.add(p)){
                JOptionPane.showMessageDialog(frame, "Player already exists!");
                return;
            }

            refreshTable();

        }catch (Exception e){
            JOptionPane.showMessageDialog(frame, "Invalid input.");
        }
    }
    public void delete(){//this is run for the delete button
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Select a player to delete.");
            return;
        }
        String name = (String) tableModel.getValueAt(row, 0);
        player toRemove = null;
        for (player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                toRemove = p;
                break;
            }
        }
        if(toRemove != null){
            players.remove(toRemove);
        }
        refreshTable();

    }
    public void edit() {//this function is responsible for the editing of data
        // and is a mix of the logic of the add(the asking the user for data)
        // and the delete button (the for loop and the temp object)

        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(frame, "Select a player to edit.");
            return;
        }

        String selectedName = (String) tableModel.getValueAt(row, 0);

        player old = null;
        for (player p : players) {
            if (p.getName().equalsIgnoreCase(selectedName)) {
                old = p;
                break;
            }
        }
        if (old == null) return;

        try {
            String name = JOptionPane.showInputDialog(frame,
                    "enter players name: ", old.getName());
            if (name == null) return;

            String posStr = JOptionPane.showInputDialog(frame, "Position (G/F/C):");
            if (posStr == null) return;

            position pos;

            if (posStr.equalsIgnoreCase("G")) {
                pos = position.GUARD;
            } else if (posStr.equalsIgnoreCase("F")) {
                pos = position.FORWARD;
            } else if (posStr.equalsIgnoreCase("C")) {
                pos = position.CENTER;
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid position. Use G, F, or C.");
                return;
            }

            double ppg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter PPG (points per game): ",
                    String.valueOf(old.getPpg())));

            double rpg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter RPG (rebounds per game): ",
                    String.valueOf(old.getRpg())));

            double apg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter APG (assists per game): ",
                    String.valueOf(old.getApg())));

            double mpg = Double.parseDouble(JOptionPane.showInputDialog(frame, "enter MPG (minutes per game): ",
                    String.valueOf(old.getMpg())));

            player p = null;

            if (pos == position.GUARD) {
                String default_tpg = "0";
                if (old instanceof Guard) {
                    default_tpg = String.valueOf(((Guard) old).getTpg());
                }

                double tpg = Double.parseDouble(
                        JOptionPane.showInputDialog(frame,
                                "enter TPG (turnovers per game): ",
                                default_tpg)
                );
                p = new Guard(name, ppg, rpg, apg, mpg, pos, tpg);

            } else if (pos == position.FORWARD) {
                String default_FGPG = "0";
                if (old instanceof Forward) {
                    default_FGPG = String.valueOf(((Forward) old).getFGpg());
                }

                double fgpg = Double.parseDouble(
                        JOptionPane.showInputDialog(frame,
                                "enter FGPG (Field goals per game): ",
                                default_FGPG)
                );

                String default_FTPG = "0";
                if (old instanceof Forward) {
                    default_FTPG = String.valueOf(((Forward) old).getFTpg());
                }

                double ftpg = Double.parseDouble(
                        JOptionPane.showInputDialog(frame,
                                "enter FTPG (Free throws per game): ",
                                default_FTPG)
                );
                //here the default value represents the old value (and if the positions changes the number 0)

                p = new Forward(name, ppg, rpg, apg, mpg, pos, fgpg, ftpg);

            } else if (pos == position.CENTER) {
                String default_Bpg = "0";

                if (old instanceof Center) {
                    default_Bpg = String.valueOf(((Center) old).getBpg());
                }

                double bpg = Double.parseDouble(
                        JOptionPane.showInputDialog(frame,
                                "enter BPG (blocks per game): ",
                                default_Bpg)
                );
                String default_Fpg = "0";

                if(old instanceof Center){
                    default_Fpg = String.valueOf(((Center) old).getFpg());

                }

                double fpg = Double.parseDouble(
                        JOptionPane.showInputDialog(frame,
                                "enter FPG (fouls per game): ",
                                default_Fpg)
                );

                p = new Center(name, ppg, rpg, apg, mpg, pos, bpg, fpg);
            }

            if (p == null) return;

            players.remove(old);

            if (!players.add(p)) {
                JOptionPane.showMessageDialog(frame, "Player already exists!");
                players.add(old);
                return;
            }

            refreshTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input.");
        }
    }
    public void save(){//this saves the data to a file
        JFileChooser chooser = new JFileChooser();
        if(chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
            File f = chooser.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                for (player p : players) {
                    pw.println(p.export());
                }
                JOptionPane.showMessageDialog(frame, "Saved!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving file.");
            }
        }
    }
    public void load(){//this function load an external file to the table
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try (Scanner sc = new Scanner(f)) {
                players.clear();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (!line.isBlank()) {
                        players.add(player.import_roster(line));
                    }
                }
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error loading file.");
            }
        }
    }
    public void stats() {//this is the first functional programming part
        // and is meant to show the stats (both average and max) form the table

        if (players.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No players available.");
            return;
        }

        int count = players.size();

        double avgPPG = players.stream()
                .mapToDouble(player::getPpg)
                .average()
                .orElse(0);

        double avgRPG = players.stream()
                .mapToDouble(player::getRpg)
                .average()
                .orElse(0);

        double avgAPG = players.stream()
                .mapToDouble(player::getApg)
                .average()
                .orElse(0);

        double avgEfficiency = players.stream()
                .mapToDouble(player::efficiency)
                .average()
                .orElse(0);

        player topScorer = players.stream()
                .max(Comparator.comparingDouble(player::getPpg))
                .orElse(null);

        player topRebounder = players.stream()
                .max(Comparator.comparingDouble(player::getRpg))
                .orElse(null);

        player topPlaymaker = players.stream()
                .max(Comparator.comparingDouble(player::getApg))
                .orElse(null);

        player bestOverall = players.stream()
                .max(Comparator.comparingDouble(player::efficiency))
                .orElse(null);

        //this sets up the JFrame window that pops up when you press the stats button with the info we have calculated
        JFrame statsFrame = new JFrame("Stats Summary (for all players)");
        statsFrame.setSize(500, 300);
        statsFrame.setLocationRelativeTo(frame);
        statsFrame.setLayout(new GridLayout(0, 1));
        statsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);


        statsFrame.add(new JLabel("Total players: " + count));
        statsFrame.add(new JLabel(String.format("Average PPG: %.2f", avgPPG)));
        statsFrame.add(new JLabel(String.format("Average RPG: %.2f", avgRPG)));
        statsFrame.add(new JLabel(String.format("Average APG: %.2f", avgAPG)));
        statsFrame.add(new JLabel(String.format("Average Efficiency: %.2f", avgEfficiency)));

        statsFrame.add(new JLabel(" "));
        statsFrame.add(new JLabel("Top Scorer: " + topScorer.getName()));
        statsFrame.add(new JLabel("Top Rebounder: " + topRebounder.getName()));
        statsFrame.add(new JLabel("Top Playmaker: " + topPlaymaker.getName()));
        statsFrame.add(new JLabel("Best Overall: " + bestOverall.getName()));

        statsFrame.setVisible(true);
    }

    public void refreshTable() {//this function is responsible with refreshing the table for changes
        // and for the filtering and sorting (using functional programming
        tableModel.setRowCount(0);

        String filter = (String) filterButton.getSelectedItem();
        String sort = (String) sortButton.getSelectedItem();

        java.util.List<String> cols = new ArrayList<>(
                Arrays.asList("NAME","POSITION","PPG","RPG","APG","MPG","EFFICIENCY")
        );

        if (filter.equals("GUARD")) {
            cols.add("TPG");
            cols.add("AST/T ratio");
        } else if (filter.equals("FORWARD")) {
            cols.add("FGPG");
            cols.add("FTPG");
            cols.add("TS%");
        } else if (filter.equals("CENTER")) {
            cols.add("BPG");
            cols.add("FPG");
            cols.add("Block efficiency");
        }

        tableModel.setColumnIdentifiers(cols.toArray(new String[0]));

        Stream<player> stream = players.stream().filter(p -> {
            if (filter.equals("ALL")) return true;
            if (filter.equals("GUARD")) return p.getPosition() == position.GUARD;
            if (filter.equals("FORWARD")) return p.getPosition() == position.FORWARD;
            if (filter.equals("CENTER")) return p.getPosition() == position.CENTER;
            return true;
        });

        Comparator<player> cmp = (a, b) -> a.getName().compareToIgnoreCase(b.getName());

        if (sort.equals("POSITION")) cmp = Comparator.comparing(p -> p.getPosition().name());
        else if (sort.equals("PPG")) cmp = Comparator.comparingDouble(player::getPpg).reversed();
        else if (sort.equals("RPG")) cmp = Comparator.comparingDouble(player::getRpg).reversed();
        else if (sort.equals("APG")) cmp = Comparator.comparingDouble(player::getApg).reversed();
        else if (sort.equals("MPG")) cmp = Comparator.comparingDouble(player::getMpg).reversed();
        else if (sort.equals("EFFICIENCY")) cmp = Comparator.comparingDouble(player::efficiency).reversed();

        List<player> list = stream.sorted(cmp).toList();

        for (player p : list) {
            ArrayList<Object> row = new ArrayList<>();
            row.add(p.getName());
            row.add(p.getPosition());
            row.add(p.getPpg());
            row.add(p.getRpg());
            row.add(p.getApg());
            row.add(p.getMpg());
            row.add(String.format("%.2f", p.efficiency()));

            if (filter.equals("GUARD")) {
                Guard c = (Guard) p;
                row.add(c.getTpg());
                row.add(String.format("%.2f", c.positionStat()));
            } else if (filter.equals("FORWARD")) {
                Forward c = (Forward) p;
                row.add(c.getFGpg());
                row.add(c.getFTpg());
                row.add(String.format("%.2f", c.positionStat()));
            } else if (filter.equals("CENTER")) {
                Center c = (Center) p;
                row.add(c.getBpg());
                row.add(c.getFpg());
                row.add(String.format("%.2f", c.positionStat()));
            }

            tableModel.addRow(row.toArray()); //this adds the new rows to the table
        }
    }
}
