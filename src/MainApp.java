

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApp {

	public static void main(String[] args) throws IOException {
		
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
				    String line = br.readLine();   
				    Matcher m = Pattern.compile("(^[\\w]+-[\\w]+)[ ]?[(](.+)[)][ ]*(\\d$)?").matcher(line);
				    if(m.find())
				    {
				    	
				    	String function_name = m.group(1);
				    	String arguments = m.group(2);
				    	
				    	if(function_name.equals("decide-rational"))
				    	{
				    	    RationalAgent ra = new RationalAgent();
				            
				            int no_decisions = Integer.parseInt(m.group(3));
				            
				            treatInfo(arguments, ra);
				            int actual_decisions = 0;
				           
				            String last_decision = "";
				            if(no_decisions == 1)
				            {
				            
				            	String decision = ra.decide();
				 	            System.out.println(decision);
				            }
				            else
				            {
				            	String decision = ra.decide();
				 	            System.out.println(decision);
				 	            actual_decisions = 1;
				 	            last_decision = decision;
				            	   while(actual_decisions != no_decisions )
						            {
				            		   	//System.out.println("actual_decisions" + actual_decisions);
				            			//System.out.println("no_decisions" + no_decisions);
				            		    String another_line = br.readLine();
				            		   	treatUpdate(another_line,ra, last_decision);
						            	decision = ra.decide();
						 	            System.out.println(decision);
						 	            last_decision = decision;
						 	            ++actual_decisions;
						 	      
						 	            //ra.update();
						            }
				            	   
				            	
				            }
				         
				            
				            
				          
				    	}
				
				    	else if(function_name.equals("decide-risk"))
				    	{
				    		//System.out.println("decide-risk");
				    	 	RationalAgent ra1 = new RationalAgent();
				        	//System.out.println("arguments" + arguments);
				        	treatInfo(arguments, ra1);
				        	HashMap<String, Task> tasks = ra1.getInternalState().getTasks();
				        	int numTasks = tasks.size();
				        	HashMap<String, Double> answer = new HashMap<String, Double>();
				        	HashMap<String, Task>  tasks_aux = new HashMap<String,Task>(tasks);
				        	if(tasks.size() != 1)
				        	{
				        	Set<String> set_tasks_id = tasks.keySet();
					        String[] tasks_id = set_tasks_id.toArray(new String[set_tasks_id.size()]);
				        	double[] utilitiesPerTask = fillUtilitiesPerTask(tasks);
				        	int numpos = findNumPositivesNegatives(tasks)[0];
				        	int numneg = findNumPositivesNegatives(tasks)[1];

				         	//System.out.println(SameNegative(utilitiesPerTask, tasks, tasks_id));
				         	//System.out.println(numneg);
				         	if(isAllNegative(utilitiesPerTask))
				        	{
				         		
				         		String most_negative = findMostNegative(tasks);
				         		tasks_aux.remove(most_negative);
				        		Collection<ArrayList<String>> test = findEqualNegatives(tasks_aux);
				        	
				        		double divide = (double)1/test.size();
				        		
				        		Iterator<ArrayList<String>> iter = test.iterator();
				        		while(iter.hasNext())
				        		{
				        			ArrayList<String> equalElements = iter.next();
				        			double effort = divide/equalElements.size();
				        			for(int i = 0; i < equalElements.size(); ++i)
				        			{
				        				String task_id = equalElements.get(i);
				        			
				        				answer.put(task_id, effort);
				        			}
				        		}
				        	}
				         	
				         	else if(SameNegative(utilitiesPerTask, tasks, tasks_id).size() != 0)
			        		{
				         		//System.out.println("entrei");
			        			int sum = findNumPositivesNegatives(tasks)[0] + findNumPositivesNegatives(tasks)[1];
			        			double effort = (double)1/sum;
			        			for(int i = 0; i < tasks_id.length ; ++i)
			        			{
			        				answer.put(tasks_id[i], effort);
			        			}
		        				
			        			
			        		}
				         
				         	else
				         	{
				         		
				         		String most_negative = findMostNegative(tasks);
					        	//System.out.println("most negative" + most_negative);
					        	if(most_negative != "")
					        	{
					        		if(!balanceMostNegative(most_negative, tasks))
					        		{
					        			
					        			//System.out.println("remove" + most_negative);
					        			tasks_aux.remove(most_negative);
					        		
					        			
					        		}
					        		
					        		
					        	}
					        	set_tasks_id = tasks_aux.keySet();
					        	tasks_id = set_tasks_id.toArray(new String[set_tasks_id.size()]);
				         	
				        	
				        	
				        
				        	
				        	if(tasks_aux.size() != 1)
				        	{
					        	
					        
					        	double[] minUtilitiesPerTask = fillMinUtilitiesPerTask(tasks_aux); 
					        	double[] maxUtilitiesPerTask = fillMaxUtilitiesPerTask(tasks_aux);
					        	utilitiesPerTask = fillUtilitiesPerTask(tasks_aux);
					        	
					        	
					        	
					        		HashMap<String,ArrayList<String>> equalStates = findNumberEqualStates(utilitiesPerTask, tasks_id);
						        	//System.out.println(equalStates);
						        	//System.out.println(findEqualStates(utilitiesPerTask, tasks_id));
						        
						        		//TESTE
							        	/*
							        	for(int i = 0; i < minUtilitiesPerTask.length ; ++i)
										{
											System.out.println("min" + minUtilitiesPerTask[i]);
													
										}
										*/
							        	
							        	//TESTE
							        	/*
							        	for(int i = 0; i < maxUtilitiesPerTask.length ; ++i)
										{
											System.out.println("max" + maxUtilitiesPerTask[i]);
													
										}*/
							        	
							        	//constraints Ax = b
							        	int num = tasks_aux.size();
							        	
							        	//double[] sumUtilitiesPerTask = new double[num]; 
										double[] b = {1,0}; //corresponds to contraints
										for(int i = 0; i < num; ++i)
										{
											//sumUtilitiesPerTask[i] = maxUtilitiesPerTask[i] + minUtilitiesPerTask[i];
											minUtilitiesPerTask[i] = -minUtilitiesPerTask[i];
										}
										double[] equality_positive = new double[num];
										double[] equality_negative = new double[num];
										for(int i = 0; i < num; ++i)
										{
											equality_positive[i] = 1;
											equality_negative[i] = -1;
										}
										double[][] a = {equality_positive, minUtilitiesPerTask};
							        	//LinearProgramming.test(a,b,maxUtilitiesPerTask); 
							        	LinearProgramming lp;
							        	 try {
							  	            lp = new LinearProgramming(a, b, utilitiesPerTask);
							  	        }
							  	        catch (ArithmeticException e) {
							  	            System.out.println(e);
							  	            return;
							  	        }
			
							        
							        	
							        	double[] efforts = lp.primal();
							        	
							        	for(int i = 0; i < efforts.length; ++i)
							        	{
							        		String task_id = tasks_id[i];
							        		if(efforts[i] != 0)
							        		{
							        			//System.out.println("different 0");
							        			//System.out.println("no equal states" + equalStates.size());
							        			if(equalStates.get(task_id).size() == tasks_id.length)
							        			{ 
							        				double max = utilitiesPerTask[0];
							        				int j_max = 0;
							        				for(int j = 1; j < tasks_id.length; ++j)
							        				{
							        					if(max < utilitiesPerTask[j] && minUtilitiesPerTask[j] > 0)
							        					{
							        						max = utilitiesPerTask[j];
							        						j_max = j;
							        					}
							        				}
							        				answer.put(tasks_id[j_max] ,1.00);
							        			}
							        			else
							        			{
							        				//System.out.println("teste coisas");
							        				//System.out.println("equalStates" + equalStates);
							        				double new_effort = efforts[i]/equalStates.get(task_id).size();
								        			answer.put(task_id, new_effort);
								        			ArrayList<String> equals = equalStates.get(task_id);
								        			for(int j = 0; j < equals.size(); ++j)
								        			{
								        				String id_equal = equals.get(j);
								        				answer.put(id_equal, new_effort);
								        			}
							        			}
							        			
							        		}
							        		
							        		
							        	}
						        	
						        	
						        	
						        	
						        	
					        
					        	
					    
					        	}
					        	
				        	else
				        	{
				        		
				        		answer.put(tasks_aux.keySet().iterator().next(), 1.00);
				        	}
				         	}	}
					        	
				        	else
				        	{
				        	
				        		answer.put(tasks_aux.keySet().iterator().next(), 1.00);
				        	}
				        	testAnswer(answer);
				    	}
					}
				    
				    else
				    {
				        Matcher m1 = Pattern.compile("(^[\\w]+-[\\w]+)[ ]mine=[(](.+)[)][,]peer=[(](.+)[)]").matcher(line);
				        if(m1.find())
				        {
				        	/*
				        	System.out.println("group 0" + m1.group(0));
				        	System.out.println("group 1" + m1.group(1));
				        	System.out.println("group2" + m1.group(2));
				        	System.out.println("group3" +  m1.group(3));*/
				        	Player mine = new Player(m1.group(2), "mine");
				        	//mine.printExpectedUtilities();
				        	Player peer = new Player(m1.group(3), "peer");
				        	//peer.printExpectedUtilities();
				        
				        	//UtilityPlayer = new UtilityPlayer();
				        	
				        	StrategyPlayers sp = new StrategyPlayers(mine, peer);
				        	
				        	if(m1.group(1).equals("decide-nash"))
				        	{
				        		//sp.test();
				        		System.out.println(sp.findNashEquilibrum());
				        	}
				        	
				        	else if(m1.group(1).equals("decide-mixed"))
				        	{
				        		//sp.test();
				        		System.out.println(sp.mixedStrategy());
				        	}
				        	
				        	else if(m1.group(1).equals("decide-conditional"))
				        	{
				        		String res = sp.findNashEquilibrum();
				        		if(res.equals("blank-decision"))
				        		{
				        			System.out.println(sp.mixedStrategy());
				        		}
				        		
				        		else
				        		{
				        			System.out.println(res);
				        		}
				        	}
				        	
				        }
				    }
				
		}
		
	
	




private static HashMap<String, ArrayList<String>> SameNegative(double[] utilitiesPerTask, HashMap<String, Task> tasks, String[] tasks_id) {
	boolean flag = true;
	boolean master_flag = false;
	Boolean[] positiveTasks = new Boolean[tasks.size()];
	HashMap<String, ArrayList<String>> hashmap = new HashMap<String,ArrayList<String>>();
	//tasks_aux.remove(most_negative_id);
	int numPositives = 0;
	int numNegatives = 0;
	ArrayList<Integer> negatives = new ArrayList<Integer>();
	int[] answers = new int[2];
	int i = 0;
	//System.out.println("tasks_aux" + tasks_aux);
	for(Task task : tasks.values())
	{
		
		flag = true;
		for(Option option : task.getOptions().values())
		{
			double eu = option.getMeasure().getUtility();
			//is all posisitive
			if(eu < 0)
			{
				flag = false;
			}
		}
	
		if(flag == false)
		{
			++numNegatives;
			negatives.add(i);
			
		}
		++i;
		
		
	}
	
	for(i = 0; i < negatives.size(); ++i)
	{
		double pivot = utilitiesPerTask[negatives.get(i)];
		String pivot_id = tasks_id[negatives.get(i)];
		for(int j = i + 1; j < negatives.size(); ++j)
		{
			//System.out.println("pivot" + pivot);
			//System.out.println("another one" + utilitiesPerTask[negatives.get(j)]);
			if(pivot == utilitiesPerTask[negatives.get(j)])
			{
				
				ArrayList<String> arr = hashmap.get(pivot_id);
				if(arr == null)
				{
					ArrayList<String> equals = new ArrayList<String>();
					equals.add(tasks_id[negatives.get(j)]);
					hashmap.put(pivot_id, equals);
				}
				
				else
				{
					arr.add(tasks_id[negatives.get(j)]);
					hashmap.put(pivot_id, arr);
				}
			}
		}
		
	}
	
	return hashmap;

}







private static 	Collection<ArrayList<String>> findEqualNegatives(HashMap<String, Task> tasks_aux) {
	Iterator<Task> it = tasks_aux.values().iterator();
	HashMap<String, ArrayList<String>> hashmap = new HashMap<String,ArrayList<String>>();
	HashMap<Double, ArrayList<String>> values = new HashMap<Double, ArrayList<String>>();
	
  	
	for(int i = 0; i < tasks_aux.values().size() ; ++i)
	{
			if(it.hasNext()) 
			{
				Task task = it.next();
				for(Option option : task.getOptions().values())
				{
					double utility = option.getMeasure().getUtility();
					ArrayList<String> equals = values.get(utility);
					if(equals == null)
					{
						equals = new ArrayList<String>();
						equals.add(task.getID());
						values.put(utility, equals);
					}
					else
					{
						equals.add(task.getID());
						values.put(utility, equals);
					}
				}
					
			
			}
				
	}
		
	
	
	return values.values();
	   
	
	  	
	  	
		
	}



private static boolean isAllNegative(double[] utilitiesPerTask) {
	boolean isNegative = true;	
	for(int i = 0; i < utilitiesPerTask.length; ++i)
	{
		if(utilitiesPerTask[i] > 0)
		{
			isNegative=false;
		}
	}
	return isNegative;
}



private static double[] fillUtilitiesPerTask(HashMap<String, Task> tasks) {
	double[] utilities = new double[tasks.size()];
	Iterator<Task> it = tasks.values().iterator();
	
	for(int i = 0; i < tasks.values().size() ; ++i)
	{
			
				Task task = it.next();
				utilities[i] = task.getExpectedUtility();
				//System.out.println("eu" + utilities[i]);
			
				
	}
			
	
	return utilities;

	}



private static boolean balanceMostNegative(String most_negative_id, HashMap<String, Task> tasks) {
		//System.out.println("most_negative id" + most_negative_id);
		double most_negative = tasks.get(most_negative_id).findMostNegative();
		boolean flag = true;
		boolean master_flag = false;
		Boolean[] positiveTasks = new Boolean[tasks.size()];
		HashMap<String, Task> tasks_aux = new HashMap<String,Task>(tasks);
		tasks_aux.remove(most_negative_id);
		int numPositives = 0;
		int numNegatives = 0;
		//System.out.println("tasks_aux" + tasks_aux);
		for(Task task : tasks_aux.values())
		{
		
			flag = true;
			for(Option option : task.getOptions().values())
			{
				double eu = option.getMeasure().getUtility();
				//is all posisitive
				if(eu < 0)
				{
					flag = false;
				}
			}
			if(flag == true)
			{
				master_flag = true;
				++numPositives; 
			}
			if(flag == false)
			{
				++numNegatives;
			}
			
		}
		
		
		return master_flag;
	}



private static int[] findNumPositivesNegatives(HashMap<String, Task> tasks) {
	//System.out.println("most_negative id" + most_negative_id);
	//double most_negative = tasks.get(most_negative_id).findMostNegative();
	boolean flag = true;
	boolean master_flag = false;
	Boolean[] positiveTasks = new Boolean[tasks.size()];
	HashMap<String, Task> tasks_aux = new HashMap<String,Task>(tasks);
	//tasks_aux.remove(most_negative_id);
	int numPositives = 0;
	int numNegatives = 0;
	int[] answers = new int[2];
	//System.out.println("tasks_aux" + tasks_aux);
	for(Task task : tasks_aux.values())
	{
	
		flag = true;
		for(Option option : task.getOptions().values())
		{
			double eu = option.getMeasure().getUtility();
			//is all posisitive
			if(eu < 0)
			{
				flag = false;
			}
		}
		if(flag == true)
		{
			master_flag = true;
			++numPositives; 
		}
		if(flag == false)
		{
			++numNegatives;
		}
		
		answers[0] = numPositives;
		answers[1] = numNegatives;
		
	}
	return answers;
}
	
	



private static String findMostNegative(HashMap<String, Task> tasks) {
		String most_negative_id = "";
		double most_negative = 0;
		for(Task task : tasks.values())
		{
			double actual = task.findMostNegative();
			if(actual < most_negative)
			{
				most_negative = actual;
				most_negative_id = task.getID();
			}
		}
		//System.out.println("most_negative" + most_negative);
		return most_negative_id;
	}


public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
}

