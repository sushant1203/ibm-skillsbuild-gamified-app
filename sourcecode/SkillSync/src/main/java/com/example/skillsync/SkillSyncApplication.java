package com.example.skillsync;

import com.example.skillsync.model.Option;
import com.example.skillsync.model.Question;
import com.example.skillsync.model.Quiz;
import com.example.skillsync.repo.OptionRepository;
import com.example.skillsync.repo.QuestionRepository;
import com.example.skillsync.repo.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private OptionRepository optionRepository;
	public static void main(String[] args) {SpringApplication.run(SkillSyncApplication.class, args);}

	@Bean
	public CommandLineRunner theCourses(CourseRepository courseRepository) {
		return args -> {
			List<Course> courses = Arrays.asList(
					new Course("Artificial Intelligence Fundamentals", "AI", "Beginner", 5,"https://students.yourlearning.ibm.com/activity/PLAN-CC702B39D429", null),
					new Course("Web Development Fundamentals", "Web Development", "Intermediate", 10,"https://skills.yourlearning.ibm.com/activity/PLAN-8749C02A78EC?channelId=CNL_LCB_1616516409884", null),
					new Course("Data Fundamentals", "Data Analyst", "Advanced", 15, "https://skills.yourlearning.ibm.com/activity/PLAN-BC0FAEE8E439?channelId=CNL_LCB_1592251313526" , null),
					new Course("Cybersecurity Fundamentals", "Cyber Security", "Intermediate", 5, "https://skills.yourlearning.ibm.com/activity/PLAN-FA511CDFAF48?channelId=CNL_LCB_1580409324694", null),
					new Course("Building Trustworthy AI Enterprise Solutions","AI","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-A2AAD89B7676?ngo-id=0302", null),
					new Course("Cloud Computing Fundamentals","Web Development","Beginner",5,"https://skills.yourlearning.ibm.com/activity/PLAN-2EC3A305F2C3?ngo-id=0302", null),
					new Course("Professional Skills"," Computing Education","Beginner",5, "https://skills.yourlearning.ibm.com/activity/PLAN-C7EE7CC95370?ngo-id=0302", null),
					new Course("Getting Started with Cybersecurity","Cyber Security","Intermediate",10, "https://skills.yourlearning.ibm.com/activity/PLAN-D8E7C82C1D76?ngo-id=0302", null),
					new Course("Machine Learning for Data Science Projects","AI","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-D5ED0773935F?ngo-id=0302", null),
					new Course("Getting Started with Threat Intelligence and Hunting","Cyber Security","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-A44B849031F1?ngo-id=0302", null),
					new Course("Journey to Cloud: Orchestrating Your Solution","AI","Advanced",15 ,"https://skills.yourlearning.ibm.com/activity/PLAN-14F2691E3A32?ngo-id=0302", null),
					new Course("Getting Started with Data","Data Analyst","Intermediate",10, "https://skills.yourlearning.ibm.com/activity/PLAN-BE0E24A0BA5C?ngo-id=0302", null),
					new Course("Fundamentals of Sustainability and Technology","Cyber Security","Beginner",5,"https://skills.yourlearning.ibm.com/activity/PLAN-BBCABF0CF5B0?ngo-id=0302", null),
					new Course("Enterprise Security in Practice","Cyber Security","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-BBCABF0CF5B0?ngo-id=0302", null)			);


			for (Course course : courses) {
				List<Course> existingCourses = courseRepository.findByTitle(course.getTitle());

				if (existingCourses.isEmpty()) {
					courseRepository.save(course);
				} else {
					Course oldCourse = existingCourses.get(0);
					if (!course.equals(oldCourse)) {
						oldCourse.setCategory(course.getCategory());
						oldCourse.setTitle(course.getTitle());
						oldCourse.setCourseScore(course.getCourseScore());
						oldCourse.setDifficulty(course.getDifficulty());
						oldCourse.setLinks(course.getLinks());

						courseRepository.save(oldCourse);
					}
				}
			}


			// -----------------------
			// AI Quiz Block
			// -----------------------
			{
				Course aiCourse = courseRepository.findByTitle("Artificial Intelligence Fundamentals").get(0);
				if (quizRepository.findByCourse(aiCourse).isEmpty()) {
					Quiz quiz = new Quiz(aiCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What is Artificial Intelligence?", quiz, null);
					Question q2 = new Question("Which of the following is a type of AI?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("A field of study focused on creating intelligent machines", true, q1),
							new Option("A type of hardware", false, q1),
							new Option("A programming language", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("Supervised Learning", true, q2),
							new Option("Unstructured Learning", false, q2),
							new Option("Data Engineering", false, q2)
					);

					q1.setOptions(optionsQ1);
					q2.setOptions(optionsQ2);
					quiz.setQuestions(Arrays.asList(q1, q2));

					questionRepository.saveAll(Arrays.asList(q1, q2));
				}
			}

			// -----------------------
			// Web Development Fundamentals Quiz Block
			// -----------------------
			{
				Course webCourse = courseRepository.findByTitle("Web Development Fundamentals").get(0);
				if (quizRepository.findByCourse(webCourse).isEmpty()) {
					Quiz quiz = new Quiz(webCourse, null);
					quiz = quizRepository.save(quiz);

					// Questions for Web Development
					Question q1 = new Question("What is HTML?", quiz, null);
					Question q2 = new Question("What is CSS used for?", quiz, null);
					Question q3 = new Question("What is JavaScript's role in web development?", quiz, null);
					Question q4 = new Question("What is the purpose of responsive design?", quiz, null);
					Question q5 = new Question("Which framework is popular for front-end development?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					// Options for each question
					List<Option> optionsQ1 = Arrays.asList(
							new Option("HTML stands for HyperText Markup Language", true, q1),
							new Option("HTML is used for styling", false, q1),
							new Option("HTML is a programming language", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("CSS is used to style and layout web pages", true, q2),
							new Option("CSS is a database query language", false, q2),
							new Option("CSS is used for server-side logic", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("JavaScript adds interactivity to web pages", true, q3),
							new Option("JavaScript is used for creating server databases", false, q3),
							new Option("JavaScript is a styling language", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("Responsive design ensures web pages adapt to various screen sizes", true, q4),
							new Option("Responsive design is used for backend development", false, q4),
							new Option("Responsive design improves database performance", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("React is a popular front-end framework", true, q5),
							new Option("Laravel is a popular front-end framework", false, q5),
							new Option("Django is a popular front-end framework", false, q5)
					);

					q1.setOptions(optionsQ1);
					q2.setOptions(optionsQ2);
					q3.setOptions(optionsQ3);
					q4.setOptions(optionsQ4);
					q5.setOptions(optionsQ5);
					quiz.setQuestions(Arrays.asList(q1, q2, q3, q4, q5));

					questionRepository.saveAll(Arrays.asList(q1, q2, q3, q4, q5));
				}
			}

			// -----------------------
			// Data Fundamentals Quiz Block
			// -----------------------
			{
				Course dataCourse = courseRepository.findByTitle("Data Fundamentals").get(0);
				if (quizRepository.findByCourse(dataCourse).isEmpty()) {
					Quiz quiz = new Quiz(dataCourse, null);
					quiz = quizRepository.save(quiz);

					// Questions for Data Fundamentals
					Question q1 = new Question("What is Data Fundamentals?", quiz, null);
					Question q2 = new Question("What is the purpose of data analysis?", quiz, null);
					Question q3 = new Question("What is a database used for?", quiz, null);
					Question q4 = new Question("What is data visualization?", quiz, null);
					Question q5 = new Question("Which tool is commonly used for data analysis?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					// Options for each question
					List<Option> optionsQ1 = Arrays.asList(
							new Option("Data Fundamentals covers basics of data management", true, q1),
							new Option("Data Fundamentals is about web development", false, q1),
							new Option("Data Fundamentals is a networking course", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("Data analysis involves inspecting and modeling data", true, q2),
							new Option("Data analysis is used to design graphics", false, q2),
							new Option("Data analysis is only about data storage", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("A database stores, retrieves, and manages data", true, q3),
							new Option("A database is used for front-end development", false, q3),
							new Option("A database is a type of operating system", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("Data visualization is the graphical representation of data", true, q4),
							new Option("Data visualization is used to create animations", false, q4),
							new Option("Data visualization refers to data storage techniques", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("Excel is a common tool for data analysis", true, q5),
							new Option("Photoshop is a common tool for data analysis", false, q5),
							new Option("Illustrator is a common tool for data analysis", false, q5)
					);

					q1.setOptions(optionsQ1);
					q2.setOptions(optionsQ2);
					q3.setOptions(optionsQ3);
					q4.setOptions(optionsQ4);
					q5.setOptions(optionsQ5);
					quiz.setQuestions(Arrays.asList(q1, q2, q3, q4, q5));

					questionRepository.saveAll(Arrays.asList(q1, q2, q3, q4, q5));
				}
			}


		};
		}
	}





