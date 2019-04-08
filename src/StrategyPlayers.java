import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

public class StrategyPlayers {
	Payoffs[][] strategies;
	TreeMap<String, Integer> task_ids;
	int dimension;
	//mine ---> lines
	//peer ---> columns
	public StrategyPlayers(Player mine, Player peer) {
		Double[][] mine_expectedUtilities = mine.getExpectedUtilities();
		Double[][] peer_expectedUtilities = peer.getExpectedUtilities();
		this.task_ids = mine.task_ids;
		this.dimension = task_ids.size();
		this.strategies = new Payoffs[dimension][dimension];
		
		for(int i = 0; i < this.dimension; ++i )
		{
			for(int j = 0; j < this.dimension; ++j )
			{
				Payoffs payoffs = new Payoffs(mine_expectedUtilities[i][j], peer_expectedUtilities[i][j], i, j);
				this.strategies[i][j] = payoffs;
			}
		}
		
	}
	public void test()
	{
		
		for(int i = 0; i < this.dimension; ++i )
			for(int j = 0; j < this.dimension; ++j )
			{
				System.out.println("i: " + i + "j: " + j);
				System.out.println("test mine " + strategies[i][j].utility1);
				System.out.println("test peer" + strategies[i][j].utility2);
			}
	}
	public String findNashEquilibrum()
	{
		ArrayList<Payoffs> nashEquilibriums = new ArrayList<Payoffs>();
		ArrayList<Payoffs> nashEquilibriums_mine = new ArrayList<Payoffs>();
		ArrayList<Payoffs> nashEquilibriums_peer = new ArrayList<Payoffs>();
		//System.out.println("utility1"+ this.strategies[0][0].utility1 + "utility2" + this.strategies[0][0].utility2);
		//System.out.println("utility1"+ this.strategies[3][1].utility1 + "utility2" + this.strategies[3][1].utility2);
		//System.out.println("utility1"+ this.strategies[3][2].utility1 + "utility2" + this.strategies[3][2].utility2);
		//System.out.println("utility1"+ this.strategies[3][3].utility1 + "utility2" + this.strategies[3][3].utility2);
		//System.out.println("utility1"+ this.strategies[1][1].utility1 + "utility2" + this.strategies[1][1].utility2);
		//System.out.println("utility1"+ this.strategies[0][1].utility1 + "utility2" + this.strategies[0][1].utility2);
		//System.out.println("utility1"+ this.strategies[1][0].utility1 + "utility2" + this.strategies[1][0].utility2);
		for(int i = 0; i < this.dimension; ++i )
			for(int j = 0; j < this.dimension; ++j)
			{
				Payoffs pivot = this.strategies[i][j];
				if(findBiggerUtilitySameLine(i, j, pivot) && findBiggerUtilitySameCol(i, j, pivot))
				{
					nashEquilibriums.add(pivot);
				}
			}
				
		
	 
		//System.out.println("nashEquilibrium size" + nashEquilibriums.size());
		/*
		for(int i = 0; i < nashEquilibriums.size(); ++i)
		{
			Payoffs max = nashEquilibriums.get(i);
			String line_id = "";
			String column_id = "";
			for (Entry<String, Integer> entry : task_ids.entrySet()) {
		        if (Objects.equals(max.line, entry.getValue())) {
		            line_id = entry.getKey();
		        }
		        if (Objects.equals(max.column, entry.getValue())) {
		            column_id = entry.getKey();
		        }
		    }
			
			System.out.println("mine=" + line_id + ",peer=" + column_id);
		}*/
			
	
		if(nashEquilibriums.size() != 0)
		{
			
			
			//Payoffs max = nashEquilibriums.get(0);
			return(highestSumOfPayOffs(nashEquilibriums));
			/*String line_id = "";
			String column_id = "";
			for (Entry<String, Integer> entry : task_ids.entrySet()) {
		        if (Objects.equals(max.line, entry.getValue())) {
		            line_id = entry.getKey();
		        }
		        if (Objects.equals(max.column, entry.getValue())) {
		            column_id = entry.getKey();
		        }
		    }
			
			System.out.println("mine=" + line_id + ",peer=" + column_id);*/
			
		}
		else
			return "blank-decision";
		
	}

