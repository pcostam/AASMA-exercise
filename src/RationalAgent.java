


import java.util.Iterator;


public class RationalAgent  {
	/**
	* Agent initializations
	*/
	InternalState internalState;
	
	
	public RationalAgent()
	{
		this.internalState = new InternalState();
	
	}
	
	public InternalState getInternalState()
	{
		return this.internalState;
	}
	
	protected void takeDown()
	{
		
	}
	
	public String decide()
	{
		
		double actual = 0;
		
		Iterator<Task> iter = internalState.tasks.values().iterator();
		Task task_max = new Task();
		//System.out.println(internalState.tasks.get("T1").getExpectedUtility());

		if(iter.hasNext()) task_max = iter.next();
	
		while (iter.hasNext()) {
		    Task task = iter.next();
			actual = task.getExpectedUtility();
		    if(task_max.getExpectedUtility() < actual)
		    {
		    	task_max = task;
		    }
			
		}
		
		return task_max._id;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}
	
}
