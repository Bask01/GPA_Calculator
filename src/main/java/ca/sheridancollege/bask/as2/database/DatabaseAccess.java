package ca.sheridancollege.bask.as2.database;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.bask.as2.beans.Course;
import ca.sheridancollege.bask.as2.beans.Evaluation;

/**
 * Models a Database access class.
 * Calls the database, gets the course list from the courses table,
 * inserts into evaluations table and gets a list from evaluations table
 * also allows to update the evaluations table
 * 
 * @author Kubra Bas
 *
 */

@Repository
public class DatabaseAccess {

	@Autowired
    protected NamedParameterJdbcTemplate jdbc;  // the jdbc template connected to the db
		
	/**
	 * Method for calling course list
	 */
	public List<Course> getCourses(){
		
		//grab the course list from the courses table, sort by title and code
		String sql = "SELECT * FROM courses ORDER BY title, code;";
	
	//execute the query string, store it in an ArrayList named courses
	ArrayList<Course> courses =(ArrayList<Course>)jdbc.query(sql, 
			new BeanPropertyRowMapper<Course>(Course.class)); //we're giving all the information of the Course class
	// to the BeanPropertyRowMApper.
	
	//return the list
	return courses;

    }
	
	
	/**
	 * Method to add an Evaluation record to the evaluation table
	 * @param eval receives an Evaluation object
	 */
	public int addEval(Evaluation eval) {
		
		//query to insert the Evaluation object into the evaluations table 
		String sql = "INSERT INTO evaluations (title, course, grade, max, weight, duedate)"
				+ "VALUES (:title, :course, :grade, :max, :weight, :duedate);";
		
		//transfer the Evaluation object values into the related columns of the evaluations table
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("title", eval.getTitle()).addValue("course", eval.getCourse())
			  .addValue("grade", eval.getGrade()).addValue("max", eval.getMax())
			  .addValue("weight", eval.getWeight()).addValue("duedate", java.sql.Date.valueOf(eval.getDueDate()));
		
		//update the database
		return jdbc.update(sql, params);
	}
	
	/**
	 * Method to get the content of the evaluations table
	 * 
	 */
	public List<Evaluation> getEvals() {
		
		//query to grab all the content of the evaluations table, sorted by due date and course code	
		String sql = "SELECT * FROM evaluations ORDER BY duedate, course;";
	
		//execute query, store the table values in an Evaluation type ArrayList
		ArrayList<Evaluation> evals = (ArrayList<Evaluation>) jdbc.query(sql, 
			new BeanPropertyRowMapper<Evaluation>(Evaluation.class));
		
		// return a list of Evaluation objects 
		return evals;
	}
	
	/**
	 * This method is called when an evaluation object's wanted to be edited 
	 * @param id, id of the evaluation object that wanted to be edited
	 * @return: returns the object if id is found in the table
	 */
	public Evaluation getEvaluation(int id) {
		
		//query to grab a row if 'id' is found in the table 
		String sql = "SELECT * FROM evaluations WHERE id=:id;";
		
		//find ':id' value in the parameter 'id'		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		//store the found row in an ArrayList named eval
		ArrayList<Evaluation> eval = (ArrayList<Evaluation>) jdbc.query(sql, 
				params, new BeanPropertyRowMapper<Evaluation>(Evaluation.class));
  		//if size of the ArrayList grater than 0 return the object
		if(eval.size() > 0)
			return eval.get(0);
		else //if size of the ArrayList lower than 0 return null
			return null;
	}
	
	/**
	 * Method to update a certain row in the evaluations table 
	 * this method is call when user edits an existing record of evaluations table
	 * @param eval: this is the record wanted to be edited 
	 * @return : updates the database
	 */
	public int updateEval(Evaluation eval) {
		//query to update the row where id s match in the evaluations table 
		String sql ="UPDATE evaluations SET title=:title, "
				+ "course=:course, grade=:grade, "
				+ "max=:max, weight=:weight, "
				+ "duedate=:duedate WHERE id=:id;";	
		
		//transfer the Evaluation object values into the related columns of the evaluations table
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", eval.getId())
			  .addValue("title", eval.getTitle())
			  .addValue("course", eval.getCourse())
			  .addValue("grade", eval.getGrade())
			  .addValue("max", eval.getMax())
			  .addValue("weight", eval.getWeight())
			  .addValue("duedate", java.sql.Date.valueOf(eval.getDueDate()));
		//update the database
		return jdbc.update(sql, params);
	}	
}
