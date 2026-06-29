public class Center extends player{
    private double bpg;
    private double fpg;
    public Center(String name, double ppg, double rpg, double apg, double mpg, position Position, double bpg, double fpg) {
        super(name, ppg, rpg, apg, mpg, Position);
        this.bpg = bpg;
        this.fpg = fpg;
    }// this is the constructor for the child class Center

    // the lines 11 - 25 are the setters and getters
    public double getBpg() {
        return bpg;
    }

    public void setBpg(double bpg) {
        this.bpg = bpg;
    }

    public double getFpg() {
        return fpg;
    }

    public void setFpg(double fpg) {
        this.fpg = fpg;
    }


    @Override
    public double positionStat() {
        if(bpg == 0 || fpg == 0){
            return 0.0;
        }
        else{
            return (bpg/fpg);
        }
    }// this function calculates the position specific stat

    @Override
    public String extraToTable() {
        return bpg + ", " + fpg;
    }// this function adds the extra info to the table

    @Override
    public void loadExtraFromFile(String extra) {
        String[] p = extra.split(",");

        this.bpg = Double.parseDouble(p[0]);
        this.fpg = Double.parseDouble(p[1]);

    }//this function adds the capability to load extra info from a file
}
