package aisd.q05_vector;

import java.util.Random;

public class Main {
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        Vector<Integer> v = new Vector<>(4);

        for (int n = 4; n <= 32; n *= 2) {
            while (v.size() < n) {
                v.pushBack(random.nextInt(100));
            }
            System.out.printf("size=%2d capacity=%2d %s%n", v.size(), v.capacity(), v);
        }

        System.out.println("get(0)  = " + v.get(0));
        System.out.println("pop()   = " + v.pop());
        System.out.println("size    = " + v.size());
    }
}

interface DynamicArray<T> {
    void pushBack(T elem);
    T get(int index);
    T pop();
    int size();
}

class Vector<T> implements DynamicArray<T> {
    private T[] data;
    private int size;

    @SuppressWarnings("unchecked")
    public Vector(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("capacity < 0: " + capacity);
        this.data = (T[]) new Object[capacity];
    }

    public Vector() {
        this(32);
    }

    @Override
    public void pushBack(T elem) {
        if (size == data.length) {
            resize();
        }
        data[size] = elem;
        size++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Yeah, you're OoB, shithead.");
        return data[index];
    }

    @Override
    public T pop() {
        if (size == 0) throw new IndexOutOfBoundsException("Yeah, you're OoB, shithead.");
        size--;
        T elem = data[size];
        data[size] = null; // отпускаем ссылку, иначе объект не соберёт GC
        return elem;
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        T[] oldData = data;
        data = (T[]) new Object[Math.max(1, oldData.length * 2)];
        System.arraycopy(oldData, 0, data, 0, size);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        return sb.append(']').toString();
    }
}