///////////////////////////////////////////////////////////////////////////////
// Main Class File:  RegistrationApp.java
// File:             EnrollmentDB.java
// Semester:         Fall 2011
//
// Author:           Peter Collins pmcollins2@wisc.edu
// CS Login:         pcollins
// Lecturer's Name:  Beck Hasti
// Lab Section:      NA
//
///////////////////////////////////////////////////////////////////////////////

import java.util.*;

/**
 * A database that can manage and manipulate a list of courses.
 *
 * <p>Bugs: none known
 *
 * @author Peter Collins
 */
public class EnrollmentDB {
	/** A private List containing the course objects represented */
	private List<Course> courses;

    /**
     * Constructs an empty enrollment database.
     */
	public EnrollmentDB() {
		courses = new ArrayList<Course>();

	}

	/** 
	 * Add a course with the given title t to the end of the database.
	 * If a course with the title t is already in the database, it returns.
	 *  
	 * @param t is the title of the course to add 
	 */
	public void addCourse(String t) {
		if (t == null) {
			throw new IllegalArgumentException("Course title was null!");
		}

		Iterator<Course> courseIter = courses.iterator();
		Course course;
		while (courseIter.hasNext()) {
			course = (Course) courseIter.next();
			if (course.getTitle().equals(t)) {
				return;
			}
		}
		courses.add(new Course(t));
	}

	/**
	  * Add the student with given name n to the course with the given title t in
	  * the database. If a course with the title t is not in the database it throws a
	  * java.lang.IllegalArgumentException. If n is already in the list of
	  * students enrolled in the course with title t, it returns.
	  *
	  * @param n is the name of the student to add to the specified course
	  * @param t is the title of the course to add the student to
	  */
	public void addStudent(String n, String t) {
		if (n == null || t == null) {
			throw new IllegalArgumentException(
					"Student name or course title was null!");
		}

		Iterator<Course> courseIter = courses.iterator();
		Course course;
		while (courseIter.hasNext()) {
			course = (Course) courseIter.next();
			if (course.getTitle().equals(t)) {
				if (course.getRoster().contains(n)) {
					return;
				}
				course.getRoster().add(n);
				return;
			}
		}
		throw new IllegalArgumentException("The specified course " + n
			+ " was not in the enrollment database.");
	}

	/**
	  * Removes the course with the title t from the database. If a course with
	  * the title t is not in the database, it returns false, otherwise (i.e.,
	  * removal is successful) it returns true.
	  * 
	  * @param t is the title of the course to cancel
	  * @return if the course was removed returns true, false if otherwise
	  */

	public boolean cancelCourse(String t) {
		if (t == null) {
			throw new IllegalArgumentException("Course title was null!");
		}

		Iterator<Course> courseIter = courses.iterator();
		int index = 0; // Use an index for quick removal from the list
		Course course;
		while (courseIter.hasNext()) {
			course = (Course) courseIter.next();
			if (course.getTitle().equals(t)) {
				courses.remove(index);
				return true;
			}
			index++;
		}
		return false;
	}

