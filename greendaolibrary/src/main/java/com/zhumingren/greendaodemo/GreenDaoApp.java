package com.zhumingren.greendaodemo;

import android.app.Application;
import android.util.Log;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Author    ZhuMingren
 * Date      2018/9/11
 * Time      下午9:05
 * DESC      com.zhumingren.greendaodemo
 */
public class GreenDaoApp extends Application {
    
    private static final String TAG = GreenDaoApp.class.getSimpleName() + "/zmr";
    private DaoSession daoSession;
    private UserDao userDao;
    
    @Override
    public void onCreate() {
        super.onCreate();
        /** 全局配置 */
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "green-database");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    
        /** 使用时候的配置 */
        userDao = getDaoSession().getUserDao();
        insertUser();
        for (User user : queryList()) {
            Log.i(TAG, "user: " + user.toString());
        }
    }
    
    /** 全局配置 */
    public DaoSession getDaoSession() {
        return daoSession;
    }
    
    /** 使用时候的配置, 及以下 */
    //插入数据
    private void insertUser() {
        User user = new User(null, "jianguotang", "男", 18, 2000);
        userDao.insert(user);
    }
    
    //查询全部的数据
    private List<User> queryList() {
        UserDao userDao = daoSession.getUserDao();
        Query<User> userQuery = userDao.queryBuilder().orderAsc(UserDao.Properties.Id).build();
        List<User> users = userQuery.list();
        return users;
    }
    
    //删除特定位置的数据
    private void deleteUser(){
        
        userDao.deleteByKey(5l);
    }
    
    /**
     * 对位置 为position的的数据进行修改
     * @param position
     */
    private void updateUser(Long position){
        //查询id是1位置的数据
        User user = userDao.load(5l);
        //对其进行修改
        user.setName("简国堂");
        userDao.update(user);
        
    }
    
    /**
     *  按照属性name和sex来查询user
     * @param name
     */
    private List<User> queryByName(String name,String sex){
        QueryBuilder<User> builder = userDao.queryBuilder();
        Query<User> query = builder
                .where(UserDao.Properties.Name.eq(name),UserDao.Properties.Sex.eq(sex))
                .build();
        List<User> list = query.list();
        return  list;
    }

}
