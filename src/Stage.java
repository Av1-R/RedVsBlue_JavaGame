import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.util.*;

public class Stage {
    Grid grid;
    ArrayList<Actor> actors;
    List<Cell> cellOverlay;
    List<MenuItem> menuOverlay;
    Optional<Actor> actorInAction;

    //enum State {ChoosingActor, SelectingNewLocation, CPUMoving, SelectingMenuItem, SelectingTarget}
    State choosingActor = new ChoosingActor(this);
    State selectingNewLocation = new SelectingNewLocation(this);
    State CPUMoving = new CPUMoving(this);
    State selectingMenuItem = new SelectingMenuItem(this);
    State selectingTarget = new SelectingTarget(this);

    State currentState;



    
    public Stage(){
        grid = new Grid();
        actors = new ArrayList<Actor>();
        cellOverlay = new ArrayList<Cell>();
        menuOverlay = new ArrayList<MenuItem>();
        currentState = choosingActor;
    }

    public void setState(State state){
        currentState = state;
    }

    public void paint(Graphics g, Point mouseLoc){
        
        //Call handleCpu method, which runs behaviour of CPUMoving state (all handleCpu() methods are empty for all states classes besides that of CPUMoving).
        currentState.handleCpu();

        grid.paint(g,mouseLoc);
        grid.paintOverlay(g, cellOverlay, new Color(0f, 0f, 1f, 0.5f));

        for(Actor a: actors){
            a.paint(g);   
        }
        
        // state display
        g.setColor(Color.DARK_GRAY);
        g.drawString(currentState.getStateName(),720,20);

        // display cell
        Optional<Cell> cap = grid.cellAtPoint(mouseLoc);
        if (cap.isPresent()){
            Cell capc = cap.get();
            g.setColor(Color.DARK_GRAY);
            g.drawString(String.valueOf(capc.col) + String.valueOf(capc.row), 720, 50);
            g.drawString(capc.description, 820, 50);
            g.drawString("movement cost", 720, 65);
            g.drawString(String.valueOf(capc.movementCost()), 820, 65);
        } 

        // agent display
        int yloc = 138;
        for(int i = 0; i < actors.size(); i++){
            Actor a = actors.get(i);
            g.drawString(a.getClass().toString(),720, yloc + 70*i);
            g.drawString("location:", 730, yloc + 13 + 70 * i);
            g.drawString(Character.toString(a.loc.col) + Integer.toString(a.loc.row), 820, yloc + 13 + 70 * i);
            g.drawString("redness:", 730, yloc + 26 + 70*i);
            g.drawString(Float.toString(a.redness), 820, yloc + 26 + 70*i);
            g.drawString("strat:", 730, yloc + 39 + 70*i);
            g.drawString(a.strat.toString(), 820, yloc + 39 + 70*i);
        }

        // menu overlay (on top of everything else)
        for(MenuItem mi: menuOverlay){
            mi.paint(g);
        }


    }

    public List<Cell> getClearRadius(Cell from, int size, boolean considerCost){
        List<Cell> init = grid.getRadius(from, size, considerCost);
        for(Actor a: actors){
            init.remove(a.loc);
        }
        return init;
    }

    public void mouseClicked(int x, int y){
        currentState.handle(x, y);
    }

    public Optional<Actor> actorAt(Cell c){
        for(Actor a: actors){
            if (a.loc == c){
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }
}