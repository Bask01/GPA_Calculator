package ca.sheridancollege.bask.as2.beans;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

/**
 * Evaluation models an evaluation (test, assignment, quiz, etc) in a course
 * or class that is graded.
 * Updated June 2021 - Assignment 2 Version
 *
 * @author Wendi Jollymore 
 */
@Data
@NoArgsConstructor
public class Evaluation implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String title = "TBD";
	private String course;
	private double grade;
	private double max;
	private double weight = 10;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dueDate;
	
	/**
	 * Constructs a new Evaluation with a specific title, course, grade, max,
	 * weight, and due date.
	 * 
	 * @param title this evaluation't title
	 * @param course this evaluation's course code
	 * @param grade this evaluation's actual grade
	 * @param max what this evaluation is out of
	 * @param weight the weight of this evaluation
	 * @param dueDate this evaluation's due date
	 */
	public Evaluation(String title, String course, double grade, double max, 
			double weight, LocalDate dueDate) {
		setTitle(title);
		setCourse(course);
		setGrade(grade);
		setMax(max);
		setWeight(weight);
		setDueDate(dueDate);
	}
	
	/**
	 * Constructs an evaluation with a specific id, title, course, grade, max, 
	 * weight, and due date.
	 * 
	 * @param id this evaluation's unique ID
	 * @param title this evaluation't title
	 * @param course this evaluation's course code
	 * @param grade this evaluation's actual grade
	 * @param max what this evaluation is out of
	 * @param weight the weight of this evaluation
	 * @param dueDate this evaluation's due date
	 */
	public Evaluation(int id, String title, String course, double grade, 
			double max, double weight, LocalDate dueDate) {
		this(title, course, grade, max, weight, dueDate);
		setId(id);		
	}
	
	/**
	 * Ensure a valid title goes into this evaluation's title member.  Title
	 * can't be null or empty.
	 * 
	 * @param title the specified title
	 */
	public void setTitle(String title) {
		
		// title can't be null or empty
		if (title == null || title.trim().isEmpty())
			throw new IllegalArgumentException("Evaluation title can't be empty.");
		else
			this.title = title;
	}
	
	/**
	 * Ensure this evaluation's grade receives a valid value.  Grade can't
	 * be less than 0.  A 0 grade could mean the evaluation is not yet
	 * graded.
	 * 
	 * @param grade the specified actual grade
	 */
	public void setGrade(double grade) {
		
		// make sure grade is 0 or more
		if (grade >= 0)
			this.grade = grade;
		else
			throw new IllegalArgumentException("Grade must be greater than 0.");
	}
	
	/**
	 * Ensures a valid value is placed into this evaluation's maximum grade.
	 * Maximum grade must be 0 to 100.  A 0 maximum is possible for check-off
	 * evaluations.
	 * 
	 * @param max the specified maximum grade
	 */
	public void setMax(double max) {
		
		// ensure max is valid
		if (max >= 0 && max <= 100)
			this.max = max;
		else
			throw new IllegalArgumentException("Maximum grade must be between 0 "
					+ "and 100.");
	}
	
	/**
	 * Ensure's this evaluation's weight is assigned a valid value. Weight must
	 * be more than 0 but no more than 100.
	 * 
	 * @param weight the specified weight for this evaluation
	 */
	public void setWeight(double weight) {
		
		// make sure weight is valid
		if (weight > 0 && weight <= 100)
			this.weight = weight;
		else 
			throw new IllegalArgumentException("Evaluation Weight must be between"
					+ " 0 and 100.");
	}
	
	/**
	 * Determines the amount of weight earned for this evaluation item.
	 * 
	 * @return the weight of this item as grade / max * weight
	 */
	public double calcGrade() {
		
		// standard formula for earned weight
		return grade / max * weight;
	}
	
	/**
	 * Retrieves this Evaluation as a String, which includes the title,
	 * grade, max grade, and the earned weight.
	 * 
	 * @return this Evaluation as a String
	 */
	public String toString() {
		return String.format("%s: %.1f/%.1f%nEarned %.1f%%", title,
				grade, max, calcGrade());
	}
}
