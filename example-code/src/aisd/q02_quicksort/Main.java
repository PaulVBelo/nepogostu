package aisd.q02_quicksort;

import java.util.Arrays;
import java.util.Random;

public class Main {
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        System.out.println("=== QuickSort Example ===");
        int[] arrayToSort = randomIntArray(25);
        System.out.println("Before: " + Arrays.toString(arrayToSort));

        Demo.quickSort(arrayToSort);
        System.out.println("After:  " + Arrays.toString(arrayToSort));
        // ==========================================================================

        System.out.println("\n=== QuickSelect (k-th order statistic) Example ===");
        int[] array = randomIntArray(25);
        int k = random.nextInt(0, array.length);

        System.out.println("Array:  " + Arrays.toString(array));
        System.out.println(k + "-th smallest element: " + Demo.quickSelect(array, k));
    }

    private static int[] randomIntArray(int size) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = random.nextInt(0, 1024);
        }
        return result;
    }
}

class Demo {
    private static final Random random = new Random();

    public static void quickSort(int[] array) {
        if (array == null || array.length < 2) return;
        quickSort(array, 0, array.length);
    }

    public static int quickSelect(int[] array, int k) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        if (k < 0 || k >= array.length) {
            throw new IndexOutOfBoundsException("Index " + k + " is out of bounds for length " + array.length);
        }

        int[] copy = array.clone();
        int left = 0;
        int right = copy.length;

        while (left < right) {
            int pivotPos = randomizedPartition(copy, left, right);

            if (pivotPos == k) {
                return copy[pivotPos];
            } else if (pivotPos < k) {
                left = pivotPos + 1;
            } else {
                right = pivotPos;
            }
        }

        throw new IllegalStateException("Unreachable state");
    }

    private static void quickSort(int[] array, int left, int right) {
        if (left >= right - 1) return;

        int pivotPos = randomizedPartition(array, left, right);

        quickSort(array, left, pivotPos);
        quickSort(array, pivotPos + 1, right);
    }

    private static int randomizedPartition(int[] array, int left, int right) {
        int pivotIndex = random.nextInt(left, right);
        swap(array, pivotIndex, right - 1);
        return partition(array, left, right);
    }

    private static int partition(int[] array, int left, int right) {
        int pivot = array[right - 1];
        int i = left;

        for (int j = left; j < right - 1; j++) {
            if (array[j] < pivot) {
                swap(array, i, j);
                i++;
            }
        }

        swap(array, i, right - 1);
        return i;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
