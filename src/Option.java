

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Option {
	String id;
	Measure measure;
	Task task;
	HashMap<String, Option> _suboptions;
	double eu;
	public Option(String id, Measure measure, Task task)
	{
		this.id = id;
		this.measure = measure;
		this._suboptions = new HashMap<String,Option>();
		this.task = task;
	}
	
	public Option(String id,Task task)
	{
		this.id = id;
		this._suboptions = new HashMap<String,Option>();
		this.task = task;
	}
	
	public Option(String id, Measure measure)
	{
		this.id = id;
		this.measure = measure;
		this._suboptions = new HashMap<String,Option>();
	}
	
	public Option(String id)
	{
		this.id = id;
		this.measure = null;
		this._suboptions = new HashMap<String,Option>();
	}
	public HashMap<String, Option> getSuboptions()
	{
		return this._suboptions;
	}
	public Measure getMeasure()
	{
		return measure;
	}
	public Task getTask()
	{
		return this.task;
	}
	public double calculateEUSubOptions()
	{

		double eu = 0;
		int totalEvidence = 0;
		boolean flag = false;
		for(Option suboption : this._suboptions.values())
		{
			//System.out.println("SUBOPTION " + suboption.id);
			eu = eu + suboption.calculateEU();	
			
			if (suboption.getMeasure() instanceof Evidence) { 
				flag = true;
				totalEvidence += ((Evidence)suboption.getMeasure()).getNoOcurrences();
			
			}
		}
		
		if(flag == true)
		{
			//System.out.println("totalEvidence"+ totalEvidence);
			eu = eu/totalEvidence;
		}
		return eu;
	}
	
	public double calculateEU()
	{
		double eu = 0;
		if(!this.getSuboptions().isEmpty())
		{
			
			if (measure instanceof Belief) { 
				//System.out.println("option id " + this.id + " has probability " + ((Belief) measure).getProbability());
				eu = ((Belief) measure).getProbability() * calculateEUSubOptions();
				
			}
			else if (measure instanceof Evidence)
			{
				eu = ((Evidence) measure).getNoOcurrences() * calculateEUSubOptions();
			}
		}
		
		else
		{
			//System.out.println("no suboptions");
			if (measure instanceof Belief) { 
				
				eu = ((Belief) measure).calculateEUterm();
				
			}
			
			else if (measure instanceof Evidence) {
				/*
				System.out.println("evidence");
				System.out.println("test" + this.getTask().getTotalNoOcurrences());
				System.out.println("no occurences evidence" +  ((Evidence) measure).getNoOcurrences() + "for id" + measure.id);
				*/
				/*
				int newtotalNoOccurences =  this.getTask().getTotalNoOcurrences() + ((Evidence) measure).getNoOcurrences();
				float avg = 0;
				avg = this.getTask().getExpectedUtility();
				
				eu = ((Evidence) measure).calculateEUterm(newtotalNoOccurences,this.getTask().getTotalNoOcurrences(), avg);
				*/
				//System.out.println("evidence eu" + eu);
				//this.getTask().setTotalNoOcurrences(newtotalNoOccurences);
				
				eu = ((Evidence) measure).calculateEUterm();
				
			}
		}
		
		return eu;
	}
	
	public void setEU(float eu)
	{
		this.eu = eu;
	}
	public void  setSuboptions( HashMap<String, Option> options)
	{
		this._suboptions = options ;
	}
	
	public void setMeasure(Measure measure)
	{
		this.measure = measure;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void addOption(Option suboption) {
		_suboptions.put(suboption.id, suboption);
		
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
	             
	   
	   public static boolean findBrackets(String line)
	   {
		     Matcher m2 = Pattern.compile("(?<=\\[).*(?=\\])").matcher(line);
		 
		     if(m2.find( )) return true;
	         
	         return false;
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
	   
	   public static String[] splitByEquals(String line)
	   {
		   String[] pair = line.split("=", 2);
   	       String id = pair[0];
   	       String utility = pair[1];
   	      
   	       return pair;
	   }
	   
	   public static boolean isBelief(String line)
	   {
		   Pattern r2 = Pattern.compile("%");
		   Matcher m2 = r2.matcher(line);
		   if(m2.find())	return true;
		   return false;
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
}

