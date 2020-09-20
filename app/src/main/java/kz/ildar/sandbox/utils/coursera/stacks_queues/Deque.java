package kz.ildar.sandbox.utils.coursera.stacks_queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;

public class Deque<Item> implements Iterable<Item> {

    private class Node{
        Item value;
        Node next;
        Node previous;

        public Node(Item value, Node next, Node previous) {
            this.value = value;
            this.next = next;
            this.previous = previous;
        }
    }

    private int n;
    private Node first;
    private Node last;

    // construct an empty deque
    public Deque() {
        n = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        n++;
        Node oldFirst = this.first;
        this.first = new Node(item, oldFirst, null);

        if (oldFirst == null) {
            last = this.first;
        } else {
            oldFirst.previous = first;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        n++;
        Node oldLast = this.last;
        this.last = new Node(item, null, oldLast);

        if (oldLast == null) {
            first = this.last;
        } else {
            oldLast.next = last;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        n--;
        Item item = first.value;
        first = first.next;
        if (first == null) last = null;
        else first.previous = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        n--;
        Item item = last.value;
        last = last.previous;
        if (last == null) first = null;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) throw new NoSuchElementException("no more items");
                Item item = current.value;
                current = current.next;
                return item;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(n + ": ");
        for (Item item : this) {
            sb.append(item).append(" ");
        }
        return sb.toString();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addFirst("1");
        deque.addLast("1");
        deque.size();
        deque.removeFirst();
        deque.removeLast();
        deque.isEmpty();
        while (true) {
            String input = StdIn.readString();
            switch (input) {
                case "-": {
                    System.out.println("removed first: " + deque.removeFirst());
                    break;
                }
                case "--": {
                    System.out.println("removed last: " + deque.removeLast());
                    break;
                }
                case "s": {
                    System.out.println("size: " + deque.size());
                    break;
                }
                case "e": {
                    System.out.println("isEmpty: " + deque.isEmpty());
                    break;
                }
                case "exit": {
                    System.out.println("exiting");
                    return;
                }
                case "last": {
                    deque.addLast(input);
                    break;
                }
                default: {
                    deque.addFirst(input);
                }
            }
            System.out.println(deque);
        }
    }
}