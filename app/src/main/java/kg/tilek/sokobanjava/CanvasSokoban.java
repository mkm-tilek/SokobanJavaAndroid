package kg.tilek.sokobanjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;

import java.util.HashMap;
import java.util.Timer;

public class CanvasSokoban extends View{

    private Model model;
    private Paint paint;

    //  Bitmap images
    private Bitmap errorBitmap;
    private Bitmap grassSprite;
    private Bitmap wallSprite;
    private Bitmap goalSprite;
    private Bitmap boxSprite;
    private Bitmap pcSprite;
    private Bitmap javaSprite;

    //  Bitmap sprites
    private Bitmap sonicUpSprite;
    private Bitmap sonicDownSprite;
    private Bitmap standRightSprite;
    private Bitmap runRightSprite1;
    private Bitmap runRightSprite2;
    private Bitmap standLeftSprite;
    private Bitmap runLeftSprite1;
    private Bitmap runLeftSprite2;

    //  Screen size
    private int screenWidth;
    private int screenHeight;

    //  Cell size
    private int cellWidth;
    private int cellHeight;

    //  Canvas margin ?
    private int posY = 0;
    private int posX = 0;

    private int spriteRunCounter = 0;
    private HashMap<String, Integer> fieldLength;

    public CanvasSokoban(Viewer viewer, Model model, int width, int height){
        super(viewer);
        this.model = model;
        this.screenWidth  = width;
        this.screenHeight = height;
        this.fieldLength = model.getFieldLength();
        paint = new Paint();

        sonicUpSprite = BitmapFactory.decodeResource(getResources(), R.raw.sonic_stand_up);
        sonicDownSprite = BitmapFactory.decodeResource(getResources(), R.raw.sonic_stand_down);

        standRightSprite = BitmapFactory.decodeResource(getResources(), R.raw.sonic_stand_right);
        runRightSprite1 = BitmapFactory.decodeResource(getResources(), R.raw.sonic_run_right_1);
        runRightSprite2 = BitmapFactory.decodeResource(getResources(), R.raw.sonic_run_right_2);

        standLeftSprite = BitmapFactory.decodeResource(getResources(), R.raw.sonic_stand_left);
        runLeftSprite1 = BitmapFactory.decodeResource(getResources(), R.raw.sonic_run_left_1);
        runLeftSprite2 = BitmapFactory.decodeResource(getResources(), R.raw.sonic_run_left_2);

        grassSprite = BitmapFactory.decodeResource(getResources(), R.raw.grass_default);
        wallSprite = BitmapFactory.decodeResource(getResources(), R.raw.wall_sonic);

        goalSprite = BitmapFactory.decodeResource(getResources(), R.raw.goal_sonic);
        boxSprite = BitmapFactory.decodeResource(getResources(), R.raw.box_default);

        javaSprite = BitmapFactory.decodeResource(getResources(), R.raw.java_goal);
        pcSprite = BitmapFactory.decodeResource(getResources(), R.raw.java_pc);

        setBackgroundColor(Color.BLACK);
    }

    public void calculateScaleRatio(){
//      Default scales
        float highScale = 20F;
        float lowScale  = 10F;
        float bitmapScale = 100F;

//      Screen orientation default vertical & horizontal lengths
        int verLength;
        int horLength;

//      Set screen orientation default field length
        if (this.screenHeight > this.screenWidth){
            verLength = 20;
            horLength = 10;
        } else {
            verLength = 7;
            horLength = 20;
        }

//      Calculate bitmap width & heigth ratio
        float bitmapScaleX = bitmapScale / (((fieldLength.get("x") * bitmapScale) / horLength) / bitmapScale);
        float bitmapScaleY = bitmapScale / (((fieldLength.get("y") * bitmapScale) / verLength) / bitmapScale);
        if (fieldLength.get("x") > horLength && fieldLength.get("y") > verLength){
            if (bitmapScaleY > bitmapScaleX){
                bitmapScale = bitmapScaleY / (((fieldLength.get("x") * bitmapScaleY) / horLength) / bitmapScaleY);
            } else {
                bitmapScale = bitmapScaleX / (((fieldLength.get("y") * bitmapScaleX) / verLength) / bitmapScaleX);
            }
        } else if (fieldLength.get("x") > horLength){
            bitmapScale = bitmapScaleX;
        } else if (fieldLength.get("y") > verLength){
            bitmapScale = bitmapScaleY;
        }

        highScale = highScale * (highScale / ((highScale * bitmapScale) / 100));
        lowScale  = lowScale * (lowScale / ((lowScale * bitmapScale) / 100));

        float widthRatio  = this.screenHeight > this.screenWidth ? lowScale : highScale;
        float HeightRatio = this.screenHeight > this.screenWidth ? highScale : lowScale;

        cellWidth  = Math.round(this.screenWidth  / widthRatio);
        cellHeight = Math.round(this.screenHeight / HeightRatio);
    }

