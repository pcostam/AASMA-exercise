

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Task {
	String _id;
	HashMap<String,Option> _options;
	double _expectedUtility = 0;
	int total_no_occurences = 0;
	double min_utility = 0;
	double max_utility = 0;
	
	public Task(String id)
	{
		this._id = id;
		this.total_no_occurences = 0;
	}
	
	public Task(String id, HashMap<String,Option> m)
	{
		this._id = id;
		this._options = m;
		this.total_no_occurences = 0;
	}
	
	public Task() {
		// TODO Auto-generated constructor stub
	}

	public double getExpectedUtility()
	{
	  return this._expectedUtility;
	}
	
	public HashMap<String, Option> getOptions()
	{
		return this._options;
	}
	
	public int getTotalNoOcurrences()
	{
		return this.total_no_occurences;
	}
	public String getID()
	{
		return this._id;
	}
	public void setMeasures(HashMap<String, Option> m)
	{
		this._options = m;
	}
	
	public void setTotalNoOcurrences(int total_no_occurences)
	{
		this.total_no_occurences = total_no_occurences;
	}
	public void setExpectedUtility(double eu)
	{
		this._expectedUtility = eu;
	}
	public void addOption(Option option)
	{
		this._options.put(option.id, option);
	}
	public void UpdateExpectedUtility()
	{
		double eu = 0;
		this.setTotalNoOcurrences(0);
		this.setExpectedUtility(0);
		int totalNoOcorrences = 0;
		for (Option option : this._options.values()) {
			if (option.getMeasure() instanceof Belief) { 
				
			eu += option.calculateEU();
			setExpectedUtility(eu);
			}
			else
			{
				//System.out.println("Task" + this.getID());
				//System.out.println("updateExpectedUtility" + option.id);
				//System.out.println("option" + option.id +"" + ((Evidence)option.getMeasure()).getNoOcurrences());
				totalNoOcorrences += ((Evidence)option.getMeasure()).getNoOcurrences();
				//System.out.println("option utility" + option.calculateEU());
				eu += option.calculateEU();
				
			}
	  }

		if(totalNoOcorrences != 0)
		{
			//System.out.println("eu" + eu);
			//System.out.println("totalNoOccurences" + totalNoOcorrences);
			//System.out.println("eu num" + eu);
			eu = eu/totalNoOcorrences;
			setExpectedUtility(eu);
		}
		//System.out.println("Task" + this.getID() + "EU" + eu);
		
	}
	
	public double getMinUtility()
	{
		double mu = 0;
		
		Iterator<Option> iter = this._options.values().iterator();
		
		if(iter.hasNext())
	    {
			Measure measure = iter.next().getMeasure();
			mu = measure.calculateEUterm();
	    }
				
		while (iter.hasNext()) {
		    Option option = iter.next();
			double actual = option.getMeasure().calculateEUterm();
			
		    if(mu > actual)
		    {
		    	mu = actual;
		    }
			
		}
		this.min_utility = mu;
		return this.min_utility;
	}
	
	public double getMaxUtility()
	{
		double mu = 0;
		
		Iterator<Option> iter = this._options.values().iterator();
		
		if(iter.hasNext()) 
		{ 
			Measure measure = iter.next().getMeasure();
			
			mu = measure.calculateEUterm();
		
		}
				
		while (iter.hasNext()) {
		    Option option = iter.next();
			double actual = option.getMeasure().calculateEUterm();
		    if(mu < actual)
		    {
		    	mu = actual;
		    }
			
		}
		this.max_utility = mu;
		return this.max_utility;
	}
	
	public double findMostNegative()
	{
		double most_negative = 0;
		
		Iterator<Option> iter = this._options.values().iterator();
		
	
		while (iter.hasNext()) {
		    Option option = iter.next();
			double actual = option.getMeasure().getUtility();
			if(actual < 0)
		    if(most_negative > actual)
		    {
		    	most_negative = actual;
		    	//System.out.println("most_negative" + most_negative);
		    }
			
		}
		
		return most_negative;
	}
}