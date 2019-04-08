

public class Evidence extends Measure{

	int no_occurences;
	int total_no_occurences;
	
	public Evidence(String id, int no_occurences, double utility)
	{
		super(utility,id);
		this.no_occurences = no_occurences;
	}
	
	public int getNoOcurrences()
	{
		return this.no_occurences;
	}
	
	public void setNoOcurrences(int no_occurences)
	{
		this.no_occurences = no_occurences;
	}
	

	@Override
	public double calculateEUterm(int new_N, int old_n, double old_EU)
	{ 
		/*
		System.out.println("old_eu" + old_EU);
		System.out.println("old_n" + old_n);
		System.out.println("new_N" + new_N);
		System.out.println("utility" + getUtility());
		System.out.println("no occurences" + no_occurences);*/
		//System.out.println(((old_EU*old_n) + (getUtility()*no_occurences))/(new_N));
		return ((old_EU*old_n) + (getUtility()*no_occurences))/(new_N);
	}

	@Override
	public double calculateEUterm() {
		return getUtility()*getNoOcurrences();
	}

	@Override
	public double calculateEUterm(int new_N, double old_EU) {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
