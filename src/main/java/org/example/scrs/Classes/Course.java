package org.example.scrs.Classes;

import org.example.scrs.Enums.PROGRAMME;

public class Course {
    private int id;
    private PROGRAMME programme;
    private String name;

    public Course(int id, String name,PROGRAMME programme){
        this.id=id;
        this.name=name;
        this.programme=programme;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public PROGRAMME getProgramme() {
        return programme;
    }

    public String getDetails(){
        return this.name+","+this.name+","+this.programme;
    }
}
