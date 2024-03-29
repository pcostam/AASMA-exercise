

public class LinearProgramming {
	 private static final double EPSILON = 1.0E-10;
	    private double[][] a;   // tableaux
	    private int m;          // number of constraints
	    private int n;          // number of original variables

	    private int[] basis;    // basis[i] = basic variable corresponding to row i
	                            // only needed to print out solution, not book

	    /**
	     * Determines an optimal solution to the linear program
	     * { max cx : Ax &le; b, x &ge; 0 }, where A is a m-by-n
	     * matrix, b is an m-length vector, and c is an n-length vector.
	     *
	     * @param  A the <em>m</em>-by-<em>b</em> matrix
	     * @param  b the <em>m</em>-length RHS vector
	     * @param  c the <em>n</em>-length cost vector
	     * @throws IllegalArgumentException unless {@code b[i] >= 0} for each {@code i}
	     * @throws ArithmeticException if the linear program is unbounded
	     */ 
	    public LinearProgramming(double[][] A, double[] b, double[] c) {
	        m = b.length;
	        n = c.length;
	        for (int i = 0; i < m; i++)
	            if (!(b[i] >= 0)) throw new IllegalArgumentException("RHS must be nonnegative");

	        a = new double[m+1][n+m+1];
	        for (int i = 0; i < m; i++)
	            for (int j = 0; j < n; j++)
	                a[i][j] = A[i][j];
	        for (int i = 0; i < m; i++)
	            a[i][n+i] = 1.0;
	        for (int j = 0; j < n; j++)
	            a[m][j] = c[j];
	        for (int i = 0; i < m; i++)
	            a[i][m+n] = b[i];

	        basis = new int[m];
	        for (int i = 0; i < m; i++)
	            basis[i] = n + i;

	        solve();

	        // check optimality conditions
	        assert check(A, b, c);
	    }

	    // run simplex algorithm starting from initial BFS
	    private void solve() {
	        while (true) {

	            // find entering column q
	            int q = bland();
	            if (q == -1) break;  // optimal

	            // find leaving row p
	            int p = minRatioRule(q);
	            if (p == -1) throw new ArithmeticException("Linear program is unbounded");

	            // pivot
	            pivot(p, q);

	            // update basis
	            basis[p] = q;
	        }
	    }

	    // lowest index of a non-basic column with a positive cost
	    private int bland() {
	        for (int j = 0; j < m+n; j++)
	            if (a[m][j] > 0) return j;
	        return -1;  // optimal
	    }

	   // index of a non-basic column with most positive cost
	    private int dantzig() {
	        int q = 0;
	        for (int j = 1; j < m+n; j++)
	            if (a[m][j] > a[m][q]) q = j;

	        if (a[m][q] <= 0) return -1;  // optimal
	        else return q;
	    }

	    // find row p using min ratio rule (-1 if no such row)
	    // (smallest such index if there is a tie)
	    private int minRatioRule(int q) {
	        int p = -1;
	        for (int i = 0; i < m; i++) {
	            // if (a[i][q] <= 0) continue;
	            if (a[i][q] <= EPSILON) continue;
	            else if (p == -1) p = i;
	            else if ((a[i][m+n] / a[i][q]) < (a[p][m+n] / a[p][q])) p = i;
	        }
	        return p;
	    }

	    // pivot on entry (p, q) using Gauss-Jordan elimination
	    private void pivot(int p, int q) {

	        // everything but row p and column q
	        for (int i = 0; i <= m; i++)
	            for (int j = 0; j <= m+n; j++)
	                if (i != p && j != q) a[i][j] -= a[p][j] * a[i][q] / a[p][q];

	        // zero out column q
	        for (int i = 0; i <= m; i++)
	            if (i != p) a[i][q] = 0.0;

	        // scale row p
	        for (int j = 0; j <= m+n; j++)
	            if (j != q) a[p][j] /= a[p][q];
	        a[p][q] = 1.0;
	    }

	    /**
	     * Returns the optimal value of this linear program.
	     *
	     * @return the optimal value of this linear program
	     *
	     */
	    public double value() {
	        return -a[m][m+n];
	    }