    public void onDraw(Canvas canvas){
        boolean isOk = model.getFieldState();

        if (isOk){
            calculateScaleRatio();
            drawField(canvas);
        } else{
            drawError(canvas);
        }
    }

    private void drawError(Canvas canvas){
        if(errorBitmap == null)errorBitmap = BitmapFactory.decodeResource(getResources(), R.raw.error);
        paint.setColor((Color.WHITE));
        paint.setTypeface(Typeface.create("SANS_SERIF", Typeface.BOLD));
        paint.setTextSize(100F);
        canvas.drawBitmap(errorBitmap, 20F, 300F, paint);
        canvas.drawText("Error Initialization", 230F, 430F, paint);
    }

    private void drawField(Canvas canvas) {

        int[][] field = model.getField();
        int offset = 0;

        paint.setStrokeWidth(10);

        for (int rowY = 0; rowY < field.length; rowY++){
            for(int rowX = 0; rowX < field[rowY].length; rowX++){

                canvas.drawBitmap(Bitmap.createScaledBitmap(grassSprite, cellWidth, cellHeight, true), posX,posY, null);
                int cell = field[rowY][rowX];

                if (cell == 1) {
                    runRight(canvas);
//                    paint.setColor(Color.BLUE);
//                    canvas.drawRect(x, y, x + width, y + height, paint);
                } else if (cell == 11){
                    runLeft(canvas);
                } else if (cell == 12){
                    canvas.drawBitmap(Bitmap.createScaledBitmap(sonicUpSprite, cellWidth, cellHeight, true), posX,posY, null);
                } else if (cell == 13){
                    canvas.drawBitmap(Bitmap.createScaledBitmap(sonicDownSprite, cellWidth, cellHeight, true), posX,posY, null);
                } else if (cell == 2) {
                    canvas.drawBitmap(Bitmap.createScaledBitmap(wallSprite, cellWidth, cellHeight, true), posX,posY, null);
                }  else if (cell == 3) {
                    canvas.drawBitmap(Bitmap.createScaledBitmap(pcSprite, cellWidth, cellHeight, true), posX,posY, null);
                } else if (cell == 4) {
                    canvas.drawBitmap(Bitmap.createScaledBitmap(javaSprite, cellWidth, cellHeight, true), posX,posY, null);
                }

                posX = posX + cellWidth + offset;
            }
            posX = 0;
            posY = posY + cellHeight + offset;
        }
        posY = 0;
    }

    private void runRight(Canvas canvas){
        if (spriteRunCounter == 1) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(runRightSprite1, cellWidth, cellHeight, true), posX,posY, null);
            spriteRunCounter++;
        } else if (spriteRunCounter == 2) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(runRightSprite2, cellWidth, cellHeight, true), posX,posY, null);
            spriteRunCounter = spriteRunCounter - 2;
        } else {
            canvas.drawBitmap(Bitmap.createScaledBitmap(standRightSprite, cellWidth, cellHeight, true), posX,posY, null);
            spriteRunCounter++;
        }
    }

    private void runLeft(Canvas canvas){
        if (spriteRunCounter == 1) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(runLeftSprite1, cellWidth, cellHeight, true), posX,posY, null);
            spriteRunCounter++;
        } else if (spriteRunCounter == 2) {
            canvas.drawBitmap(Bitmap.createScaledBitmap(runLeftSprite2, cellWidth, cellHeight, true), posX,posY, null);
            spriteRunCounter = spriteRunCounter - 2;
        } else {
            canvas.drawBitmap(Bitmap.createScaledBitmap(standLeftSprite, cellWidth, cellHeight, true), posX,posY, null);
            spriteRunCounter++;
        }
    }

/*  Previously Used to resize Bitmap

    public Bitmap drawResizedBitmap(Bitmap image, int maxSize) {
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            return Bitmap.createScaledBitmap(image, width, height, true);
        }
    }
*/



}
