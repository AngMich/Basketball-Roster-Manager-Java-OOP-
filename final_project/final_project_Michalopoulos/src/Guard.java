public class Guard extends player{
    private double tpg;

    public Guard(String name, double ppg, double rpg, double apg, double mpg, position Position, double tpg) {
        super(name,ppg, rpg, apg, mpg, Position);
        this.tpg = tpg;
    }// this is the constructor for the child class Guard

    // the lines 10 - 18 are the setters and getters
    public double getTpg() {

        return tpg;
    }

    public void setTpg(double tpg){

        this.tpg = tpg;
    }


    @Override
    public double positionStat() {
        if(tpg == 0){
            return getApg();
        }
        else{
            return getApg()/tpg;
        }
    }// this function calculates the position specific stat

    @Override
    public String extraToTable() {
        return String.valueOf(tpg);
    }// this function adds the extra info to the table

    @Override
    public void loadExtraFromFile(String extra) {
        this.tpg = Double.parseDouble(extra);
    } //this function adds the capability to load extra info from a file
}
