import java.util.ArrayList;

public class Production {
    private String consistent;
    private ArrayList<String> antecedents;
    private boolean flag1 = false;
    private boolean flag2 = false;

    Production(String consistent, ArrayList<String> antecedents){
        this.consistent = consistent;
        this.antecedents = antecedents;
        flag1 = false;
        flag2 = false;
    }

    public String printAntecendents(){
        String text = "";
        for(int i = 0; i < antecedents.size() - 1; i++){
            text += antecedents.get(i) + ",";
        }
        text += antecedents.get(antecedents.size() - 1);
        return text;
    }

    public String getConsistent(){
        return consistent;
    }

    public ArrayList<String> getAntecedents(){
        return antecedents;
    }

    public void setFlag1(boolean flag1){
        this.flag1 = flag1;
    }

    public boolean getFlag1(){
        return flag1;
    }

    public void setFlag2(boolean flag2){
        this.flag2 = flag2;
    }

    public boolean getFlag2(){
        return flag2;
    }
}
