package com.example.holdfoot.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.DevBean;
import model.RepositoryBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.GitHubService;
import services.PersonDevwikiService;

import static services.GitHubService.retrofit;
import static services.PersonDevwikiService.retrofit1;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.btn)
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
//                    getContributorListA();
             //       mHandler.obtainMessage().sendToTarget();
                    getTextBeans();
                    //getContributorListA();
                }catch (Exception e){
                    e.printStackTrace();
                    tv.setText(e.toString()+"onClick");
                }

            }
        });
        super.onResume();
    }
    //同步调用，Android中不允许在Main Thread中进行网络请求，这里使用下面的一步请求
    public List<String> getContributorList() throws Exception{
        GitHubService gitHubService = retrofit.create(GitHubService.class);
        Call<List<String>> call = gitHubService.queryUserReposStr("HOLDfoot");
        List<String> result = call.execute().body();
        Log.d("zhumr","result="+result);
        tv.setText(result.toString());
        return result;

    }

    //异步请求
    public void getContributorListA() throws Exception {
        GitHubService gitHubService = retrofit.create(GitHubService.class);

        Call<List<RepositoryBean>> call = gitHubService.queryUserRepos("HOLDfoot");
        call.enqueue(new Callback<List<RepositoryBean>>() {
            @Override
            public void onResponse(Call<List<RepositoryBean>> call, Response<List<RepositoryBean>> response) {
                List<RepositoryBean> conList=response.body();
                StringBuilder builder =new StringBuilder();
                if (conList ==null){
                    tv.setText("null");
                    return;
                }
                String item=null;
                for (RepositoryBean bean :conList){
                    item = bean.toString();
                    builder.append(item+"\n");
                }
                tv.setText(builder.toString());
            }

            @Override
            public void onFailure(Call<List<RepositoryBean>> call, Throwable t) {
                tv.setText(t.getLocalizedMessage());
            }
        });
    }

    //异步请求
    public void getTextBeans() throws Exception {
        PersonDevwikiService service = retrofit1.create(PersonDevwikiService.class);

        Call<DevBean> call = service.getTestBean();
        call.enqueue(new Callback<DevBean>() {
            @Override
            public void onResponse(Call<DevBean> call, Response<DevBean> response) {
                DevBean conList=response.body();
                StringBuilder builder =new StringBuilder();
                if (conList ==null){
                    tv.setText("null");
                    return;
                }
                tv.setText(conList.toString());
                String item=null;
//                for (DevBean bean :conList){
//                    item = bean.toString();
//                    builder.append(item+"\n");
//                }
//                tv.setText(builder.toString());
            }

            @Override
            public void onFailure(Call<DevBean> call, Throwable t) {
                tv.setText(t.getLocalizedMessage());
            }
        });
    }
}
