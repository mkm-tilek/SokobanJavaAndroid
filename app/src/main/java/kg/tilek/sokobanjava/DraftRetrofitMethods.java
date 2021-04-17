package kg.tilek.sokobanjava;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//interface LevelCallback {
//    String strField = null;
//    void onSucess(String value);
//    void onFailure();
//}
//
//class LevelFromCallback implements LevelCallback{
//    String strField = "";
//
//    @Override
//    public void onSucess(String value) {
////        System.out.println(value);
//        strField = value;
//    }
//
//    @Override
//    public void onFailure() {
//
//    }
//}


//  Custom Callback to return data from Async request
/*
        Callback<ApiLevel> myCallBack = new Callback<ApiLevel>() {
            String strLevel = "";
            @Override
            public void onResponse(Call<ApiLevel> call, Response<ApiLevel> response) {
                strLevel = response.body().getLevel();
            }
            @Override
            public void onFailure(Call<ApiLevel> call, Throwable t) {

            }
        };
        call.enqueue(myCallBack);*/


//  Retrofit Async request
 /*
    private void getLevelFromApi(int level, LevelCallback apiCallBack)  {
        final BlockingQueue<String> strField = new ArrayBlockingQueue<>(1);
        String apiURL = "https://6077b5331ed0ae0017d6b2f6.mockapi.io/fields/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ApiLevel> call = service.getId(level);

        call.enqueue(new Callback<ApiLevel>() {
            @Override
            public void onResponse(Call<ApiLevel> call, Response<ApiLevel> response) {
//                strField.add(response.body().getLevel());
                apiCallBack.onSucess(response.body().getLevel());
//                apiCallBack.strField = response.body().getLevel();
            }
            @Override
            public void onFailure(Call<ApiLevel> call, Throwable t) {

            }
        });
   }*/


//   Retrofit Sync request must be executed on other Background or other Thread
/*
  private void getLevelFromApi(int level, LevelCallback apiCallBack)  {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(apiURL);
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ApiLevel> call = service.getId(level);

        try {
            Response<ApiLevel> response = call.execute();
            return response.body().getLevel();
        }catch (IOException e){
            System.out.println(e);
        }

        return  apiCallBack.strField;
    }*/

