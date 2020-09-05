package kz.ildar.sandbox.utils.leetcode.interview;

class InterviewJava {

    private InterviewJava() {
        //do nothing
    }

    public static int[] sumOptimal(int[] array) {
        int[] output = new int[array.length];

        int sum = 0;
        for (int value : array) {
            sum += value;
        }

        for (int i = 0; i < array.length; i++) {
            output[i] = sum - array[i];
        }
        return output;
    }


    /**
     * Дан массив [array] длиной N
     * Нужно вычислить массив, где каждое i-e значение -
     * это сумма всех значений массива [array], кроме значения на i
     * array:  [2, 3, 1]
     * output: [4, 3, 5]
     */
    public static int[] sumExceptI(int[] array) {
        int[] output = new int[array.length];
        for (int i = 0; i < output.length; i++) {    // i = 0, 1, 2

            for (int a = 0; a < output.length; a++) {   // a = 1, 2   ++++++

                if (a != i) {
                    output[i] += array[a];         // o[0] = a[1]+a[2]
                }
            }
        }
        Object a;

        return output;               // [4, ]
    }

}
