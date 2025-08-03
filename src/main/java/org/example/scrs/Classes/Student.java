package org.example.scrs.Classes;

import org.example.scrs.Enums.PROGRAMME;

public class Student extends User{
    private int semester;
    private PROGRAMME programme;
    public Student(int id, String name, String email, String password, PROGRAMME programme,int semester){
        super(id,name,email,password);
        this.programme=programme;
        this.semester=semester;
    }

    public PROGRAMME getProgramme() {
        return programme;
    }

    public int getSemester() {
        return semester;
    }

    @Override
    public String getDetails(){
        return this.getId()+","+this.getName()+","+this.getEmail()+","+this.getPassword()+","+this.programme+","+this.semester;
    }
}
