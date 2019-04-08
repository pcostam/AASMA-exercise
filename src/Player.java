import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player {
	Double[][] _expectedUtilities;
	//double[][] expectedUtilities;
	String id;
	TreeMap<String,Integer> task_ids = new TreeMap<String, Integer>();
	public Player(String line,String id)
	{
		  this.id = id;
		  setTasksIds(line);
		
		  int dimension = task_ids.size();
		
		  this._expectedUtilities = new Double[dimension][dimension];
		  for(int i = 0; i < this._expectedUtilities.length; ++i)
			  for(int j = 0; j < this._expectedUtilities.length; ++j)
			  {
				  this._expectedUtilities[i][j] = (double) 0;
			  }
		  Matcher m = Pattern.compile("(\\w+\\|\\w+)").matcher(line);
		  String initial_pivot = "";
	
		  HashMap<String, Option> options = new HashMap<String,Option>();
		 
		  
			  if(m.find())
			  {	
				
				 //System.out.println("group 1 player" + m.group(1));
				 line = line.substring(m.group(1).length()+1, line.length() );
				 //System.out.println("line 1" + line);
				  String new_line = removeMatchingBrackets(line);
				 //line = line.substring(new_line.length() + 3, line.length() );
				  line = updateLine(line);
				 //System.out.println("line 2" + line);
				 String[] tasks = m.group(1).split("\\|");
				 String mine = tasks[0];
				 String peer = tasks[1];
				 int i = 0;
				 int j = 0;
				if(this.id.equals("mine"))
				{
				 j = task_ids.get(peer);
				
				 i = task_ids.get(mine);
				}
				
				else if(this.id.equals("peer"))
				{
				 i = task_ids.get(peer);
				
				 j = task_ids.get(mine);
				}
				
				  Task task = new Task();
			
				  Option option = Option.treatOption(new_line, task);
				  options.put(option.id, option);
				  //System.out.println("option" + option.id);
				  task.setMeasures(options);
				  task.UpdateExpectedUtility();
			
			  	  this._expectedUtilities[i][j] = task._expectedUtility;
			  }
			  while(m.find() && line != "")
			  {	
			
				  //System.out.println("group player" + m.group(1));
				  String[] tasks = m.group(1).split("\\|");
				  line = line.substring(m.group(1).length()+1, line.length());
				  //System.out.println("line 3" + line);
				  String new_line = removeMatchingBrackets(line);
				  line = updateLine(line);
				
				  //System.out.println("new_line length" + new_line.length());
				  //System.out.println("length" + line.length());
				  //line = line.substring(new_line.length() + 2, line.length());
				  if(new_line.length() == line.length())
				  {
					  line = "";
				  }
				
				  String mine = tasks[0];
					 String peer = tasks[1];
					
					 int j = 0;
					 
					 int i = 0;
				
						if(this.id.equals("mine"))
						{
						 j = task_ids.get(peer);
						
						 i = task_ids.get(mine);
						}
						
						else if(this.id.equals("peer"))
						{
						 i = task_ids.get(peer);
						
						 j = task_ids.get(mine);
						}
		     
				     
				  Task task = new Task();
			
				  Option option = Option.treatOption(new_line, task);
				  options.put(option.id, option);
				  task.setMeasures(options);
				  task.UpdateExpectedUtility();
			
			  	  this._expectedUtilities[i][j] = task._expectedUtility;
			 
				 // System.out.println("end");
			   
			   
			        	
		   	
			 
			  
		}
			  
			  
		  
	
		  
		  
		  
	}
	
	
	  private void setTasksIds(String line) {
		Matcher m = Pattern.compile("(\\w+\\|\\w+)").matcher(line);
		int i = 0;
		while(m.find())
		{
			String[] tasks = m.group().split("\\|");
			if(!task_ids.containsKey(tasks[0]))
			{
				this.task_ids.put(tasks[0], i);
				++i;
			}
			
			if(!task_ids.containsKey(tasks[1]))
			{
				this.task_ids.put(tasks[1], i);
				++i;
			}
			
		}
	
	}


	public static String removeMatchingBrackets(String line)
	   {
		   int index_last_par = 0;
		   int index_first_par = 0;
		   Stack<Character> stack = new Stack<Character>();
		   boolean flag = false;
		   for (int i = 0; i < line.length(); i++) {
				if(line.charAt(i) == '[' && flag == true)
				{
					stack.push('[');	
				}
				
			
				if(line.charAt(i) == '[' && flag == false)
				{
					index_first_par = i;
					stack.push('[');
					flag = true;
				}
				
				
				else if(line.charAt(i) ==']')
				{
					stack.pop();
					
					if(stack.empty() && flag == true)
					{					
						index_last_par = i;
						break;
					}
				}
				
			}
            
		   //StringBuilder sb = new StringBuilder(line);
		   String option = line.substring(index_first_par + 1, index_last_par);
		   line =  line.substring(index_last_par, line.length());
		   //sb.deleteCharAt(index_first_par);
		   //sb.deleteCharAt(index_last_par-1);
		   //line = sb.toString();
		
	
		   return option;
	   }
	  
	  
	  public static String updateLine(String line)
	   {
		   int index_last_par = 0;
		   int index_first_par = 0;
		   Stack<Character> stack = new Stack<Character>();
		   boolean flag = false;
		   for (int i = 0; i < line.length(); i++) {
				if(line.charAt(i) == '[' && flag == true)
				{
					stack.push('[');	
				}
				
			
				if(line.charAt(i) == '[' && flag == false)
				{
					index_first_par = i;
					stack.push('[');
					flag = true;
				}
				
				
				else if(line.charAt(i) ==']')
				{
					stack.pop();
					
					if(stack.empty() && flag == true)
					{					
						index_last_par = i;
						break;
					}
				}
				
			}
           
		   //StringBuilder sb = new StringBuilder(line);
		   String option = line.substring(index_first_par + 1, index_last_par + 1);
		   line =  line.substring(index_last_par, line.length());
		   //sb.deleteCharAt(index_first_par);
		   //sb.deleteCharAt(index_last_par-1);
		   //line = sb.toString();
		
	
		   return line;
	   }

	  
	public 	Double[][] getExpectedUtilities()
	{
		return this._expectedUtilities;
	}
	
	
	public void printExpectedUtilities()
	{
		System.out.println("id" + this.id);
		for(int i = 0; i < this.task_ids.size(); ++i)
			for(int j = 0; j < this.task_ids.size(); ++j)
			{
			
				System.out.println("element i: " + i + "j "+ j + "value " + this._expectedUtilities[i][j] );
			}
	}
	
	

}
