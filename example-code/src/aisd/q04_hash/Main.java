package aisd.q04_hash;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Main {
    private static final Random random = new Random(42);

    private static final int STEPS = 40;
    private static final int KEY_RANGE = 20;

    public static void main(String[] args) {
        demo(new SimpleBucketHashTable<>(2));
    }

    private static void demo(HashTable<Integer> table) {
        System.out.println("=== " + table.getClass().getSimpleName() + " ===");
        System.out.println("start            -> " + table);

        for (int step = 1; step <= STEPS; step++) {
            int x = random.nextInt(2 * KEY_RANGE + 1) - KEY_RANGE;
            int op = random.nextInt(10);
            int sizeBefore = table.size();

            if (op < 5) {
                table.insert(x);
                String note = table.size() == sizeBefore ? "  (уже есть)" : "";
                System.out.printf("%2d. insert(%3d)   -> %s%s%n", step, x, table, note);
            } else if (op < 8) {
                table.delete(x);
                String note = table.size() == sizeBefore ? "  (нет такого)" : "";
                System.out.printf("%2d. delete(%3d)   -> %s%s%n", step, x, table, note);
            } else {
                System.out.printf("%2d. contains(%3d) = %b%n", step, x, table.contains(x));
            }
        }

        System.out.println("final size = " + table.size());
    }
}

interface HashTable<T> {
    public void insert(T value);

    public boolean contains(T value);

    public void delete(T value);

    public int size();
}

class UniversalHashFunction<T> {
    private static final long P = 1_000_000_007;
    private static final Random random = new Random(42);

    private final long a, b;
    private final int m;

    private UniversalHashFunction(long a, long b, int m) {
        this.a = a;
        this.b = b;
        this.m = m;
    }

    public static <T> UniversalHashFunction<T> makeUniversalHashFunction(int m) {
        return new UniversalHashFunction<T>(
                random.nextLong(1, P),
                random.nextLong(0, P),
                m
        );
    }

    public int hash(T value) {
        long x = Integer.toUnsignedLong(value.hashCode());
        return (int) (((a * x + b) % P) % m);
    }
}

class SimpleBucketHashTable<T> implements HashTable<T> {
    private static final int DEFAULT_CAPACITY = 32;
    private static final double MAX_LOAD_FACTOR = 3;

    private List<T>[] table;
    private UniversalHashFunction<T> hashFunction;
    private int size;

    public SimpleBucketHashTable() {
        this(DEFAULT_CAPACITY);
    }

    public SimpleBucketHashTable(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        allocate(capacity);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void allocate(int capacity) {
        table = (List<T>[]) new List[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ArrayList<>();
        }
        hashFunction = UniversalHashFunction.makeUniversalHashFunction(capacity);
    }

    @Override
    public void insert(T value) {
        int hash = hashFunction.hash(value);

        if (find(value, hash) == -1) {
            table[hash].add(value);
            size++;

            if (((double) size) / table.length > MAX_LOAD_FACTOR) {
                resize();
            }
        }
    }

    @Override
    public boolean contains(T value) {
        return find(value, hashFunction.hash(value)) != -1;
    }

    @Override
    public void delete(T value) {
        int hash = hashFunction.hash(value);
        int index = find(value, hash);

        if (index != -1) {
            List<T> chain = table[hash];
            chain.set(index, chain.get(chain.size() - 1));
            chain.remove(chain.size() - 1);

            size--;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private void resize() {
        List<T>[] oldTable = table;

        allocate(oldTable.length * 2);

        for (List<T> l: oldTable) {
            for (T value: l) {
                this.place(value);
            }
        }
    }

    private void place(T value) {
        table[hashFunction.hash(value)].add(value);
    }

    private int find(T value, int hash) {
        List<T> chain = table[hash];
        for (int i = 0; i < chain.size(); i++) {
            if (Objects.equals(chain.get(i), value)) return i;
        }

        return -1;
    }

    // m — число бакетов, n — число элементов, alpha — load factor; дальше бакеты по порядку
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("m=%d, n=%d, alpha=%.2f |", table.length, size, (double) size / table.length));
        for (List<T> chain : table) {
            sb.append(' ').append(chain);
        }
        return sb.toString();
    }
}
