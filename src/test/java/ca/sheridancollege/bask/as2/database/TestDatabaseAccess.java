package ca.sheridancollege.bask.as2.database;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import ca.sheridancollege.bask.as2.beans.Evaluation;

/**
 * Test class for Database class
 * @author Kubra Bas
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class TestDatabaseAccess {

	@Autowired
	private DatabaseAccess da; //inject the database from IOCC
	
	
	/**
	 * method to test getCourses Pass method 
	 * @throws Exception
	 */
	@Test
	public void testGetCoursesPass() {
		
		Assert.assertTrue(da.getCourses().size() > 0);
	}
	

	/**
	 * method to test addEval Pass method 
	 * @throws Exception
	 */
	@Test
	public void testAddEvalPass() {
		//Create a new Evaluation object
		Evaluation eval = new Evaluation("Assignment1", "PROG10082",
				12, 15, 6.0, LocalDate.parse("2021-07-27"));
		
		int origNumRecs = da.getEvals().size();//check the size of the evaluations table
		da.addEval(eval); //add the the new object to the evaluations table in the database
		
		int newNumRecs = da.getEvals().size();//check again the size of the evaluations table
		Assert.assertTrue(origNumRecs + 1 == newNumRecs); //compare if the new size is larger 1 than original size
	}
	
	/**
	 * method to test addEval fail method 
	 * @throws Exception
	 */
	@Test(expected=DataIntegrityViolationException.class)
	public void testAddEvalFail() {
		//Create a new Evaluation object with an invalid course code
		Evaluation eval = new Evaluation("Assignment1", "PROG100823",
				12, 15, 6.0, LocalDate.parse("2021-07-27"));
		
		int origNumRecs = da.getEvals().size(); //check the size of the evaluations table
	
		da.addEval(eval); //try to add the the new object into the table

		int newNumRecs = da.getEvals().size();//check again the size of the evaluations table		
		Assert.assertTrue(origNumRecs == newNumRecs);//compare if the two sizes are equal 	
	}
	
	/**
	 * method to test getEvals Pass method 
	 * @throws Exception
	 */
	@Test 
	public void testGetEvalsPass() {
		
		List<Evaluation> evaluation = da.getEvals();
		
		Assert.assertTrue(evaluation.size() > 0);//check if the size of the  evaluations table is greater than 0
	}
	
	
	/**
	 * method to test getEvaluation Pass method 
	 * @throws Exception
	 */	
	@Test
	public void testGetEvaluationPass() {
		
		Evaluation eval = new Evaluation("Assignment2", "PROG10082",
				12, 15, 6.0, LocalDate.parse("2021-07-28"));
		eval.setId(1);
		da.addEval(eval);		
		Assert.assertNotNull(da.getEvaluation(1));
		
	}
	
	/**
	 * method to test getEvaluation Fail method 
	 * @throws Exception
	 */	
	@Test
	public void testGetEvaluationFail() {
		
		Evaluation eval = new Evaluation("Assignment2", "PROG10082",
				12, 15, 6.0, LocalDate.parse("2021-07-28"));
		eval.setId(1);
		da.addEval(eval);		
		Assert.assertNull(da.getEvaluation(88));		
	}
	
	/**
	 * method to test updateEval Pass method 
	 * @throws Exception
	 */		
	@Test
	public void testUpdateEvalPass() {
		
		Evaluation origEvalRec = new Evaluation("Assignment3", "SYST10199",
			13, 25, 5.0, LocalDate.parse("2021-08-01"));
		
		//set the id to 1, this will be the first record,
		//so it will match with database id number
		origEvalRec.setId(1); 
		da.addEval(origEvalRec);
		
		origEvalRec.setTitle("Assignment2");
		origEvalRec.setCourse("PROG24178");
		origEvalRec.setGrade(15);
		origEvalRec.setMax(20);
		origEvalRec.setWeight(6);
		origEvalRec.setDueDate(LocalDate.parse("2021-08-02"));
		//test if update is successful
		Assert.assertEquals(da.updateEval(origEvalRec), 1);		
	}
	
	/**
	 * method to test updateEval Fail method 
	 * @throws Exception
	 */
	@Test
	public void testUpdateEvalFail() {
		//create a new evaluation object
		Evaluation origEvalRec = new Evaluation("Assignment3", "SYST10199",
				13, 25, 5.0, LocalDate.parse("2021-08-01"));		
			origEvalRec.setId(5); //set the id to 5 
			da.addEval(origEvalRec); // add to the database
			
			//change the values, especially set the id to test update method
			origEvalRec.setId(19);
			origEvalRec.setTitle("Assignment2");
			origEvalRec.setCourse("PROG24178");
			origEvalRec.setGrade(15);
			origEvalRec.setMax(20);
			origEvalRec.setWeight(6);
			origEvalRec.setDueDate(LocalDate.parse("2021-08-02"));
			
			//test if update is not successful 
			Assert.assertEquals(da.updateEval(origEvalRec), 0);		
	}
}
