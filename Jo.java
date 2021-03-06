import java.util.Random;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.*;
import java.awt.*;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

/*
this class will represent the player/MC(main character).
    - a blue Rectangle
    - can shoot a projectile in the direction he is facing 
    - can jump
    - can move left and right
*/
public class Jo extends Player
{
    public String ora = "ora.wav";
    public String idle = "idle.gif";
    public String run = "run.gif";
    public String jump = "jump.gif";
    public String midAir = "midAir.gif";
    public String landing = "landing.gif";

    public boolean win = false;
    public boolean up1 = false;
    public boolean up2 = false;
    public boolean up3 = false;
    
    public Jo(GamePanel p){
        super(p, "jo");
        DX = 10;
        DY = 100;
        super.width = 40;
        super.height = 60;
        super.x = 10;
        super.y = environment.getGround();
        health = 5;
        score = 0;
        damage = 1;
        facingRight = true;
        isFlying = false;
        isIdle = true;
        initAudio();
        initImages();
        ////System.out.println("Jo created! " + this.toString());
    }
    
    private void initAudio(){
        this.audioHandler.loadClip(ora);
        //System.out.println("initAudio for " + name + audioHandler.toString());
    }
    private void initImages(){
        this.imageHandler.loadImage(idle);
        this.imageHandler.loadImage(run);
        this.imageHandler.loadImage(jump);
        this.imageHandler.loadImage(midAir);
        this.imageHandler.loadImage(landing);
        //System.out.println("initAudio for " + name + audioHandler.toString());
    }

    public void draw (Graphics2D g2) {
        g2.setColor (Color.BLUE);
        g2.fill (new Rectangle2D.Double (x, y, width, height));
        // Image image;
        
        // if(isIdle){
        //     image = this.imageHandler.getImage(idle);
        //     g2.drawImage(
        //         image,
        //         x, y,
        //         width, 
        //         height,
        //         null
        //     );
        // }
        // if(isJumping){
        //     image = this.imageHandler.getImage(jump);
        //     g2.drawImage(
        //         image,
        //         x, y,
        //         image.getWidth(null), 
        //         image.getHeight(null),
        //         null
        //     );
        // }
        // if(isIdle){
        //     image = this.imageHandler.getImage(idle);
        //     g2.drawImage(
        //         image,
        //         x, y,
        //         image.getWidth(null), 
        //         image.getHeight(null),
        //         null
        //     );
        // }
        // if(isIdle){
        //     image = this.imageHandler.getImage(idle);
        //     g2.drawImage(
        //         image,
        //         x, y,
        //         image.getWidth(null), 
        //         image.getHeight(null),
        //         null
        //     );
        // }
        // if(isIdle){
        //     image = this.imageHandler.getImage(idle);
        //     g2.drawImage(
        //         image,
        //         x, y,
        //         image.getWidth(null), 
        //         image.getHeight(null),
        //         null
        //     );
        // }
    }

    public void fire() {
		int xV = x + width;
		// if (facingRight){
		// 	xV = xV * -1;
		// }
		Missile m = new Missile(panel, xV, y + height / 4);
		missiles.put(m.getId(), m);
        // play sound
        this.audioHandler.getClip(ora).play();
		//System.out.println("ORA!");
    }
    
    public void upgrade1(){
        damage += 2;
    }
    public void upgrade2(){
        health = 8;
        damage += 1;
    }
    public void upgrade3(){
        health = 12;
        damage += 1;
        DX += 20;
    }
    
    public int getDamage(){return this.damage;}

    public void updateJo(){
        if(score >= 120 && !up1){
            upgrade1();
            up1 = true;
        } 
        
        if(score >= 240 && !up2){
            upgrade2();
            up2 = true;
        }
        
        if(score >= 300 && !up3){
            upgrade3();
            up3 = true;
        }
        if(this.health <= 0){
            //this.yeet();
            win = false;
            panel.endGame();
        }
    }
}