	    /**
	     * Returns the optimal primal solution to this linear program.
	     *
	     * @return the optimal primal solution to this linear program
	     */
	    public double[] primal() {
	        double[] x = new double[n];
	        for (int i = 0; i < m; i++)
	            if (basis[i] < n) x[basis[i]] = a[i][m+n];
	        return x;
	    }

	    /**
	     * Returns the optimal dual solution to this linear program
	     *
	     * @return the optimal dual solution to this linear program
	     */
	    public double[] dual() {
	        double[] y = new double[m];
	        for (int i = 0; i < m; i++)
	            y[i] = -a[m][n+i];
	        return y;
	    }


	    // is the solution primal feasible?
	    private boolean isPrimalFeasible(double[][] A, double[] b) {
	        double[] x = primal();

	        // check that x >= 0
	        for (int j = 0; j < x.length; j++) {
	            if (x[j] < 0.0) {
	                System.out.println("x[" + j + "] = " + x[j] + " is negative");
	                return false;
	            }
	        }

	        // check that Ax <= b
	        for (int i = 0; i < m; i++) {
	            double sum = 0.0;
	            for (int j = 0; j < n; j++) {
	                sum += A[i][j] * x[j];
	            }
	            if (sum > b[i] + EPSILON) {
	                System.out.println("not primal feasible");
	                System.out.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
	                return false;
	            }
	        }
	        return true;
	    }

	    // is the solution dual feasible?
	    private boolean isDualFeasible(double[][] A, double[] c) {
	        double[] y = dual();

	        // check that y >= 0
	        for (int i = 0; i < y.length; i++) {
	            if (y[i] < 0.0) {
	                System.out.println("y[" + i + "] = " + y[i] + " is negative");
	                return false;
	            }
	        }

	        // check that yA >= c
	        for (int j = 0; j < n; j++) {
	            double sum = 0.0;
	            for (int i = 0; i < m; i++) {
	                sum += A[i][j] * y[i];
	            }
	            if (sum < c[j] - EPSILON) {
	                System.out.println("not dual feasible");
	                System.out.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
	                return false;
	            }
	        }
	        return true;
	    }

	    // check that optimal value = cx = yb
	    private boolean isOptimal(double[] b, double[] c) {
	        double[] x = primal();
	        double[] y = dual();
	        double value = value();

	        // check that value = cx = yb
	        double value1 = 0.0;
	        for (int j = 0; j < x.length; j++)
	            value1 += c[j] * x[j];
	        double value2 = 0.0;
	        for (int i = 0; i < y.length; i++)
	            value2 += y[i] * b[i];
	        if (Math.abs(value - value1) > EPSILON || Math.abs(value - value2) > EPSILON) {
	            System.out.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
	            return false;
	        }

	        return true;
	    }

	    private boolean check(double[][]A, double[] b, double[] c) {
	        return isPrimalFeasible(A, b) && isDualFeasible(A, c) && isOptimal(b, c);
	    }

	    // print tableaux
	    private void show() {
	        System.out.println("m = " + m);
	        System.out.println("n = " + n);
	        for (int i = 0; i <= m; i++) {
	            for (int j = 0; j <= m+n; j++) {
	                System.out.printf("%7.2f ", a[i][j]);
	                // System.out.printf("%10.7f ", a[i][j]);
	            }
	            System.out.println();
	        }
	        System.out.println("value = " + value());
	        for (int i = 0; i < m; i++)
	            if (basis[i] < n) System.out.println("x_" + basis[i] + " = " + a[i][m+n]);
	        System.out.println();
	    }


	    static void test(double[][] A, double[] b, double[] c) {
	        LinearProgramming lp;
	        try {
	            lp = new LinearProgramming(A, b, c);
	        }
	        catch (ArithmeticException e) {
	            System.out.println(e);
	            return;
	        }

	        System.out.println("value = " + lp.value());
	        double[] x = lp.primal();
	        for (int i = 0; i < x.length; i++)
	            System.out.println("x[" + i + "] = " + x[i]);
	        double[] y = lp.dual();
	        for (int j = 0; j < y.length; j++)
	            System.out.println("y[" + j + "] = " + y[j]);
	    }

	 
	  

}
