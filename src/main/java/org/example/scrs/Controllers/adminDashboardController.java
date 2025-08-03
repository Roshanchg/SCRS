package org.example.scrs.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.scrs.Classes.Course;
import org.example.scrs.Classes.Student;
import org.example.scrs.Classes.User;
import org.example.scrs.Enums.NAVIGATIONS;
import org.example.scrs.Enums.PROGRAMME;
import org.example.scrs.Enums.USERTYPE;
import org.example.scrs.handlers.*;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class adminDashboardController implements Initializable {
    @FXML public Button backButton;
    @FXML public PieChart pieChart;
    @FXML private VBox leftVbox;
    @FXML private BorderPane mainPane;

    @FXML public TableView<Student> leftTable;
    @FXML private TableColumn<Student,Integer> idColumn;
    @FXML private TableColumn<Student,String> nameColumn;
    @FXML private TableColumn<Student,String > emailColumn;
    @FXML private TableColumn<Student, PROGRAMME> programmeColumn;
    @FXML private TableColumn<Student,Integer> semesterColumn;
    @FXML private TableColumn<Student,Void> actionColumn;


    @FXML public TableView<Course> rightTable;
    @FXML private TableColumn<Course,Integer> courseIdColumn;
    @FXML private TableColumn<Course,String> courseNameColumn;
    @FXML private TableColumn<Course,PROGRAMME> courseProgrammeColumn;
    @FXML private TableColumn<Course,Void> courseActionColumn;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        leftVbox.prefWidthProperty().bind(
            mainPane.widthProperty().multiply(.55)
        );
        try {
            loadPieChart();
            loadStudentTable();
            loadCoursesTable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadPieChart()throws IOException {
        Map<Integer,Integer> dataMap= FileHandling.getPieMap();
        for(Map.Entry<Integer,Integer> entry:dataMap.entrySet()){
            if(entry.getKey()==0)continue;
            String name= Objects.requireNonNull(ObjectFinder.GetUser(entry.getKey(), USERTYPE.Student)).getName();
            pieChart.getData().add(new PieChart.Data(name,entry.getValue()));
        }
    }

    private void loadStudentTable()throws IOException{
        List<User> users=FileHandling.AllUsers(USERTYPE.Student);
        ObservableList<Student> students= FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        programmeColumn.setCellValueFactory(new PropertyValueFactory<>("programme"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));

        for(User user:users){
            assert user!=null;
            students.add((Student) user);
        }
        leftTable.setItems(students);
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button removeBtn = new Button("Remove");

            {
                removeBtn.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(student);
                    try {
                        DeletionHandler.onUserDelete(student.getId());
                        Navigator.Navigate(NAVIGATIONS.ADMIN);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox container = new HBox(removeBtn);
                    container.setAlignment(Pos.CENTER);
                    setGraphic(container);
                }
            }
        });
    }


    private void loadCoursesTable()throws IOException{
        List<Course> courses=FileHandling.AllCourses();
        ObservableList<Course> coursesList= FXCollections.observableArrayList();

        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseProgrammeColumn.setCellValueFactory(new PropertyValueFactory<>("programme"));

        for(Course course:courses){
            assert course!=null;
            coursesList.add( course);
        }
        rightTable.setItems(coursesList);

        courseActionColumn.setCellFactory(param->new TableCell<>(){
            private final Button remove=new Button("Remove");
            {
                remove.setOnAction(event->
                {
                    Course course=getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(course);
                    try {
                        DeletionHandler.onCourseDelete(course.getId());
                        Navigator.Navigate(NAVIGATIONS.ADMIN);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            @Override
            protected void updateItem(Void item,boolean empty){
                super.updateItem(item,empty);
                if(empty){
                    setGraphic(null);
                }
                else {
                    HBox container =new HBox(remove);
                    container.setAlignment(Pos.CENTER);
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    private void addStudent()throws IOException{
        StudentForm.showForm();
        Navigator.Navigate(NAVIGATIONS.ADMIN);
    }

    @FXML private void addCourse() throws IOException {
        CourseForm.showForm();
        Navigator.Navigate(NAVIGATIONS.ADMIN);
    }

    public void goHome(ActionEvent actionEvent)throws IOException {
        SessionHandler.endSession();
        Navigator.Navigate(NAVIGATIONS.LOGIN);
    }
}
