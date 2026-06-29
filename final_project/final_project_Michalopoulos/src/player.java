import java.util.*;
public abstract class player {
    private String name;
    private double ppg;
    private double rpg;
    private double apg;
    private double mpg;
    private position Position;

    public player(String name, double ppg, double rpg, double apg, double mpg, position Position){
        //this is the constructor for the player class
        this.name = name;
        this.ppg = ppg;
        this.rpg = rpg;
        this.apg = apg;
        this.mpg = mpg;
        this.Position = Position;
    }

    //for line 21 to 71 are the setters and getters of this class
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getPpg() {
        return ppg;
    }

    public void setPpg(double ppg) {
        this.ppg = ppg;
    }

    public double getRpg() {
        return rpg;
    }

    public void setRpg(double rpg) {
        this.rpg = rpg;
    }

    public double getApg() {
        return apg;
    }

    public void setApg(double apg) {
        this.apg = apg;
    }

    public double getMpg() {
        return mpg;
    }

    public void setMpg(double mpg) {
        this.mpg = mpg;
    }

    public position getPosition() {
        return Position;
    }

    public void setPosition(position Position) {
        this.Position = Position;
    }

    public double efficiency(){
        return (((ppg)+(rpg)+(apg))/mpg)*36;
    }

    public abstract double positionStat(); //this is a function meant to be used by the child class
    public abstract String extraToTable(); //this is a function meant to be used by the child class
    public abstract void loadExtraFromFile(String extra); //this is a function meant to be used by the child class

    public String export(){
        return getName() + ";" + getPpg() + ";" +
                getRpg() + ";" + getApg() + ";" +
                getMpg() + ";" + getPosition().name() +
                ";" + extraToTable();
    } //this function is used to export the data to a file in your computer

    public static player import_roster(String line){
        String[] parts = line.split(";");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid CSV line: " + line);
        }
        String name = parts[0];
        double ppg = Double.parseDouble(parts[1]);
        double rpg = Double.parseDouble(parts[2]);
        double apg = Double.parseDouble(parts[3]);
        double mpg = Double.parseDouble(parts[4]);
        position pos = position.valueOf(parts[5]);
        String extra = parts[6];

        player p;

        if(pos == position.GUARD){
            p = new Guard(name, ppg, rpg, apg, mpg, pos, 0);
        } else if (pos == position.CENTER) {
            p = new Center(name, ppg, rpg, apg, mpg, pos, 0, 0);
        } else if (pos == position.FORWARD) {
            p = new Forward(name, ppg, rpg, apg, mpg, pos, 0, 0);
        }else{
            throw new IllegalArgumentException("Unknow Position :" + pos);
        }

        p.loadExtraFromFile(extra);
        return p;
    }// this function is responsible for loading a file into the table

    //the functions (equals, HashCode and toString)
    // are important to set a player so that it can be used as an object in a set
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof player)) return false;
        player player = (player) o;
        return name.equalsIgnoreCase(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return (getName() + "," +getPosition());
    }
}
