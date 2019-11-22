package Statistics;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StatisticSaver {
    
    private final static Map<Integer, Integer> CPU_SUCCESSFUL_SHOTS = new HashMap<>();
    private final static Map<Integer, Integer> CPU_MISSED_SHOTS = new HashMap<>();

    private final static Map<Integer, Integer> HUMAN_SUCCESSFUL_SHOTS = new HashMap<>();
    private final static Map<Integer, Integer> HUMAN_MISSED_SHOTS = new HashMap<>();
    
    private static Integer roundCounter = 0;
    private static Integer playerWonCounter = 0;
    private static Integer playerLoseCounter = 0;

    static {
        populateMap(CPU_SUCCESSFUL_SHOTS);
        populateMap(CPU_MISSED_SHOTS);

        populateMap(HUMAN_SUCCESSFUL_SHOTS);
        populateMap(HUMAN_MISSED_SHOTS);

    }

    private File saveFile;

    public StatisticSaver() {
        saveFile = new File("stats.txt");
        loadStats();
    }

    private static void populateMap(Map<Integer, Integer> mapToPopulate) {
        for (int i = 0; i < 100; i++) {
            mapToPopulate.put(i, 0);
        }
    }


    public void addSuccessfulShotsToHumanMap(Set<Integer> allCellsHit) {
        for (Integer i : allCellsHit) {
            Integer newValue = HUMAN_SUCCESSFUL_SHOTS.get(i) + 1;
            HUMAN_SUCCESSFUL_SHOTS.put(i, newValue);
        }

    }

    public  void addSuccessfulShotsToCpuMap(Set<Integer> allCellsHit) {
        for (Integer i : allCellsHit) {
            Integer newValue = CPU_SUCCESSFUL_SHOTS.get(i) + 1;
            CPU_SUCCESSFUL_SHOTS.put(i, newValue);
        }
        
    }
    
    public  void addMissedShotsToCpuMap(Set<Integer> allCellsMissed) {
        for (Integer i : allCellsMissed) {
            Integer newValue = CPU_MISSED_SHOTS.get(i) + 1;
            CPU_MISSED_SHOTS.put(i, newValue);
        }

    }

    public  void addMissedShotsToHumanMap(Set<Integer> allCellsMissed) {
        for (Integer i : allCellsMissed) {
            Integer newValue = CPU_MISSED_SHOTS.get(i) + 1;
            CPU_MISSED_SHOTS.put(i, newValue);
        }

    }

    public  void setRoundCounter(int roundCounter) {
        StatisticSaver.roundCounter = roundCounter;
    }

    public  int getRoundCounter() {
        return roundCounter;
    }

    public  int getPlayerWonCounter() {
        return playerWonCounter;
    }

    public  void setPlayerWonCounter(int playerWonCounter) {
        StatisticSaver.playerWonCounter = playerWonCounter;
    }

    public  int getPlayerLoseCounter() {
        return playerLoseCounter;
    }

    public  void setPlayerLoseCounter(int playerLoseCounter) {
        StatisticSaver.playerLoseCounter = playerLoseCounter;
    }
    
    public void saveStats(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile))){
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
    
    private void loadStats(){
        StringBuilder sb = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(saveFile))){
            
            while(br.ready()){
                sb.append(br.readLine() + "\n");
            }

            System.out.println(sb);

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
    
    private void stringToMap(String stringWithMap, Map<Integer, Integer> mapToPopulate){
        String removeCurlyBraces = stringWithMap.substring(1, stringWithMap.length() - 1);
        
        String[] keyValuePairs = removeCurlyBraces.split(",");
        
        for(String pair : keyValuePairs){
            String[] entry = pair.split("=");
            
            mapToPopulate.put(Integer.parseInt(entry[0].trim()), Integer.parseInt(entry[1].trim()));
        }
    }
    
    
}
