package services;

import model.DevBean;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by holdfoot on 2017/2/18.
 * http://retrofit.devwiki.net/simple
 */

public interface PersonDevwikiService {
    @GET("simple")
    Call<DevBean> getTestBean();
    Retrofit retrofit1=new Retrofit.Builder().baseUrl("http://retrofit.devwiki.net/").addConverterFactory(GsonConverterFactory.create()).build();
}
