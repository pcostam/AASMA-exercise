

import java.util.HashMap;

public class InternalState {
	HashMap<String, Task> tasks;
	int no_queries;
	
	public InternalState(HashMap<String, Task> tasks)
	{
		this.tasks = tasks;
	}
	
	public InternalState() {
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, Task> getTasks()
	{
		return tasks;
	}
	public void setTasks(HashMap<String, Task> tasks)
	{
		this.tasks = tasks;
	}
	
	public void setNoQueries(int no_queries)
	{
		this.no_queries = no_queries;
	}

	public Task getTask(String task_id) {
		return this.tasks.get(task_id);
		
	}
}
