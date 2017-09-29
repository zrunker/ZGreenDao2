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
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.WhereCondition;

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

    // 插入朋友数据
    private long insertFriend(Friend friend) {
        return friendDao.insert(friend);
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

    // 一对一查询
    private void queryFriendOneToOne() {
        List<Friend> list = friendDao.queryBuilder().list();
        for (Friend friend : list) {
            User user = friend.getUser();
            Log.d("onetoone", friend.toString());
            Log.d("onetoone", user.toString());
        }
    }

    /**
     * 插入User
     */
    // 插入用户数据
    private long insertUser(User user) {
        return userDao.insert(user);
    }

    // 批量插入
    private void insertUsers(List<User> users) {
        userDao.insertInTx(users);
    }

    /**
     * 修改User
     */
    // 修改用户数据
    private void updateUser(User user) {
        userDao.update(user);
    }

    // 批量修改
    private void updateUsers(List<User> users) {
        userDao.updateInTx(users);
    }

    /**
     * 删除User
     */
    // 删除用户数据
    private void deleteUser(User user) {
        userDao.delete(user);
    }

    // 根据ID删除用户信息
    private void deleteUserById(Long id) {
        userDao.deleteByKey(id);
    }

    // 批量删除数据
    private void deleteUsers(List<User> users) {
        userDao.deleteInTx(users);
    }

    /**
     * 查询
     */
    // 查询所有用户信息
    private List<User> queryUserAll() {
        // 方式一
//       List<User> list = userDao.queryBuilder().list();
        // 方式二
//        LazyList<User> list = userDao.queryBuilder().listLazy();
//        list.close();
        // 方式三
//        List<User> list = new ArrayList<>();
//        Iterator iterator = userDao.queryBuilder().listIterator();
//        while (iterator.hasNext()) {
//            User user = (User) iterator.next();
//            // 一般用来筛选需要的对象
//            list.add(user);
//        }
        // 方式四
        return userDao.loadAll();
    }

    // 查询相等数据
    private User queryEqUser(String keyword) {
        return userDao.queryBuilder().where(UserDao.Properties.URealname.eq(keyword)).unique();
    }

    // 查询相等数据集
    private List<User> queryEqUsers(String keyword) {
        return userDao.queryBuilder().where(UserDao.Properties.URealname.eq(keyword)).list();
    }

    // 查询不相等数据
    private List<User> queryNotEqUsers(String keyword) {
        return userDao.queryBuilder().where(UserDao.Properties.URealname.notEq(keyword)).list();
    }

    // 模糊查询
    private List<User> queryLikeUsers(String keyWord) {
        return userDao.queryBuilder().where(UserDao.Properties.UBirthday.like("%" + keyWord + "%")).list();
    }

    // 范围查询
    private List<User> queryBetweenUsers(String minValue, String maxValue) {
        return userDao.queryBuilder().where(UserDao.Properties.UBirthday.between(minValue, maxValue)).list();
    }

    // 查询大于
    private List<User> queryGtUsers() {
        return userDao.queryBuilder().where(UserDao.Properties.UHeight.gt(170)).list();
    }

    // 大于等于
    private List<User> queryGeUsers() {
        return userDao.queryBuilder().where(UserDao.Properties.UHeight.ge(170)).list();
    }

    // 小于
    private List<User> queryLtUsers() {
        return userDao.queryBuilder().where(UserDao.Properties.UWeight.lt(62)).list();
    }

    // 小于等于
    private List<User> queryLeUsers() {
        return userDao.queryBuilder().where(UserDao.Properties.UWeight.le(62)).list();
    }

    // 升序查询
    private List<User> queryAscUsers() {
        return userDao.queryBuilder().orderAsc(UserDao.Properties.Id).list();
    }

    // 倒序查询
    private List<User> queryDescUsers() {
        return userDao.queryBuilder().orderDesc(UserDao.Properties.Id).list();
    }

    // 原生SQL查询
    private List<User> querySqlUsers() {
        // 查询有朋友的用户信息
        String sql = "_id in (select f_uid from friend)";
        return userDao.queryBuilder().where(new WhereCondition.StringCondition(sql)).list();
    }

    // 线程查询
    private void queryThreadUsers() {
        final Query query = userDao.queryBuilder().build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                query.forCurrentThread().list();
            }
        }).start();
    }

    // 一对多查询
    private void queryOneToMany() {
        List<User> list = userDao.queryBuilder().list();
        for (User user : list) {
            List<Friend> friends = user.getFriendList();
            Log.d("oneTomany", user.toString());
            for (Friend friend : friends) {
                Log.d("oneTomany", friend.toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDb();

        saveData1();
//        saveData2();
        List<User> users = querySqlUsers();
        for (User user : users) {
            Log.d("datas", user.toString());
        }
        List<Friend> friends = queryFriendListLazy();
        for (Friend friend : friends) {
            Log.d("datas", friend.toString());
        }

        queryOneToMany();
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
