package org.example;

import java.util.List;

public class Arguments {
    private final List<String> files;
    private final String PathResult;
    private final String prefix;
    private final boolean AddToExist;
    private final Statistics.StatMode statMode;

    public Arguments(List<String> files, String PathResult, String prefix, boolean AddToExist, Statistics.StatMode statMode){
        this.files=files;
        this.PathResult=PathResult;
        this.prefix=prefix;
        this.AddToExist=AddToExist;
        this.statMode=statMode;
    }

    public List<String> getFiles(){
        return files;
    }
    public String getPathResult(){
        return PathResult;
    }
    public String getPrefix(){
        return prefix;
    }
    public boolean getAddToExist(){
        return AddToExist;
    }
    public Statistics.StatMode getStatMode(){
        return statMode;
    }
}
