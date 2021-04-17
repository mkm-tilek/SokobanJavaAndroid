package kg.tilek.sokobanjava;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;


interface ApiService {
    @GET("levels/{id}")
    Call<ApiLevel> getId(@Path("id") int id);
}


public class ApiLevel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("level")
    @Expose
    private String level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}