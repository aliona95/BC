import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static String filename = "";
    private static ArrayList<Production> productions = new ArrayList<Production>();
    private static List<String> facts;
    private static List<String> newFacts = new ArrayList<String>();
    private static Stack goal = new Stack();
    private static Stack facts1 = new Stack();
    private static String production;
    private static String consistent;
    private static ArrayList<String> antecedents;
    private static ArrayList<String> GDB = new ArrayList<String>();
    private static boolean isFlag1 = false;
    private static int counter = 0;
    private static String missingAntecedent = "";
    private static Boolean consistentInFacts = false;
    private static Boolean isFlag2 = false;
    private static ArrayList<Integer> path = new ArrayList<Integer>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("Pasirinkite failą: 1 -> test1.txt, 2 -> test2.txt, 3 -> test3.txt, ...");
        switch (scanner.nextInt()) {
            case 1:
                filename = "test1.txt";
                break;
            case 2:
                filename = "test2.txt";
                break;
            case 3:
                filename = "test3.txt";
                break;
            case 4:
                filename = "test4.txt";
                break;
            case 5:
                filename = "test5.txt";
                break;
            case 6:
                filename = "test6.txt";
                break;
            case 7:
                filename = "test7.txt";
                break;
            case 8:
                filename = "test8.txt";
                break;
            case 9:
                filename = "test9.txt";
                break;
            case 10:
                filename = "test10.txt";
                break;
            default:
                System.out.println("ERROR");
        }
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String line;
        br.readLine();  // Studente Aliona Lisovskaja, KM, 4, 1. Data
        br.readLine();  // 1 testas. Paskaitos pavyzdys
        br.readLine();  // 1) Taisykles
        try {
            while ((line = br.readLine()) != null) {
                if (line.equals("2) Faktai")){
                    line = br.readLine();
                    facts = new ArrayList<String>(Arrays.asList(line.split(" ")));
                    GDB = (ArrayList<String>) facts;
                }else if (line.equals("3) Tikslas")){
                    line = br.readLine();
                    goal.push(String.valueOf(line.charAt(0)));
                }else{
                    // getting all productions
                    production = line.substring(0, line.indexOf("/"));
                    consistent = String.valueOf(production.charAt(0));
                    antecedents = new ArrayList<String>(Arrays.asList((production.substring(2)).split(" ")));
                    productions.add(new Production(consistent, antecedents));
                }
            }
        } finally {
            br.close();
        }
        System.out.println("1 DALIS. Duomenys");
        System.out.println();
        System.out.println("  1) Taisyklės");
        for(int i = 0; i < productions.size(); i++){
            System.out.println("     R" + (i + 1) + ": " + productions.get(i).printAntecendents() + " -> " + productions.get(i).getConsistent());
        }
        System.out.println();
        System.out.println("  2) Faktai");
        System.out.print("     ");
        printFacts();
        System.out.println("\n");
        System.out.println("  3) Tikslas");
        System.out.print("     " + goal + "\n\n");
        if(facts.contains(goal)){
            System.out.println("\n3 DALIS. Rezultatai");
            System.out.println("  Tikslas " + goal + " tarp faktų. Kelias tuščias.");
        }else{
            System.out.println("2 DALIS. Vykdymas");

            backwardChaining();

        }
    }

    public static void printFacts(){
        for(int i = 0; i < facts.size() - 1; i++){
            System.out.print(facts.get(i) + ", ");
        }
        System.out.print(facts.get(facts.size() - 1));
    }
    public static void printNewFacts(){
        if (newFacts.size() > 0) {
            for (int i = 0; i < newFacts.size() - 2; i++) {
                System.out.print(newFacts.get(i) + ", ");
            }
            System.out.print(newFacts.get(newFacts.size() - 2) + ".");
            //System.out.print(newFacts.get(newFacts.size() - 1) + "."); PASKUTINIO NEPRINTINAME
        }else{
            System.out.println("Nėra naujų faktų");
        }
    }

    static int depthCounter = 0;
    private static Stack tempGoal = new Stack();
    private static Stack usedRules = new Stack();
    static boolean isCycle = false;
    static String nextGoal;

    public static void backwardChaining(){
        for(int i = 0; i < productions.size(); i++) {

            if (!goal.empty()) {
                //printDepth();
                //System.out.println(goal.lastElement() + " " + newFacts);
                if (noRules()) {
                    System.out.print(++counter + ") ");
                    printDepth();
                    System.out.println("Tikslas " + goal.lastElement() + ". Nėra taisyklių jo išvedimui. Grįžtame, FAIL. " + usedRules);
                    depthCounter--;
                    goal.pop();
                    //tempGoal.push(goal.lastElement());
                } else if (noMoreRules()) {
                    //System.out.println(goal.lastElement() + " " + usedRules + " " + noMoreRules() + " facts " + newFacts);
                    //System.out.println("Nera " + goal.lastElement());
                    System.out.print(++counter + ") ");
                    printDepth();
                    System.out.println("Tikslas " + goal.lastElement() + ". Nėra daugiau taisyklių jo išvedimui. Grįžtame, FAIL. " + usedRules);
                    goal.pop();
                    depthCounter--;
                    usedRules.pop();
                } else if (facts.contains(goal.lastElement())) {
                    System.out.print(++counter + ") ");
                    printDepth();
                    System.out.print("Tikslas " + goal.lastElement() + ". Faktas (duotas), nes faktai ");  printFacts(); System.out.println(". Grįžtame, sėkmė. " + usedRules);
                    goal.pop();
                    usedRules.pop();
                    //depthCounter--;
                    //tempGoal.push(goal.lastElement());
                } else if (newFacts.contains(goal.lastElement())) {
                    System.out.print(++counter + ") ");
                    printDepth();
                    System.out.print("Tikslas " + goal.lastElement() + ". Faktas (dabar gautas). Faktai "); printFacts(); System.out.print(" ir "); printNewFacts();
                    //tempGoal.push(goal.lastElement());
                    System.out.println(usedRules);
                    goal.pop();
                    //usedRules.pop();
                } else if (goal.lastElement().equals(productions.get(i).getConsistent()) && !productions.get(i).getFlag1()) {
                    System.out.print(++counter + ") ");
                    printDepth();
                    System.out.print("Tikslas " + goal.lastElement() + ". Randame R" + (i + 1) + ":" + productions.get(i).printAntecendents() + "->" + goal.lastElement() + ". ");
                    usedRules.push(i+1);
                    System.out.println("Nauji tikslai " + productions.get(i).printAntecendents() + ". " + usedRules);
                    //System.out.println(goal);
                    tempGoal.push(goal.lastElement());

                    productions.get(i).setFlag1(true);
                    for (int j = productions.get(i).getAntecedents().size()-1; j >= 0 ; j--) {
                        goal.push(productions.get(i).getAntecedents().get(j));
                    }
                    for (int j = 0; j < productions.get(i).getAntecedents().size(); j++) {
                        depthCounter++;
                        /*if (newFacts.contains(goal.lastElement())){
                            System.out.print(++counter +") ");
                            printDepth();
                            System.out.println("Tikslas " + goal.lastElement() + ". Buvo.");
                            depthCounter--;
                            goal.pop();
                        } else */if (tempGoal.contains(productions.get(i).getAntecedents().get(j))){
                            tempGoal.push(goal.lastElement());// nebuvo ife
                            System.out.print(++counter +") ");
                            printDepth();
                            System.out.println("Tikslas " + goal.lastElement() + ". Ciklas. Grįžtame, FAIL. " + usedRules);
                            depthCounter--;
                            goal.pop();
                            //usedRules.pop();
                        }else {// nebuvo else
                            backwardChaining();

                            depthCounter--;
                            newFacts.add(0, productions.get(i).getAntecedents().get(j));
                        }
                    }
                }else{
                    printDepth();
                    System.out.println("Tikslas " + goal.lastElement() + " prod " + productions.get(i).getAntecedents() + " -> " + productions.get(i).getConsistent() + ", goal " + goal + ", facts " + newFacts);
                }
            }
        }
        if (depthCounter == 1 && goal.size() == 1){
            System.out.print(++counter + ") ");
            System.out.print("Tikslas " + goal.lastElement() + ". Faktas (dabar gautas). Faktai "); printFacts(); System.out.print(" ir "); printNewFacts();
            System.out.println(" Sėkmė. " + usedRules);
        }/*else{
            System.out.print("Nesėkmė.");   // GALI BUTI KLAIDU
        }*/
    }

    public static boolean noRules(){
        if (facts.contains(goal.lastElement())){
            return false;
        }
        for(int i = 0; i < productions.size(); i++){
            if (goal.lastElement().equals(productions.get(i).getConsistent())){
                return false;
            }
        }
        return true;
    }

    public static boolean noMoreRules(){
        if (facts.contains(goal.lastElement())){
            return false;
        }
        for(int i = 0; i < productions.size(); i++){
            if (goal.lastElement().equals(productions.get(i).getConsistent()) /*&& !productions.get(i).getFlag1()*/ && !usedRules.contains(i+1)){
                return false;
            }
        }
        return true;
    }

    public static void printDepth(){
        for(int i = 0; i < depthCounter; i++){
            System.out.print("-");
        }
    }

    public static boolean isCycle(int begin, int end){
        if (productions.get(end).getAntecedents().size() == 1) {
            for (int i = begin; i < end; i++) {
                if (goal.lastElement().equals(productions.get(i).getConsistent())) {
                    System.out.println(goal.lastElement() + " vs " + productions.get(i).getConsistent());
                    return true;
                }
            }
        }
        return false;
    }
}
