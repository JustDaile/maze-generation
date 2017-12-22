package info.justdaile.algorithm;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application{

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final int COLUMNS = 50;
    public static final int ROWS = 50;

    @Override
    public void start(Stage primary){
        RecursiveBacktrackerMazeGenerationCycle algorithm = new RecursiveBacktrackerMazeGenerationCycle(App.WIDTH, App.HEIGHT, App.COLUMNS, App.ROWS);
        Scene scene = new Scene(new MazeGenerationScene(App.WIDTH, App.HEIGHT, algorithm));
        primary.setScene(scene);
        primary.sizeToScene();
        primary.show();
    }

    public static void main(String[] args) {
        new App().launch(args);
    }

    private class MazeGenerationScene extends BorderPane{

        private Canvas canvas;
        private UpdateRenderCycle updateRenderCycle;
        private final Color BACKGROUND_COLOR = Color.BLACK;
        private final Color FOREGROUND_COLOR = Color.WHITE;

        public MazeGenerationScene(int w, int h, final UpdateRenderCycle updateRenderCycle){
            this.canvas = new Canvas(w, h);
            this.updateRenderCycle = updateRenderCycle;
            this.setCenter(this.canvas);
            this.autosize();

            final AnimationTimer timer = new AnimationTimer() {
                public void handle(long now) {
                    update();
                    draw(canvas.getGraphicsContext2D());
                }
            };


            new Thread(new Runnable() {
                @Override
                public void run() {
                    timer.start();
                    RecursiveBacktrackerMazeGenerationCycle cycle =
                            (RecursiveBacktrackerMazeGenerationCycle) updateRenderCycle;
                    while(!cycle.isFinished()){
                        try{
                            Thread.sleep(1);
                            Thread.yield();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    timer.stop();
                    System.out.println("ended");
                 }
            }).start();
        }

        private void update(){
            updateRenderCycle.update();
        }

        private void draw(GraphicsContext gc){
            gc.setFill(BACKGROUND_COLOR);
            gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
            gc.setFill(FOREGROUND_COLOR);
            updateRenderCycle.draw(gc);
        }

    }

}
