package org.example;

import java.util.Set;
import java.util.TreeSet;

public class DataClassifier {
    private Set<Integer> integerSet = new TreeSet<>();
    private Set<Float> floatSet = new TreeSet<>();
    private Set<String> stringSet = new TreeSet<>();

    public void classify(String line){
        try {
            int intValue = Integer.parseInt(line);
            integerSet.add(intValue);
        } catch (NumberFormatException e1) {
            try {
                float floatValue = Float.parseFloat(line);
                floatSet.add(floatValue);
            } catch (NumberFormatException e2) {
                stringSet.add(line);
            }
        }
    }

    public Set<Integer> getInteger(){
        return new TreeSet<>(integerSet);
    }

    public Set<Float> getFloat(){
        return new TreeSet<>(floatSet);
    }

    public Set<String> getString(){
        return new TreeSet<>(stringSet);
    }
}
