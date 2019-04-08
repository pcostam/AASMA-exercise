

public class Belief extends Measure {
	double probability;
	
	public Belief(String id, double probability, double utility)
	{
		super(utility,id);
		this.probability = probability;
	}
	
	public double getProbability()
	{
		return probability;
	}
	
	@Override
	public double calculateEUterm()
	{
		//System.out.println("utility" + getUtility() + "probability" + getProbability());
		return getUtility()*getProbability();
	}

	@Override
	public double calculateEUterm(int new_N, double old_EU) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double calculateEUterm(int new_N, int old_n, double old_EU) {
		// TODO Auto-generated method stub
		return 0;
	}

	





	
	
}
