package org.example.scrs.handlers;

import org.example.scrs.Classes.*;
import org.example.scrs.Enums.PROGRAMME;
import org.example.scrs.Enums.USERTYPE;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHandling {
    public static String StudentFile="students.csv";
    public static String AdminFile="admins.csv";
    public static String CoursesFile="courses.csv";
    public static String RegistrationsFile="registrations.csv";

    public static String tempdir ="temp";


    public static void init() throws IOException {
        File[] files={
                new File(StudentFile),
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
        File adminFile=new File(AdminFile);
        if(adminFile.exists()){
            adminFile.delete();
        }
        adminFile.createNewFile();
        Admin admin=new Admin(FileHandling.getNextId(AdminFile),"Admin",
                "admin@gmail.com", "admin");
        FileHandling.addUser(USERTYPE.Admin,admin.getDetails());
    }

    public static int getNextId(String Filename)throws IOException{
        int id=0;
        try(BufferedReader br=new BufferedReader(new FileReader(Filename))){
            String line;
            String[] parts;
            while((line=br.readLine())!=null){
                if (line.trim().isEmpty()) continue;
                parts=line.split(",");
                id=Integer.parseInt(parts[0]);
            }
            id++;
        }
        return id;
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
                try(BufferedWriter bw=new BufferedWriter(new FileWriter(AdminFile, true))){
                    bw.write(data);
                    bw.newLine();
                }
            }
            case Student -> {
                try(BufferedWriter bw=new BufferedWriter(new FileWriter(StudentFile,true))){
                    bw.write(data);
                    bw.newLine();
                }
            }
        }
    }


    public static void removeUser(int uid)throws IOException{
        Student user=(Student) ObjectFinder.GetUser(uid,USERTYPE.Student);
        assert user!=null;
        File originalFile=new File(StudentFile);
        File tempFile=new File(tempdir,StudentFile);
        if(tempFile.exists()){
            tempFile.delete();
        }
        tempFile.createNewFile();
        List<User> students=AllUsers(USERTYPE.Student);
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(tempFile,true))) {
            for (User c : students) {
                c=(Student) c;
                if (c.getId() == uid) continue;
                bw.write(c.getDetails());
                bw.newLine();
            }
        }
        if(originalFile.delete()){
            tempFile.renameTo(originalFile);
        }
        else{
            System.out.printf("File Removal failed");
        }

    }

    public static void editUser(int uid,Student student)throws IOException{
        Student user=(Student) ObjectFinder.GetUser(uid,USERTYPE.Student);
        assert user!=null;
        File originalFile=new File(StudentFile);
        File tempFile=new File(tempdir,StudentFile);
        if(tempFile.exists()){
            tempFile.delete();
        }
        tempFile.createNewFile();
        List<User> students=AllUsers(USERTYPE.Student);
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(tempFile,true))) {
            for (User c : students) {
                if (c.getId() == uid) {
                    bw.write(student.getDetails());
                    bw.newLine();
                    continue;
                };
                bw.write(c.getDetails());
                bw.newLine();
            }
        }
        if(originalFile.delete()){
            tempFile.renameTo(originalFile);
        }
        else{
            System.out.printf("File Removal failed");
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CoursesFile,true))) {
            bw.write(data);
            bw.newLine();
        }
    }

    public static void removeCourse(int cid)throws IOException{
        Course course=ObjectFinder.GetCourse(cid);
        assert course!=null;
        File originalFile=new File(CoursesFile);
        File tempFile=new File(tempdir,CoursesFile);
        if(tempFile.exists()){
            tempFile.delete();
        }
        tempFile.createNewFile();
        List<Course> courses=AllCourses();
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(tempFile,true))) {
            for (Course c : courses) {
                if (c.getId() == cid) continue;
                bw.write(c.getDetails());
                bw.newLine();
            }
        }
        if(originalFile.delete()){
            tempFile.renameTo(originalFile);
        }
        else{
            System.out.printf("File Removal failed");
        }

    }




    public static void addRegistration(String data)throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RegistrationsFile,true))) {
            bw.write(data);
            bw.newLine();
        }
    }
    public static void removeRegistration(int regId)throws IOException{
        Registration registration=ObjectFinder.getRegistration(regId);
        assert registration!=null;
        File originalFile=new File(RegistrationsFile);
        File tempFile=new File(tempdir,RegistrationsFile);
        if(tempFile.exists()){
            tempFile.delete();
        }
        tempFile.createNewFile();
        List<Registration> registrations=AllRegistrations();
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(tempFile,true))) {
            for (Registration reg : registrations) {
                if (reg.getId() == regId) continue;
                bw.write(reg.getDetails());
                bw.newLine();
            }
        }
        if(originalFile.delete()){
           tempFile.renameTo(originalFile);
        }
        else{
            System.out.printf("File Removal failed");
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


    public static Map<Integer,Integer> getPieMap()throws IOException{
        Map<Integer,Integer> studentCourseMap=new HashMap<>();
        List<Registration> registrations=AllRegistrations();
        Student student;
        for(Registration reg :registrations){
            student=(Student)ObjectFinder.GetUser(reg.getStudentId(),USERTYPE.Student);
            assert student != null;
            studentCourseMap.put(student.getId(),studentCourseMap.getOrDefault(student.getId(),0)+1);
        }
        return studentCourseMap;
    }


}
