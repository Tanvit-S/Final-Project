package game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JFrame;
/**
 *
 * @author dhalt0019
 */
public class Game extends JComponent implements KeyListener{

    // Height and Width of our game
    static final int WIDTH = 600;
    static final int HEIGHT = 700;

    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    //create a int to stote the first pipe x value 

    ArrayList<Rectangle> blocks = new ArrayList<>();

    //create player 
    Rectangle android = new Rectangle(60, 100, 60, 60);

    //ceate a integer for the y value 
    int movey = 0;
    //create gravity integer 
    int gravity = 1;
    int pipe = 0;
    int counter = 0;
    //create a speed integer for the pipes
    int blockSpeed = 5;
    //create jump variable (keyboard variables)
    boolean jump = false;
    //previous jump boolean 
    boolean backjump = false;
    //make done varible stops 
    boolean done = false;
    //make second screen
    int screen = 0;
    //create a resatart boolean(restart game)
    boolean restart = false;
    //create a boolean dead to that game can stop 
    boolean dead = false;

    //create new fonts
    Font newFont = new Font("Times New Roman", Font.BOLD, 50);
    Font small = new Font("Times New Roman", Font.BOLD, 25);
//load title screen image 
        Rectangle back = new Rectangle(800, 600, 50, 50);
        BufferedImage backround = loadImage("UZKEjzG.png");
    //load flappy bird image  
    Rectangle player = new Rectangle(60, 100, 60, 60);    
    BufferedImage redandroid = loadImage("droid-logo-red.png");
//loading images method 
    public BufferedImage loadImage(String filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (Exception e) {
            System.out.println("Error loading " + filename);
        }
        return img;
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {

// always clear the screen first!
        //make new color for the ground
        Color Ggreen = new Color(159, 242, 131);

        g.clearRect(0, 0, WIDTH, HEIGHT);
        
        //draw rules screen 
        if (screen == 2) {
            //setcoor to black
            g.setColor(Color.black);
            //fill backround
            g.fillRect(0, 0, WIDTH, HEIGHT);
            //set color to whte 
            g.setColor(Color.WHITE);
            //set the font 
            g.setFont(small);

            g.drawString("Press Enter To Start", 200, 300);

        }

        //draw the title screen (screen 0 is title screen)
        if (screen == 0) {
            Color BLACK= new Color(5, 69, 3);
            g.drawImage(backround, 0, 0, WIDTH, HEIGHT, null);
            g.setFont(newFont);
            g.setColor(BLACK);

            g.drawString("AndroidJump ", 10, 100);
            g.setFont(small);
            g.drawString("Press C To Continue ", 160, 200);

        }
        //if screen is 1 (main game green)draw all the game graphics 
        if (screen == 1) {
            
            Color Backround = new Color(144, 215, 245);
            // GAME DRAWING GOES HERE
            
            
            g.setColor(Backround);
           
            //go through all of the blocks in the array and draw them 
            for (Rectangle block : blocks) {
                // draw the block
                g.fillRect(block.x, block.y, block.width, block.height);

            }

            //set color to yellow
            g.setColor(Color.yellow);
            //draw bird 
            g.drawImage(redandroid, android.x - 30, android.y - 30, 105, 110, null);
            //set color to green 
            g.setColor(Ggreen);
            //SET COLOR TO BLACK
            g.setColor(Color.BLACK);
            //DRAW GROUND OUTLINE
            g.fillRect(0, 647, 600, 3);

            //SET COLOR TO BLACK 
            g.setColor(Color.black);
            g.setFont(newFont);
            //out put the pipe counter to the screen
            g.drawString("" + counter / 2, 500, 100);

            // Ask Lamont why this is messing up, doesn't need to be in the game but it would be a nice feature
            //if (counter % 10 == 0 && counter != 0) {
            // blockSpeed += 1;
            
            //if dead is equal to true(gameover) print score,gameover,and the restart key 
            //}
            if (dead) { //if game ends 
                //set new font created 
                g.setFont(newFont);
                //output gameover (tell user game is over )
                g.drawString("GAMEOVER", 125, 200);
                //set custom font "small"
                g.setFont(small);
                //when game is over tell user there score
                g.drawString(" SCORE: " + counter / 2, HEIGHT / 4, 275);
                //print out  the retart key on screen 
                  g.drawString("Press R to restart", 150, 350);
                // game drawing ends 
                
            }

        }
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        
        
        //call initialize method 
        initialize();

        //end initail things 
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime = 0;
        long deltaTime;

        // the main game loop section
        // game will end if you set done = false;
        while (!done) {

            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            //if screen is 1 then start game 
            if (screen == 1 && dead == false) {

//gravity pulling player down
                movey = movey + gravity;
                //jumping 
                if (jump && !backjump) {
                    //make a big change in y direction
                    movey = -15;
                    backjump = true;

                }

                //move player in y direction 
                android.y = android.y + movey;
                //if feet of playet become lower than floor
                if (android.y + android.height > 647) {
                    android.y = 647 - android.height;
                    movey = 0;

                }

                // go through all blocks and decrase the x value by 5 (moving function)
                for (Rectangle block : blocks) {
                    block.x = block.x - blockSpeed;
                    if (android.intersects(block)) {
                       
                        dead = true;

                    }

                }
                //when the block gets to x=-2500 respawn at x=500
                for (Rectangle block : blocks) {
                    if (block.x == -2500) {
                        block.x = 500;

                    }
                }

                //if player hits the ground or if player hits the roof end game 
                if (android.y == 597 || android.y <= 0) {//play block hitting sounds(dead sound)

                    dead = true;

                }

                for (Rectangle block : blocks) {
                    if (block.x + 50 == android.x) {
                        //play counter noise 

                        //add 1 to the counter each time 
                        counter++;

                    }
                    //if dead is true(gameover) play sound 
                    if (dead == true) {
                        //play game over music 


                    }

                }

            }
            //if restart is equal to true restart the game
            if (restart == true) {
                initialize();
                screen = 1;

            }

            // GAME LOGIC ENDS HERE 
            //
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            if (deltaTime > desiredTime) {
                //took too much time, don't wait
            } else {
                try {
                    Thread.sleep(desiredTime - deltaTime);
                } catch (Exception e) {
                };
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");

        // creates an instance of my game
        Game game = new Game();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(game);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //use space bar to jump feature 
        if (key == KeyEvent.VK_SPACE) {
            jump = true;
        }
        //if enter is clicked change to screen 1 
        //if enter is hit the screen changes to 1 and game will start 
        if (key == KeyEvent.VK_ENTER && screen == 2) {
            screen = 1;
        }
        //if "R" is clicked and the game is over then restart the game 
        if (key == KeyEvent.VK_R && dead == true) {
            restart = true;

        }
        //if c is clicked then go to rules screen
        if (key == KeyEvent.VK_C) {
            //set screen to 2 
            screen = 2;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        //if space bar is pressed player jumps 
        if (key == KeyEvent.VK_SPACE) {
            jump = false;
            backjump = false;

        }

    }

    
//create a method that will initaize all drawings and variables 

    public void initialize() {
        blocks.clear();//clear all drawings 
        blocks.add(new Rectangle(1000, 0, 100, 200));
        blocks.add(new Rectangle(1500, 0, 100, 300));
        blocks.add(new Rectangle(2000, 0, 100, 100));
        blocks.add(new Rectangle(2500, 0, 100, 300));
        blocks.add(new Rectangle(3000, 0, 100, 350));
        blocks.add(new Rectangle(3500, 0, 100, 100));

        //draw bottom pipes 
        blocks.add(new Rectangle(1000, 450, 100, 647 - 450));
        blocks.add(new Rectangle(1500, 550, 100, 647 - 550));
        blocks.add(new Rectangle(2000, 350, 100, 647 - 350));
        blocks.add(new Rectangle(2500, 550, 100, 647 - 550));
        blocks.add(new Rectangle(3000, 600, 100, 647 - 600));
        blocks.add(new Rectangle(3500, 350, 100, 647 - 350));
        //reset all varibles 
        counter = 0;
        //redraw player at original starting point
        android = new Rectangle(60, 100, 50, 50);
        movey = 0;
        gravity = 1;
        pipe = 0;
        counter = 0;
        jump = false;
        backjump = false;
        done = false;
        screen = 0;
        restart = false;
        dead = false;

    }
}

