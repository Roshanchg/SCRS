package org.example.scrs.handlers;

import org.example.scrs.Enums.USERTYPE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserHandling {
    public static int authenticate(String email, String password,USERTYPE usertype)throws IOException {
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
                            if(password.equals(passwordPart)){
                                return id;
                            }
                        }
                    }
                }
                return 0;
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
                        if(password.equals(passwordPart)){
                            return id;
                        }
                    }
                }
                return 0;
            }
        }
        return 0;
    }
//    returns id of user
}
