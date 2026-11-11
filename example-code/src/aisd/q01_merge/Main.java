package aisd.q01_merge;


import java.util.Arrays;
import java.util.Random;

public class Main {
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        // ==========================================================================
        System.out.println("=== Merge Example ===");

        int[] A = randomIntArray(8);
        Arrays.sort(A);
        int[] B = randomIntArray(7);
        Arrays.sort(B);

        System.out.println("A: " + Arrays.toString(A));
        System.out.println("B: " + Arrays.toString(B));

        int[] merged = Demo.merge(A, B);
        System.out.println("merge(A, B): " + Arrays.toString(merged));

        // ==========================================================================

        System.out.println("=== MergeSort Example ===");

        int[] arrayToSort = randomIntArray(25);
        System.out.println("Before: " + Arrays.toString(arrayToSort));

        Demo.mergeSort(arrayToSort);
        System.out.println("After: " + Arrays.toString(arrayToSort));

        // ==========================================================================
        System.out.println("=== MergeSort Example ===");

        int[] arrayToCount = randomIntArray(12);
        System.out.println("Array: " + Arrays.toString(arrayToCount));

        System.out.println("Inversions: " + Demo.inversionCount(arrayToCount));
    }

    private static int[] randomIntArray(int size) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) result[i] = random.nextInt(0, 1024);
        return result;
    }
}

class Demo {
    // Merge as described in the related note
    public static int[] merge(int[] A, int[] B) {
        int[] result = new int[A.length + B.length];
        
        int i = 0, j = 0;
        
        while (i < A.length && j < B.length) {
            if (A[i] <= B[j]) {
                result[i+j] = A[i];
                i++;
            } else {
                result[i+j] = B[j];
                j++;
            }
        }

        while (i < A.length) {
            result[i+j] = A[i];
            i++;
        }

        while (j < B.length) {
            result[i+j] = B[j];
            j++;
        }

        return result;
    }

    // MergeSort as described in the related note
    public static void mergeSort(int[] array) {
        mergeSort(array, 0, array.length-1);
    }

    private static void mergeSort(int[] array, int start, int end) {
        if (start >= end) return;

        mergeSort(array, start, start + (end - start) / 2);
        mergeSort(array, start + (end - start) / 2 + 1, end);

        helperMerge(array, start, start + (end - start) / 2, end);
    }

    private static void helperMerge(int[] array, int start, int middle, int end) {
        int[] temp = new int[end + 1 - start];

        int i = 0, j = 0;
        while (i < middle + 1 - start && j < end - middle) {
            if (array[start + i] <= array[middle + 1 + j]) {
                temp[i+j] = array[start + i];
                i++;
            } else {
                temp[i+j] = array[middle + 1 + j];
                j++;
            }
        }

        while (i < middle + 1 - start) {
            temp[i+j] = array[start + i];
            i++;
        }

        while (j < end - middle) {
            temp[i+j] = array[middle + 1 + j];
            j++;
        }

        System.arraycopy(temp, 0, array, start, temp.length);
    }

    // Inversion Count as described in the related note
    public static long inversionCount(int[] array) {
        if (array == null || array.length < 2) return 0;
        return inversionCount(array, 0, array.length-1);
    }

    private static long inversionCount(int[] array, int start, int end) {
        if (start >= end) return 0;

        long left = inversionCount(array, start, start + (end - start) / 2);
        long right = inversionCount(array, start + (end - start) / 2 + 1, end);

        long merge = mergeAndCount(array, start, start + (end - start) / 2, end);

        return left + right + merge;
    }

    private static long mergeAndCount(int[] array, int start, int middle, int end) {
        int[] temp = new int[end + 1 - start];
        long count = 0;

        int i = 0, j = 0;
        while (i < middle + 1 - start && j < end - middle) {
            if (array[start + i] <= array[middle + 1 + j]) {
                temp[i+j] = array[start + i];
                i++;
            } else {
                temp[i+j] = array[middle + 1 + j];
                j++;
                count += middle + 1 - start - i;
            }
        }

        while (i < middle + 1 - start) {
            temp[i+j] = array[start + i];
            i++;
            count += end - middle - j;
        }

        while (j < end - middle) {
            temp[i+j] = array[middle + 1 + j];
            j++;
        }

        System.arraycopy(temp, 0, array, start, temp.length);

        return count;
    }
}