///////////////////////////////////////////////////////////////////////////////
// Title:            RegistrationApp
// Files:            RegistrationApp.java, EnrollmentDB.java
// Semester:         Fall 2011
//
// Author:           Peter Collins pmcollins2@wisc.edu
// CS Login:         pcollins
// Lecturer's Name:  Beck, Hasti
// Lab Section:      NA
//
///////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.util.*;

/**
 * An application meant for managing courses enrollment by students from the 
 * given inputed file.
 *
 * <p>Bugs: none known
 *
 * @author Peter Collins
 */
public class RegistrationApp {
	/** A private instance of our enrollment database which stays throughout 
	 * the scope of the application. */
	private static EnrollmentDB enrollmentDB;

	/**
	  * Main method of the registration application.  Controls the, reading of
	  * the input file, add new entries to the database, and the managing
	  * of the database via keyboard inputs.
	  * 
	  * @param args command line arguments, should only have one the input file
	  *  roster of each students schedule.
	  */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java RegistrationApp FileName");
			System.exit(1);
		}

		enrollmentDB = new EnrollmentDB();

		try {
			File file = new File(args[0]);  // The file object representation 
											// of the file being read
			if (!file.exists()) {
				System.err.println("Error: Cannot access input file");
				System.exit(2);
			}

			Scanner fileIn = new Scanner(file);  // The scanner to the input file

			String line;
			String[] tokens;
			while (fileIn.hasNext()) {
				line = fileIn.nextLine();
				tokens = line.split(",");
				for (int i = 1; i < tokens.length; i++) {
					enrollmentDB.addCourse(tokens[i]);
					enrollmentDB.addStudent(tokens[0], tokens[i]);
				}
			}

			fileIn.close();

		} catch (FileNotFoundException e) {
			// Shouldn't ever reach here, but still handle it.
			System.err.println("Error: Cannot access input file");
			System.exit(2);
		} catch (IOException e) {
			System.err.println("Error: An io exception occured reading the input file");
			e.printStackTrace();
			System.exit(2);
		}

		Scanner stdin = new Scanner(System.in); // for reading console input
		boolean done = false;
		while (!done) {
			System.out.print("Enter option ( cdprswx ): ");
			String input = stdin.nextLine();

			// only do something if the user enters at least one character
			if (input.length() > 0) {
				char choice = input.charAt(0); // strip off option character
				String remainder = ""; // used to hold the remainder of input
				if (input.length() > 1) {
					// trim off any leading or trailing spaces
					remainder = input.substring(1).trim();
				}

				switch (choice) {
					case 'c':
						if (enrollmentDB.cancelCourse(remainder)) {
							System.out.println("course canceled");
						} else {
							System.out.println("course not found");
						}
						break;
	
					case 'd':
						// First get number of unique students
						
						// Will contains the unique names of students 
						List<String> uniqueStudents = new ArrayList<String>();
						
						Iterator<Course> courseIter = enrollmentDB.iterator();
						Course course;
						Iterator<String> rosterIter;
						String student;
						while (courseIter.hasNext()) {
							course = (Course) courseIter.next();
							rosterIter = enrollmentDB.getRoster(
								course.getTitle()).iterator();
							while (rosterIter.hasNext()) {
								student = (String) rosterIter.next();
								if (!uniqueStudents.contains(student)) {
									uniqueStudents.add(student);
								}
							}
						}
	
						System.out.println("Courses: " + enrollmentDB.size()
							+ ", Students: " + uniqueStudents.size());
	
						// Find the most and least number of students per courses,
						// and add all the roster sizes to find the averages
						// Also look for most popular and least popular courses as
						// well
						
						// Most and least amount of students per course
						int mostStudents = 0,
						leastStudents = Integer.MAX_VALUE;
						// Total number of non-unique enrolled students in all courses 
						float totalEnrolledStudents = 0;
						
						List<String> leastPopular = new ArrayList<String>(),
						mostPopular = new ArrayList<String>();
						courseIter = enrollmentDB.iterator();
						int rosterSize;
						while (courseIter.hasNext()) {
							course = (Course) courseIter.next();
							rosterSize = enrollmentDB.getRoster(
								course.getTitle()).size();
							
							// Append to the list of most popular courses if the
							// courses sizes are the same, if there course that
							// is larger, clear the list and append that course
							if (mostStudents == rosterSize) {
								mostPopular.add(course.getTitle());
							} else if (mostStudents < rosterSize) {
								mostPopular.clear();
								mostPopular.add(course.getTitle());
								mostStudents = rosterSize;
							}	

							// Append to the list of least popular courses if the
							// courses sizes are the same, if there course that
							// is smaller, clear the list and append that course
							if (leastStudents == rosterSize) {
								leastPopular.add(course.getTitle());
							} else if (leastStudents > rosterSize) {
								leastPopular.clear();
								leastPopular.add(course.getTitle());
								leastStudents = rosterSize;
							}
	
							// Add the total number of enrolled students in all
							// the courses
							totalEnrolledStudents += rosterSize;
						}
						
						// Calculated average students per course
						int averageStudentsPerCourse = Math.round(
							totalEnrolledStudents / (float) enrollmentDB.size());
	
						System.out.println("# of students/course: most "
							+ mostStudents
							+ ", least "
							+ leastStudents
							+ ", average "
							+ averageStudentsPerCourse);
	
						// Find the most and least number of courses per student,
						// and add all the student sizes to find the averages
						
						// Most and least amount of courses per student
						int mostCourses = 0,
						leastCourses = Integer.MAX_VALUE;
						// Total number of courses taken
						float totalCoursesTaken = 0;
	
						Iterator<String> uniqueStudentIter = uniqueStudents
								.iterator();
						int numberOfCourses;
						while (uniqueStudentIter.hasNext()) {
							student = (String) uniqueStudentIter.next();
							numberOfCourses = enrollmentDB.getCourses(student)
									.size();
							// Find the maximum number of courses for a student
							mostCourses = Math.max(mostCourses, numberOfCourses);
							// Find the minimum number of courses for a student
							leastCourses = Math.min(leastCourses, numberOfCourses);
							// Add the total number of courses taken for all students							
							totalCoursesTaken += numberOfCourses;
						}
	
						// Calculated average courses per student
						int averageCoursesPerStudent = Math.round(
							totalCoursesTaken / (float) uniqueStudents.size());
						
						System.out.println("# of courses/student: most "
							+ mostCourses
							+ ", least "
							+ leastCourses
							+ ", average "
							+ averageCoursesPerStudent);
	
						// Already calculated
						System.out.println("Most Popular: "
							+ join(mostPopular.iterator(), ",") + " ["
							+ mostStudents + "]");	
						System.out.println("Least Popular: "
							+ join(leastPopular.iterator(), ",") + " ["
							+ leastStudents + "]");
	
						break;
	
					case 'p':
						// Courses the database contains
						List<String> courses = enrollmentDB.getCourses(remainder);
						if (courses == null) {
							System.out.println("student not found");
						} else {
							System.out.println(join(courses.iterator(), ","));
						}
						break;
	
					case 'r':
						// Course roster of a specific student
						List<String> roster = enrollmentDB.getRoster(remainder);
						if (roster == null) {
							System.out.println("course not found");
						} else {
							System.out.println(join(roster.iterator(), ","));
						}
						break;
	
					case 's':
						// The following code reads in a comma-separated sequence
						// of strings. If there are exactly two strings in the
						// sequence, the strings are assigned to name1 and name2.
						// Otherwise, an error message is printed.
						String[] tokens = remainder.split("[,]+");
						if (tokens.length != 2) {
							System.out.println("need to provide exactly two names");
						} else {
							String name1 = tokens[0].trim();
							String name2 = tokens[1].trim();
							
							// The result of all of the combined courses
							List<String> combinedCourses = new ArrayList<String>();
							// The course roster of name 1
							List<String> name1Courses = enrollmentDB
									.getCourses(name1);
							// The course roster of name 2
							List<String> name2Courses = enrollmentDB
									.getCourses(name2);
							
							Iterator<String> iter;
							String courseTitle;
							
							// Check if names even existed in the database, then
							// iterate through one's courses and find matches
							// with two's courses
							if (name1Courses != null && name2Courses != null) {
								iter = name1Courses.iterator();
	
								while (iter.hasNext()) {
									courseTitle = (String) iter.next();
									if (name2Courses.contains(courseTitle)) {
										combinedCourses.add(courseTitle);
									}
								}
							}
	
							if (combinedCourses.size() == 0) {
								System.out.println("none");
							} else {
								System.out.println(join(combinedCourses.iterator(),
									","));
							}
						}
						break;
	
					case 'w':						
						if (enrollmentDB.withdrawStudent(remainder)) {
							System.out.println(remainder
								+ " withdrawn from all courses");
						} else {
							System.out.println("student not found");
						}
						break;
	
					case 'x':
						done = true;
						System.out.println("exit");
						break;
	
					default: // ignore any unknown commands
						break;

				} // end switch
			} // end if
		} // end while
	} // end main

	/**
	  * Joins together an iterator of strings into a single string,
	  * separated by the glue parameter.
	  *
	  * @param iter is the iterator of strings to combine
	  * @param glue is the string to be inserted between each element
	  *  of the iterator
	  * @return A combined string of the whole iterator
	  */
	private static String join(Iterator<String> iter, String glue) {
		String returnString = ""; // The end resultant string of combined values

		// Start with the first element in the return string
		if (iter.hasNext()) {
			returnString = iter.next();
		}

		// Append glue and the next element to the return string for the rest
		// of the elements
		String token;
		while (iter.hasNext()) {
			token = (String) iter.next();
			returnString += glue + token;
		}

		return returnString;
	}
}