import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static String filename = "";
    private static ArrayList<Production> productions = new ArrayList<Production>();
    private static List<String> facts;
    private static List<String> newFacts = new ArrayList<String>();
    private static Stack goals = new Stack();
    private static String goal = "";
    private static String production;
    private static String consistent;
    private static ArrayList<String> antecedents;
    private static ArrayList<String> GDB = new ArrayList<String>();
    private static int counter = 0;
    private static Scanner scanner = new Scanner(System.in);
    static int depthCounter = 0;
    private static boolean canBeApplied = false;

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
            case 11:
                filename = "test11.txt";
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
                    goal = (String.valueOf(line.charAt(0)));
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

            backwardChaining(goal);

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
            /*for (int i = 0; i < newFacts.size() - 2; i++) {
                System.out.print(newFacts.get(i) + ", ");
            }
            //if (newFacts.size()> 1) {
                System.out.print(newFacts.get(newFacts.size() - 2) + ".");
                //System.out.print(newFacts.get(newFacts.size() - 1) + "."); PASKUTINIO NEPRINTINAME//?????
            //}*/
            for (int i = 0; i < newFacts.size(); i++) {
                System.out.print(newFacts.get(i) + ", ");
            }
        }else{
            System.out.println("Nėra naujų faktų");
        }
    }

    public static void printDepth(){
        for(int i = 0; i < depthCounter; i++){
            System.out.print("-");
        }
    }

    public static void printInfo(String text){
        System.out.print(++counter + ") ");
        printDepth();
        System.out.print(text);
    }

    private static boolean isProd = false;

    public static boolean backwardChaining(String goal){
        goals.push(goal); // 1
        if (facts.contains(goal)){ // 2
            printInfo("Tikslas " + goal + ". Faktas (duotas), nes faktai ");  printFacts(); System.out.println(". Grįžtame, sėkmė. "); //??? faktai???
            goals.pop();
            depthCounter--;
            return true;
        }else if (newFacts.contains(goal)){ // 3
            printInfo("Tikslas " + goal + ". Faktas (buvo gautas). Faktai "); printFacts(); System.out.print(" ir "); printNewFacts();
            System.out.println();
            goals.pop();
            depthCounter--;
            return true;
        }else if (Collections.frequency(goals, goal) > 1){  // 4
            printInfo("Tikslas " + goal + ". Ciklas. Grįžtame, FAIL. \n");
            goals.pop();
            depthCounter--;
            return false;
        }

        // truksta 5,6 zingsnius
        else {
            for (int i = 0; i < productions.size(); i++) {  // 7
                // GAL REIKIA PADUOT TIK TAS TAISYKLES< KURIU FLAG==FALSE???
                canBeApplied = true;
                productions.get(i).setFlag1(true);  // 8
                if (productions.get(i).getConsistent().equals(goal)) {  // 9
                    isProd = true;
                    printInfo("Tikslas " + goal + ". Randame R" + (i + 1) + ":" + productions.get(i).printAntecendents() + "->" + goal + ". \n");
                    for (int j = 0; j < productions.get(i).getAntecedents().size(); j++) {  // 10
                        depthCounter++;
                        if (!backwardChaining(productions.get(i).getAntecedents().get(j))) {  // 11
                            canBeApplied = false;
                            productions.get(i).setFlag1(false);  // 12
                            // truksta 13, 14
                            //newFacts.remove(newFacts.size() - 1);
                            break; // 15
                        }
                    }
                    if (canBeApplied){
                        // 16
                        newFacts.add(goal); // 17   ????????????????????????????????????????????????????????????????????
                        printInfo("Tikslas " + goal + ". Faktas (dabar gautas). Faktai "); printFacts(); System.out.print(" ir "); printNewFacts();
                        System.out.println();
                        goals.pop(); // 18
                        // truksta 19
                        depthCounter--;
                        return true;
                    }

                }
            }
            if (isProd){
                printInfo("Tikslas " + goal + ". Nėra taisyklių jo išvedimui. Grįžtame, FAIL. \n");
                isProd = false;
            }else {
                printInfo("Tikslas " + goal + ". Nėra daugiau taisyklių jo išvedimui. Grįžtame, FAIL. \n");
            }
            goals.pop(); // 20
            depthCounter--;
        }
        return false;
    }
}