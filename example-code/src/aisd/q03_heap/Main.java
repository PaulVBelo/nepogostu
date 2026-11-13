package aisd.q03_heap;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class Main {
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        System.out.println("--------------------------------------------------");

        List<Integer> list = randomIntList(10);
        System.out.println("Исходный список: " + list);
        BinaryHeap.heapify(list);
        System.out.println("Результат heapify: " + list);
        System.out.println("--------------------------------------------------");

        // ==========================================================================

        list = randomIntList(10);
        System.out.println("Новый список: " + list);
        BinaryHeap<Integer> heap = new BinaryHeap<>(list);
        System.out.println("Куча после инициализации: " + heap);

        System.out.println("Текущий минимум (getMin): " + heap.getMin());

        int newVal1 = -7;
        int newVal2 = 500;
        heap.insert(newVal1);
        heap.insert(newVal2);
        System.out.println("После вставки " + newVal1 + " и " + newVal2 + ": " + heap);
        System.out.println("Новый минимум (getMin): " + heap.getMin());
        System.out.println("--------------------------------------------------");

        System.out.print("Извлекаем все элементы (extractMin): ");
        List<Integer> sorted = new ArrayList<>();
        while (!heap.isEmpty()) {
            sorted.add(heap.extractMin());
        }
        System.out.println(sorted);

        // ==========================================================================

        System.out.println("--------------------------------------------------");
        List<Integer> sortList = randomIntList(10);
        System.out.println("До heapSort: " + sortList);
        BinaryHeap.heapSort(sortList);
        System.out.println("После heapSort: " + sortList);
    }

    private static List<Integer> randomIntList(int size) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(random.nextInt(0, 1024));
        }
        return result;
    }
}

class BinaryHeap<T extends Comparable<? super T>> {
    private final List<T> data;

    public BinaryHeap() {
        this.data = new ArrayList<>();
    }

    public BinaryHeap(List<T> values) {
        this.data = values;
        heapify(data);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public T extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap empty");
        }
        return extractMin(this.data);
    }

    public T getMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap empty");
        }
        return data.get(0);
    }

    public void insert(T value) {
        data.add(value);
        siftUp(data, data.size() - 1);
    }

    public static <U extends Comparable<? super U>> void heapify(List<U> data) {
        for (int i = data.size() / 2 - 1; i >= 0; i--) {
            siftDown(data, i, data.size());
        }
    }

    public static <U extends Comparable<? super U>> void heapSort(List<U> data) {
        heapify(data);

        for (int i = 0; i < data.size() - 1; i++) {
            swap(data, 0, data.size() - i - 1);
            siftDown(data, 0, data.size() - i - 1);
        }

        reverse(data);
    }

    private static <U extends Comparable<? super U>> U extractMin(List<U> list) {
        swap(list, 0, list.size() - 1);
        U out = list.remove(list.size() - 1);
        siftDown(list, 0, list.size());
        return out;
    }

    private static <U extends Comparable<? super U>> void siftUp(List<U> list, int idx) {
        if (idx == 0) return;

        int parent = (idx - 1) / 2;

        if (list.get(parent).compareTo(list.get(idx)) > 0) {
            swap(list, idx, parent);
            siftUp(list, parent);
        }
    }

    private static <U extends Comparable<? super U>> void siftDown(List<U> list, int idx, int actualSize) {
        int left = 2 * idx + 1;
        int right = 2 * idx + 2;

        int candidate = idx;

        if (right < actualSize && list.get(right).compareTo(list.get(candidate)) < 0) {
            candidate = right;
        }

        if (left < actualSize && list.get(left).compareTo(list.get(candidate)) < 0) {
            candidate = left;
        }

        if (candidate != idx) {
            swap(list, idx, candidate);
            siftDown(list, candidate, actualSize);
        }
    }

    private static <U> void swap(List<U> list, int idx1, int idx2) {
        U t = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, t);
    }

    private static <U> void reverse(List<U> list) {
        for (int i = 0; i < list.size() / 2; i++) {
            swap(list, i, list.size() - i - 1);
        }
    }

    @Override
    public String toString() {
        return data.toString();
    }
}