private static void testAnswer(HashMap<String, Double> answer) {
		String final_answer = "(";
		
		SortedSet<String> keys = new TreeSet<>(answer.keySet());
		Iterator<String> iterator = keys.iterator();
		while(iterator.hasNext())
		{
			String id = (String) iterator.next();
			String effort =  String.format(Locale.US, "%.2f", answer.get(id) );
			
			final_answer = final_answer + effort + ',' + id ;
			if(iterator.hasNext())
				final_answer = final_answer + ";";
			
			
		}
		
		final_answer += ")";
		System.out.println(final_answer);
		
	}


private static HashMap<String, ArrayList<String>> findNumberEqualStates(double[] maxUtilitiesPerTask,String[] tasks_id) {
	   HashMap<String, ArrayList<String>> hashmap = new HashMap<String,ArrayList<String>>();
	
	  	for(int i = 0; i < maxUtilitiesPerTask.length; i++)
        	{
	  			String pivot_id = tasks_id[i];
        		double pivot = maxUtilitiesPerTask[i];
        		
        		ArrayList<String> equalStates = new ArrayList<String>(); 
        		equalStates.add(pivot_id);
        		for (int j = 0; j < maxUtilitiesPerTask.length; j++)
	        	{
        			
	        		if(!tasks_id[j].equals(pivot_id) && maxUtilitiesPerTask[j] == pivot)
	        		{
	        			
	        			equalStates.add(tasks_id[j]);
	        		}
	        			
	        		
	        	}
        	
        		hashmap.put(pivot_id, equalStates);
        	}
	  	
	  
	  	return hashmap;
		
}

