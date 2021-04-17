package kg.tilek.sokobanjava;

import java.util.HashMap;

public class Model {

    private Viewer viewer;
    private Levels levels;

    private int[][] field;
    private int[][] indexes;
    private int indexX;
    private int indexY;
    private boolean isOk;
    private boolean restoreState;
    private HashMap<String, Integer> fieldLength;


    public Model(Viewer viewer){
        this.viewer = viewer;
        levels = new Levels(viewer);
        field  = levels.nextLevel();
        fieldLength = new HashMap<String, Integer>();
        restoreState = false;
        initialization();
    }

    /*  Player sprite values
        1  - Sprite right
        11 - Sprite left
        12 - Sprite up
        13 - Sprite down
    */

    private void initialization(){
        isOk = true;
        int counterOne = 0;
        int counterThree = 0;
        int counterFour = 0;
        fieldLength.put("x", 0);
        fieldLength.put("y", field.length);

        for (int rowY = 0; rowY < field.length; rowY++){
            if (fieldLength.get("x") < field[rowY].length) {
                fieldLength.put("x", field[rowY].length);
            }

            for(int rowX = 0; rowX < field[rowY].length; rowX++){
                int cell = field[rowY][rowX];
                if (cell == 1){
                    indexX = rowX;
                    indexY = rowY;
                    counterOne++;
                } else if(cell == 3) {
                    counterThree++;
                } else if(cell == 4) {
                    counterFour++;
                }
            }
        }

        if((counterOne != 1 || counterThree != counterFour || counterThree == 0) && !restoreState) {
            isOk = false;
            return;
        }

        indexes = new int[2][counterFour];
        int goalIndex = 0;

        for(int rowY = 0; rowY < field.length; rowY++) {
            for(int rowX = 0; rowX < field[rowY].length; rowX++) {
                int element = field[rowY][rowX];
                if(element == 4) {
                    indexes[0][goalIndex] = rowY;
                    indexes[1][goalIndex] = rowX;
                    goalIndex++;
                }
            }
        }
    }

    public void move(String direction){
        switch (direction){
            case "DOWN":
                moveDown(field[indexY + 1][indexX]);
                break;
            case "UP":
                moveUp(field[indexY - 1][indexX]);
                break;
            case "LEFT":
                moveLeft(field[indexY][indexX - 1]);
                break;
            case "RIGHT":
                moveRight(field[indexY][indexX + 1]);
                break;
        }

        viewer.update();
        checkGoalSpot();
    }

    private void checkGoalSpot() {
        boolean flag  = true;

        for(int i = 0; i < indexes[0].length; i++) {
            int goalY = indexes[0][i];
            int goalX = indexes[1][i];

            if (field[goalY][goalX] == 0) {
                field[goalY][goalX] = 4;
                return;
            } else if (field[goalY][goalX] != 3) {
                flag = false;
            }
        }

        if(flag) {
            int nextLevel = levels.getLevel();
            viewer.openDialog(nextLevel);
        }
    }

    private void moveDown(int nextCell){
        if (nextCell == 3) moveBox('y', '+');

        if (field[indexY + 1][indexX] == 0 || field[indexY + 1][indexX] == 4) {
            field[indexY][indexX] = 0;
            indexY = indexY + 1;
            field[indexY][indexX] = 13;
        }
    }

    private void moveUp(int nextCell){
        if (nextCell == 3) moveBox('y', '-');

        if (field[indexY - 1][indexX] == 0 || field[indexY - 1][indexX] == 4) {
            field[indexY][indexX] = 0;
            indexY = indexY - 1;
            field[indexY][indexX] = 12;
        }
    }

    private void moveLeft(int nextCell){
        if (nextCell == 3) moveBox('x', '-');

        if (field[indexY][indexX - 1] == 0 || field[indexY][indexX - 1] == 4) {
            field[indexY][indexX] = 0;
            indexX = indexX - 1;
            field[indexY][indexX] = 11;
        }
    }

    private void moveRight(int nextCell){
        if (nextCell == 3) moveBox('x', '+');

        if (field[indexY][indexX + 1] == 0 || field[indexY][indexX + 1] == 4) {
            field[indexY][indexX] = 0;
            indexX = indexX + 1;
            field[indexY][indexX] = 1;
        }
    }

    private void moveBox(char axis, char sign){
        if (axis == 'x'){
            int boxCell  = sign == '+' ? indexX + 1 : indexX - 1;
            int freeCell = sign == '+' ? indexX + 2 : indexX - 2;

            if (field[indexY][freeCell] == 0 || field[indexY][freeCell] == 4){
                field[indexY][boxCell] = 0;
                field[indexY][freeCell] = 3;
            }
        } else if (axis == 'y'){
            int boxCell  = sign == '+' ? indexY + 1 : indexY - 1;
            int freeCell = sign == '+' ? indexY + 2 : indexY - 2;

            if (field[freeCell][indexX] == 0 || field[freeCell][indexX] == 4) {
                field[boxCell][indexX] = 0;
                field[freeCell][indexX] = 3;
            }
        }
    }

    public int getLevel() { return levels.getLevel(); }

    public int[][] getField(){ return field; }

    public int[][] getIndexes(){ return indexes; }

    public HashMap getFieldLength() {
        return fieldLength;
    }

    public boolean getFieldState(){
        return isOk;
    }

    public void nextLevel() {
        field = levels.nextLevel();
        initialization();
        viewer.update(levels.getLevel());
    }

    public void previousLevel() {
        field = levels.previousLevel();
        initialization();
        viewer.update(levels.getLevel());
    }

    public void restart() {
        field = levels.currentLevel();
        initialization();
        viewer.update(levels.getLevel());
    }

    public void restoreGameState(String field, String indexes, String level){
        restoreState = true;
        levels.setLevel(Integer.parseInt(level));
        this.field = levels.createField(field.replaceAll("\\[", "").replaceAll(",", "").replaceAll("\\s","").replaceAll("]", "\n"));
        this.indexes = levels.createField(indexes.replaceAll("\\[", "\n").replaceAll(",", "").replaceAll("\\s","").replaceAll("]", "\n"));
        initialization();
        viewer.update(levels.getLevel());
        restoreState = false;
    }
}
