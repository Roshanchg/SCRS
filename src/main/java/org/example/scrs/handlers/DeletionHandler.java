package org.example.scrs.handlers;

import org.example.scrs.Classes.Registration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeletionHandler {
    public static void onUserDelete(int id)throws IOException {
        FileHandling.removeUser(id);

        List<Registration> allRegs=FileHandling.AllRegistrations();
        for(Registration reg:allRegs) {
            if(reg.getStudentId()==id) {
                FileHandling.removeRegistration(reg.getId());
            }
        }
    }

    public static void onCourseDelete(int id)throws IOException {
        FileHandling.removeCourse(id);

        List<Registration> allRegs=FileHandling.AllRegistrations();
        for(Registration reg:allRegs) {
            if(reg.getCourseId()==id) {
                FileHandling.removeRegistration(reg.getId());
            }
        }
    }
}
