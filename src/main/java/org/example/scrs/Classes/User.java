package org.example.scrs.Classes;

public class User {
    private int id;
    private String email,password,name;
    public User(int id,String name, String email,String password){
        this.id=id;
        this.name=name;
        this.email=email;
        this.password=password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return this.name;
    }
    public String getDetails(){
        return this.id+","+this.name+","+this.email+","+this.password;
    }
    public String getPassword(){
        return this.password;
    }

}
