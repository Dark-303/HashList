import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Resizable-array and Hash-table based implementation of the {@code Collection} interface. This
 * class provides a hybrid structure that combines the {@code O(1)} constant-time performance for
 * positional access ({@code get}) and attribute lookups ({@code contains}), while ensuring that all
 * elements in the collection are unique.
 *
 * <p>
 * Unlike a standard {@link HashSet}, this implementation maintains a defined iteration order via an
 * internal {@link ArrayList}. However, please note that the {@link #removeFast(int)} operation
 * performs a "swap-to-back" movement which <i>does not</i> preserve the insertion order of
 * elements.
 *
 * <p>
 * The {@code size}, {@code isEmpty}, {@code get}, {@code contains}, and {@code iterator} operations
 * run in constant time. The {@code add} operation runs in <i>amortized constant time</i>. All other
 * operations run in linear time.
 *
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If multiple threads access a
 * {@code HashList} instance concurrently, and at least one of the threads modifies the list
 * structurally, it <i>must</i> be synchronized externally. This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the collection.
 *
 * <p>
 * The iterators returned by this class's {@link #iterator()} method are <i>fail-fast</i>: if the
 * collection is structurally modified at any time after the iterator is created, in any way except
 * through the iterator's own {@code remove} method, the iterator will throw a
 * {@link ConcurrentModificationException}.
 *
 * @param <T> the type of elements maintained by this collection * @see ArrayList
 * @see HashSet
 * @see AbstractCollection
 */
public class HashList<T> extends AbstractCollection<T> {
    private final ArrayList<T> list;
    private final HashSet<T> set;
    private static final Random random = new Random();

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity is negative
     */
    public HashList(int initialCapacity) {
        this.list = new ArrayList<>(initialCapacity);
        this.set = new HashSet<>(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public HashList() {
        this(10);
    }

    @Override
    public boolean add(T element) {
        if (set.add(element)) {
            return list.add(element);
        }
        return false;
    }

    @Override
    public void clear() {
        list.clear();
        set.clear();
    }

    @Override
    public boolean contains(Object element) {
        return set.contains(element);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public boolean remove(Object o) {
        if (set.remove(o)) {
            return list.remove(o);
        }
        return false;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
        return list.toArray(a);
    }

    /**
     * Inserts the specified element at the specified position in this list. Shifts the element
     * currently at that position (if any) and any subsequent elements to the right (adds one to
     * their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public boolean add(int index, T element) {
        if (set.add(element)) {
            list.add(index, element);
            return true;
        }
        return false;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public T get(int index) {
        return list.get(index);
    }

    /**
     * Returns a random element from this collection.
     *
     * @param rnd the source of randomness
     * @return a random element from this collection
     * @throws IndexOutOfBoundsException if the collection is empty
     */
    public T getRandom(Random rnd) {
        return list.get(rnd.nextInt(list.size()));
    }

    /**
     * Returns a random element from this collection.
     *
     * @return a random element from this collection
     * @throws IndexOutOfBoundsException if the collection is empty
     */
    public T getRandom() {
        return this.getRandom(random);
    }

    /**
     * Returns the index of the specified element in this list, or -1 if this list does not contain
     * the element. More formally, returns the lowest index {@code i} such that
     * {@code Objects.equals(o, get(i))}, or -1 if there is no such index.
     * 
     * @param o element to search for
     * @return the index of the specified element in this list, or -1 if this list does not contain
     *         the element
     */
    public int indexOf(Object o) {
        if (!set.contains(o)) {
            return -1;
        }
        return list.indexOf(o);
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the index of the specified element in this list, or -1 if this list does not contain
     * the element. More formally, returns the highest index {@code i} such that
     * {@code Objects.equals(o, get(i))}, or -1 if there is no such index.
     * 
     * @param o element to search for
     * @return the index of the specified element in this list, or -1 if this list does not contain
     *         the element
     */
    public int lastIndexOf(Object o) {
        if (!set.contains(o)) {
            return -1;
        }
        return list.lastIndexOf(o);
    }

    /**
     * Removes the element at the specified position in this list. Shifts any subsequent elements to
     * the left (subtracts one from their indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public T remove(int index) {
        T element = list.remove(index);
        set.remove(element);
        return element;
    }

    /**
     * Removes the element at the specified position in this list. Replaces the specified position
     * with the last element. Does not preserve the order of the elements in this list.
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public T removeFast(int index) {
        int last = list.size() - 1;
        T element = list.get(index);
        T lastElement = list.get(last);
        if (index != last) {
            list.set(index, lastElement);
        }

        set.remove(element);
        list.remove(last);
        return element;
    }

    /**
     * Randomly permutes this list using the default source of randomness.
     * 
     * @see Collections#shuffle(java.util.List)
     */
    public void shuffle() {
        Collections.shuffle(list);
    }

    /**
     * Randomly permutes this list using the default source of randomness.
     * 
     * @see Collections#shuffle(java.util.List)
     * 
     * @param rnd the source of randomness to use to shuffle the list.
     */
    public void shuffle(Random rnd) {
        Collections.shuffle(list, rnd);
    }
}
