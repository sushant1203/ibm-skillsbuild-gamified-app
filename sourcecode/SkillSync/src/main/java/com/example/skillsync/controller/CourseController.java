package com.example.skillsync.controller;

import com.example.skillsync.model.Course;
import com.example.skillsync.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses")
    public String getCourses(Model model) { // adding courses to page
        List<Course> courses = new ArrayList<>();
        courses.add(new Course()); // Dummy course so ID (in db) matches index in array
        courses.addAll(courseRepository.findAll());
        model.addAttribute("courses", courses);
        return "courses";
    }

}
