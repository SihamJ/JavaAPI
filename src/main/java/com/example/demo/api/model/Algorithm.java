package com.example.demo.api.model;


public abstract class Algorithm {
    private int id;
    private String name;
    private String filename;
    private int type;

    public Algorithm(int id, String name, String filename, int type){
        this.id = id;
        this.name = name;
        this.filename = filename;
        this.type = type;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name){
        this.name = name;

    }
    public String getFilename() {
        return this.filename;
    }
    public void setFilename(String filename){
        this.filename = filename;
    }

    public int getType() {
        return this.type;
    }
    public void setType(int type){
        this.type = type;
    }

    public abstract float predict(float[] values) throws Exception;
    public abstract void loadAlgorithm() throws Exception;

}
