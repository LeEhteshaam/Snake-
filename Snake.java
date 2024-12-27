import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Snake extends JPanel implements ActionListener, KeyListener {

    private class Tile {
        int x;
        int y;

        private Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    private SoundPlayer backgroundMusic;
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile food;

    Random rand;
    boolean gameOver = false;

    Timer gameLoop;

    int velocityX;
    int velocityY;
    Color darkGreen = Color.GREEN.darker();

    public Snake(int boardWidth, int boardHeight) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setPreferredSize(new Dimension(this.boardHeight, this.boardWidth));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        food = new Tile(10, 10);
        rand = new Random();
        placeFood();

        gameLoop = new Timer(100, this);
        gameLoop.start();

        velocityX = 0;
        velocityY = 0;

        backgroundMusic = new SoundPlayer("music.wav");
        backgroundMusic.loop(); 

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g, snakeHead);
    }

    public boolean collision (Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    protected void draw(Graphics G, Tile tile) {
       
        // Red
        G.setColor(Color.RED);
        G.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);


        // Snake
        G.setColor(darkGreen);
        G.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);


        for (int i = 0; i < snakeBody.size(); i++)  {
            G.setColor(Color.GREEN);
            Tile snakePart = snakeBody.get(i);
            G.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        G.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            G.setColor(Color.RED);
            G.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize -16, tileSize);
        } else {
            G.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        do { 
            food.x = rand.nextInt(boardWidth / tileSize); 
            food.y = rand.nextInt(boardHeight / tileSize);  
        } 
        while (collisionWithSnake(food));
    }

    private boolean collisionWithSnake(Tile tile) {
        // Check if the tile (food) collides with the snake's head or any part of its body
        if (collision(tile, snakeHead)) return true;
        for (Tile snakePart : snakeBody) {
            if (collision(tile, snakePart)) return true;
        }
        return false;
    }

    public void move() {

        // Check if the snake head collides with the food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(snakeHead.x, snakeHead.y)); // Add a new segment to the snake
            placeFood(); // Place a new food item
        }
    
        // Update the snake's body positions
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile previousPart = snakeBody.get(i - 1);
                snakePart.x = previousPart.x;
                snakePart.y = previousPart.y;
            }
        }
    
        // Move the snake's head in the current direction
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    
        // Check for collisions with the snake's own body
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();       
        if (gameOver) {
            gameLoop.stop();
            backgroundMusic.stop();
        }

        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0; 
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0; 
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityY = 0; 
            velocityX = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityY = 0; 
            velocityX = 1;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}
