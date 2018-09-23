package services;

import java.util.List;

import model.RepositoryBean;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by holdfoot on 2017/2/18.
 * 3. 定一个Interface，完成Retrofit初始化和封装网络请求涉及的接口
 */

public interface GitHubService {
    @GET("orgs/{orgName}/repos")
    Call<List<RepositoryBean>> queryOrgRepos(
            @Path("orgName") String orgName);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /***
     * 以下是自定义部分
     ***/
    @GET("users/{userName}/repos")
    Call<List<RepositoryBean>> queryUserRepos(
            @Path("userName") String userName);

    @GET("users/{userName}/repos")
    Call<List<String>> queryUserReposStr(
            @Path("userName") String userName);
}