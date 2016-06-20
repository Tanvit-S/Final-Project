
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
/**
 *
 * @author dhalt0019
 */
public class Game extends JComponent implements KeyListener {

    // Height and Width of my game
    static final int WIDTH = 600;
    static final int HEIGHT = 700;

    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;

    // create pipe array
    ArrayList<Rectangle> pipes = new ArrayList<>();

    //blockguy was born
    Rectangle blockguy = new Rectangle(60, 100, 60, 60);

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
    // back jump boolean 
    boolean backjump = false;
    //game done variable
    boolean done = false;
    //make another screen
    int screen = 0;
    //create a redo boolean so game retarts
    boolean redo = false;
    //create a death boolean so game stops
    boolean death = false;
    //create all fonts 
    Font Largefont = new Font("Times New Roman", Font.BOLD, 50);
    Font Smallfont = new Font("Times New Roman", Font.BOLD, 25);
    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    @Override
    public void paintComponent(Graphics g) {
        //make color for the ground
        g.clearRect(0, 0, WIDTH, HEIGHT);
        //draw title screen
        if (screen == 0) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(Largefont);
            g.drawString("Block Jump", 150, 100);
            g.setFont(Smallfont);
            g.drawString("Press Enter To Start", 160, 200);

        }
        //if screen is 1 draw everything
        if (screen == 1) {
            g.setColor(Color.BLACK);
            
            g.fillRect(0, 0,WIDTH,HEIGHT);

            g.setColor(Color.BLUE);
            //go through all of the blocks in the array and draw them 
            for (Rectangle block : pipes) {
                // draw the blocks
                g.fillRect(block.x, block.y, block.width, block.height);
                
            }
            g.setColor(Color.RED);
            g.fillRect(blockguy.x, blockguy.y, 50, 50);
            //set color to white
            g.setColor(Color.WHITE);
            //fill ground
            g.fillRect(0, 647, 600, HEIGHT - 647);
            //SET COLOR TO BLACK
            g.setColor(Color.BLACK);
            //DRAW GROUND OUTLINE
            g.fillRect(0, 647, 600, 3);

            //SET COLOR TO WHITE
            g.setColor(Color.WHITE);
            g.setFont(Largefont);
            //pipe counter on the screen
            g.drawString("" + counter / 2, 275, 100);

                //if game ends 
                 if (death) {
                g.setFont(Largefont);
                //gameover
                g.drawString("GAMEOVER", 125, 200);
                //small font
                g.setFont(Smallfont);
                //score 
                g.drawString(" SCORE: " + counter / 2, HEIGHT / 4, 275);
                //restart screen 
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

            //gravity
                movey = movey + gravity;
                //jumping 
                if (jump && !backjump) {
                    //change in y direction
                    movey = -15;
                    backjump = true;
                    

                }

                //move blockguy in y direction 
                blockguy.y = blockguy.y + movey;
                //if blockguy becomes lower than floor
                if (blockguy.y + blockguy.height > 647) {
                    blockguy.y = 647 - blockguy.height;
                    movey = 0;

                }

                //decrease the x value by 5 to move the blocks 
                for (Rectangle block : pipes) {
                    block.x = block.x - blockSpeed;
                    if (blockguy.intersects(block)) {
                        death = true;

                    }

                }
                //when the block gets to x = -2500 respawn at x=500
                for (Rectangle block : pipes) {
                    if (block.x == -2500) {
                        block.x = 500;

                    }
                }

                //if blockguy hits the ground or if he hits the roof, end game 
                if (blockguy.y == 597 || blockguy.y <= 0){
                    death = true;

                }

                for (Rectangle block : pipes) {
                    if (block.x + 50 == blockguy.x) {
                        
                        //add 1 to counter each time 
                        counter++;

                    }

                }

            }
            //if restart is equal to true restart the game
            if (redo == true) {
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
        //if enter is clicked screen goes to 1
        //if enter is hit the screen changes to 1 and game will start 
        if (key == KeyEvent.VK_ENTER && screen == 0) {
            screen = 1;
        }
        //if "R" is clicked and the game is over then restart the game 
        if (key == KeyEvent.VK_R && death == true) {
            redo = true;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        //if space bar is pressed blockguy jumps 
        if (key == KeyEvent.VK_SPACE) {
            jump = false;
            backjump = false;

        }

    }
    //create method that will initaize all drawings and variables 
    public void initialize() {
        pipes.clear();//redraw all drawings
        //draw top pipes
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
        //blockguy is born again
        blockguy = new Rectangle(60, 100, 50, 50);
        movey = 0;
        gravity = 1;
        pipe = 0;
        counter = 0;
        jump = false;
        backjump = false;
        done = false;
        screen = 0;
        redo = false;
        death = false;
    }
}
