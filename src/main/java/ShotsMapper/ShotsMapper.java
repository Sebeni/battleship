package ShotsMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShotsMapper {
    private final static Map<Integer, Integer> cpuAllShots = new HashMap<>();
    
    private final static Map<Integer, Integer> humanAllShots = new HashMap<>();
    
    static {
        if(cpuAllShots.isEmpty()){
            populateMap(cpuAllShots);
        }
        
        if(humanAllShots.isEmpty()){
            populateMap(humanAllShots);
        }
    }
    
    private static void populateMap(Map<Integer, Integer> mapToPopulate){
        for(int i = 0; i < 100; i++){
            mapToPopulate.put(i, 0);
        }
    }
    
    
    public static void addShotsToHumanMap(List<Integer> allCellsHit){
        for(Integer i : allCellsHit){
            Integer newValue = humanAllShots.get(i) + 1;
            humanAllShots.put(i, newValue);
        }
        System.out.println("HUMAN SHOTS:\n" + humanAllShots);
       
    }
    
    public static void addShotsToCpuMap(List<Integer> allCellsHit){
        for(Integer i : allCellsHit){
            Integer newValue = cpuAllShots.get(i) + 1;
            cpuAllShots.put(i, newValue);
        }
        System.out.println("CPU SHOTS:\n" + humanAllShots);
    }
    
}
