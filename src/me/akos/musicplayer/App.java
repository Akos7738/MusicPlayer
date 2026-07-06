package me.akos.musicplayer;

import javax.swing.*;

public class App {
    static void main() {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new GUI().setVisible(true);
        }
    });
    }
}
