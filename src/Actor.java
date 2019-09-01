import java.awt.*;
import java.util.ArrayList;

public abstract class Actor {
    Color colour;
    Cell loc;
    ArrayList<Polygon> display;
    float redness;
    int turns;
    int moves;

    public void paint(Graphics g){
        for(Polygon p: display){
            g.setColor(new Color(redness, 0f, 1f-redness));
            g.fillPolygon(p);
            g.setColor(Color.GRAY);
            g.drawPolygon(p);
        }
    }

    public abstract void setPoly();

    public boolean isTeamRed(){
        return redness >= 0.5;
    }

    public void setLocation(Cell loc){
        this.loc = loc;
        setPoly();
    }
}