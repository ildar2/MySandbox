package kz.ildar.sandbox.utils.coursera.union_find;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * indices by convention are from 1 to n
 */
public class Percolation {
    private static final boolean OPEN = true;
    private static final boolean BLOCKED = false;
    private static final int TOP_POINT_OFFSET = 1;
    private static final int SIZE = 3;

    private final int n;
    private final boolean[] sites;
    private int openSites = 0;
    private final WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    // should be O(n^2)
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n: " + n);
        this.n = n;
        sites = new boolean[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sites[i * n + j] = BLOCKED;
            }
        }
        uf = new WeightedQuickUnionUF(2 + n * n);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(sites[i * n + j] ? 0 : 1).append(" ");
            }
            sb.append("\n");
        }
//        try {
//            Field f = uf.getClass().getDeclaredField("parent"); //NoSuchFieldException
//            f.setAccessible(true);
//            int[] ufFields = (int[]) f.get(uf);
//            sb.append(Arrays.toString(ufFields));
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
        return sb.toString();
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n)
            throw new IllegalArgumentException("row1: " + row + " col1: " + col);
        int i = row - 1;
        int j = col - 1;
        int ij = i * n + j;
        if (sites[ij] == BLOCKED) {
            sites[ij] = OPEN;
            // connect to adjacent sites
            // top
            if (i == 0) {
                // if in top row, connect to top virtual point
                uf.union(0, ij + TOP_POINT_OFFSET);
            } else {
                // else connect to higher site
                if (sites[ij - n] != BLOCKED) {
                    uf.union(ij - n + TOP_POINT_OFFSET, ij + TOP_POINT_OFFSET);
                }
            }
            // bottom
            if (i == n - 1) {
                // if in bottom row, connect to bottom virtual point
                uf.union(n * n + TOP_POINT_OFFSET, ij + TOP_POINT_OFFSET);
            } else {
                // else connect to lower site
                if (sites[ij + n] != BLOCKED) {
                    uf.union(ij + n + TOP_POINT_OFFSET, ij + TOP_POINT_OFFSET);
                }
            }
            // left
            if (j != 0 && sites[ij - 1] != BLOCKED) {
                uf.union(ij - 1 + TOP_POINT_OFFSET, ij + TOP_POINT_OFFSET);
            }
            // right
            if (j != n - 1 && sites[ij + 1] != BLOCKED) {
                uf.union(ij + 1 + TOP_POINT_OFFSET, ij + TOP_POINT_OFFSET);
            }
            openSites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n)
            throw new IllegalArgumentException("row: " + row + " col: " + col);
        return sites[(row - 1) * n + col - 1] == OPEN;
    }

    // is the site (row, col) full?
    // by definition, a full site is open
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && uf.find(0) == uf.find((row - 1) * n + col - 1 + TOP_POINT_OFFSET);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(n * n + TOP_POINT_OFFSET);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(SIZE);
        System.out.println(perc);
        // problem: bottom points are connected via virtual point (while shouldn't)
        while (true) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            int open = StdIn.readInt();
            if (open != 0) {
                perc.open(row, col);
            }

            System.out.println("percolates: " + perc.percolates());
            System.out.println("openSites: " + perc.openSites);
            System.out.println("isOpen: " + perc.isOpen(row, col));
            System.out.println("isFull: " + perc.isFull(row, col));

            System.out.println(perc);
        }
    }
}