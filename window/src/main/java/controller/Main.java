package controller;


import view.SwingWindow;

public class Main {
    public static void main(String[] args) {
        SwingWindow GUI = new SwingWindow();
        GUI.startWindow();

        GUI.repaint();
    }
}
