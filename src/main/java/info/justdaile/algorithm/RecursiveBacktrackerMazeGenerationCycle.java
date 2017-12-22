package info.justdaile.algorithm;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Random;

public class RecursiveBacktrackerMazeGenerationCycle implements UpdateRenderCycle{

    private int columns, rows;
    private ArrayList<Cell> cellStack = new ArrayList<Cell>();
    private Cell[] cells;
    private Cell selectedCell;
    private Random random = new Random();
    private boolean finished = false;

    public RecursiveBacktrackerMazeGenerationCycle(int width, int height, int columns, int rows){
        this.columns = columns;
        this.rows = rows;

        int cw = width / rows; // cell width
        int ch = height / columns; // cell height
        cells = new Cell[rows * columns];
        for(int i = 0; i < cells.length; i++){
            int y = i / rows;
            int x = i - (columns * y);
            cells[i] = new Cell(i, cw * x, ch * y, cw, ch);
        }

        // pick a start cell?
        selectedCell = cells[0];
        selectedCell.visit();
    }

    @Override
    public void update(){
        boolean hasUnvisitedCells = false;
        for(int i = 0; i < this.cells.length; i++){
            if(!this.cells[i].isVisited()){
                hasUnvisitedCells = true;
                break;
            }
        }

        ArrayList<Cell> availableNeighbours = calculateNeighbours();
        Cell selectedNeighbour = null;

        if(!availableNeighbours.isEmpty()){
            selectedNeighbour = availableNeighbours.get(random.nextInt(availableNeighbours.size()));
        }

        if(selectedNeighbour != null){
            selectedCell.link(selectedNeighbour);
            selectedCell.leave();
            selectedCell = selectedNeighbour;
            selectedCell.visit();
            cellStack.add(selectedCell);
        }else if(cellStack.size() > 0){
            selectedCell.leave();
            selectedCell = cellStack.remove(cellStack.size() - 1);
            selectedCell.visit();
        }else{
            selectedCell.leave();
            this.finished = true;
        }

    }

    public ArrayList<Cell> calculateNeighbours(){
        ArrayList<Cell> cells = new ArrayList<Cell>();
        int y = selectedCell.getId() / rows;
        int x = selectedCell.getId() - (columns * y);

        // test neighbours
        // id is the index of the cell
        // because we do not know the x and y of the neighbours we need to calculate their id's
        int leftNeighbourId = selectedCell.getId() - 1;
        int rightNeighbourId = selectedCell.getId() + 1;
        int topNeighbourId = selectedCell.getId() - rows;
        int bottomNeighbourId = selectedCell.getId() + rows;

        if(x - 1 >= 0){
            if(!this.cells[leftNeighbourId].isVisited()){
                cells.add(this.cells[leftNeighbourId]);
            }
        }

        if(x + 1 < this.columns){
            if(!this.cells[rightNeighbourId].isVisited()){
                cells.add(this.cells[rightNeighbourId]);
            }
        }

        if(y - 1 >= 0){
            if(!this.cells[topNeighbourId].isVisited()){
                cells.add(this.cells[topNeighbourId]);
            }
        }

        if(y + 1 < this.rows){
            if(!this.cells[bottomNeighbourId].isVisited()){
                cells.add(this.cells[bottomNeighbourId]);
            }
        }
        return cells;
    }

    @Override
    public void draw(GraphicsContext gc){
        for(int i = 0; i < cells.length; i++){
            cells[i].draw(gc);
        }
    }

    public boolean isFinished(){
        return this.finished;
    }

}