	/**
	 * Returns true if a course with the title t is in the database.
	 * 
	 * @param t is the title of course to check if it is in the database.
	 * @return true if it's in the database, false if otherwise
	 */
	public boolean containsCourse(String t) {
		if (t == null) {
			throw new IllegalArgumentException("Course title was null!");
		}

		Iterator<Course> courseIter = courses.iterator();
		Course course;
		while (courseIter.hasNext()) {
			course = (Course) courseIter.next();
			if (course.getTitle().equals(t)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if a student with the name n appears in the roster for at
	 * least one course in the database.
	 * 
	 * @param n is the name of the student to check if it is in the database
	 * @return true if it's in the database for at least one course, false if
	 *  otherwise
	 */
	public boolean containsStudent(String n) {
		if (n == null) {
			throw new IllegalArgumentException("Student name was null!");
		}

		Iterator<Course> courseIter = courses.iterator();
		Course course;
		while (courseIter.hasNext()) {
			course = (Course) courseIter.next();
			if (course.getRoster().contains(n)) {
				return true;
			}
		}
		return false;
	}

	/** 
	 * Returns true if the given student n is enrolled in the course with the
	 * given title t. If a course with the title t is not in the database,
	 * it returns false.
	 * 
	 * @param n is the name of the student to check if they're enrolled
	 * @param t is title of the course to check if the student is in
	 * @return true if student is enrolled in the course, false if otherwise or if the course
	 *  isn't in the database.
	 */ 
	public boolean isEnrolled(String n, String t) {
		if (n == null || t == null) {
			throw new IllegalArgumentException(
				"Student name or course title was null!");
		}

		Iterator<Course> courseIter = courses.iterator();
		Course course;
		while (courseIter.hasNext()) {
			course = (Course) courseIter.next();
			if (course.getTitle() == t) {
				if (course.getRoster().contains(n)) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

	/** 
	 * Returns the roster (list of students) for the course with the given title
	 * t. If a course with the title t is not in the database, it returns null.
	 * 
	 * @param t is the title of the course to get the roster of students for
	 * @return a list of Strings with the names of students in the specified course,
	 * otherwise if the course doesn't exist, it returns null 
	 */
	public List<String> getRoster(String t) {
		if (t == null) {
			throw new IllegalArgumentException("Course title was null!");
		}

		Iterator<Course> courseIter = courses.iterator();
		Course course;
		while (courseIter.hasNext()) {
			course = (Course) courseIter.next();
			if (course.getTitle().equals(t)) {
				return course.getRoster();
			}
		}
		return null;
	}

	/**
	 * Returns the list of courses in which the student with the given name n is
	 * enrolled. If a student with the name n is not in the database, it returns
	 * null. 
	 * 
	 * @param n is the name of the student
	 * @return is a list of Strings of the names of the courses the specified
	 * student is enrolled in, otherwise if the student doesn't exist it returns
	 * null
	 */
	public List<String> getCourses(String n) {
		if (n == null) {
			throw new IllegalArgumentException("Student name was null!");
		}

		
		List<String> returnCourses = new ArrayList<String>(); // The resultant list of 
															  // courses to return
		
		Iterator<Course> courseIter = courses.iterator();
		Course course;
		while (courseIter.hasNext()) {
			course = (Course) courseIter.next();
			if (course.getRoster().contains(n)) {
				returnCourses.add(course.getTitle());
			}
		}
		
		// If there was nothing in the list return null, not an empty list
		if (returnCourses.size() == 0) {
			return null;
		} else {
			return returnCourses;
		}
	}

	/**
	 * Returns an Iterator over the Course objects in the database. The courses
	 * are returned in the order they were added to the database
	 * (resulting from the order in which they are in the text file).
	 * 
	 * @return an over the courses objects stored in the database
	 */
	public Iterator<Course> iterator() {
		return courses.iterator();
	}

	/** 
	 * Returns the number of unique courses in this database.
	 * 
	 * @return the number of courses this database contains  
	 */
	public int size() {
		return courses.size();
	}

	/**
	 * Removes the student with the given name n from the database (i.e., remove
	 * the student from every course in which they are enrolled). If a student
	 * with the name n is not in the database, it returns false, otherwise (i.e.,
	 * the removal is successful) it returns true. 
	 * 
	 * @param n is the name of the student to withdraw from all courses
	 * @return true if the withdrawal was successful, otherwise false if the
	 * student didn't exist in the database. 
	 */
	public boolean withdrawStudent(String n) {
		if (n == null) {
			throw new IllegalArgumentException("Student name was null!");
		}

		boolean withdrawn = false; // Whether the student was withdrawn in the end
		
		Iterator<Course> courseIter = courses.iterator();
		Course course;
		List<String> roster;		
		while (courseIter.hasNext()) {
			course = (Course) courseIter.next();
			roster = course.getRoster();
			if (roster.remove(n)) {
				withdrawn = true;
			}
		}
		return withdrawn;
	}
}
