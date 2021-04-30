import helper.ItemSet;
import helper.OneSet;
import helper.ThreeSet;
import helper.TwoSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class APriori<T> {

    private final Integer support1 = 100;
    private final Integer support2 = 50;
    private final Integer support3 = 50;
    private final Double confidence = 0.4;
    private HashSet<ItemSet<T>> candidates = new HashSet<>();
    private HashSet<String> basket = new HashSet<>();
    Set<ItemSet<T>> added = new HashSet<>();
    private HashMap<ItemSet<T>, Integer> onecounters = new HashMap<>();
    private HashMap<ItemSet<T>, Integer> onecandidates = new HashMap<>();
    private HashMap<ItemSet<T>, Integer> twocounters = new HashMap<>();
    private HashMap<ItemSet<T>, Integer> twocandidates = new HashMap<>();
    private HashMap<ItemSet<T>, Integer> threecounters = new HashMap<>();
    private HashMap<ItemSet<T>, Integer> threecandidates = new HashMap<>();

    public static void main(String[] args){ new APriori<String>(); }

    public APriori(){
        ones();
        twos();
        threes();
        findRule();
    }

    public void ones(){
        parseFile(1);
        filter(onecounters, onecandidates, support1);
        System.out.println(onecandidates.size());
    }

    public void twos(){
        parseFile(2);
        filter(twocounters, twocandidates,support2);
        filterTwos();
        System.out.println(twocandidates.size());
    }

    public void threes(){
        parseFile(3);
        filter(threecounters, threecandidates, support3);
        System.out.println(threecandidates.size());
    }

    private void parseFile(Integer step){
        try {
            File myObj = new File("in");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.startsWith("-")) {
                    handleBasket(step);
                    continue;
                }
                basket.add(data);
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void handleBasket(Integer step){
        if(step == 1)
            handleOneSetBasket();
        else if(step == 2)
            handleTwoSetBasket();
        else if(step == 3)
            handleThreeSetBasket();
        basket.clear();
        added.clear();
    }

    private void handleOneSetBasket(){
        basket.forEach(item -> {
            ItemSet<T> itemSet = new OneSet(item);
            onecounters.put(itemSet, onecounters.getOrDefault(itemSet, 0) + 1);
        });
    }

    private void handleTwoSetBasket(){
        basket.forEach((item1) -> {
            if(onecandidates.containsKey(new OneSet(item1))) {
                basket.forEach(item2 -> {
                    if (!item1.equals(item2) && onecandidates.containsKey(new OneSet(item2))) {
                        ItemSet<T> key = new TwoSet(item1, item2);
                        if (!added.contains(key)) {
                            twocounters.put(key, twocounters.getOrDefault(key, 0) + 1);
                            added.add(key);
                        }
                    }
                });
            }
        });
    }

    private void handleThreeSetBasket(){
        threecounters.forEach((tmpset, freq) -> {
            ThreeSet<T> set = (ThreeSet<T>) tmpset;
            if(basket.contains(set.first) && basket.contains(set.second) && basket.contains(set.third)){
                threecounters.put(set, freq + 1);
            }
        });
    }

    private void filter(HashMap<ItemSet<T>, Integer> map, HashMap<ItemSet<T>, Integer> candidateMap, Integer support){
        map.forEach((k,v) -> {
            if(v > support)
                candidates.add(k);
        });
        candidates.forEach(i -> candidateMap.put(i, map.get(i)));
        map.clear();
        candidates.clear();
    }

    private void filterTwos(){
        List<TwoSet<T>> sets = new ArrayList<>(twocandidates.size());
        for (Map.Entry me : twocandidates.entrySet()) {
            sets.add((TwoSet<T>) me.getKey());
        }
        int i = 0;
        int j;
        boolean found;
        while(i < twocandidates.size()){
            found = false;
            j = 0;
            while(j < twocandidates.size()){
                if( j != i && sets.get(i).containsOneItem(sets.get(j))){
                    found = true;
                    threecounters.put(new ThreeSet(sets.get(i), sets.get(j)), 0);
                }
                ++j;
            }
            if(!found)
                twocandidates.remove(sets.get(i));
            ++i;
        }
        filterThrees();
    }


    private void filterThrees(){
        HashSet<ThreeSet<T>> toremove = new HashSet<>();
        for (Map.Entry me : threecounters.entrySet()) {
            ThreeSet<T> set = (ThreeSet<T>) me.getKey();
            if(!(
                    twocandidates.containsKey(new TwoSet<T>(set.first, set.second))
                    && twocandidates.containsKey(new TwoSet<T>(set.first, set.third))
                    && twocandidates.containsKey(new TwoSet<T>(set.third, set.second))
            )){
                toremove.add(set);
            }
        }
        toremove.forEach(set -> threecounters.remove(set));
    }

    private void findRule(){
        threecandidates.forEach((tmp,freq)->{
            ThreeSet<T> set = (ThreeSet<T>) tmp;
            ThreeSet<T> candidate;
            if(twocandidates.containsKey(new TwoSet(set.first, set.second)) &&
                    new Float(freq)/twocandidates.get(new TwoSet(set.first, set.second)) > confidence){

                candidate = set;
                System.out.println(candidate + " with confidence " + new Float(freq)/twocandidates.get(new TwoSet(set.first, set.second)));
            }
            if(twocandidates.containsKey(new TwoSet(set.second, set.third)) &&
                    new Float(freq) / twocandidates.get(new TwoSet(set.second, set.third)) > confidence){
                candidate = new ThreeSet(set.second, set.third, set.first);
                System.out.println(candidate + " with confidence " + new Float(freq) / twocandidates.get(new TwoSet(set.second, set.third)));

            }
            if(twocandidates.containsKey(new TwoSet(set.first, set.third)) &&
                    new Float(freq) / twocandidates.get(new TwoSet(set.first, set.third)) > confidence){
                candidate = new ThreeSet(set.first, set.third, set.second);
                System.out.println(candidate + " with confidence " + new Float(freq) / twocandidates.get(new TwoSet(set.first, set.third)));

            }
        });
    }

}
