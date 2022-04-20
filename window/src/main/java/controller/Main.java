package controller;


public class Main {
    public static void main(String[] args) {
        System.out.println("HELLO I AM VIEW");

        if (args.length == 0) {
            System.out.println("local host");
        } else {
            System.out.println(args[1]);
        }
    }
}
