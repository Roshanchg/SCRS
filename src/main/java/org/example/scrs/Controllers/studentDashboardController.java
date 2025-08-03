package org.example.scrs.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.example.scrs.Classes.Course;
import org.example.scrs.Classes.Registration;
import org.example.scrs.Classes.Student;
import org.example.scrs.Enums.NAVIGATIONS;
import org.example.scrs.Enums.USERTYPE;
import org.example.scrs.Singletons.studentMainPaneSingleton;
import org.example.scrs.handlers.FileHandling;
import org.example.scrs.handlers.Navigator;
import org.example.scrs.handlers.ObjectFinder;
import org.example.scrs.handlers.SessionHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class studentDashboardController implements Initializable {


    public Label studentNameLabel;
    public BorderPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        studentMainPaneSingleton.setBorderPane(mainPane);
        try {
            Student activeStudent= (Student) ObjectFinder.GetUser(SessionHandler.getUserid(), USERTYPE.Student);
            assert activeStudent != null;
            studentNameLabel.setText(activeStudent.getName());
        } catch (IOException e) {
            AlertManager.showError("Student Dashboard","Session handler problem");
        }
        try {
            ShowMyCourses();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ShowAllCourses()throws IOException{
        List<Registration> allRegs= FileHandling.AllRegistrations();
        List<Course> allCourses =FileHandling.AllCourses();
        Student student=(Student) ObjectFinder.GetUser(SessionHandler.getUserid(),USERTYPE.Student);

        List<Course> filteredCourses=new ArrayList<>();
        for(Course course:allCourses){
            assert student != null;
            if(course.getProgramme()==student.getProgramme()){
                filteredCourses.add(course);
            }
        }
        for(Registration reg:allRegs){
            Course course=ObjectFinder.GetCourse(reg.getCourseId());
            if(SessionHandler.getUserid()==reg.getStudentId()){
                filteredCourses.removeIf(c-> {
                    assert course != null;
                    return c.getId()==course.getId();
                });
            }
        }
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/example/scrs/allCourses.fxml"));
        Parent loaded=loader.load();

        AllCoursesController controller=loader.getController();
        controller.setCourses(filteredCourses);
        mainPane.setCenter(loaded);

    }
    @FXML
    public void ShowProfileEdit() throws  IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/scrs/editStudent.fxml"));
        Parent form = loader.load();

        EditStudentController controller = loader.getController();
        Student activeStudent = (Student) ObjectFinder.GetUser(SessionHandler.getUserid(), USERTYPE.Student);
        assert activeStudent != null;
        controller.setStudent(activeStudent);
        mainPane.setCenter(form);
    }
    public void ShowMyCourses()throws IOException{
        List<Registration> allRegistrations=FileHandling.AllRegistrations();
        List<Registration> myRegs=new ArrayList<>();
        List<Course> myCourse=new ArrayList<>();
        for(Registration reg:allRegistrations){
            if (reg.getStudentId()==SessionHandler.getUserid()){
                myRegs.add(reg);
                myCourse.add(ObjectFinder.GetCourse(reg.getCourseId()));
            }
        }
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/example/scrs/myCourses.fxml"));
        Parent loaded=loader.load();

        MyCoursesController controller=loader.getController();
        controller.setData(myCourse,myRegs);
        mainPane.setCenter(loaded);

    }
    public void onLogout() throws IOException {
        SessionHandler.endSession();
        AlertManager.showMessage("Log Out","logged out");
        studentMainPaneSingleton.reset();
        Navigator.Navigate(NAVIGATIONS.LOGIN);
    }

}
