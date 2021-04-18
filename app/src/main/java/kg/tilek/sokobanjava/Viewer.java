package kg.tilek.sokobanjava;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Viewer extends AppCompatActivity {

    private CanvasSokoban canvasSokoban;
    private Controller controller;
    private static int mpState = 0;
    private static int mpInit = 0;
    private boolean musicPlaying;
    static MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenWidth = getWidthAndHeight()[0];
        int screenHeight = getWidthAndHeight()[1];

        controller = new Controller(this);
        Model model = controller.getModel();
        canvasSokoban = new CanvasSokoban(this, model, screenWidth, screenHeight);
        canvasSokoban.setOnTouchListener(controller);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_level_previous);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("SokobanJAVA - Level 1");
        setContentView(canvasSokoban);
        if (mpInit == 0) {
            mpInit++;
            mediaPlayer = MediaPlayer.create(this, R.raw.jacques);
            playMusic(canvasSokoban);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        String[] data = controller.getGameState();
        outState.putString("field", data[0]);
        outState.putString("indexes", data[1]);
        outState.putString("level", data[2]);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstance){
        super.onRestoreInstanceState(savedInstance);
        controller.setGameState(savedInstance.getString("field"), savedInstance.getString("indexes"), savedInstance.getString("level"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sokoban_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next:
                controller.nextLevel();
                break;
            case R.id.restart:
                controller.restartLevel();
                break;
            case R.id.exit:
                stopMusic();
                finish();
                break;
            case R.id.music:
                if (musicPlaying){
                    stopMusic();
                } else {
                    playMusic(canvasSokoban);
                }
                break;
            case android.R.id.home:
                controller.previousLevel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int[] getWidthAndHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int[] array = {width, height};
        return array;
    }

    public void update() {
        runSpriteThread();
    }

    public void update(int level) {
        if (level == 10){
            setTitle("Sokoban - Level " + level);
        } else {
            setTitle("SokobanJAVA - Level " + level);
        }
        runSpriteThread();
    }


    private void runSpriteThread() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        canvasSokoban.invalidate();
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void openDialog(int level) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        this.runOnUiThread(new Runnable() {
            public void run() {
                builder.setTitle("Message")
                        .setIcon(R.drawable.error)
                        .setMessage("You won!!! \nLoad next " + level + " level.")
                        .setPositiveButton("OK", controller)
                        .create();
                builder.show();
            }
        });
    }

    public InputStream getFileResource(int level) {
        InputStream inputStream = null;
        if (level == 3) {
            inputStream = getResources().openRawResource(R.raw.level3);
        } else if (level == 4) {
            inputStream = getResources().openRawResource(R.raw.level4);
        } else if (level == 5) {
            inputStream = getResources().openRawResource(R.raw.level5);
        } else if (level == 6) {
            inputStream = getResources().openRawResource(R.raw.level6);
        }

        return inputStream;
    }

    public void playMusic(View view) {
        musicPlaying = true;
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        mediaPlayer.seekTo(mpState);
    }

    public void stopMusic() {
        musicPlaying = false;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mpState = mediaPlayer.getCurrentPosition();
        }
    }

//    @Override
//    public void onBackPressed() {
//        mpState = 0;
//        super.onBackPressed();
//        mediaPlayer.release();
//        mediaPlayer.stop();
//        mediaPlayer = null;
//        System.exit(0);
//    }

}
