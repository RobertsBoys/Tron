/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class Tron extends JFrame implements Runnable, KeyListener
{
    /*
    The Driver/main
    You shouldnt be using any functions or variables from this class in your AI
    You can edit it though, and what you will probably want to edit
        is the keyPressed function to create debugging tools
        and you may need to add something to the step() function
        if your AI needs some function called in a static context every turn
    
    */
    Container con = getContentPane();
    Thread t = new Thread(this);
    
    private enum Status {
        Run,Win,Pause,OneStep
    }
    Status status;
    long statusTime=0;
    static int SCWIDTH = 1200;
    static int SCLENGTH = 750;
    double sleep;
    double scale;
    long lastTime;
    public Tron()
    {
        
        con.setBackground(Color.BLUE);
        con.setLayout(new FlowLayout());
        addKeyListener(this);
        t.start();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Util.onStart();
        Draw.onStart();
        Square.onStart();
        status=Status.Pause;
        Bike.onStart();
        sleep=50;
        lastTime=statusTime=System.currentTimeMillis();
//        repaint();
        
    }
    public void run()
    {
        try
        {
            repaint();
            t.sleep(100);
            while(true)
            {
                if (status==Status.Run)
                {
//                    long wait=System.currentTimeMillis()-lastTime;
//                    if (wait<sleep)
//                        t.sleep((long)sleep-wait);
//                    lastTime=System.currentTimeMillis();
                    t.sleep((long)sleep);
                    step();
                }
                else
                {
                    t.sleep(100);
                }
                repaint();
            }
        }
        catch(Exception e){}
    }
    public void step()
    {
        Util.onFrame();
        int amtAlive=0;
        Robobike.updateMap();
        for (int n=0;n<Bike.amt();n++)
        {
            if (Bike.get(n).isAlive())
            {
                amtAlive++;
                Bike.get(n).update();
            }
        }
        if (amtAlive<=1)
        {
            status=Status.Win;
            for (int n=0;n<Bike.amt();n++)
            {
                Bike.get(n).onEnd(Bike.get(n).isAlive());
            }
            statusTime=System.currentTimeMillis();
        }
    }
    public void update(Graphics g)
    {
        paint(g);
    } 
    public void paint(Graphics gr)
    {
        Image i=createImage(getSize().width, getSize().height);
        Graphics2D g2 = (Graphics2D)i.getGraphics();
        if (status==Status.Run || status==Status.OneStep)
        {
            g2.setColor(Color.BLACK);
            g2.fillRect(0,0,SCWIDTH,SCLENGTH);
            Square.drawAll(g2);
            for (int n=0;n<Bike.amt();n++)
            {
                Bike.get(n).draw(g2);
            }
            Draw.drawList(g2,true);
//            if (status==Status.OneStep)
//                status=Status.Pause;
        }
        else if (status==Status.Win)
        {
            int winner=-1;
            for (int n=0;n<Bike.amt();n++)
            {
                if (Bike.get(n).isAlive())
                {
                    winner=n;
                    Bike.get(n).draw(g2);
                    break;
                }
            }
            Square.drawAll(g2);
            Color c=Bike.getColor(winner);
            c=new Color(c.getRed(),c.getGreen(),c.getBlue(),100);
            g2.setColor(c);
            g2.fillRect(0,0,SCWIDTH,SCLENGTH);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial",Font.BOLD,90));
            if (winner!=-1)
                g2.drawString(Bike.get(winner).getWinPhrase(),300,470);
            else 
                g2.drawString("Tie!",470,470);
        }
        else if (status==Status.Pause)
        {
            
            g2.setColor(Color.BLACK);
            g2.fillRect(0,0,SCWIDTH,SCLENGTH);
            Square.drawAll(g2);
            for (int n=0;n<Bike.amt();n++)
            {
                Bike.get(n).draw(g2);
            }
            Draw.drawList(g2,false);
            g2.setColor(Color.WHITE);
            g2.drawString("Paused",500,450);
            
        }
        g2.dispose();
        gr.drawImage(i, 0, 0, this);
    }
    public void keyPressed(KeyEvent k)
    {
        if (k.getKeyCode()==KeyEvent.VK_OPEN_BRACKET)
        {
            sleep*=.8;
//            System.out.println("sleep="+sleep);
        }
        else if (k.getKeyCode()==KeyEvent.VK_CLOSE_BRACKET)
        {
            if (sleep < 1000)
                sleep*=1.25;
//            System.out.println("sleep="+sleep);
        }
        else if (k.getKeyCode()==KeyEvent.VK_ESCAPE)
        {
            System.exit(0);
        }
        else if (k.getKeyCode()==KeyEvent.VK_BACK_SPACE)
            reset();
        else if (k.getKeyCode()==KeyEvent.VK_SPACE)
        {
            if (status==Status.Pause)
                status=Status.Run;
            else if (status==Status.Run)
                status=Status.Pause;
        }
        else if (k.getKeyCode()==KeyEvent.VK_LEFT)
        {
            for (int n=0;n<Bike.amt();n++)
            {
                if (Bike.get(n) instanceof PlayerBike)
                    Bike.get(n).setCurDir(Dir.West);
            }
        }
        else if (k.getKeyCode()==KeyEvent.VK_RIGHT)
        {
            for (int n=0;n<Bike.amt();n++)
            {
                if (Bike.get(n) instanceof PlayerBike)
                    Bike.get(n).setCurDir(Dir.East);
            }
        }
        else if (k.getKeyCode()==KeyEvent.VK_UP)
        {
            for (int n=0;n<Bike.amt();n++)
            {
                if (Bike.get(n) instanceof PlayerBike)
                    Bike.get(n).setCurDir(Dir.North);
            }
        }
        else if (k.getKeyCode()==KeyEvent.VK_DOWN)
        {
            for (int n=0;n<Bike.amt();n++)
            {
                if (Bike.get(n) instanceof PlayerBike)
                    Bike.get(n).setCurDir(Dir.South);
            }
        }
        else if (k.getKeyCode()==KeyEvent.VK_P)
        {
            
            ArrayList<Square> path=Square.getPath(Bike.get(0).getPos(),Bike.get(2).getPos());
            System.out.println("Path from "+Bike.get(0).getPos().toString()
                +" to "+Bike.get(2).getPos());
            System.out.println("    size "+path.size());
            for (int n=1;n<path.size();++n)
            {
                Draw.draw(new Draw(Draw.Type.Line,
                        path.get(n).px()+5, path.get(n).py()+5,
                        path.get(n-1).px()+5,path.get(n-1).py()+5,
                        Color.GREEN));
            }
            
        }
        else if (k.getKeyCode()==KeyEvent.VK_COMMA)
        {
            if (status==Status.Pause)
            {
                status=Status.OneStep;
                step();
                status=Status.Pause;
            }
        }
    }
    public void reset()
    {
        System.out.println("-----------Reset------------");
        Square.resetAll();
        Bike.resetAll();
        Util.reset();
        Draw.reset();
        status=Status.Run;
        repaint();
    }
    public void keyReleased(KeyEvent k)
    {
        
    }
    public void keyTyped(KeyEvent k)
    {
        
    }
    public static void main(String[] args)
    {
        Tron frame = new Tron();

        frame.setSize(SCWIDTH,SCLENGTH);
        frame.setVisible(true);
    }
    
}
