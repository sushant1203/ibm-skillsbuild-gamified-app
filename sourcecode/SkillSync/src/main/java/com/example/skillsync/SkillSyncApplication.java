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
					new Course("Cybersecurity Fundamentals", "Cyber Security", "Beginner", 5)
			);

			for (Course course : courses) {
				if (!courseRepository.existsByTitle(course.getTitle())) {  // Check if course exists
					courseRepository.save(course);
				}
			}
		};
	}





}
