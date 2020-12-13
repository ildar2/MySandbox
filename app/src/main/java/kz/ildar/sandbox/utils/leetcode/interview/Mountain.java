package kz.ildar.sandbox.utils.leetcode.interview;

class Mountain {


    /**
     * by @meliphant
     * bug: second while loop didn't track results of the first one
     *
     * @param A
     * @return
     */
    public static int getLongestMountain(int[] A) {
        int max = 0;
        int increasingLength;
        int decreasingLength;

        for (int i = 1; i < A.length; i++) {
            increasingLength = 0;
            decreasingLength = 0;
            while (i < A.length && A[i] > A[i - 1]) {
                increasingLength++;
                i++;
            }
            while (increasingLength != 0 && i < A.length && A[i] < A[i - 1]) {
                decreasingLength++;
                i++;
            }
            System.out.println("" + (i < A.length ? A[i] : "end") + ": " + increasingLength + " " + decreasingLength);
            max = (increasingLength == 0 || decreasingLength == 0) ? max : Math.max(max, 1 + increasingLength + decreasingLength);

        }

        return max;

    }
}