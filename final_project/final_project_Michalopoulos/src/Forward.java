public class Forward extends player{
    private double FGpg;
    private double FTpg;
    public Forward(String name,  double ppg, double rpg, double apg, double mpg, position Position, double FGpg, double FTpg) {
        super(name, ppg, rpg, apg, mpg, Position);
        this.FGpg = FGpg;
        this.FTpg = FTpg;
    }// this is the constructor for the child class Forward

    // the lines 11 - 25 are the setters and getters
    public double getFGpg() {
        return FGpg;
    }

    public void setFGpg(double FGpg) {
        this.FGpg = FGpg;
    }

    public double getFTpg() {
        return FTpg;
    }

    public void setFTpg(double FTpg) {
        this.FTpg = FTpg;
    }

    @Override
    public double positionStat() {
        if(FTpg == 0 && FGpg == 0){
            return 0.0;
        }
        else{
            return (getPpg()/(2*(FGpg+0.44*FTpg)))*100;
        }
    }// this function calculates the position specific stat

    @Override
    public String extraToTable() {
        return FGpg + ", " + FTpg;
    }// this function adds the extra info to the table

    @Override
    public void loadExtraFromFile(String extra) {
        String[] p = extra.split(",");
        this.FGpg = Double.parseDouble(p[0]);
        this.FTpg = Double.parseDouble(p[1]);

    }//this function adds the capability to load extra info from a file
}
