package info.justdaile.algorithm;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cell implements UpdateRenderCycle{

    public static Color VISITED_COLOR = Color.GREEN;
    public static Color UNVISITED_COLOR = Color.RED;
    public static Color SELECTED_COLOR = Color.BLUE;
    public static Color WALL_COLOR = Color.BLACK;

    public static final byte TOP_WALL = 0;
    public static final byte BOTTOM_WALL = 1;
    public static final byte LEFT_WALL = 2;
    public static final byte RIGHT_WALL = 3;

    private int x, y, w, h;
    private Wall[] walls = {
            new Wall(false),
            new Wall(false),
            new Wall(false),
            new Wall(false)
    };

    private boolean selected, visited;
    private int id;

    public Cell(int id, int x, int y, int w, int h){
        this.id = id;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void update(){

    }

    @Override
    public void draw(GraphicsContext gc){
        // show cell
        if(visited && !selected){
            gc.setFill(VISITED_COLOR);
        }else if(selected){
            gc.setFill(SELECTED_COLOR);
        }else{
            gc.setFill(UNVISITED_COLOR);
        }
        gc.fillRect(x, y, w, h);

        // show walls
        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        gc.setStroke(WALL_COLOR);
        for(int i = 0; i < this.walls.length; i++){
            if(!this.walls[i].broken){
                switch(i){
                    case TOP_WALL:
                        x1 = x;
                        y1 = y;
                        x2 = x + w;
                        y2 = y;
                        break;
                    case BOTTOM_WALL:
                        x1 = x;
                        y1 = y + h;
                        x2 = x + w;
                        y2 = y + h;
                        break;
                    case LEFT_WALL:
                        x1 = x;
                        y1 = y;
                        x2 = x;
                        y2 = y + h;
                        break;
                    case RIGHT_WALL:
                        x1 = x + w;
                        y1 = y;
                        x2 = x + w;
                        y2 = y + h;
                        break;
                    default:break;
                }
                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    public void link(Cell to){
        if(to.getId() == this.getId() - 1){
            // its a left neighbour
            this.breakWall(LEFT_WALL);
            to.breakWall(RIGHT_WALL);
        }else if(to.getId() == this.getId() + 1){
            // its a right neighbour
            this.breakWall(RIGHT_WALL);
            to.breakWall(LEFT_WALL);
        }else if(to.getId() < this.getId() - 1){
            // has to be top neighbour
            this.breakWall(TOP_WALL);
            to.breakWall(BOTTOM_WALL);
        }else if(to.getId() > this.getId() + 1){
            // has to be bottom neighbour
            this.breakWall(BOTTOM_WALL);
            to.breakWall(TOP_WALL);
        }
    }

    public void visit(){
        this.visited = true;
        this.selected = true;
    }

    public boolean isVisited(){
        return this.visited;
    }

    public void leave(){
        this.selected = false;
    }

    public int getId(){
        return this.id;
    }

    private void breakWall(byte id){
        this.walls[id].broken = true;
    }

    private class Wall {

        private boolean broken;

        public Wall(boolean broken){
            this.broken = broken;
        }

        public boolean isBroken(){
            return this.broken;
        }

    }

}
