package cc.ibooker.zgreendao2;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import cc.ibooker.daogenerator.DaoMaster;
import cc.ibooker.daogenerator.DaoSession;
import cc.ibooker.daogenerator.Friend;
import cc.ibooker.daogenerator.FriendDao;
import cc.ibooker.daogenerator.User;
import cc.ibooker.daogenerator.UserDao;
import de.greenrobot.dao.query.LazyList;

public class MainActivity extends AppCompatActivity {
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;
    private FriendDao friendDao;
    private UserDao userDao;

    // 打开数据库/创建数据库/获取数据库对象
    private void openDb() {
        // 参数1-上下文对象，参数2-数据库名称，参数3-游标工厂CursorFactory
        db = new DaoMaster.DevOpenHelper(this, "ibookerdata.db", null).getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        friendDao = daoSession.getFriendDao();
        userDao = daoSession.getUserDao();
    }

    // 插入用户数据
    private long insertUser(User user) {
        return userDao.insert(user);
    }

    // 插入朋友数据
    private long insertFriend(Friend friend) {
        return friendDao.insert(friend);
    }

    // 查询所有用户信息
    private List<User> queryUserAll() {
//       return userDao.queryBuilder().list();
        return userDao.loadAll();
    }

    // User懒加载
    private List<User> queryUserListLazy() {
        return userDao.queryBuilder().listLazy();
    }

    // 查询所有用户信息
    private List<Friend> queryFriendAll() {
//        return friendDao.queryBuilder().list();
        return friendDao.loadAll();
    }

    // Friend懒加载
    private List<Friend> queryFriendListLazy() {
        return friendDao.queryBuilder().listLazy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDb();

        saveData1();
//        saveData2();
        List<User> users = queryUserAll();
        for (User user : users) {
            Log.d("datas", user.toString());
        }
        List<Friend> friends = queryFriendListLazy();
        for (Friend friend : friends) {
            Log.d("datas", friend.toString());
        }
    }

    // 保存数据1-同时插入两张外键相连的表
    // 查询Friend信息不会包含User信息
    private void saveData1() {
        User user = new User();
        user.setUId(1L);
        user.setUBirthday("1999-10-13");
        user.setUDomicile("中国");
        user.setUEmail("zrunker**@xx.com");
        user.setUHeight(175F);
        user.setUPhone(11111888665L);
        user.setURealname("zrunker");
        user.setUSex("男");
        user.setUWeibo("@zrunker");
        user.setUWeight(62F);

        long id = insertUser(user);

        Friend friend = new Friend();
        friend.setFGname("好友");
        // 添加外键
        friend.setFUid(id);

        insertFriend(friend);
    }

    // 保存数据2-同时插入两张外键相连的表
    // 查询Friend信息会包含User信息
    private void saveData2() {
        User user = new User();
        user.setUId(2L);
        user.setUBirthday("1999-11-13");
        user.setUDomicile("中国1");
        user.setUEmail("zrunker1**@xx.com");
        user.setUHeight(175F);
        user.setUPhone(22222888665L);
        user.setURealname("zrunker1");
        user.setUSex("男");
        user.setUWeibo("@zrunker1");
        user.setUWeight(62F);

        insertUser(user);

        Friend friend = new Friend();
        friend.setFGname("好友1");
        // 添加外键连接对象
        friend.setUser(user);

        insertFriend(friend);
    }
}
