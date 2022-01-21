package ca.sheridancollege.bask.as2.beans;

import java.io.Serializable;
import java.util.Comparator;
 
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * Models a course with a specific course code, title, credit
 * value and Grade.  A course can exist with a default {@link Grade}
 * (default grade is term #0 and a 0 grade, assuming the
 * course has not been completed).  Once a course has been
 * completed, it is typically assigned a term number when
 * it was completed and a final grade.
 * Updated June 2021 - Assignment 2/3 Version
 * 
 * @author Wendi Jollymore
 *
 */
@NoArgsConstructor
@Data
public class Course implements Serializable, Comparable<Course> {
 
    private static final long serialVersionUID = 1L;
 
    // note: object for numerics used to be more compatible with db fields
    private String code;
    private String title;
    private Double credits;
    private boolean complete;
    private Integer term = 1;   
    private Double finalGrade;
 
    /**
     * Constructs a Course object with a specific code, title, and credits,
     * and a default grade of 0.
     * 
     * @param code this course's course code
     * @param title this course's title
     * @param credits this course's credit value
     */
    public Course(String code, String title, double credits) {
        
        // only valid values are permitted
        setCode(code);
        setTitle(title);
        setCredits(credits);
        complete = false;
    }
 
    /**
     * Assigns a valid course code to this course object.
     * Valid course codes must have 4 letters followed by 5 digits,
     * otherwise an unchecked exception is thrown.
     * 
     * @param code the programmer-specified course code
     */
    public void setCode(String code) {
        
        // match ABCD12345
        if (code.matches("[a-zA-Z]{4}\\d{5}"))
            
            // converts the letters to ucase
            this.code = code.toUpperCase();
        else
        	this.code = code.toLowerCase();
//            throw new IllegalArgumentException("Course code must be in the " 
//                    + "form AAAA12345.");
    }
 
    /**
     * Assigns a valid credit value to this course.  Credit value must be
     * more than 0 and less than 10, otherwise an unchecked exception is thrown.
     * 
     * @param credits the programmer-specified credit value
     */
    public void setCredits(double credits) {
        if (credits > 0 && credits < 10)
            this.credits = credits;
        else
            throw new IllegalArgumentException("Credit value must be more than "
                    + "0 and less than 10.");
    }   
    
    /**
     * Retrieves the grade points for this finalGrade.  Grade points
     * are used in calculations, like the calculations for
     * grade point averages.
     * 
     * @return the points earned for this course grade
     */
    public double gradePoints() {       
                
        // Sheridan College grading system 
        //https://myotr.sheridancollege.ca/grading_systems.html
        if (finalGrade >= 90)
            return 4.0;
        else if (finalGrade >= 85)
            return 3.8;
        else if (finalGrade >= 80)
            return 3.6;
        else if (finalGrade >= 75)
            return 3.3;
        else if (finalGrade >= 70)
            return 3.0;
        else if (finalGrade >= 65)
            return 2.5;
        else if (finalGrade >= 60)
            return 2.0;
        else if (finalGrade >= 55)
            return 1.2;
        else if (finalGrade >= 50)
            return 1.0;
        else
            return 0.0;
    }
    
    /**
     * A Comparator that will allow you to sort a List of Course objects by
     * the grade's term e.g. Collections.sort(aCourseList, Course.compareByTerm)
     * - note that compareByTerm is defined as static, so to use it you
     * just say Course.compareByTerm.
     * - note that Collections.sort(list, comparator) will sort the list and not
     * a copy of the list (in other words, it modifies the order of list)
     * Used for some bonus options and A3
     * 
     * To learn more about Comparator and Comparable, see the following:
     * https://www.baeldung.com/java-comparator-comparable
     * http://www-acad.sheridanc.on.ca/~jollymor/prog24178/oop7.html     * 
     * 
     */
    public static Comparator<Course> compareByTerm = (Course c1, Course c2) ->
        Integer.compare(c1.getTerm(),  c2.getTerm());
        
    //  This is what it looks like without the lambda expression:
    /*public static Comparator<Course> compareByTerm = new Comparator<Course>() {
        
        // compares two courses by the term # inside the grade object
        public int compare(Course c1, Course c2) {
            return Integer.compare(c1.getGrade().getTerm(), c2.getGrade().getTerm());
        }
    };*/
 
    /**
     * Allows you to sort a list of courses by the course code, e.g.
     * Collections.sort(aCourseList) will sort aCourseList by the course
     * code of each Course object in the list. Note that this modifies
     * the order of aCourseList.
     * Used for some bonus options and A3
     * 
     * @param c the Course to compare this Course to
     * @return see String.compareTo() in the Java API docs
     * 
     */
    @Override
    public int compareTo(Course c) {    
        
        // course objects are compared by course code value
        return this.code.compareTo(c.getCode());
    }
 
}
 