private static HashMap<ArrayList<String>,Double> findEqualStates(double[] maxUtilitiesPerTask,String[] tasks_id) {
	   HashMap<ArrayList<String>,Double> hashmap = new HashMap<ArrayList<String>,Double>();
	   HashMap<Double, ArrayList<String>> equalValues = new HashMap<Double,ArrayList<String>>();
	  	for(int i = 0; i < maxUtilitiesPerTask.length; i++)
     {
	  			String pivot_id = tasks_id[i];
     		double pivot = maxUtilitiesPerTask[i];
     		ArrayList<String> ids = equalValues.get(pivot);
     		if(ids != null)
     			ids.add(pivot_id);
     		else
     		{
     			ids = new ArrayList<String>();
     			ids.add(pivot_id);
     		}
     		equalValues.put(pivot, ids);
     }
	  	
	  
	 
	  	for(Map.Entry<Double, ArrayList<String>> entry : equalValues.entrySet()){
	  	    hashmap.put(entry.getValue(), entry.getKey());
	  	}
	  	
	  	return hashmap;
		
}


	private static double[] fillMaxUtilitiesPerTask(HashMap<String, Task> tasks) {
			double[] max_utilities = new double[tasks.size()];
			Iterator<Task> it = tasks.values().iterator();
			
			for(int i = 0; i < tasks.values().size() ; ++i)
			{
					if(it.hasNext()) 
					{
						Task task = it.next();
						max_utilities[i] = task.getMaxUtility();
					}
						
			}
					
			
			return max_utilities;
	}


	private static double[] fillMinUtilitiesPerTask(HashMap<String, Task> tasks) {
		double[] min_utilities = new double[tasks.size()];
		Iterator<Task> it = tasks.values().iterator();
		
		for(int i = 0; i < tasks.values().size() ; ++i)
		{
				if(it.hasNext()) 
				{
					Task task = it.next();
					min_utilities[i] = task.getMinUtility();
				}
					
		}
				
		
		return min_utilities;
	}


	private static void treatUpdate(String line, RationalAgent ra, String last_decision) throws IOException {
		   
		
		  double utility = Double.parseDouble(parseUtilityUpdate(line));
		  String task_id = last_decision;
		  LinkedList<String> options_id = parseOptions(line);
		  updateTask(ra, task_id, options_id, utility);
	}


	private static void updateOption(RationalAgent ra, Task task, LinkedList<String> options, double utility)
	{
		  Option option = null;
		  //System.out.println("options len " + options.size());
		  if(!options.isEmpty()) 
		  {
			
			  String option_id = options.removeFirst();
			  
			  option = task.getOptions().get(option_id);
			  
			
			  //if option does not exist and what exists is all belief 
			  if(option == null)
			  {
				  //System.out.println("does not exist");
				  Option opt = createNewOptionEvidence(option_id, utility, task);
				  task.addOption(opt);
				
			  }
			  //if option exists
			  else
			  {  
				
				  if(option.getMeasure() instanceof Belief)
				  {
					 //System.out.println("update belief to measure");
					 Evidence evidence = new Evidence(option.id, 1,option.getMeasure().getUtility());
					 option.setMeasure(evidence);
			
				  }
				  
				  else
				  {
					  updateEvidenceOption(task, option, utility);
				  }
				
			  }
			  
			  deleteAllBeliefOptions(task);
			  
			  if(!options.isEmpty()) 
			  {
				
				  String suboption_id = options.removeFirst();
		  		  Option suboption = updateSubOption(option, suboption_id, utility, task);
		  		  
				  	while(!options.isEmpty())
				  	{
				  		String other_suboption = options.removeFirst();
				  		updateSubOption(suboption, other_suboption, utility, task);
				  		
				  	}
						 
				 
				  	 
			  }
			  
			  //option.((Evidence)getMeasure()).calculateEUterm();
			  
		  }
		  
		  task.UpdateExpectedUtility();
		  /*
		  System.out.println("TESTES");
		  for(Task task1 : ra.getInternalState().getTasks().values())
    	  {
			  System.out.println("task id " + task1.getID());
    		  System.out.println("task" + task1.getExpectedUtility());
    		  System.out.println("no options" + task1.getOptions().size());
    	  
    		  for(Option option2 : task1.getOptions().values())
    		  {
    		  System.out.println("key option" + option2.id);
    		  System.out.println("utility" + option2.getMeasure().getUtility());
    		  iterateOption(option2, null);
    		  
    		  }
    	  }  */
 	 	
			  	
	}
		

		  
		 
	private static void iterateOption(Option option2, Option old_suboption)
	{
		  for(Option suboption : option2.getSuboptions().values())
		  {
		 
		  //System.out.println("key suboption" + old_suboption.id);
	
		  if(old_suboption == null)
		  {iterateOption(option2, suboption);}
		  else
		  {iterateOption(old_suboption, suboption);}
		  
		  }
	}
	
	
	private static Option updateSubOption(Option option, String suboption_id, double utility, Task task) {
		  //System.out.println("main option" + option.id);
		  Option suboption = option.getSuboptions().get(suboption_id);
		  Option new_suboption = new Option(suboption_id, task);
		  if(suboption == null)
		  {
			  //System.out.println("suboption doesnt exists");
			  deleteSubOptionsBelief(option);
			  new_suboption = createNewOptionEvidence(suboption_id, utility, task);
			  option.addOption(new_suboption);
		  }
		  else
		  {
			  //System.out.println("suboption exists");
			  if(suboption.getMeasure() instanceof Belief)
			  {
				  new_suboption = createNewOptionEvidence(suboption_id, utility, task);
			  }
			  else
			  {
				  new_suboption = updateEvidenceOption(suboption, utility);
			  }
		  }
		  
		  return new_suboption;
		
	}


	private static Option updateEvidenceOption(Option suboption, double utility) {
		int no_occur = ((Evidence)suboption.getMeasure()).getNoOcurrences();
		if(utility == suboption.getMeasure().getUtility())
		{
			((Evidence)suboption.getMeasure()).setNoOcurrences(no_occur + 1);
		}
		else
		{
			//System.out.println("utility nao e igual");
			((Evidence)suboption.getMeasure()).setNoOcurrences(1);
			((Evidence)suboption.getMeasure()).setUtility(utility);
		}
		return suboption;
		
	}


	private static void deleteSubOptionsBelief(Option option)
	{
		HashMap<String, Option> options = option.getSuboptions();			
		Iterator<Option> iter = options.values().iterator();

		while (iter.hasNext()) {
		    Option suboption = iter.next();
		    if(suboption.getMeasure() instanceof Belief)
		    {
		    	iter.remove();
		    }
			
		}
	}
	private static void updateTask(RationalAgent ra, String id, LinkedList<String> options, double utility) {
		  Task task = ra.getInternalState().getTask(id);
		  updateOption(ra, task,options,utility);
		  task.UpdateExpectedUtility();
		
	}


	private static void deleteAllBeliefOptions(Task task)
	{
		HashMap<String, Option> options = task.getOptions();
		
		Iterator<Option> iter = options.values().iterator();

		while (iter.hasNext()) {
		    Option option = iter.next();
		    if(option.getMeasure() instanceof Belief)
		    {
		    	if(!option.getSuboptions().isEmpty())
		    	{
		    		Iterator<Option> itersub = option.getSuboptions().values().iterator();
		    		Option suboption = itersub.next();
		    		if(suboption.getMeasure() instanceof Belief)
		    		{
		    			iter.remove();
		    		}
		    	}
		    	
		    	else
		    	{
		    	iter.remove();
		    	}
		    }
			
		}
		
	}

	



	private static Option createNewOptionEvidence(String option_id, double utility, Task task) {
		Evidence evidence = new Evidence(option_id, 1,utility);
		Option option = new Option(option_id, evidence, task);
		return option;
		
	}


	private static Option updateEvidenceOption(Task task, Option option, double utility) {
		task.setTotalNoOcurrences(task.getTotalNoOcurrences() + 1);
		
		int no_occur = ((Evidence)option.getMeasure()).getNoOcurrences();
		if(utility == option.getMeasure().getUtility())
		{
			//System.out.println("equal");
			((Evidence)option.getMeasure()).setNoOcurrences(no_occur + 1);
		}
		else
		{
			//System.out.println("no equal");
			((Evidence)option.getMeasure()).setNoOcurrences(1);
			((Evidence)option.getMeasure()).setUtility(utility);
		}
		return option;
		
	}


	private static LinkedList<String >parseOptions(String line) {
		LinkedList<String> options_ids = new LinkedList<String>();
		Matcher m = Pattern.compile("\\w+").matcher(line.split(",")[1]);
		 while (m.find()) {
 	      options_ids.add(m.group());
		 }

		return options_ids;
	}


	private static String parseTask(String line) {
		String task_id = "";
		Matcher m = Pattern.compile("(?<=\\,)\\w+(?=\\.)").matcher(line);
		 if (m.find()) {
  	      task_id = m.group();
		 }
  	 
		return task_id;
		
	}


	private static String parseUtilityUpdate(String line) {
		String utility = "";
		Matcher m = Pattern.compile("(?<=\\()[+ -]?\\d+(.\\d+)?(?=,)").matcher(line);
		 if (m.find()) {
   	      utility = m.group();
   
		 }
   	 
		return utility;
	}
	
	private static String parseUtility(String line) {
		String utility = "";
		Matcher m = Pattern.compile("(\\,)([-]?\\d+(.\\d+)?)(\\))").matcher(line);
		
		 if (m.find()) {
   	      utility = m.group(2);
   	  	
		 }

		//System.out.println("utility" + utility);
		return utility;
	}


	public static int parseNoQueries(String line)
	   {
		     int no_queries = 0;
		     Matcher m2 = Pattern.compile("\\d$").matcher(line);
		 
		     while(m2.find( )) {  
	       	  no_queries = Integer.parseInt(m2.group());
	         }
	         
	         System.out.println(no_queries);
	         return no_queries;
	   }
	   public static boolean findBrackets(String line)
	   {
		     Matcher m2 = Pattern.compile("(?<=\\[).*(?=\\])").matcher(line);
		 
		     if(m2.find( )) return true;
	         
	         return false;
	   }
	   
	   public static int getLastMatchingParentheses(String line)
	   {
		   
		   Matcher m = Pattern.compile("\\)").matcher(line);
		   int index_last = 0;
		   while(m.find()) {
			   index_last = m.start();
		   }
           
          return index_last; 
	   }
	   public static boolean findParentheses(String line)
	   {
		 Pattern r1 = Pattern.compile("(?<=\\().*(?=\\))");
	     Matcher m1  = r1.matcher(line);
	     if(m1.find()) return true;
		return false;
		   
	   }
	   
	   public static String removeParentheses(String line)
	   {
		   Pattern r1 = Pattern.compile("(?<=\\().*(?=\\))");
	       Matcher m1  = r1.matcher(line);
	       String pair1 = "";
		   while(m1.find())
		   {
		    	pair1 = m1.group(); 
		   }
		   return pair1;
	   }
	 
	   
	   public static String[] splitByEquals(String line)
	   {
		   String[] pair = line.split("=", 2);
   	       String id = pair[0];
   	       String utility = pair[1];
   	      
   	       return pair;
	   }
	   
	   public static String insideBrackets(String line)
	   {
		   StringBuilder sb = new StringBuilder(line);
		   Matcher m = Pattern.compile("\\[").matcher(line);
		   if (m.find()) {
		     
		       sb.deleteCharAt(m.start());
		       line = sb.toString();
		   }
		 
		   
		   m = Pattern.compile("\\]").matcher(line);
		   int index_last = 0;
		   while(m.find()) {
			   index_last = m.start();
		   }
           sb.deleteCharAt(index_last);
           line = sb.toString();
		   
           return line;
              
		   
	   }
	   public static ArrayList<String> breakOptions(String line)
	   {
		      ArrayList<String> options = new ArrayList<String>();
	          
	             while(!"".equals(line))
	             {
	            	   Matcher m = Pattern.compile("^,").matcher(line);
	            	   
	        		   if (m.find()) {
	        		       
	        		       StringBuilder sb = new StringBuilder(line);
	        		       sb.deleteCharAt(m.start());
	        		       line = sb.toString();
	        		   
	        		   }
	            	 
	            	 
	       		   	  String inside_parentheses = insideParentheses(line);
	            	  String option = inside_parentheses;
	            	  //System.out.println("option" + option);
	            	  StringBuilder sb = new StringBuilder(line);
	            	  sb.delete(0, option.length());
	            	  line = sb.toString();
	            	  
	            	  //System.out.println("no_brackets" + line);
	            	  options.add(option);
	            	
	        		   }
	        		   
	        	
	            	
	             return options;  
	             }
	             
	   
	   public static String removeMatchingParentheses(String line)
	   {
		   int index_last_par = 0;
		   int index_first_par = 0;
		   Stack<Character> stack = new Stack<Character>();
		   boolean flag = false;
		   for (int i = 0; i < line.length(); i++) {
				if(line.charAt(i) == '(' && flag == true)
				{
					stack.push('(');	
				}
				
			
				if(line.charAt(i) == '(' && flag == false)
				{
					index_first_par = i;
					stack.push('(');
					flag = true;
				}
				
				
				else if(line.charAt(i) ==')')
				{
					stack.pop();
					
					if(stack.empty() && flag == true)
					{					
						index_last_par = i;
						break;
					}
				}
				
			}
             
		   StringBuilder sb = new StringBuilder(line);
		   sb.deleteCharAt(index_first_par);
		   sb.deleteCharAt(index_last_par-1);
		   line = sb.toString();
		
	
		   return line;
	   }
	   public static String insideParentheses(String line)
	   {
		  
		   int index_last_par = 0;
		   int index_first_par = 0;
		   Stack<Character> stack = new Stack<Character>();
		   boolean flag = false;
		   for (int i = 0; i < line.length(); i++) {
				
				
			
				if(line.charAt(i) == '(' )
				{
					index_first_par = i;
					stack.push('(');
					flag = true;
				}
				
				
				else if(line.charAt(i) ==')')
				{
					stack.pop();
					
					if(stack.empty() && flag == true)
					{
						index_last_par = i;
						break;
					}
				}
				
			}
             
		 
		   line = line.substring(0, index_last_par + 1);
	
		   return line;
		   
	   }
	   
	   public static int setNumQueries(String line, RationalAgent ra)
	   {
		   int no_queries = parseNoQueries(line);
	       ra.getInternalState().setNoQueries(no_queries);
	       return no_queries;
	   }
	   public static void treatInfo(String arguments, RationalAgent ra) throws IOException
	   {
		      String line = arguments;
	          /*
	          System.out.println("line" + line);
	          int no_queries = parseNoQueries(line);
	          ra.getInternalState().setNoQueries(no_queries);
	          */
	          
	          line = line.split("\\)( )*\\d$")[0];
	        
	          String[] lotteries = line.split("(?<=]),");
	          HashMap<String, Task> tasks = new HashMap<String, Task>();
	       
	          for(int i = 0; i < lotteries.length; ++i )
	          {
	        	   HashMap<String, Option> map = new HashMap<String, Option>();
	        	  //System.out.println("Lottery" + i);
	              String lot = lotteries[i]; 
	              //System.out.println(lot);
	              String[] l = lot.split("=(?=\\[)");
	              
	              String id_lot = l[0];
	              Task task = new Task(id_lot);
	              
	              
	              //System.out.println("id lottery" + l[0]);
	              //System.out.println("tasks" + l[1]);
	        
	              String no_brackets = insideBrackets(l[1]);
	           
	              ArrayList<String> options = new ArrayList<String>();
	          
	             while(!"".equals(no_brackets))
	             {
	            	   Matcher m = Pattern.compile("^,").matcher(no_brackets);
	            	   
	        		   if (m.find()) {
	        		       //System.out.println(m.start());
	        		       StringBuilder sb = new StringBuilder(no_brackets);
	        		       sb.deleteCharAt(m.start());
	        		       no_brackets = sb.toString();
	        		   }
	        		   
	            	 
	            	 
	       		   	  String inside_parentheses = insideParentheses(no_brackets);
	            	  String option = inside_parentheses;
	            
	            	  StringBuilder sb = new StringBuilder(no_brackets);
	            	  sb.delete(0, option.length());
	            	  no_brackets = sb.toString();
	            	  
	            	 
	            	  //option = removeMatchingParentheses(option);
	            	  options.add(option);
	            	
	            	
	            	  
	             }
	   		 
	           //TESTE
	           /*
	   		   for(String option : options)
	   		   {
	   			   System.out.println("option test" + option);
	   		   }*/
	   	
	           
	      
	              for(String option : options )
	              { 
	            	  Option opt = treatOption(option,task);
	            	  //System.out.println("option id treatInfo" + opt.id);
		 	          map.put(opt.id, opt);
		 	          //System.out.println("set task" + task.getID());
	            	  opt.setTask(task);
	            	  task.setMeasures(map);
	            	  task.UpdateExpectedUtility();
	            	  tasks.put(id_lot, task);
	            	  ra.getInternalState().setTasks(tasks);
	              
	            	  //TESTES
	            	  /*
	            		  for(Task task1 : ra.getInternalState().getTasks().values())
		            	  {
	            			  System.out.println("task id " + task1.getID());
		            		  System.out.println("task" + task1.getExpectedUtility());
		            		  System.out.println("no options" + task1.getOptions().size());
		            	  
		            		  for(String key1 : task1.getOptions().keySet())
		            		  {
		            		  System.out.println("key option" + key1);
		            		  }
		            	  }*/
	            	  
	              
	            
	              }
	          }
	           
	   }
	   
	   public static String parseProbability(String line)
	   {
		   Pattern r3 = Pattern.compile("\\d+(?=%)");
  	     Matcher m3 = r3.matcher(line);
	    String prob = "";
	    	 while(m3.find( )) {
	    		 prob = m3.group();
	    	 }
	    	 return prob;
	   }
	   public static Pair_Utility_Probability findPairUtilityProb(String pair)
	   {
		   Matcher m = Pattern.compile(",").matcher(pair);
    	   int index = 0;
		   if (m.find()) {
			   index = m.start();
		   }
		   String prob = pair.substring(0, index);
		
		   String utility = pair.substring(index+1, pair.length()-1);
	
		  
  	       Pair_Utility_Probability pair_utility_prob = new Pair_Utility_Probability(utility, prob);
  	       return pair_utility_prob;
	   }
	   //treat things like A=(80%, [A1=(16,4)...], B=(3,4)
	   public static Option treatOption(String line, Task task)
	   {
		   String[] pair = splitByEquals(line);
		   Matcher matcher = Pattern.compile("\\w+").matcher(pair[0]);
		   String id = "";
		   if(matcher.find()) {id = matcher.group();};
		
		   String prob_utility = insideParentheses(pair[1]);
		   Pair_Utility_Probability pair_utility_prob = findPairUtilityProb(prob_utility);
		   String prob = pair_utility_prob.getProbability();
		   //System.out.println("prob" + prob);
		   String utility = pair_utility_prob.getUtility();
	
		   
		   HashMap<String, Option> _suboptions = new HashMap<String,Option>();
		   Option o = new Option(id, task);
		 
			   if(!isBelief(prob))
	  	       {
	  	    	   Pattern r11 = Pattern.compile("\\d+");
	  	           Matcher m11 = r11.matcher(prob); 
	  	           while(m11.find())
	  	           {
	  	        	  int no = Integer.parseInt(m11.group());
	  	        	  Evidence e = new Evidence(id, no,0); 
	  	        	  o.setMeasure(e);
	  	           }
	  	         
	  	    	   
	  	       }
			   
			   else
	  	       {
	  	         Pattern r3 = Pattern.compile("\\d+(?=%)");
	    	     Matcher m3 = r3.matcher(prob);
	  	    
	  	    	 while(m3.find( )) {
	      	    	 double percentage = Integer.parseInt(m3.group());
	      	    	 percentage = percentage/100;
	      	    	 Belief b = new Belief(id, percentage, 0);
	      	    	 o.setMeasure(b);
	    
	      	      }
	  	       }
			   if(findBrackets(utility))
			   {	
				utility = insideBrackets(utility);
				ArrayList<String> options_inside_utility = breakOptions(utility);
				for(String option : options_inside_utility)
				{
					Option suboption = treatOption(option, task);
					_suboptions.put(suboption.id, suboption);
					o.setSuboptions(_suboptions);
				}
		   
			   }
			   
		   //utility is simply a number
		
  	           Matcher matcher2 = Pattern.compile("[+-]?([0-9]*[.])?[0-9]+").matcher(utility);
  	           double ut = 0;
  	          
  	           if(matcher2.find()) ut = Double.parseDouble(matcher2.group());
  	     
		   o.getMeasure().setUtility(ut);
		   
	      
		  return o;
	   }
   public static boolean isBelief(String line)
   {
	   Pattern r2 = Pattern.compile("%");
	   Matcher m2 = r2.matcher(line);
	   if(m2.find())	return true;
	   return false;
   }

   

   }

