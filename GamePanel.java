import java.util.Random;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.applet.Applet;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.applet.AudioClip;
import java.awt.event.*;
import java.awt.*;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.util.List;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener 
{                   
    private Jo jo = null;
    private Dio dio = null;
    private Minion minion = null;
    private Environment environment;
    private String stats;

    private Thread gameThread;
    boolean isRunning;

    public GamePanel () {
        environment = new Environment(this);
        setBackground(Color.CYAN);
        addKeyListener(this);           // respond to key events
        setFocusable(true);
        requestFocus();             // the GamePanel now has focus, so receives key events

        gameThread = null;
        isRunning = false;
        System.out.println("GP created!");
    }

    // implementation of Runnable interface

    public void run () {
        try {
            isRunning = true;
            while (isRunning) {
                gameUpdate();
                gameRender();
                Thread.sleep (20); // increase value of sleep time to slow down minion
            }
        }
        catch(InterruptedException e) {}
    }

    // implementation of KeyListener interface

    public void keyPressed (KeyEvent e) {

        if (jo == null)
            return;

        int keyCode = e.getKeyCode();

        if(keyCode == KeyEvent.VK_SPACE && !jo.getCanShoot()){
                jo.setCanShoot(true);
                jo.fire();
               //System.out.println("pressed space");
        }
            
        if (keyCode == KeyEvent.VK_LEFT) {
            jo.movingLeft = true;
            jo.movingRight = false;
        }
        else if (keyCode == KeyEvent.VK_RIGHT) {
            jo.movingRight = true;
            jo.movingLeft = false;
        }
        else if (keyCode == KeyEvent.VK_UP) {
            jo.jump();
        }
    }

    public void keyReleased (KeyEvent e) {
        if (jo == null)
            return;

        int keyCode = e.getKeyCode();

        if(keyCode == KeyEvent.VK_SPACE){
            jo.setCanShoot(false);
        }

        if (keyCode == KeyEvent.VK_LEFT) {
            jo.idle();
        }
        else if (keyCode == KeyEvent.VK_RIGHT) {
            jo.idle();
        }
        else if (keyCode == KeyEvent.VK_UP) {
            jo.land();
        }
    }

    public void keyTyped (KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            jo.jump();
        }
    }

    private void updateMissiles() {

        //List<Missile> missiles = jo.getMissiles();

        for (int i = 0; i < jo.missiles.size(); i++) {

            Missile missile = jo.missiles.get(i);

            if (missile.isVisible()) {
                missile.move();
            } else {
                jo.missiles.remove(i);
            }
        }
    }
    
    private void updateJo() {
        if(jo.movingLeft) jo.moveLeft();
        if(jo.movingRight) jo.moveRight();
    }

    private void drawMissiles(Graphics2D g2){
            //List<Missile> missiles = jo.getMissiles();

            for (Missile missile : jo.missiles) {
                missile.draw(g2);
                //missile.move();
            }
    }

    public void gameUpdate () {
        minion.move();
        if(environment.check2Mins()) dio.move();
        //dio.move();
        updateJo();
        updateMissiles();
    }

    public void gameRender () {             // draw the game objects

        Graphics2D g2 = (Graphics2D) getGraphics(); // get the graphics context for the panel
        g2.drawImage(
            environment.getImageHandler().getImage("background.png"), 
            0, 0, 
            environment.width, environment.height, null
        );      // draw the background image
        minion.draw(g2);                    // draw the minion
        jo.draw(g2);
        dio.draw(g2);
        drawMissiles(g2);
        environment.timerTask.run();    
        //stats = jo.printStats();
    }   

    public void startGame() {               // initialise and start the game thread 

        if (gameThread == null) {
            isRunning = true;
            jo = new Jo (this);
            dio = new Dio (this, jo);
            minion = new Minion (this, jo, environment);
            environment.getAudioHandler().getClip("bgm.wav").loop();
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void endGame() {                 // end the game thread

        if (isRunning) {
            isRunning = false;
            environment.getAudioHandler().getClip("bgm.wav").stop();
            //playSound.stop();
        }
    }

}