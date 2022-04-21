package controller;

import model.Model;

public class Main {
    public static void main(String[] args) {
        Model m = new Model();
        Controller c = new Controller(m);
        c.start();
    }
}
