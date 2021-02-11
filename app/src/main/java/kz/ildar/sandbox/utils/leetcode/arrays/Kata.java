package kz.ildar.sandbox.utils.leetcode.arrays;

import android.os.Build;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.IntStream;

import androidx.annotation.RequiresApi;

public class Kata {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int[] testit(int[] a, int[] b) {
        return testitWithStream(a, b);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static int[] testitWithStream(int[] a, int[] b) {
        return IntStream.concat(Arrays.stream(a).distinct(), Arrays.stream(b).distinct()).sorted().toArray();
    }

    private static int[] testitBruteForce(int[] a, int[] b) {
        int[] a_new = distinct(a);
        int[] b_new = distinct(b);
        Arrays.sort(a_new);
        Arrays.sort(b_new);
        return merge(a_new, b_new);
    }

    private static int[] merge(int[] a, int[] b) {
        int i = 0, j = 0, k = 0;
        int n = a.length;
        int m = b.length;
        int[] out = new int[n + m];

        while (i < n && j < m) {
            if (a[i] < b[j]) {
                out[k++] = a[i++];
            } else {
                out[k++] = b[j++];
            }
        }
        while (i < n) {
            out[k++] = a[i++];
        }
        while (j < m) {
            out[k++] = b[j++];
        }

        return out;
    }

    private static int[] distinct(int[] a) {
        HashSet<Integer> set = new HashSet<>();
        for (int item : a) {
            set.add(item);
        }
        int[] out = new int[set.size()];
        int i = 0;
        for (int item : set) {
            out[i] = item;
            i++;
        }
        return out;
    }
}