package pl.seb.czech.Statistics;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Stats {
    
    private final static Map<Integer, Integer> CPU_SUCCESSFUL_SHOTS = new HashMap<>();
    private final static Map<Integer, Integer> CPU_MISSED_SHOTS = new HashMap<>();

    private final static Map<Integer, Integer> HUMAN_SUCCESSFUL_SHOTS = new HashMap<>();
    private final static Map<Integer, Integer> HUMAN_MISSED_SHOTS = new HashMap<>();
    
    private static Integer roundCounter = 0;
    private static Integer playerWonCounter = 0;
    private static Integer playerLoseCounter = 0;
    
    private static int humanAllSuccessfulShots = 0;
    private static int humanAllMissedShots = 0;
    
    private static int cpuAllSuccessfulShots = 0;
    private static int cpuAllMissedShots = 0;
    
    private static File SAVE_FILE;
    

    static {
        boolean statsCreated = false;
        
        try{
            File jarFile = new File(Stats.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            SAVE_FILE = new File(jarFile.getParent() + File.separator + "stats.txt");
            statsCreated = SAVE_FILE.createNewFile();
        } catch (Exception e){
            System.out.println("ERROR WHILE CREATING FILE");
        }
        
        populateMap(CPU_SUCCESSFUL_SHOTS);
        populateMap(CPU_MISSED_SHOTS);

        populateMap(HUMAN_SUCCESSFUL_SHOTS);
        populateMap(HUMAN_MISSED_SHOTS);
        if(statsCreated) {
            saveStats();
        }
        loadStats();
    }
    

    private static void populateMap(Map<Integer, Integer> mapToPopulate) {
        for (int i = 0; i < 100; i++) {
            mapToPopulate.put(i, 0);
        }
    }


    public static void addSuccessfulShotsToHumanMap(Set<Integer> allCellsHit) {
        for (Integer i : allCellsHit) {
            Integer newValue = HUMAN_SUCCESSFUL_SHOTS.get(i) + 1;
            HUMAN_SUCCESSFUL_SHOTS.put(i, newValue);
        }

    }

    public static void addSuccessfulShotsToCpuMap(Set<Integer> allCellsHit) {
        for (Integer i : allCellsHit) {
            Integer newValue = CPU_SUCCESSFUL_SHOTS.get(i) + 1;
            CPU_SUCCESSFUL_SHOTS.put(i, newValue);
        }
        
    }

    public static void addMissedShotsToHumanMap(Set<Integer> allCellsMissed) {
        for (Integer i : allCellsMissed) {
            Integer newValue = HUMAN_MISSED_SHOTS.get(i) + 1;
            HUMAN_MISSED_SHOTS.put(i, newValue);
        }
    }

    public static void addMissedShotsToCpuMap(Set<Integer> allCellsMissed) {
        for (Integer i : allCellsMissed) {
            Integer newValue = CPU_MISSED_SHOTS.get(i) + 1;
            CPU_MISSED_SHOTS.put(i, newValue);
        }

    }

    public static void setRoundCounter(int roundCounter) {
        Stats.roundCounter = roundCounter;
    }

    public static int getRoundCounter() {
        return roundCounter;
    }

    public static int getPlayerWonCounter() {
        return playerWonCounter;
    }

    public static void setPlayerWonCounter(int playerWonCounter) {
        Stats.playerWonCounter = playerWonCounter;
    }

    public static int getPlayerLoseCounter() {
        return playerLoseCounter;
    }

    public static void setPlayerLoseCounter(int playerLoseCounter) {
        Stats.playerLoseCounter = playerLoseCounter;
    }
    
    public static void saveStats(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(SAVE_FILE))){
            bw.write(CPU_SUCCESSFUL_SHOTS.toString() + "\n");
            bw.write(CPU_MISSED_SHOTS.toString() + "\n");
            bw.write(HUMAN_SUCCESSFUL_SHOTS.toString() + "\n");
            bw.write(HUMAN_MISSED_SHOTS.toString() + "\n");
            bw.write(roundCounter.toString() + "\n");
            bw.write(playerWonCounter.toString() + "\n");
            bw.write(playerLoseCounter.toString() + "\n");
        } catch (IOException e){
            System.out.println("ERROR IN SAVING");
            e.printStackTrace();
        }
    }
    
    public static void loadStats(){
        StringBuilder sb = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE))){
            while(br.ready()){
                sb.append(br.readLine()).append("\n");
            }
        } catch (Exception e){
            System.out.println("EXCEPTION IN LOADING");
            e.printStackTrace();
        }
        
        if(!sb.toString().isEmpty()){
            String[] parts = sb.toString().split("\n");
            
            stringToMap(parts[0], CPU_SUCCESSFUL_SHOTS);
            stringToMap(parts[1], CPU_MISSED_SHOTS);
            stringToMap(parts[2], HUMAN_SUCCESSFUL_SHOTS);
            stringToMap(parts[3], HUMAN_MISSED_SHOTS);
            
            roundCounter = Integer.parseInt(parts[4]);
            playerWonCounter = Integer.parseInt(parts[5]);
            playerLoseCounter = Integer.parseInt(parts[6]);
            
        }
    }
    
    private static void stringToMap(String stringWithMap, Map<Integer, Integer> mapToPopulate){
        String removeCurlyBraces = stringWithMap.substring(1, stringWithMap.length() - 1);
        
        String[] keyValuePairs = removeCurlyBraces.split(",");
        
        for(String pair : keyValuePairs){
            String[] entry = pair.split("=");
            
            mapToPopulate.put(Integer.parseInt(entry[0].trim()), Integer.parseInt(entry[1].trim()));
        }
    }
    
    public static int getHumanAllShotsNum(){
        humanAllSuccessfulShots = HUMAN_SUCCESSFUL_SHOTS.values().stream()
                .mapToInt(value -> value)
                .sum();
        
        humanAllMissedShots = HUMAN_MISSED_SHOTS.values().stream()
                .mapToInt(value -> value)
                .sum();
        
        return humanAllMissedShots + humanAllSuccessfulShots;
    }
    
    public static int getCpuAllShotsNum(){
        cpuAllSuccessfulShots = CPU_SUCCESSFUL_SHOTS.values().stream()
                .mapToInt(value -> value)
                .sum();

        cpuAllMissedShots = CPU_MISSED_SHOTS.values().stream()
                .mapToInt(value -> value)
                .sum();

        return cpuAllSuccessfulShots + cpuAllMissedShots;
        
    }

    public static int getHumanAllSuccessfulShots() {
        return humanAllSuccessfulShots;
    }

    public static int getHumanAllMissedShots() {
        return humanAllMissedShots;
    }

    public static int getCpuAllSuccessfulShots() {
        return cpuAllSuccessfulShots;
    }

    public static int getCpuAllMissedShots() {
        return cpuAllMissedShots;
    }

    public static Map<Integer, Integer> getCpuSuccessfulShots() {
        return CPU_SUCCESSFUL_SHOTS;
    }

    public static Map<Integer, Integer> getCpuMissedShots() {
        return CPU_MISSED_SHOTS;
    }

    public static Map<Integer, Integer> getHumanSuccessfulShots() {
        return HUMAN_SUCCESSFUL_SHOTS;
    }

    public static Map<Integer, Integer> getHumanMissedShots() {
        return HUMAN_MISSED_SHOTS;
    }
}
