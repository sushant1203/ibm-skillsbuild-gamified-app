package com.example.skillsync.controller;

import com.example.skillsync.model.Course;
import com.example.skillsync.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses")
    public String getCourses(Model model) { // adding courses to page
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses";
    }
    @GetMapping("/courses/filter")
    @ResponseBody
    public List<Course> filterCourses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty) {

        // Case 1: Both category and difficulty selected
        if (category != null && difficulty != null) {
            return courseRepository.findByCategoryAndDifficulty(category, difficulty);
        }
        // Case 2: Only category selected
        else if (category != null) {
            return courseRepository.findByCategory(category);
        }
        // Case 3: Only difficulty selected
        else if (difficulty != null) {
            return courseRepository.findByDifficulty(difficulty);
        }
        // Case 4: No filters applied, return all courses
        else {
            return courseRepository.findAll();
        }
    }


}
