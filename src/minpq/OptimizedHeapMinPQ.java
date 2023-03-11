package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class OptimizedHeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of element-priority pairs.
     */
    private final List<PriorityNode<E>> elements;
    /**
     * {@link Map} of each element to its associated index in the {@code elements} heap.
     */
    private final Map<E, Integer> elementsToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        elements = new ArrayList<>();
        elements.add(new PriorityNode<>(null, (-1 * Double.POSITIVE_INFINITY)));
        elementsToIndex = new HashMap<>();
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        elements.add(new PriorityNode<>(element, priority));
        elementsToIndex.put(element, elements.size() - 1);
        swim(elements.size() - 1);
    }

    @Override
    public boolean contains(E element) {
        return elementsToIndex.containsKey(element);
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return elements.get(1).element();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        E result = elements.get(1).element();
        swap(1, elements.size()-1);
        elements.remove(elements.size()-1);
        elementsToIndex.remove(result);
        sink(1);
        return result;
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        int n = elementsToIndex.get(element);
        double oldPriority = elements.get(n).priority();
        elements.set(n, new PriorityNode<>(element, priority));
        if(priority < oldPriority){
            swim(n);
        } else { sink(n);}
    }

    @Override
    public int size() {
        return elements.size() - 1;
    }

    private boolean greater(int x, int y){
        if(x >= elements.size() || y >= elements.size()){
            return false;
        }
        return (elements.get(x).priority() > elements.get(y).priority());
    }

    private void swap(int x, int y){
        PriorityNode<E> temp = elements.get(x);
        elements.set(x, elements.get(y));
        elements.set(y, temp);
        elementsToIndex.put(elements.get(x).element(), x);
        elementsToIndex.put(elements.get(y).element(), y);
        //System.out.println(isMinHeapOrdered(1));
    }

    private void swim(int n){
        while(n > 1 && greater(n/2, n)){
            swap(n/2, n);
            n /= 2;
        }
    }

    private void sink(int n){
        while(n * 2 < elements.size()){
            int m = n * 2;
            if(m < elements.size()-1 && greater(m, m+1)){
                m++;
            }
            if(!greater(n, m)) break;
            swap(m, n);
            n = m;
        }
    }
}