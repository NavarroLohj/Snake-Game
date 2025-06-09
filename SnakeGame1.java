/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame1 extends JFrame {

    public SnakeGame1() {
        setTitle("SnakeGame1 - Java Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);
        pack();

        setLocationRelativeTo(null); 
        setVisible(true);
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new SnakeGame1());
    }

    
    private class GamePanel extends JPanel implements ActionListener {

        private final int PANEL_WIDTH = 600;
        private final int PANEL_HEIGHT = 600;
        private final int UNIT_SIZE = 25; 
        private final int GAME_UNITS = (PANEL_WIDTH * PANEL_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
        private final int DELAY = 100; 

        private final int x[] = new int[GAME_UNITS];
        private final int y[] = new int[GAME_UNITS];
        private int bodyParts = 6; 
        private int applesEaten;
        private int appleX;
        private int appleY;
        private char direction = 'R'; 
        private boolean running = false;

        private Timer timer;
        private final Random random;

        public GamePanel() {
            random = new Random();
            setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
            setBackground(Color.BLACK);
            setFocusable(true);
            addKeyListener(new MyKeyAdapter());
            startGame();
        }

        public final void startGame() {
            newApple();
            running = true;
            timer = new Timer(DELAY, this);
            timer.start();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        public void draw(Graphics g) {
            if (running) {

                
                g.setColor(new Color(40, 40, 40));
                for (int i = 0; i < PANEL_HEIGHT / UNIT_SIZE; i++) {
                    g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, PANEL_HEIGHT);
                    g.drawLine(0, i * UNIT_SIZE, PANEL_WIDTH, i * UNIT_SIZE);
                }

                
                g.setColor(Color.RED);
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

                
                g.setColor(new Color(0, 255, 0));
                g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

                
                for (int i = 1; i < bodyParts; i++) {
                    g.setColor(new Color(0, 155, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

                
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 18));
                FontMetrics metrics = getFontMetrics(g.getFont());
                String scoreText = "Score: " + applesEaten;
                g.drawString(scoreText, (PANEL_WIDTH - metrics.stringWidth(scoreText)) / 2, 25);

            } else {
                gameOver(g);
            }
        }

        public void newApple() {
            appleX = random.nextInt((int)(PANEL_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            appleY = random.nextInt((int)(PANEL_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        }

        public void move() {
            // Move body parts
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i-1];
                y[i] = y[i-1];
            }

            
            switch(direction) {
                case 'U' -> y[0] = y[0] - UNIT_SIZE;
                case 'D' -> y[0] = y[0] + UNIT_SIZE;
                case 'L' -> x[0] = x[0] - UNIT_SIZE;
                case 'R' -> x[0] = x[0] + UNIT_SIZE;
            }
        }

        public void checkApple() {
            if ((x[0] == appleX) && (y[0] == appleY)) {
                bodyParts++;
                applesEaten++;
                newApple();
            }
        }

        public void checkCollisions() {
            // Check if head collides with body
            for (int i = bodyParts; i > 0; i--) {
                if ((x[0] == x[i]) && (y[0] == y[i])) {
                    running = false;
                }
            }

           
            if (x[0] < 0) {
                running = false;
            }

            
            if (x[0] >= PANEL_WIDTH) {
                running = false;
            }

            
            if (y[0] < 0) {
                running = false;
            }

            
            if (y[0] >= PANEL_HEIGHT) {
                running = false;
            }

            if (!running) {
                timer.stop();
            }
        }

        public void gameOver(Graphics g) {
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            String scoreText = "Score: " + applesEaten;
            g.drawString(scoreText, (PANEL_WIDTH - metrics1.stringWidth(scoreText)) / 2, PANEL_HEIGHT / 3);

            
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            String gameOverText = "Game Over";
            g.drawString(gameOverText, (PANEL_WIDTH - metrics2.stringWidth(gameOverText)) / 2, PANEL_HEIGHT / 2);

            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            String restartText = "Press ENTER to Restart";
            g.drawString(restartText, (PANEL_WIDTH - metrics3.stringWidth(restartText)) / 2, PANEL_HEIGHT / 2 + 50);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                move();
                checkApple();
                checkCollisions();
            }
            repaint();
        }

        
        public class MyKeyAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (direction != 'R') {
                            direction = 'L';
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (direction != 'L') {
                            direction = 'R';
                        }
                    }
                    case KeyEvent.VK_UP -> {
                        if (direction != 'D') {
                            direction = 'U';
                        }
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (direction != 'U') {
                            direction = 'D';
                        }
                    }
                    case KeyEvent.VK_ENTER -> {
                        if (!running) {
                            restartGame();
                        }
                    }
                }
            }
        }

        public void restartGame() {
            bodyParts = 6;
            applesEaten = 0;
            direction = 'R';
            for (int i = 0; i < bodyParts; i++) {
                x[i] = 0;
                y[i] = 0;
            }
            startGame();
        }
    }
}