	private String highestSumOfPayOffs(ArrayList<Payoffs> nashEquilibriums) {
		
		double sum = nashEquilibriums.get(0).utility1 + nashEquilibriums.get(0).utility2;
		Payoffs max = nashEquilibriums.get(0);
		String line_id = "";
		String column_id = "";
		for(int i = 1; i < nashEquilibriums.size(); ++i)
		{
			double actual_sum = nashEquilibriums.get(i).utility1 + nashEquilibriums.get(i).utility2;
			if(sum < actual_sum)
			{
				max = nashEquilibriums.get(i);
				sum = actual_sum;
			}
		}
		
		for (Entry<String, Integer> entry : task_ids.entrySet()) {
	        if (Objects.equals(max.line, entry.getValue())) {
	            line_id = entry.getKey();
	        }
	        if (Objects.equals(max.column, entry.getValue())) {
	            column_id = entry.getKey();
	        }
	    }
		
		return "mine=" + line_id + ",peer=" + column_id;
	}


	private boolean findBiggerUtilitySameLine(int i, int j_tested, Payoffs pivot) {
		boolean isNashEquilibrum = true;

	
		for(int j = 0; j < this.dimension; ++j)
		{
			if(j != j_tested)
			{
			double other_utility_mine = this.strategies[i][j].utility2;
			/*System.out.println("utility 2 " + other_utility_mine);
			System.out.println("utility 1 " + this.strategies[i][j].utility1);
			System.out.println("utility_mine" + utility_mine);*/
			if(pivot.utility2 < other_utility_mine )
			{
				isNashEquilibrum = false;
			
			}
			
	
			else if (pivot.utility2 == other_utility_mine && pivot.utility1 > strategies[i][j].utility1)
			{
				isNashEquilibrum = false;
			}
			
		
		
			}
			
			
		}
		
		/*
		if(max == utility_mine && isNashEquilibrum)
		{
			for(int j = 0; j < this.dimension; ++j)
			{
				if(j != j_tested)
				{
					double other_utility_mine = strategies[i][j].utility2;
					System.out.println("i : " + i);
					System.out.println("other: " + other_utility_mine);
					if(utility_mine == other_utility_mine && other_utility_mine == strategies[i][j].utility1)
					{
						isNashEquilibrum = true;
					}
					
					
					
				
				}
			
				
			}
			
		}*/
		//System.out.println("nash equilibrium" + isNashEquilibrum);
		return isNashEquilibrum;
	}

	private Payoffs findColFixed(int j_fixed)
	{
		System.out.println("col fixed: " + j_fixed);
		Payoffs pivot = strategies[0][j_fixed];
		for(int i = 1; i < this.dimension; ++i)
		{
			System.out.println("index: " + i);
			Payoffs other = this.strategies[i][j_fixed];
			System.out.println("utility1 pivot " + pivot.utility1);
			System.out.println("utility2 pivot " + pivot.utility2);
			System.out.println("utility1 other " + other.utility1);
			System.out.println("utility2 other " + other.utility2);
			if(pivot.utility1 <= other.utility1 && other.utility1 >= other.utility2 && pivot.utility2 > other.utility2)
			{
			
				pivot = other;
				
			}
			
		}
		
	
	       
		System.out.println("answer line " + pivot.line);
		System.out.println("column line " + pivot.column);
	
		
		
		return pivot;
	}
	
	
	private Payoffs findLineFixed(int i_fixed)
	{
		System.out.println("line_fixed");
		Payoffs pivot = strategies[i_fixed][0];
		for(int j = 1; j < this.dimension; ++j)
		{
			Payoffs other = this.strategies[i_fixed][j];
			System.out.println("utility1 pivot " + pivot.utility1);
			System.out.println("utility2 pivot " + pivot.utility2);
			System.out.println("utility1 other " + other.utility1);
			System.out.println("utility2 other " + other.utility2);
			if(pivot.utility2 <= other.utility2 && other.utility2 >= other.utility1 && pivot.utility1 > other.utility1)
			{
			
				pivot = other;
				
			}
			
		}
		
		System.out.println("answer line " + pivot.line);
		System.out.println("column line " + pivot.column);
		
		return pivot;
	}
	private boolean findBiggerUtilitySameCol(int i_tested, int j, Payoffs pivot) {
		boolean isNashEquilibrum = true;
		
		for(int i = 0; i < this.dimension; ++i)
		{
			if(i != i_tested)
			{
				double other_utility_peer = strategies[i][j].utility1;
				//System.out.println("other_utility_peer " + other_utility_peer);
				//System.out.println("utility 2 " + this.strategies[i][j].utility2);
				//System.out.println("utility column" + utility_peer);
				if(pivot.utility1 < other_utility_peer)
				{
				
					isNashEquilibrum = false;
					
				}
				else if (pivot.utility1 == other_utility_peer && pivot.utility2 > strategies[i][j].utility2)
				{
					isNashEquilibrum = false;
				}
				
			
			
				
				
				
			
			}
		
			
		}
		//System.out.println("nash equilibrium" + isNashEquilibrum);
		/*
		if(max == utility_peer && isNashEquilibrum)
		{
			for(int i = 0; i < this.dimension; ++i)
			{
				if(i != i_tested)
				{
					double other_utility_peer = strategies[i][j].utility1;
					System.out.println("i : " + i);
					System.out.println("other: " + other_utility_peer);
					if(utility_peer == other_utility_peer && other_utility_peer == strategies[i][j].utility2)
					{
						isNashEquilibrum = true;
					}
					
					
					
				
				}
			
				
			}
			
		}
		*/
		
		
		
		return isNashEquilibrum;
		
	}
	public String mixedStrategy() {
		
		return expectedUtilityForActionMine(0,1);
		
		
	}
	
