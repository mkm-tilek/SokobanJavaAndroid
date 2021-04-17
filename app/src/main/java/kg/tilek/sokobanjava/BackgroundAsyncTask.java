package kg.tilek.sokobanjava;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class BackgroundAsyncTask extends AsyncTask {
    private int level;
    private BlockingQueue<String> blockingQueue;

    public BackgroundAsyncTask(int level){
        this.level = level;
        this.blockingQueue = new ArrayBlockingQueue<>(1);
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String apiURL = "https://6077b5331ed0ae0017d6b2f6.mockapi.io/fields/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ApiLevel> callSuccess = service.getId(level);

        try {
            if(callSuccess.execute().isSuccessful()){
                Call<ApiLevel> call = service.getId(level);
                blockingQueue.add(call.execute().body().getLevel());
            }else{
                blockingQueue.add("Api connection Error!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(5000);
            blockingQueue.add("Connection time out!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BlockingQueue<String> getResult(){
        return blockingQueue;
    }
}