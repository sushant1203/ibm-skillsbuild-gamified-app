package com.example.skillsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.skillsync.model.Course;
import com.example.skillsync.repo.CourseRepository;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SkillSyncApplication {

	public static void main(String[] args) {SpringApplication.run(SkillSyncApplication.class, args);}

	@Bean
	public CommandLineRunner theCourses(CourseRepository courseRepository) {
		return args -> {
			List<Course> courses = Arrays.asList(
					new Course("Artificial Intelligence Fundamentals", "AI", "Beginner", 5),
					new Course("Web Development Fundamentals", "Web Development", "Intermediate", 10),
					new Course("Data Fundamentals", "Data Analyst", "Advanced", 15),
					new Course("Cybersecurity Fundamentals", "Cyber Security", "Intermediate", 5),
					new Course("Cloud Computing Fundamentals","AI","Advanced",15),
					new Course("Learn Html","Web Development","Beginner",5),
					new Course("Professional Skills"," Computing Education","Beginner",5),
					new Course("Getting Started with Cybersecurity","Cyber Security","Intermediate",10),
					new Course("Machine Learning for Data Science Projects","AI","Advanced",15),
					new Course("Getting Started with Threat Intelligence and Hunting","Cyber Security","Advanced",15),
					new Course("Journey to Cloud: Orchestrating Your Solution","AI","Advanced",15),
					new Course("Getting Started with Data","Data Analyst","Intermediate",10),
					new Course("Fundamentals of Sustainability and Technology","Cyber Security","Beginner",5),
					new Course("Enterprise Security in Practice","Cyber Security","Advanced",15)
			);

			for (Course course : courses) {
				if (!courseRepository.existsByTitle(course.getTitle())) {  // Check if course exists
					courseRepository.save(course);
				}
			}
		};
	}





}
