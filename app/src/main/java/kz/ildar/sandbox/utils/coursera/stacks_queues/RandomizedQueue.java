package kz.ildar.sandbox.utils.coursera.stacks_queues;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        n = 0;
        items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        items[n++] = item;
        if (n == items.length) resize(n * 2);
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        if (n >= 0) System.arraycopy(items, 0, copy, 0, n);
        items = copy;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        int index = StdRandom.uniform(n);
        Item item = items[index];
        items[index] = items[n - 1];
        items[--n] = null;
        if (n > 0 && n == items.length / 4) resize(items.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        return items[StdRandom.uniform(n)];
    }

    @Override
    public String toString() {
        return "RandomizedQueue{" +
                "n=" + n +
                ", items=" + Arrays.toString(items) +
                '}';
    }

    public String print() {
        StringBuilder sb = new StringBuilder(n + ": ");
        for (Item item : this) {
            sb.append(item).append(" ");
        }
        return sb.toString();
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item> {
        private final int[] order;
        private int i = n;

        public RandomizedIterator() {
            order = new int[i];
            for (int j = 0; j < i; ++j) {
                order[j] = j;
            }
            StdRandom.shuffle(order);
        }

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("no more items");
            return items[order[--i]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (true) {
            String input = StdIn.readString();
            switch (input) {
                case "-": {
                    System.out.println("dequeue: " + queue.dequeue());
                    break;
                }
                case "sa": {
                    System.out.println("sample: " + queue.sample());
                    break;
                }
                case "s": {
                    System.out.println("size: " + queue.size());
                    break;
                }
                case "e": {
                    System.out.println("isEmpty: " + queue.isEmpty());
                    break;
                }
                case "r": {
                    System.out.println("print: " + queue.print());
                    break;
                }
                case "exit": {
                    System.out.println("exiting");
                    return;
                }
                default: {
                    queue.enqueue(input);
                }
            }
            System.out.println(queue);
        }
    }
}