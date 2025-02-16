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
					new Course("Artificial Intelligence Fundamentals", "AI", "Beginner", 5,"https://students.yourlearning.ibm.com/activity/PLAN-CC702B39D429"),
					new Course("Web Development Fundamentals", "Web Development", "Intermediate", 10,"https://skills.yourlearning.ibm.com/activity/PLAN-8749C02A78EC?channelId=CNL_LCB_1616516409884"),
					new Course("Data Fundamentals", "Data Analyst", "Advanced", 15, "https://skills.yourlearning.ibm.com/activity/PLAN-BC0FAEE8E439?channelId=CNL_LCB_1592251313526"),
					new Course("Cybersecurity Fundamentals", "Cyber Security", "Intermediate", 5, "https://skills.yourlearning.ibm.com/activity/PLAN-FA511CDFAF48?channelId=CNL_LCB_1580409324694"),
					new Course("Building Trustworthy AI Enterprise Solutions","AI","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-A2AAD89B7676?ngo-id=0302"),
					new Course("Cloud Computing Fundamentals","Web Development","Beginner",5,"https://skills.yourlearning.ibm.com/activity/PLAN-2EC3A305F2C3?ngo-id=0302"),
					new Course("Professional Skills"," Computing Education","Beginner",5, "https://skills.yourlearning.ibm.com/activity/PLAN-C7EE7CC95370?ngo-id=0302"),
					new Course("Getting Started with Cybersecurity","Cyber Security","Intermediate",10, "https://skills.yourlearning.ibm.com/activity/PLAN-D8E7C82C1D76?ngo-id=0302"),
					new Course("Machine Learning for Data Science Projects","AI","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-D5ED0773935F?ngo-id=0302"),
					new Course("Getting Started with Threat Intelligence and Hunting","Cyber Security","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-A44B849031F1?ngo-id=0302"),
					new Course("Journey to Cloud: Orchestrating Your Solution","AI","Advanced",15 ,"https://skills.yourlearning.ibm.com/activity/PLAN-14F2691E3A32?ngo-id=0302"),
					new Course("Getting Started with Data","Data Analyst","Intermediate",10, "https://skills.yourlearning.ibm.com/activity/PLAN-BE0E24A0BA5C?ngo-id=0302"),
					new Course("Fundamentals of Sustainability and Technology","Cyber Security","Beginner",5,"https://skills.yourlearning.ibm.com/activity/PLAN-BBCABF0CF5B0?ngo-id=0302"),
					new Course("Enterprise Security in Practice","Cyber Security","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-BBCABF0CF5B0?ngo-id=0302")			);


			for (Course course : courses) {
				Course oldCourse = courseRepository.findByTitle(course.getTitle());

				if (!courseRepository.existsByTitle(course.getTitle())) {  // Check if course exists
					courseRepository.save(course);
				}
				else if(!course.equals(oldCourse)){ // Check if course was edited
					oldCourse.setCategory(course.getCategory());
					oldCourse.setTitle(course.getTitle());
					oldCourse.setCourseScore(course.getCourseScore());
					oldCourse.setDifficulty(course.getDifficulty());
					oldCourse.setLinks(course.getLinks());

					courseRepository.save(oldCourse);
				}


			}


		};
	}





}
