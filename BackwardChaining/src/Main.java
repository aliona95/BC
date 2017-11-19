import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static String filename = "";
    private static ArrayList<Production> productions = new ArrayList<Production>();
    private static List<String> facts;
    private static List<String> newFacts = new ArrayList<String>();
    private static List<String> savedFacts = new ArrayList<String>();
    private static Stack goals = new Stack();
    private static String goal = "";
    private static String finalGoal = "";
    private static String production;
    private static String consistent;
    private static ArrayList<String> antecedents;
    private static ArrayList<String> GDB = new ArrayList<String>();
    private static int counter = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static int depthCounter = 0;
    private static boolean canBeApplied = false;
    private static List<Integer> path = new ArrayList<>();
    private static List<Integer> savedPath = new ArrayList<>();

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
                    finalGoal = (String.valueOf(line.charAt(0)));
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
            System.out.println("2 DALIS. Vykdymas\n");
            boolean find = false;
            find = backwardChaining(goal);
            if (find){
                System.out.println("\n3 DALIS. Rezultatai\n");
                System.out.println("  1) Tikslas " + goal + " pasiektas.");
                System.out.print("  2) Kelias: ");
                printPath();;
                System.out.println();
            }else{
                System.out.println("\n3 DALIS. Rezultatai\n");
                System.out.println("  1) Tikslas " + goal + " nepasiektas.");
                System.out.println("  2) Kelias neegzistuoja.");
            }
        }
    }

    public static void printFacts(){
        for(int i = 0; i < facts.size() - 1; i++){
            System.out.print(facts.get(i) + ", ");
        }
        System.out.print(facts.get(facts.size() - 1));
    }
    public static void printNewFacts(){
        for(int z = 0; z < newFacts.size(); z++){
            if (newFacts.size() == 1 || z == newFacts.size() - 1){
                System.out.print(newFacts.get(z) + ". ");
            }else{
                System.out.print(newFacts.get(z) + ", ");
            }
        }
    }

    public static void printDepth(){
        for(int i = 0; i < depthCounter; i++){
            System.out.print("-");
        }
    }

    public static void printInfo(String text){
        System.out.printf(String.format("%3s", ++counter) + ") ");
        printDepth();
        System.out.print(text);
    }

    public static void printPath(){
        for(int i = 0; i < path.size(); i++){
            if (i != path.size() - 1){
                System.out.print("R" + path.get(i) + ", ");
            }else{
                System.out.print("R" + path.get(i) + ". ");
            }
        }
    }

    private static boolean isProd = false;

    public static boolean backwardChaining(String goal){
        goals.push(goal); 
        if (facts.contains(goal)){
            printInfo("Tikslas " + goal + ". Faktas (duotas), nes faktai ");  printFacts(); System.out.println(". Grįžtame, sėkmė. ");
            goals.pop();
            depthCounter--;
            return true;
        }else if (newFacts.contains(goal)){
            printInfo("Tikslas " + goal + ". Faktas (buvo gautas). Faktai "); printFacts(); System.out.print(" ir "); printNewFacts();
            System.out.println("Grįžtame, sėkmė.");
            goals.pop();
            depthCounter--;
            return true;
        }else if (Collections.frequency(goals, goal) > 1){
            printInfo("Tikslas " + goal + ". Ciklas. Grįžtame, FAIL. \n");
            goals.pop();
            depthCounter--;
            return false;
        }

        else {
            savedFacts.clear();
            for(int i = 0; i < newFacts.size(); i++){
                savedFacts.add(newFacts.get(i));
            }
            savedPath.clear();
            for(int i = 0; i < path.size(); i++){
                savedPath.add(path.get(i));
            }
            for (int i = 0; i < productions.size(); i++) {
                canBeApplied = true;
                productions.get(i).setFlag1(true);
                if (productions.get(i).getConsistent().equals(goal)) {
                    isProd = true;
                    printInfo("Tikslas " + goal + ". Randame R" + (i + 1) + ":" + productions.get(i).printAntecendents() + "->" + goal + ". ");
                    System.out.print("Nauji tikslai ");
                    for(int z = 0; z < productions.get(i).getAntecedents().size(); z++){
                        if (productions.get(i).getAntecedents().size() == 1 || z == productions.get(i).getAntecedents().size() - 1){
                            System.out.print(productions.get(i).getAntecedents().get(z) + ".");
                        }else{
                            System.out.print(productions.get(i).getAntecedents().get(z) + ",");
                        }
                    }
                    System.out.println();
                    for (int j = 0; j < productions.get(i).getAntecedents().size(); j++) {
                        depthCounter++;
                        if (!backwardChaining(productions.get(i).getAntecedents().get(j))) {
                            canBeApplied = false;
                            path.clear();
                            for(int z = 0; z < savedPath.size(); z++){
                                path.add(savedPath.get(z));
                            }
                            newFacts.clear();
                            for(int z = 0; z < savedFacts.size(); z++){
                                newFacts.add(savedFacts.get(z));
                            }
                            productions.get(i).setFlag1(false);
                            break;
                        }
                    }
                    if (canBeApplied){
                        newFacts.add(goal);
                        printInfo("Tikslas " + goal + ". Faktas (dabar gautas). Faktai "); printFacts(); System.out.print(" ir "); printNewFacts();
                        if (goals.size() == 1 && goals.lastElement().equals(finalGoal)){
                            System.out.println("Sėkmė.");
                        }else {
                            System.out.println("Grįžtame, sėkmė.");
                        }
                        goals.pop();
                        path.add(i + 1);
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
            goals.pop();
            depthCounter--;
        }
        return false;
    }
}