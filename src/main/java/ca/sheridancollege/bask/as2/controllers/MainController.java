package ca.sheridancollege.bask.as2.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ca.sheridancollege.bask.as2.beans.Evaluation;
import ca.sheridancollege.bask.as2.database.DatabaseAccess;



/**
 * Controller class to handle the requests
 * @author Kubra Bas
 */

@Controller
public class MainController {
	
	@Autowired 
	private DatabaseAccess da;//inject the database from IOCC

	/**
	 * method to load the index page
	 * @return
	 */
	@GetMapping("/")
	public String goindex() {
		
		return "index.html";	
	}
	
	/**
	 * method to load the evaluation page
	 *
	 * @param model: creates a new Evaluation object and keeps in model attribute
	 * @param session : if course list is empty grabs the list from the database
	 * @return : loads evaluation.html
	 */
	@GetMapping("/evalc")
	public String goevals(Model model, HttpSession session) {
		
		//create an Evaluation object
		model.addAttribute("evaluation", new Evaluation());
		
		Object o = session.getAttribute("courses");
				
		//If course list is empty grab the list from the database
		if(o == null) {
			 session.setAttribute("courses", da.getCourses());
		}			

		//load the evaluation.html page
		return "evaluation.html";
	}
	
	/**
	 * Method to load Result page
	 * If Evaluation object is a new object create a new record
	 * If the Evaluation object is already exist allow to edit 
	 * @param model : gets the evaluation list from the database and keep in model attribute
	 * @return
	 */
	@PostMapping("/evals")
	public String doEval(HttpSession session, Model model, @ModelAttribute Evaluation evaluation) {
		
		boolean edit = (evaluation.getId() == 0) ? false : true;
		session.setAttribute("edit", edit);
		int isThereError; //variable to keep the return value of addEval and updateEval methods in the database
		
		//If Evaluation object not exist create a new Evaluation object 
		if (!edit) {
				isThereError = da.addEval(evaluation);// returns a non 0 value if there is no problem
							
			// if Evaluation object is already exists, call the database update method	
			} else {	
				model.addAttribute("evalId", evaluation.getId());
				isThereError = da.updateEval(evaluation); // returns a non 0 value if there is no problem												
			
			}
			//Grab the evaluation list from the database and keep it in model addribute
			model.addAttribute("evals", da.getEvals());
			session.removeAttribute("edit"); //clear session attribute for edit
			
		//load evaluation result page or Error page.	
			
		if(isThereError == 0) // check if there is an error, load the error page
			return "error.html" ;
		else 
			return "evalResults.html"; // load the result page if everything is all right.
	}
	
	/**
	 * Method for editing Evaluation records
	 * return the evaluation.html page
	 * Loads the same evaluation object which is wanted to edit
	 */
	@GetMapping("/editEvaluation/{id}")
	public String editEval(Model model, @PathVariable int id) {
		//call the getEvaluation method in the database with
		//the 'id', store it in an Evaluation object named "e" 
		Evaluation e = da.getEvaluation(id); 
		
		//keep it in model attribute
		model.addAttribute("evaluation", e);
		
		//load the evaluation.html page
		return "evaluation.html";		
	}
	
	
}

