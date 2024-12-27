import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        int frameWidth = 600;
        int frameLength = frameWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(frameWidth, frameLength);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Snake snakeGame = new Snake(frameWidth, frameLength);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();

    }
}