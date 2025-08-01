package org.example.scrs.handlers;

import org.example.scrs.Classes.*;
import org.example.scrs.Enums.PROGRAMME;
import org.example.scrs.Enums.USERTYPE;
import org.example.scrs.handlers.FileHandling.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.example.scrs.handlers.FileHandling.RegistrationsFile;

public class ObjectFinder {
    public static User GetUser(int searchId,USERTYPE usertype)throws IOException {
        int id = 0;
        String name;
        String email = "";
        String password = "";
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
                        email=parts[2];
                        password=parts[3];
                        if(searchId==id) {
                            return new Admin(id, name, email, password);
                        }
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
                        if(searchId==id) {
                            return new Student(id, name, email, password, programme, semester);
                        }
                    }
                }
            }
        }

        return null;
    }
    public static Course GetCourse(int searchId)throws IOException{
        try(BufferedReader br=new BufferedReader(new FileReader(FileHandling.CoursesFile))){
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
                if(searchId==id) {
                    return new Course(id, name, programme);
                }
            }
        }
        return null;
    }

    public static Registration AllRegistrations(int searchId) throws IOException{
        try(BufferedReader br=new BufferedReader(new FileReader(FileHandling.RegistrationsFile))){
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
                if(searchId==regid) {
                    return new Registration(regid, stid, cid, datetime);
                }
            }
        }
        return null;
    }
}
