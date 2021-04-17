package kg.tilek.sokobanjava;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Levels {

    private int level;
    private Viewer viewer;

    public Levels(Viewer viewer) {
        level = 0;
        this.viewer = viewer;
    }

    public int[][] nextLevel() {
        level++;
        return  pickLevel(level);
    }

    public int[][] previousLevel() {
        level--;
        return  pickLevel(level);
    }

    public int[][] currentLevel() {
        return pickLevel(level);
    }

    private int[][] pickLevel(int levelNumber) {
        String contentFile;

        int[][] field = null;
        switch (levelNumber) {
//  Read from local Array
            case 1:
                field = getFirstLevel();
                break;
            case 2:
                field = getSecondLevel();
                break;
//  Read from file
            case 3:
            case 4:
            case 5:
            case 6:
                contentFile = readFromFie(levelNumber);
                field = createField(contentFile);
                break;
//  Read from Api
            case 7:
            case 8:
            case 9:
            case 10:
                contentFile = readFromApi(levelNumber);
                if (contentFile == "Api connection Error!"){
                    Toast toast = Toast.makeText(viewer.getApplicationContext(), "Api connection Error! Starting level 1.", Toast.LENGTH_SHORT);
                    toast.show();
                    field = getFirstLevel();
                    level = 1;
                } else if (contentFile == "Connection time out!"){
                    Toast toast = Toast.makeText(viewer.getApplicationContext(), "No internet connection! Starting level 1.", Toast.LENGTH_SHORT);
                    toast.show();
                    field = getFirstLevel();
                    level = 1;
                } else {
                    if (levelNumber == 10){
                        Toast toast = Toast.makeText(viewer.getApplicationContext(), "Congratulations! This is last level.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    field = createField(contentFile);
                }

                break;
            default:
                level = 1;
                field = getFirstLevel();
        }

        return field;
    }

    public int[][] createField(String contentFile) {
        int row = 0;
        for (int i = 0; i < contentFile.length(); i++) {
            char symbol = contentFile.charAt(i);
            if (symbol == '\n') {
                row++;
            }
        }

        int[][] array = new int[row][];
        int column = 0;
        int count = 0;
        for (int i = 0; i < contentFile.length(); i++) {
            char symbol = contentFile.charAt(i);
            if (symbol == '\n') {
                array[count] = new int[column];
                count++;
                column = 0;
            } else {
                column++;
            }
        }

        int a = 0;
        int b = 0;
        for (int i = 0; i < contentFile.length(); i++) {
            char symbol = contentFile.charAt(i);
            if (symbol == '\n') {
                b = 0;
                a++;
            } else {
                int element = Integer.parseInt("" + symbol);
                array[a][b] = element;
                b++;
            }
        }
        return array;
    }

    private String readFromFie(int level) {
        String content = "";
        InputStream inputStream = viewer.getFileResource(level);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    char symbol = line.charAt(i);
                    if ('0' <= symbol && symbol <= '9') {
                        content = content + symbol;
                    }
                }
                content = content + '\n';
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return content;
    }

    private String readFromApi(int level) {
        String content = "";
        BackgroundAsyncTask asyncTask = new BackgroundAsyncTask(level);
        asyncTask.execute();
        try {
            content = asyncTask.getResult().take();
        } catch(InterruptedException e){
            Log.getStackTraceString(e);
        }
        return content;
    }

    private int[][] getFirstLevel() {
        int[][] field = new int[][]{
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 0, 0, 0, 0, 0, 0, 0, 2},
                {2, 0, 1, 0, 0, 3, 4, 0, 2},
                {2, 0, 0, 0, 0, 0, 0, 0, 2},
                {2, 0, 0, 0, 0, 0, 0, 0, 2},
                {2, 0, 0, 0, 0, 0, 0, 0, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2}
        };
        return field;
    }

    private int[][] getSecondLevel() {
        int[][] field = new int[][]{
                {2, 2, 2, 2, 9, 9, 9, 9, 9, 9},
                {2, 1, 0, 2, 9, 9, 9, 9, 9, 9},
                {2, 0, 0, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 0, 2, 0, 0, 0, 0, 0, 2},
                {9, 2, 0, 2, 4, 0, 0, 0, 0, 2},
                {9, 2, 0, 2, 2, 2, 2, 0, 0, 2},
                {9, 2, 0, 0, 0, 0, 2, 0, 0, 2},
                {9, 2, 0, 3, 0, 0, 0, 0, 0, 2},
                {9, 2, 0, 0, 0, 0, 2, 0, 2, 2},
                {9, 2, 0, 2, 2, 2, 2, 0, 2, 9},
                {9, 2, 0, 0, 0, 0, 0, 0, 2, 9},
                {9, 2, 2, 2, 2, 2, 2, 2, 2, 9},
        };
        return field;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }


//  Lesson legacy code for AnotherClient

    /*public class Levels extends Activity implements Runnable{
        public Levels(){
        thread = new Thread(this);
        send();
        }
        private void send() {
        thread.start();
        }
        @Override
        public void run() {
        return;
        }
        @Override
        public void run() {
        AnotherClient anotherClient = new AnotherClient("194.152.37.7", 4446);
        anotherClient.sendMessageToBackendServer("I live in Bishkek.");
        }

   }*/

}

