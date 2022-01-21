package ca.sheridancollege.bask.as2.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import ca.sheridancollege.bask.as2.beans.Evaluation;
import ca.sheridancollege.bask.as2.database.DatabaseAccess;

/**
 * Test class for main controller
 * @author Kubra Bas
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestMainController {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private DatabaseAccess da;
	
	/**
	 * method to test goIndex Pass method 
	 * @throws Exception
	 */
	@Test
	public void testGoIndexPass() throws Exception {
		mockMvc.perform(get("/")) //checks if root "/" can be performed
		.andExpect(status().isOk()) //check the status message is 'OK'
		.andExpect(view().name("index.html")); //check if 'index.html' loaded
	}
	
	/**
	 * method to test goIndex Fail method 
	 * @throws Exception
	 */
	@Test
	public void testGoIndexFail() throws Exception {
		mockMvc.perform(get("/boo"))  //check if a fake directory can be loaded
		.andExpect(status().isNotFound()); //check the status message 'notFound' thrown
	}
	
	/**
	 * method to test goEvals Pass method 
	 * @throws Exception
	 */
	@Test
	public void testGoEvalsPass() throws Exception{
		//check if "/evalc" can be perform
		//check if sessionAttribute can be found 
		mockMvc.perform(get("/evalc").sessionAttr("courses", da.getCourses()))
		.andExpect(model().attributeExists("evaluation")) //check if model attribute("evaluation") exists
		.andExpect(status().isOk()) //check the status message is 'OK'
		.andExpect(view().name("evaluation.html"));//check if 'evaluation.html' loaded
	}
	
	/**
	 * method to test goIndex Fail method 
	 * @throws Exception
	 */
	@Test
	public void testGoEvalsFail() throws Exception{
			
		mockMvc.perform(get("/evalc").sessionAttr("courses", da.getCourses()))
			   .andExpect(model().attributeDoesNotExist("evaluations"));//check if fake model Attribute can't be found 
	}
	

	/**
	 * method to test doEvals Pass method 
	 * @throws Exception
	 */
	@Test
	public void testDoEvalPass() throws Exception {
		//Create a new Evaluation object
		Evaluation testEvaluation = new Evaluation("Assignment1", "PROG10082",
				12, 15, 6.0, LocalDate.parse("2021-07-27"));
		
		//test adding a new evaluation 
		mockMvc.perform(post("/evals").flashAttr("evaluation", testEvaluation).sessionAttr("edit", false))
		    .andExpect(model().attributeDoesNotExist("evalId"))//check if evalId doesn't exists which means user wants to add a new record
			.andExpect(status().isOk())//check the status message is 'OK'
			.andExpect(view().name("evalResults.html"));//check if 'evalResults.html' loaded

		//test editing the existing evaluation record
		testEvaluation.setId(1);
		testEvaluation.setTitle("Assignment2");
		testEvaluation.setCourse("PROG24178");
		testEvaluation.setGrade(15);
		testEvaluation.setMax(20);
		testEvaluation.setWeight(6);
		testEvaluation.setDueDate(LocalDate.parse("2021-08-02"));
		
		
		mockMvc.perform(post("/evals").flashAttr("evaluation", testEvaluation).sessionAttr("edit", true))
			.andExpect(model().attributeExists("evalId")) // check if evalId exists which means user wants to edit
			.andExpect(status().isOk())//check the status message is 'OK'
			.andExpect(view().name("evalResults.html"));//check if 'evalResults.html' loaded
		
	}
	
	
	/**
	 * method to test doEval Fail method 
	 * @throws Exception
	 */
	@Test
	public void testDoEvalFail() throws Exception {
			//Create a new Evaluation object
			Evaluation testEvaluation = new Evaluation("Assignment1", "PROG10082",
					12, 15, 6.0, LocalDate.parse("2021-07-27"));
			
			testEvaluation.setId(1);		
			//test adding a new evaluation record, error page will be load
			mockMvc.perform(post("/evals").flashAttr("evaluation", testEvaluation).sessionAttr("edit", false))
				.andExpect(view().name("error.html"));//check if 'error page' loaded	
			
			//test updating, error page will be load
			mockMvc.perform(post("/evals").flashAttr("evaluation", testEvaluation).sessionAttr("edit", true))
			.andExpect(view().name("error.html"));//check if 'error page' loaded		
	}

	
	/**
	 * method to test editEval Pass method 
	 * @throws Exception
	 */	
	@Test
	public void testEditEvalPass() throws Exception {
		//Create a new Evaluation object
		Evaluation eval = new Evaluation("Assignment1", "PROG10082",
				12, 15, 6.0, LocalDate.parse("2021-07-27"));
		
		eval.setId(1); //set the 'id' of 'eval'
		da.addEval(eval); // add the 'eval' to the database
		
		mockMvc.perform(get("/editEvaluation/{id}", "1"))//check if "/editEvaluation/{id}" performed
			.andExpect(status().isOk()) //check the status message is 'OK'
			.andExpect(view().name("evaluation.html"));//check if 'evaluation.html' loaded
	}
	
	/**
	 * method to test editEval Fail method 
	 * @throws Exception
	 */	
	@Test(expected=NestedServletException.class)
	public void testEditEvalFail() throws Exception {
		//Create a new Evaluation object
		Evaluation eval = new Evaluation("Assignment1", "PROG10082",
				12, 15, 6.0, LocalDate.parse("2021-07-27"));
		
		eval.setId(1); //set the 'id' of 'eval'
		da.addEval(eval); // add the 'eval' to the database
		
		//check if "/editEvaluation/{10}" can be performed. we're asking for an invalid id
		mockMvc.perform(get("/editEvaluation/{id}", "10")); // this will throw NestedServletException
			
		
	}
	
	
}
