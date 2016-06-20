
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author lalim9800
 */
public class Game extends JComponent implements KeyListener, MouseMotionListener, MouseListener {

    // Height and Width of our game
    static final int WIDTH = 600;
    static final int HEIGHT = 700;

    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    //create a int to stote the first pipe x value 

    ArrayList<Rectangle> pipes = new ArrayList<>();

    //create player 
    Rectangle player = new Rectangle(60, 100, 60, 60);

    //ceate a integer for the y value 
    int movey = 0;
    //create gravity integer 
    int gravity = 1;
    int thispipe = 0;
    int counter = 0;
    //create a speed integer for the pipes
    int blockSpeed = 5;
    //create jump variable (keyboard variables)
    boolean jump = false;
    //previous jump boolean 
    boolean pjump = false;
    //make done varible stops 
    boolean done = false;
    //make second screen
    int screen = 0;
    //create a resatart boolean(restart game)
    boolean restart = false;
    //create a boolean dead to that game can stop 
    boolean death = false;

    
    //create all fonts 
    Font Coolfont = new Font("Helvetica", Font.BOLD, 50);
    Font Smallfont = new Font("Helvetica", Font.BOLD, 25);
//load title screen image 
    BufferedImage Droid = loadImage("Android_robot.svg.png");
    //load flappy bird image 
    BufferedImage backround = loadImage("Sky-Blue-Background-Images.jpg");
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
       
        g.clearRect(0, 0, WIDTH, HEIGHT);

        //draw the title screen
        if (screen == 0) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(Coolfont);
            g.drawString("ANDRIOD JUMP ", 100, 100);
            g.setFont(Smallfont);
            g.drawString("Press Enter To Start", 160, 200);
            g.drawImage(Droid, 175, 200, 200, 200, null);

        }
        //if screen is = to 1 draw everything
        if (screen == 1) {
            g.setColor(Color.BLACK);
            
            g.fillRect(0, 0,WIDTH,HEIGHT);

            g.setColor(Color.BLUE);
            //go through all of the blocks in the array and draw them 
            for (Rectangle block : pipes) {
                // draw the block
                g.fillRect(block.x, block.y, block.width, block.height);

            }
            g.setColor(Color.RED);
            g.fillRect(player.x, player.y, 50, 50);
          //draw Droid
            g.drawImage(Droid, player.x - 30, player.y - 30, 60, 60, null);
            //set color to green 
            g.setColor(Color.WHITE);
            //fill ground
            g.fillRect(0, 647, 600, HEIGHT - 647);
            //SET COLOR TO BLACK
            g.setColor(Color.BLACK);
            //DRAW GROUND OUTLINE
            g.fillRect(0, 647, 600, 3);

            //SET COLOR TO BLACK 
            g.setColor(Color.WHITE);
            g.setFont(Coolfont);
            //out put the pipe counter to the screen
            g.drawString("" + counter / 2, 500, 100);

                if (death) { //if game ends 
                //set new font created 
                g.setFont(Coolfont);
                //output gameover (tell user game is over )
                g.drawString("GAMEOVER", 125, 200);
                //set custom font "small"
                g.setFont(Smallfont);
                //reveal score 
                g.drawString(" SCORE: " + counter / 2, HEIGHT / 4, 275);
                //print out  the retart key on screen 
                g.drawString("Press R to restart Game", 150, 350);
                // game drawing ends here 

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
            if (screen == 1 && death == false) {

            //gravity pulling player down
                movey = movey + gravity;
                //jumping 
                if (jump && !pjump) {
                    //make a big change in y direction
                    movey = -15;
                    pjump = true;
                    

                }

                //move player in y direction 
                player.y = player.y + movey;
                //if feet of playet become lower than floor
                if (player.y + player.height > 647) {
                    player.y = 647 - player.height;
                    movey = 0;

                }

                // go through all blocks and decrase the x value by 5 to move blocks 
                for (Rectangle block : pipes) {
                    block.x = block.x - blockSpeed;
                    if (player.intersects(block)) {
                        //play block hitting sound 

                        death = true;

                    }

                }
                //when the block gets to x=-2500 respawn at x=500
                for (Rectangle block : pipes) {
                    if (block.x == -2500) {
                        block.x = 500;

                    }
                }

                //if player hits the ground or if player hits the roof end game 
                if (player.y == 597 || player.y <= 0) {//play block hitting sounds(dead sound)
                    death = true;

                }

                for (Rectangle block : pipes) {
                    if (block.x + 50 == player.x) {
                        //play counter noise 

                        //add 1 to the counter each time 
                        counter++;

                    }
                    //if dead is true(gameover) play sound 
                    if (death == true) {
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
        if (key == KeyEvent.VK_ENTER && screen == 0) {
            screen = 1;
        }
        //if "R" is clicked and the game is over then restart the game 
        if (key == KeyEvent.VK_R && death == true) {
            restart = true;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        //if space bar is pressed player jumps 
        if (key == KeyEvent.VK_SPACE) {
            jump = false;
            pjump = false;

        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
//create a method that will initaize all drawings and variables 

    public void initialize() {
        pipes.clear();//clear all drawings 
        pipes.add(new Rectangle(1000, 0, 100, 200));
        pipes.add(new Rectangle(1500, 0, 100, 300));
        pipes.add(new Rectangle(2000, 0, 100, 100));
        pipes.add(new Rectangle(2500, 0, 100, 300));
        pipes.add(new Rectangle(3000, 0, 100, 350));
        pipes.add(new Rectangle(3500, 0, 100, 100));

        //draw bottom pipes 
        pipes.add(new Rectangle(1000, 450, 100, 647 - 450));
        pipes.add(new Rectangle(1500, 550, 100, 647 - 550));
        pipes.add(new Rectangle(2000, 350, 100, 647 - 350));
        pipes.add(new Rectangle(2500, 550, 100, 647 - 550));
        pipes.add(new Rectangle(3000, 600, 100, 647 - 600));
        pipes.add(new Rectangle(3500, 350, 100, 647 - 350));
        //reset all varibles 
        counter = 0;
        //redraw player at original starting point
        player = new Rectangle(60, 100, 50, 50);
        movey = 0;
        gravity = 1;
        thispipe = 0;
        counter = 0;
        jump = false;
        pjump = false;
        done = false;
        screen = 0;
        restart = false;
        death = false;

    }
}
