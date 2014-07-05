
package tron;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author Isaac
 */
public class Draw {

    

    enum Type {

        Line, Oval, SolidOval, Rect, SolidRect, String
        //Solid just means the shape is filled in, instead of traced
    }
    Type type;
    int x;//Pixel Location
    int y;
    String text="";//Only if type is String
    int param3;
    int param4;
    /*
    Param3 & param4 are used differently by type:
        Line :
            p3=x2, p4 = y2
        Oval,SolidOval, Rect, SolidRect :
            p3=width, p4=height
        String : 
            p3=font, p4 isnt used. (Font is always Arial)
    */
    Color color;
    int duration;//allows you to have a shape linger on screen for X updates

    public Draw(Type t, int setx, int sety, int p3, int p4, Color c, int dur) {
        //Standard shape constructor.
        type = t;
        color = c;
        duration = dur;
        x = setx;
        y = sety;
        param3 = p3;
        param4 = p4;
    }
    public Draw(Type t, int setx, int sety, int p3, int p4, Color c) {
        //Duration defaults to one
        type = t;
        color = c;
        x = setx;
        y = sety;
        param3 = p3;
        param4 = p4;
        duration = 1;
    }
    public Draw(String txt, int setx, int sety, int p3, Color c, int dur) {
        //String constructor
        type = Type.String;
        text = txt;
        x = setx;
        y = sety;
        color = c;
        param3 = p3; //will be size
        duration = dur;
    }
    public Draw(Square from, Square to, Color c, int dur) {
        //Draws a line from one square to the other
        type= Type.Line;
        color=c;
        duration=dur;
        x=from.px()+Square.SQUARE_SIZE/2;
        y=from.py()+Square.SQUARE_SIZE/2;
        param3=to.px()+Square.SQUARE_SIZE/2;
        param4=to.py()+Square.SQUARE_SIZE/2;
    }
    public Draw(Square fill,Color c,int dur) {
        //Draws a solid box over the square
        type=Type.SolidRect;
        color=c;
        duration=dur;
        x=fill.px()+1;
        y=fill.py()+1;
        param3=param4=Square.SQUARE_SIZE-2;
    }
    /*
    --------------------------------------------------------
    -----------Static---------------------------------------
    --------------------------------------------------------
    */
    
    public static void draw(Draw add) {
    /*
        Get your Draw objects drawn here
        ex:
            Draw.draw(new Draw(Draw.Type.Line,50,70,90,100,Color.Green,3)
        will render a green line from (50,70) to (90,100) for 3 game steps 
        yes its a lot of typing and i dont care
        
    */
        
        if (add==null) {
            System.out.println("Adding null to drawList");
        }
        else
        drawList.add(add);
    }
    
    
    private static ArrayList<Draw> drawList;//Not for you
    
    public static void onStart()
    {//Not for you
        drawList=new ArrayList<Draw>();
    }
    public static void drawList(Graphics2D g,boolean update)
        //Not for you
    {
        for (int n=0;n<drawList.size();n++)
        {
            if (drawList.get(n)==null)
            {
                System.out.println("drawList element is null");
                drawList.remove(n);
            }
            else {
            g.setColor(drawList.get(n).color);
            switch (drawList.get(n).type)
            {
                case Line:
                    g.drawLine(drawList.get(n).x,drawList.get(n).y,drawList.get(n).param3,drawList.get(n).param4);
                    break;
                case Oval:
                    g.drawOval(drawList.get(n).x,drawList.get(n).y,drawList.get(n).param3,drawList.get(n).param4);
                    break;
                case SolidOval:
                    g.fillOval(drawList.get(n).x,drawList.get(n).y,drawList.get(n).param3,drawList.get(n).param4);
                    break;
                case Rect:
                    g.drawRect(drawList.get(n).x,drawList.get(n).y,drawList.get(n).param3,drawList.get(n).param4);
                    break;
                case SolidRect:
                    g.fillRect(drawList.get(n).x,drawList.get(n).y,drawList.get(n).param3,drawList.get(n).param4);
                    break;
                case String:
                    g.setFont(new Font("Arial",Font.PLAIN,drawList.get(n).param3));
                    g.drawString(drawList.get(n).text,drawList.get(n).x,drawList.get(n).y);
                    break;
            }
            if (update)
            {
                drawList.get(n).duration--;
                if (drawList.get(n).duration<=0)
                {
                    drawList.remove(n);
                    n--;
                }
            }
            }
        }
    }
    public static void reset()
    {//Not for you
        drawList.clear();
    }
    
}
