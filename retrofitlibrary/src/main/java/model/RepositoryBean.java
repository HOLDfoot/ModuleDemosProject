package model;

/**
 * Created by holdfoot on 2017/2/18.
 * 2. 定义一个Bean，使用Gson完成请求数据与POJO的转换
 */
public class RepositoryBean {

    //int id;
    //String name;
    String full_name;
    String owner;

    int contributions;

    @Override
    public String toString() {
        return full_name + " (" + contributions + ")";
    }
}
