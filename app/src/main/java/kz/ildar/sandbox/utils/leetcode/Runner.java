package kz.ildar.sandbox.utils.leetcode;

import java.util.HashMap;
import java.util.Hashtable;

public class Runner {
    public static void main(String[] args) {
//        Solution.Companion.overflowIssues();
//        ParenthesisKt.parenthesis(1);
//        ParenthesisKt.parenthesis(2);
//        ParenthesisKt.parenthesis(3);
//        stringCollisions();
        System.out.println(CheckingKt.value());
    }

    private static void stringCollisions() {
        System.out.println("Aa hash: " + "Aa".hashCode());
        System.out.println("BB hash: " + "BB".hashCode());
        System.out.println("Siblings hash: " + "Siblings".hashCode());
        System.out.println("Teheran hash: " + "Teheran".hashCode());
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Aa", 1);
        map.put("BB", 2);
        System.out.println("map Aa: " + map.get("Aa"));
        System.out.println("map BB: " + map.get("BB"));
        new Hashtable();
    }
}
