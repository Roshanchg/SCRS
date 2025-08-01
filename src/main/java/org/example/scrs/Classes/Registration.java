package org.example.scrs.Classes;

import java.time.LocalDateTime;

public class Registration {
    private int id;
    private int studentId;
    private int courseId;
    private LocalDateTime datetime;
    public Registration(int id, int student,int course,LocalDateTime datetime){
        if(datetime==null){
            datetime=LocalDateTime.now();
        }
        this.id=id;
        this.studentId=student;
        this.courseId=course;
        this.datetime=datetime;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public String getDetails(){
        return this.id+","+this.studentId+","+this.courseId+","+this.datetime;
    }
}
