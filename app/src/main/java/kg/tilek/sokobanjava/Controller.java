package kg.tilek.sokobanjava;

import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import java.util.Arrays;

public class Controller implements View.OnTouchListener, DialogInterface.OnClickListener{

    private Model model;
    private int x1, x2, y1, y2;

    public Controller(Viewer viewer){
        model = new Model(viewer);
    }

    public Model getModel(){
        return model;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String direction = "";

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = (int) event.getX();
                y1 = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                x2 = (int) event.getX();
                y2 = (int) event.getY();

                int deltaX = x1 - x2;
                int deltaY = y1 - y2;

                if (Math.abs(deltaX)> Math.abs(deltaY)){
                    direction = deltaX < 0 ? "RIGHT" : "LEFT";
                } else{
                    direction = deltaY < 0 ? "DOWN" : "UP";
                }
                break;
            default:
                break;
        }

        model.move(direction);
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int selectedButton) {
        if (selectedButton == -1){
            model.nextLevel();
        }
        dialog.dismiss();
    }

    public void restartLevel(){
        model.restart();
    }

    public void nextLevel(){
        model.nextLevel();
    }

    public void previousLevel(){
        model.previousLevel();
    }

    public String[] getGameState(){
        String[] data = new String[3];
        int[][] field = model.getField();

        for (int row = 0; row < field.length; row++){
            for (int i = 0; i < field[row].length; i++){
                if (field[row][i] == 11 || field[row][i] == 12 || field[row][i] == 13){
                    field[row][i] = 1;
                    break;
                }
            }
        }

        data[0] = Arrays.deepToString(field);
        data[1] = Arrays.deepToString(model.getIndexes());
        data[2] = String.valueOf(model.getLevel());
        return data;
    }

    public void setGameState(String field, String indexes, String level){
        model.restoreGameState(field, indexes, level);
    }
}
