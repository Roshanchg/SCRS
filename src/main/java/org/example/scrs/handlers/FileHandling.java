package org.example.scrs.handlers;

import org.example.scrs.Classes.*;
import org.example.scrs.Enums.PROGRAMME;
import org.example.scrs.Enums.USERTYPE;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileHandling {
    public static String StudentFile="students.csv";
    public static String AdminFile="admins.csv";
    public static String CoursesFile="courses.csv";
    public static String RegistrationsFile="registrations.csv";

    public static String tempdir ="temp";


    public static void init() throws IOException {
        File[] files={
                new File(StudentFile),
                new File(AdminFile),
                new File(CoursesFile),
                new File(RegistrationsFile)
        };

        for(File file :files){
            if(!file.exists()){
                file.createNewFile();
            }
        }
        File tempDir=new File(FileHandling.tempdir);
        tempDir.mkdir();
    }

    public static boolean emailExists(String email,USERTYPE usertype)throws IOException {
        int id = 0;
        String name;
        String emailPart = "";
        String passwordPart = "";
        String line;
        String[] parts;


        switch (usertype){
            case Admin -> {
                try(BufferedReader br=new BufferedReader(new FileReader(FileHandling.AdminFile))){
                    while((line=br.readLine())!=null){
                        if(line.trim().isEmpty()) continue;
                        parts=line.split(",");
                        id=Integer.parseInt(parts[0]);
                        name=parts[1];
                        emailPart=parts[2];
                        if(email.equals(emailPart.trim())){
                            return true;
                        }
                    }
                }
                return false;
            }
            case Student -> {
                try(BufferedReader br=new BufferedReader(new FileReader(FileHandling.StudentFile))){
                    while((line=br.readLine())!=null){
                        if(line.trim().isEmpty()) continue;
                        parts=line.split(",");
                        id=Integer.parseInt(parts[0]);
                        name=parts[1];
                        emailPart=parts[2];
                    }
                    if(email.equals(emailPart.trim())){
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }



    public static List<User> AllUsers(USERTYPE usertype)throws IOException{
        int id = 0;
        String name;
        String email = "";
        String password = "";
        String line;
        String[] parts;
        List<User> users=new ArrayList<>();
        switch (usertype){
            case Admin -> {
                try(BufferedReader br=new BufferedReader(new FileReader(FileHandling.AdminFile))){
                    while((line=br.readLine())!=null){
                        if(line.trim().isEmpty()) continue;
                        parts=line.split(",");
                        id=Integer.parseInt(parts[0]);
                        name=parts[1];
                        email=parts[2];
                        password=parts[3];
                        users.add(new Admin(id,name,email,password));
                    }
                }
            }
            case Student -> {
                PROGRAMME programme;
                int semester;
                try(BufferedReader br=new BufferedReader(new FileReader(FileHandling.StudentFile))){
                    while((line=br.readLine())!=null){
                        if(line.trim().isEmpty()) continue;
                        parts=line.split(",");
                        id=Integer.parseInt(parts[0]);
                        name=parts[1];
                        email=parts[2];
                        password=parts[3];
                        programme=switch (parts[4]){
                            case "BCS" ->PROGRAMME.BCS;
                            case "BIHM" -> PROGRAMME.BIHM;
                            case "BBA" -> PROGRAMME.BBA;
                            default -> throw new IllegalStateException("Unexpected value: " + parts[4]);
                        };
                        semester=Integer.parseInt(parts[5]);
                        users.add(new Student(id,name,email,password,programme,semester));
                    }
                }
            }
        }

        return users;
    }

    public static void addUser(USERTYPE usertype,String data)throws IOException{
        switch (usertype){
            case Admin -> {
                try(BufferedWriter bw=new BufferedWriter(new FileWriter(AdminFile))){
                    bw.write(data);
                    bw.newLine();
                }
            }
            case Student -> {
                try(BufferedWriter bw=new BufferedWriter(new FileWriter(StudentFile))){
                    bw.write(data);
                    bw.newLine();
                }
            }
        }
    }






    public static List<Course> AllCourses()throws IOException{
        List<Course> courses=new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new FileReader(CoursesFile))){
            String line;
            String[] parts;
            int id;
            String name;
            PROGRAMME programme;
            while((line=br.readLine())!=null){
                parts=line.split(",");
                id=Integer.parseInt(parts[0]);
                name=parts[1];
                programme=switch (parts[2]){
                    case "BCS"->PROGRAMME.BCS;
                    case "BIHM" -> PROGRAMME.BIHM;
                    case "BBA" -> PROGRAMME.BBA;
                    default -> throw new IllegalStateException("Unexpected value: " + parts[2]);
                };
                courses.add(new Course(id,name,programme));
            }
        }
        return courses;
    }

    public static void addCourse(String data)throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CoursesFile))) {
            bw.write(data);
            bw.newLine();
        }
    }




    public static void addRegistration(String data)throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RegistrationsFile))) {
            bw.write(data);
            bw.newLine();
        }
    }
    public static List<Registration> AllRegistrations() throws IOException{
        List<Registration> registrations=new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new FileReader(RegistrationsFile))){
            String line;
            String[] parts;
            int regid;
            int stid;
            int cid;
            LocalDateTime datetime;
            while((line=br.readLine())!=null){
                parts=line.split(",");
                regid=Integer.parseInt(parts[0]);
                stid=Integer.parseInt(parts[1]);
                cid=Integer.parseInt(parts[2]);
                datetime=LocalDateTime.parse(parts[3]);
                registrations.add(new Registration(regid,stid,cid,datetime));
            }
        }
        return registrations;
    }
}
