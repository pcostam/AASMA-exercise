

public abstract class Measure {
	String id;
	double utility;
	
	public Measure(double utility, String id)
	{
		this.id = id;
		this.utility = utility;
	}
	public double getUtility() {
		return this.utility;
	}

	public abstract double calculateEUterm();
	public abstract double calculateEUterm(int new_N, double old_EU);
	public void setUtility(double ut) {
		this.utility = ut;
		
	}
	public abstract double calculateEUterm(int new_N, int old_n, double old_EU);
	
}
