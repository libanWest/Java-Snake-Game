import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

//Variable initialisation
    static final int screen_width = 600;
    static final int screen_height = 600;
    static final int grid_size = 25; // how big we want objects on the screen / grid cell size
    static final int game_objects  = (screen_width / grid_size) * (screen_height / grid_size);// how many objects we can fit
    static final int delay = 75; // higher the number, the slower the gameplay
//Arrays of game objects
    final int[] x = new int[game_objects];
    final int [] y = new int[game_objects];
//Snake & food
    int bodyPart = 6; // how long grid size
    int foodEaten;
    int foodX; // random each time the food is eaten
    int foodY; // random each time the food is eaten
//Movement & Timer
    char direction = 'R' ; // starting Direction
    boolean gameRunning = false;
    Timer timer;
    Random random;
//Constructor
    GamePanel(){
    random = new Random(); //instance of random Class
    this.setPreferredSize(new Dimension(screen_width,screen_height)); //set the screen size
    this.setBackground(new Color(147, 138, 138, 182));
    this.setFocusable(true);
    this.requestFocusInWindow();
    this.addKeyListener(new myKeyAdapter()); // instance of inner class
    startGame();
    }
//Methods
    public void startGame(){
        newFood(); // call method to create food
        gameRunning = true;
        timer = new Timer(delay,this); //creates a periodic event that triggers the actionPerformed() method at regular intervals
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if (gameRunning) {
         /*   //grid
            g.setColor(Color.WHITE);
            for (int i = 0; i < screen_height / grid_size; i++) {
                g.drawLine(i * grid_size, 0, i * grid_size, screen_height);
                g.drawLine(0, i * grid_size, screen_width, i * grid_size); // two loops because we want grid to be fully rendered first
            } */
            //food
            g.setColor(new Color(190, 7, 23, 182));
            g.fillOval(foodX, foodY, grid_size, grid_size);
            //snake
            for (int i = 0; i < bodyPart; i++) {
                if (i == 0) {
                    g.setColor(new Color(21, 57, 9)); // head if its 0 index
                    g.fillRoundRect(x[i], y[i], grid_size, grid_size, 8, 8);
                } else {
                    g.setColor(new Color(30, 82, 15));// rest of body
                    g.fillRoundRect(x[i], y[i], grid_size, grid_size, 8, 8);
                }
            }
            g.setColor(new Color(30, 82, 15, 255));
            g.setFont( new Font("Ink Free",Font.BOLD, 20));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Score: "+foodEaten, (screen_width- metrics1.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    public void move(){
        for(int i = bodyPart; i > 0; i--){
        x[i] = x[i-1]; // switch case shifts head to right, first body part is drawn as it takes previous head coordinate & so on
        y[i] = y[i-1]; // any new body part being added will also follow previous body part coordinate. ensures they are straight like a snake
        }              // resulting in the appearance of a continuous snake body as it moves on the game grid.
        switch (direction) {
            case 'U' -> y[0] = y[0] - grid_size; // its zero because we move the head and the rest of the body follows
            case 'D' -> y[0] = y[0] + grid_size;
            case 'L' -> x[0] = x[0] - grid_size;
            case 'R' -> x[0] = x[0] + grid_size;
        }
    }
    public void checkFood(){
        if(x[0] == foodX && y[0] == foodY){
            foodEaten++;
            bodyPart++;
            newFood();
        }
    }
    public void newFood(){
    foodX = random.nextInt((int) (screen_width/ grid_size)) * grid_size;
    foodY = random.nextInt((int) (screen_height/ grid_size)) * grid_size;
    }
    public void checkCollision(){
        //checks head vs body collision
        for(int i = bodyPart;i > 0; i--){ // 0 is head
            if((x[0] == x[i]) && (y[0] == y[i])){ // if the head has same coordinate as any body part
                gameRunning = false;
            }
            //collision on left & right border
            if( (x[0] < 0) || x[0] >= screen_width ){
                gameRunning = false;
            }
            //collision on up & down border
            if( (y[0] < 0) || y[0] >= screen_height ){
                gameRunning = false;
            }
            if(!gameRunning){
                timer.stop();
            }
        }
    }
    public void gameOver(Graphics g ){
        if(!gameRunning){
            //Score
            g.setColor(new Color(30, 82, 15, 255));
            g.setFont( new Font("Ink Free",Font.BOLD, 30));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Score: "+foodEaten, (screen_width- metrics1.stringWidth("Score: "+foodEaten))/2, screen_height / 3); //g.getFont().getSize());
            //Game Over text
            g.setColor(new Color(30, 82, 15, 255));
            g.setFont( new Font("Ink Free",Font.BOLD, 60));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Game Over", (screen_width - metrics2.stringWidth("Game Over"))/2, screen_height/2);
            //Restart instruction
            g.setColor(new Color(30, 82, 15, 255));
            g.setFont( new Font("Ink Free",Font.BOLD, 25));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("Press Enter to restart", (screen_width- metrics3.stringWidth("Press Enter to restart"))/2,(screen_height/2) + 70 );
        }

    }
    public void restartGame() {
        if(!gameRunning){
        setVisible(false);
        new GameFrame();
        }

    }
    @Override
    public void actionPerformed(ActionEvent e) { // this is essentially the game loop as long as gameRunning is true
        if (gameRunning){ //to move our snake & show body
            move();       //move it
            checkFood();  //check if we eat
            checkCollision();
        }
        repaint(); // rub the snake out if game ends
    }

    public class myKeyAdapter extends KeyAdapter{ //inner class to override
        @Override
        public void keyPressed(KeyEvent e) {

            //stop 360 turns!
            if (gameRunning){
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
                }
            }
            else if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
               restartGame();
            }
        }
    }
}
