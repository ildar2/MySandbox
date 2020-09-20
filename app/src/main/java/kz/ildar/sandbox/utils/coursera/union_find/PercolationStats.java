package kz.ildar.sandbox.utils.coursera.union_find;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;
    private static final double CONFIDENCE_95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n: " + n + " trials: " + trials);

        double[] results = new double[trials];

        // perform T independent computational experiments on a n-by-n grid
        // O(T * N^2)
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                perc.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            // The fraction of sites that are opened when the system
            // percolates provides an estimate of the percolation threshold
            results[i] = (double) perc.numberOfOpenSites() / (n * n);
        }

        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceLo = mean - CONFIDENCE_95 * (stddev / Math.sqrt(trials));
        confidenceHi = mean + CONFIDENCE_95 * (stddev / Math.sqrt(trials));
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    /**
     * Also, include a main() method that takes two command-line arguments n and T,
     * performs T independent computational experiments (discussed above) on an n-by-n grid,
     * and prints the sample mean, sample standard deviation, and the 95% confidence interval
     * for the percolation threshold. Use StdRandom to generate random numbers;
     * use StdStats to compute the sample mean and sample standard deviation.
     *
     * @param args
     */
    // test client (see below)
    public static void main(String[] args) {
        if (args.length < 2)
            throw new IllegalArgumentException("Enter n and T");
        PercolationStats percolationStats = new PercolationStats(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
        );
        System.out.println("mean = " + percolationStats.mean());
        System.out.println("stddev = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }

}