	public String expectedUtilityForActionMine(int i_A, int i_B)
	{
		//iterate columns
			
			//double[] eq1 = {strategies[0][i_A].utility2-strategies[1][i_A].utility2, -1};
			//double math = strategies[0][i_A].utility2-strategies[1][i_A].utility2;
			//System.out.println("test 1" + math );
			//double[] eq2 = {strategies[0][i_B].utility2-strategies[1][i_B].utility2, -1};
			//double math2 = strategies[0][i_B].utility2-strategies[1][i_B].utility2;
			//System.out.println("test 2" + math2 );
			//double[][] A = {eq1,eq2};
			//double[] b = {strategies[1][i_A].utility2, strategies[1][i_B].utility2};
			double non_free_mine = strategies[0][i_A].utility2 - strategies[1][i_A].utility2 - strategies[0][i_B].utility2 + strategies[1][i_B].utility2;
			double c_mine = -strategies[1][i_A].utility2+strategies[1][i_B].utility2; 
			double x_mine = c_mine/non_free_mine;
			//printMatrix(A);
			//double x = LinearEquation.solveSystem(A,b);
			double non_free_peer = strategies[i_A][0].utility1 - strategies[i_A][1].utility1 - strategies[i_B][0].utility1 + strategies[i_B][1].utility1;
			double c_peer = -strategies[i_A][1].utility1+strategies[i_B][1].utility1; 

			if(non_free_peer != 0 && non_free_mine != 0)
			{
				double x_peer = c_peer/non_free_peer;
			
				return("mine=("+ String.format(Locale.US, "%.2f", x_mine ) + "," + 
						String.format(Locale.US, "%.2f", 1-x_mine ) + "),"+ "peer=(" + String.format(Locale.US, "%.2f", x_peer ) +
						"," + String.format(Locale.US, "%.2f",  1- x_peer ) + ")");
			}
			
			else
			{
				return("black-decision");
			}
			//double[] eq1_peer = {strategies[i_A][1].utility2-strategies[i_A][1].utility2, -1};
			//System.out.println("test 1" + math );
			//double[] eq2_peer = {strategies[i_B][0].utility2-strategies[i_B][1].utility2, -1};
			//System.out.println("test 2" + math2 );
			//double[][] A_peer = {eq1,eq2};
		
			//double[] b_peer = {strategies[1][i_A].utility2, strategies[1][i_B].utility2};
			//printMatrix(A);
			//double x_peer = LinearEquation.solveSystem(A_peer,b_peer);
			
			
			
		
			
		
	}
	
	public void printMatrix(double[][] A)
	{
		System.out.println(this.dimension);
		for(int i = 0; i < A.length; ++i)
			for(int j = 0; j < A[i].length; ++j)
			{
				System.out.println("A[i][j]" + A[i][j]);
			}
	}

	
}
