package com.example.skillsync;

import com.example.skillsync.model.*;
import com.example.skillsync.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableAsync; // Add this import
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@EnableAsync // Add this annotation to enable asynchronous processing
@EnableScheduling
@SpringBootApplication
  // Add this annotation

public class SkillSyncApplication {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private QuizRepository quizRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private OptionRepository optionRepository;

	public static void main(String[] args) {
		SpringApplication.run(SkillSyncApplication.class, args);
	}


	@Bean
	public CommandLineRunner theUsers(UserRepository userRepository) {
		return args -> {
			// Check if the user already exists
			User existingUser = userRepository.findByEmail("email@example.com");
			if (existingUser == null) {
				// Create a new user only if it doesn't exist
				User user = new User("newUsername", "email@example.com", "password123", "New User");
				userRepository.save(user);
				System.out.println("User created: " + user.getUsername());
			} else {
				System.out.println("User already exists: " + existingUser.getUsername());
			}
		};
	}

	@Bean
	public CommandLineRunner theCourses(CourseRepository courseRepository) {
		return args -> {

			List<Course> courses = Arrays.asList(
					new Course("Artificial Intelligence Fundamentals", "AI", "Beginner", 5,"https://students.yourlearning.ibm.com/activity/PLAN-CC702B39D429","Ai-fundamentals.png"),
					new Course("Web Development Fundamentals", "Web Development", "Intermediate", 10,"https://skills.yourlearning.ibm.com/activity/PLAN-8749C02A78EC?channelId=CNL_LCB_1616516409884","web-development-fundamentals.png"),
					new Course("Data Fundamentals", "Data Analyst", "Advanced", 15, "https://skills.yourlearning.ibm.com/activity/PLAN-BC0FAEE8E439?channelId=CNL_LCB_1592251313526","data-analyst.png"),
					new Course("Cybersecurity Fundamentals", "Cyber Security", "Intermediate", 10, "https://skills.yourlearning.ibm.com/activity/PLAN-FA511CDFAF48?channelId=CNL_LCB_1580409324694","cybersecruity-fundementals.png"),
					new Course("Building Trustworthy AI Enterprise Solutions","AI","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-A2AAD89B7676?ngo-id=0302","learning-plan-building-trustworthy-ai.png"),
					new Course("Cloud Computing Fundamentals","Web Development","Beginner",5,"https://skills.yourlearning.ibm.com/activity/PLAN-2EC3A305F2C3?ngo-id=0302","cloud-computing.png"),
					new Course("Professional Skills","Computing Education","Beginner",5, "https://skills.yourlearning.ibm.com/credential/CREDLY-dafd91d2-b4fa-46e2-a1fe-c9b0428d6ea7","professional-skills.png"),
					new Course("Getting Started with Cybersecurity","Cyber Security","Intermediate",10, "https://skills.yourlearning.ibm.com/activity/PLAN-C7EE7CC95370?ngo-id=0302","getting-started-cybersecruity.png"),
					new Course("Machine Learning for Data Science Projects","AI","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-D8E7C82C1D76?ngo-id=0302","machine-learning.png"),
					new Course("Getting Started with Threat Intelligence and Hunting","Cyber Security","Advanced",15, "https://skills.yourlearning.ibm.com/activity/URL-A3CBFC2EAA5F","getting-started-with-threat.png"),
					new Course("Journey to Cloud: Orchestrating Your Solution","AI","Advanced",15 ,"https://skills.yourlearning.ibm.com/activity/PLAN-A44B849031F1?ngo-id=030","journey-cloud.png"),
					new Course("Getting Started with Data","Data Analyst","Intermediate",10, "https://skills.yourlearning.ibm.com/activity/URL-968D607D4A12","started-data.png"),
					new Course("Fundamentals of Sustainability and Technology","Cyber Security","Beginner",5,"https://skills.yourlearning.ibm.com/activity/PLAN-BE0E24A0BA5C?ngo-id=0302","fundamentals-of-sustainability.png"),
					new Course("Enterprise Security in Practice","Cyber Security","Advanced",15, "https://skills.yourlearning.ibm.com/activity/PLAN-BBCABF0CF5B0?ngo-id=0302","enterprise-secruity.png")			);



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

			// -----------------------
// Cybersecurity Fundamentals Quiz Block
// -----------------------
			{
				Course cyberCourse = courseRepository.findByTitle("Cybersecurity Fundamentals").get(0);
				if (quizRepository.findByCourse(cyberCourse).isEmpty()) {
					Quiz quiz = new Quiz(cyberCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What is Cybersecurity?", quiz, null);
					Question q2 = new Question("Which of the following is a common cybersecurity threat?", quiz, null);
					Question q3 = new Question("What is the purpose of a firewall?", quiz, null);
					Question q4 = new Question("What does encryption do in cybersecurity?", quiz, null);
					Question q5 = new Question("Which practice is essential for maintaining cybersecurity?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("The practice of protecting systems, networks, and programs from digital attacks", true, q1),
							new Option("An outdated technology", false, q1),
							new Option("A type of hardware", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("Phishing", true, q2),
							new Option("Cloud computing", false, q2),
							new Option("Social media scams", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("To block unauthorized access", true, q3),
							new Option("To speed up network traffic", false, q3),
							new Option("To encrypt data", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("It converts data into a secure format", true, q4),
							new Option("It compresses data", false, q4),
							new Option("It deletes data", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("Regular software updates and strong password practices", true, q5),
							new Option("Using the same password across all platforms", false, q5),
							new Option("Disabling antivirus software for performance", false, q5)
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
// Building Trustworthy AI Enterprise Solutions Quiz Block
// -----------------------
			{
				Course trustCourse = courseRepository.findByTitle("Building Trustworthy AI Enterprise Solutions").get(0);
				if (quizRepository.findByCourse(trustCourse).isEmpty()) {
					Quiz quiz = new Quiz(trustCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What is the focus of Building Trustworthy AI Enterprise Solutions?", quiz, null);
					Question q2 = new Question("Why is transparency important in AI systems?", quiz, null);
					Question q3 = new Question("What role does data quality play in trustworthy AI?", quiz, null);
					Question q4 = new Question("Which factor is crucial for ethical AI?", quiz, null);
					Question q5 = new Question("What is a key benefit of implementing trustworthy AI solutions?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("Developing reliable, ethical, and transparent AI systems", true, q1),
							new Option("Maximizing profits without regulation", false, q1),
							new Option("Focusing solely on technology performance", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("It builds trust and accountability", true, q2),
							new Option("It makes the system slower", false, q2),
							new Option("It reduces security", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("High-quality data ensures accurate and unbiased results", true, q3),
							new Option("Data quality is irrelevant", false, q3),
							new Option("It only affects storage costs", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("Fairness in decision-making", true, q4),
							new Option("Increasing automation at all costs", false, q4),
							new Option("Ignoring potential biases", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("Enhanced user trust and regulatory compliance", true, q5),
							new Option("Increased system vulnerabilities", false, q5),
							new Option("Reduced system performance", false, q5)
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
			// Cloud Computing Fundamentals Quiz Block
			// -----------------------
			{
				Course cloudCourse = courseRepository.findByTitle("Cloud Computing Fundamentals").get(0);
				if (quizRepository.findByCourse(cloudCourse).isEmpty()) {
					Quiz quiz = new Quiz(cloudCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What is Cloud Computing?", quiz, null);
					Question q2 = new Question("What is one common benefit of cloud computing?", quiz, null);
					Question q3 = new Question("Which cloud service model provides virtualized computing resources?", quiz, null);
					Question q4 = new Question("What does scalability in cloud computing refer to?", quiz, null);
					Question q5 = new Question("Which deployment model combines on-premises and cloud-based resources?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("Delivery of computing services over the internet", true, q1),
							new Option("Local storage on personal devices", false, q1),
							new Option("Traditional on-premises computing", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("On-demand resource scalability", true, q2),
							new Option("Increased hardware maintenance", false, q2),
							new Option("Limited accessibility", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("Infrastructure as a Service (IaaS)", true, q3),
							new Option("Platform as a Service (PaaS)", false, q3),
							new Option("Software as a Service (SaaS)", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("The ability to increase or decrease resources as needed", true, q4),
							new Option("A fixed amount of resources", false, q4),
							new Option("A measure of data encryption strength", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("Hybrid Cloud", true, q5),
							new Option("Public Cloud", false, q5),
							new Option("Private Cloud", false, q5)
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
			// Professional Skills Quiz Block
			// -----------------------
			{
				Course profCourse = courseRepository.findByTitle("Professional Skills").get(0);
				if (quizRepository.findByCourse(profCourse).isEmpty()) {
					Quiz quiz = new Quiz(profCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What are Professional Skills?", quiz, null);
					Question q2 = new Question("Why are communication skills important in a professional setting?", quiz, null);
					Question q3 = new Question("Which skill is crucial for effective teamwork?", quiz, null);
					Question q4 = new Question("How can problem-solving skills benefit your career?", quiz, null);
					Question q5 = new Question("What is an example of a professional skill?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("A set of interpersonal and technical abilities for workplace success", true, q1),
							new Option("Only technical expertise", false, q1),
							new Option("Only theoretical knowledge", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("They facilitate clear understanding and collaboration", true, q2),
							new Option("They hinder workflow", false, q2),
							new Option("They are irrelevant in a professional setting", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("Active listening", true, q3),
							new Option("Ignoring feedback", false, q3),
							new Option("Overcomplicating tasks", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("It leads to efficient and innovative solutions", true, q4),
							new Option("It only increases stress", false, q4),
							new Option("It slows down progress", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("Time management", true, q5),
							new Option("Procrastination", false, q5),
							new Option("Disorganization", false, q5)
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
// Getting Started with Cybersecurity Quiz Block
// -----------------------
			{
				Course startCyberCourse = courseRepository.findByTitle("Getting Started with Cybersecurity").get(0);
				if (quizRepository.findByCourse(startCyberCourse).isEmpty()) {
					Quiz quiz = new Quiz(startCyberCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What is the primary goal of cybersecurity?", quiz, null);
					Question q2 = new Question("Which of the following is a common method used in cybersecurity attacks?", quiz, null);
					Question q3 = new Question("What is the purpose of antivirus software?", quiz, null);
					Question q4 = new Question("Why is it important to update software regularly in cybersecurity?", quiz, null);
					Question q5 = new Question("Which of these is a good practice for online security?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("To protect computers, networks, and data from attacks", true, q1),
							new Option("To promote unrestricted access to all information", false, q1),
							new Option("To create more viruses", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("Phishing", true, q2),
							new Option("Legitimate email communication", false, q2),
							new Option("Regular maintenance", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("To detect and remove malicious software", true, q3),
							new Option("To slow down the computer", false, q3),
							new Option("To store backup data", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("It fixes vulnerabilities and prevents attacks", true, q4),
							new Option("It is only for cosmetic updates", false, q4),
							new Option("It increases system risk", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("Using strong, unique passwords", true, q5),
							new Option("Sharing passwords openly", false, q5),
							new Option("Ignoring security updates", false, q5)
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
// Machine Learning for Data Science Projects Quiz Block
// -----------------------
			{
				Course mlCourse = courseRepository.findByTitle("Machine Learning for Data Science Projects").get(0);
				if (quizRepository.findByCourse(mlCourse).isEmpty()) {
					Quiz quiz = new Quiz(mlCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What is Machine Learning?", quiz, null);
					Question q2 = new Question("Which technique is commonly used in machine learning?", quiz, null);
					Question q3 = new Question("What is a key benefit of using machine learning in data science?", quiz, null);
					Question q4 = new Question("Which of the following is a machine learning algorithm?", quiz, null);
					Question q5 = new Question("What role does data play in machine learning?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("A subset of AI that enables systems to learn from data", true, q1),
							new Option("A method for manual data entry", false, q1),
							new Option("A type of hardware component", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("Supervised learning", true, q2),
							new Option("Unstructured data storage", false, q2),
							new Option("Simple arithmetic operations", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("It helps uncover patterns and insights from large datasets", true, q3),
							new Option("It eliminates the need for data", false, q3),
							new Option("It makes data irrelevant", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("Decision Trees", true, q4),
							new Option("Web Browsers", false, q4),
							new Option("Operating Systems", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("Data is essential for training and validating models", true, q5),
							new Option("Data is not needed in machine learning", false, q5),
							new Option("Data slows down the process", false, q5)
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
// Getting Started with Threat Intelligence and Hunting Quiz Block
// -----------------------
			{
				Course threatCourse = courseRepository.findByTitle("Getting Started with Threat Intelligence and Hunting").get(0);
				if (quizRepository.findByCourse(threatCourse).isEmpty()) {
					Quiz quiz = new Quiz(threatCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What is Threat Intelligence in cybersecurity?", quiz, null);
					Question q2 = new Question("How does threat hunting improve security?", quiz, null);
					Question q3 = new Question("Which tool is often used for threat analysis?", quiz, null);
					Question q4 = new Question("What is a key benefit of threat intelligence?", quiz, null);
					Question q5 = new Question("Why is proactive threat hunting important?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("It involves gathering information to identify potential threats", true, q1),
							new Option("It is a method for encrypting data", false, q1),
							new Option("It is used to speed up network connections", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("By identifying and mitigating threats before they cause harm", true, q2),
							new Option("By allowing all threats to pass through", false, q2),
							new Option("By slowing down response times", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("SIEM (Security Information and Event Management)", true, q3),
							new Option("Word Processor", false, q3),
							new Option("Spreadsheet Software", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("It enhances the ability to predict and mitigate cyber attacks", true, q4),
							new Option("It reduces the need for cybersecurity measures", false, q4),
							new Option("It makes data encryption unnecessary", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("It allows organizations to respond to emerging threats faster", true, q5),
							new Option("It increases system downtime", false, q5),
							new Option("It is only useful after an attack", false, q5)
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
// Journey to Cloud: Orchestrating Your Solution Quiz Block
// -----------------------
			{
				Course journeyCourse = courseRepository.findByTitle("Journey to Cloud: Orchestrating Your Solution").get(0);
				if (quizRepository.findByCourse(journeyCourse).isEmpty()) {
					Quiz quiz = new Quiz(journeyCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What is the main goal of Journey to Cloud?", quiz, null);
					Question q2 = new Question("What does 'orchestrating your solution' imply in cloud computing?", quiz, null);
					Question q3 = new Question("Which cloud deployment model supports hybrid solutions?", quiz, null);
					Question q4 = new Question("What is a key benefit of cloud orchestration?", quiz, null);
					Question q5 = new Question("How does cloud orchestration improve IT operations?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("It focuses on migrating and managing resources in the cloud", true, q1),
							new Option("It solely deals with on-premise solutions", false, q1),
							new Option("It is about building physical servers", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("It involves coordinating various cloud services into a cohesive system", true, q2),
							new Option("It refers to manual configuration of servers", false, q2),
							new Option("It is about data encryption", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("Hybrid Cloud", true, q3),
							new Option("Private Cloud", false, q3),
							new Option("Community Cloud", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("It streamlines resource management and increases efficiency", true, q4),
							new Option("It complicates IT operations", false, q4),
							new Option("It reduces security measures", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("By automating and simplifying resource allocation", true, q5),
							new Option("By increasing manual configurations", false, q5),
							new Option("By isolating all systems", false, q5)
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
// Getting Started with Data Quiz Block
// -----------------------
			{
				Course dataCourse = courseRepository.findByTitle("Getting Started with Data").get(0);
				if (quizRepository.findByCourse(dataCourse).isEmpty()) {
					Quiz quiz = new Quiz(dataCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What does 'Getting Started with Data' primarily cover?", quiz, null);
					Question q2 = new Question("Why is data important in modern businesses?", quiz, null);
					Question q3 = new Question("What is data collection?", quiz, null);
					Question q4 = new Question("Which tool is commonly used for basic data analysis?", quiz, null);
					Question q5 = new Question("What is one benefit of data-driven decision making?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("An introduction to handling and interpreting data", true, q1),
							new Option("A course on advanced networking", false, q1),
							new Option("A guide on software development", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("It drives informed decision-making and strategy", true, q2),
							new Option("It is only useful for IT professionals", false, q2),
							new Option("It is irrelevant in modern businesses", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("The process of gathering raw data for analysis", true, q3),
							new Option("A method for encrypting data", false, q3),
							new Option("A technique for designing user interfaces", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("Excel", true, q4),
							new Option("Adobe Photoshop", false, q4),
							new Option("Microsoft Word", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("More accurate and timely business insights", true, q5),
							new Option("Increased guesswork", false, q5),
							new Option("Delayed decision-making", false, q5)
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
// Fundamentals of Sustainability and Technology Quiz Block
// -----------------------
			{
				Course sustainCourse = courseRepository.findByTitle("Fundamentals of Sustainability and Technology").get(0);
				if (quizRepository.findByCourse(sustainCourse).isEmpty()) {
					Quiz quiz = new Quiz(sustainCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What is the focus of Fundamentals of Sustainability and Technology?", quiz, null);
					Question q2 = new Question("How does sustainability impact technology development?", quiz, null);
					Question q3 = new Question("What is one example of sustainable technology?", quiz, null);
					Question q4 = new Question("Why is sustainable technology important for the future?", quiz, null);
					Question q5 = new Question("How can technology promote sustainability?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("It integrates sustainable practices into tech solutions", true, q1),
							new Option("It focuses solely on profit maximization", false, q1),
							new Option("It disregards environmental concerns", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("It encourages the development of eco-friendly innovations", true, q2),
							new Option("It slows down technological advancement", false, q2),
							new Option("It only applies to renewable energy", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("Solar panels", true, q3),
							new Option("High-consumption data centers", false, q3),
							new Option("Fossil fuel-based generators", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("It ensures long-term viability and environmental health", true, q4),
							new Option("It limits innovation", false, q4),
							new Option("It has no impact on future technology", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("By integrating renewable energy solutions", true, q5),
							new Option("By increasing resource consumption", false, q5),
							new Option("By ignoring environmental data", false, q5)
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
// Enterprise Security in Practice Quiz Block
// -----------------------
			{
				Course entSecurityCourse = courseRepository.findByTitle("Enterprise Security in Practice").get(0);
				if (quizRepository.findByCourse(entSecurityCourse).isEmpty()) {
					Quiz quiz = new Quiz(entSecurityCourse, null);
					quiz = quizRepository.save(quiz);

					Question q1 = new Question("What does Enterprise Security in Practice focus on?", quiz, null);
					Question q2 = new Question("Which is a common challenge in enterprise security?", quiz, null);
					Question q3 = new Question("What role does risk management play in enterprise security?", quiz, null);
					Question q4 = new Question("Which of the following is a best practice in enterprise security?", quiz, null);
					Question q5 = new Question("How can organizations improve their enterprise security?", quiz, null);
					q1 = questionRepository.save(q1);
					q2 = questionRepository.save(q2);
					q3 = questionRepository.save(q3);
					q4 = questionRepository.save(q4);
					q5 = questionRepository.save(q5);

					List<Option> optionsQ1 = Arrays.asList(
							new Option("It emphasizes practical approaches to protecting organizational assets", true, q1),
							new Option("It is only theoretical", false, q1),
							new Option("It ignores regulatory requirements", false, q1)
					);

					List<Option> optionsQ2 = Arrays.asList(
							new Option("Integration of legacy systems", true, q2),
							new Option("Unlimited budget", false, q2),
							new Option("Overabundance of resources", false, q2)
					);

					List<Option> optionsQ3 = Arrays.asList(
							new Option("It identifies and mitigates potential security risks", true, q3),
							new Option("It eliminates all threats", false, q3),
							new Option("It focuses only on hardware", false, q3)
					);

					List<Option> optionsQ4 = Arrays.asList(
							new Option("Implementing multi-factor authentication", true, q4),
							new Option("Using default passwords", false, q4),
							new Option("Neglecting software updates", false, q4)
					);

					List<Option> optionsQ5 = Arrays.asList(
							new Option("By continuously updating security protocols and training staff", true, q5),
							new Option("By ignoring emerging threats", false, q5),
							new Option("By reducing security measures", false, q5)
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





