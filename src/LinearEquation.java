
public class LinearEquation {

	public void LinearEquation()
    {
 
    }
	/**
	 * Method that calculates determinant of given matrix
	 *
	 * @param matrix matrix of which we need to know determinant
	 *
	 * @return determinant of given matrix
	 */
	
	static void getCofactor(double[][] matrixA,  
            double[][] temp, int p, int q, int n) 
   { 
       int i = 0, j = 0; 
     
       // Looping for each element of  
       // the matrix 
       for (int row = 0; row < n; row++) 
       { 
           for (int col = 0; col < n; col++) 
           { 
                 
               // Copying into temporary matrix  
               // only those element which are  
               // not in given row and column 
               if (row != p && col != q) 
               { 
                   temp[i][j++] = matrixA[row][col]; 
     
                   // Row is filled, so increase  
                   // row index and reset col  
                   //index 
                   if (j == n - 1) 
                   { 
                       j = 0; 
                       i++; 
                   } 
               } 
           } 
       } 
   } 
     
   /* Recursive function for finding determinant 
   of matrix. n is current dimension of mat[][]. */
   static double determinantOfMatrix(double[][] matrixA, int n) 
   { 
       int D = 0; // Initialize result 
     
       // Base case : if matrix contains single 
       // element 
       if (n == 1) 
           return matrixA[0][0]; 
         
       // To store cofactors 
       double[][] temp = new double[n][n];  
         
       // To store sign multiplier 
       int sign = 1;  
     
       // Iterate for each element of first row 
       for (int f = 0; f < n; f++) 
       { 
           // Getting Cofactor of mat[0][f] 
           getCofactor(matrixA, temp, 0, f, n); 
           D += sign * matrixA[0][f]  
              * determinantOfMatrix(temp, n - 1); 
     
           // terms are to be added with  
           // alternate sign 
           sign = -sign; 
       } 
     
       return D; 
   } 
     
 
	public static double cramerRule (double[][] matrixA, double[][] matrixB) {
		double x = determinantOfMatrix(matrixA,2)/determinantOfMatrix(matrixB,2);
		//System.out.println("determinant A" + determinantOfMatrix(matrixA,2));
		//System.out.println("determinant B" + determinantOfMatrix(matrixB,2));
		return x;
	}
	
	public static double solveSystem(double[][] A, double[] b)
	{
		
		double[][] Ax = {{b[0], A[0][1]},
							{b[1], A[1][1]}};
		double x = cramerRule(Ax,A);
		//System.out.println("x" + x);
		double[][] Ay = {{A[0][0], b[0]},
						  {A[1][0],b[1]}};
		double y = cramerRule(Ay, A);
		//System.out.println("y" + y);
		return x;
		
	}
	
	
}
