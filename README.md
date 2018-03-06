# ZGreenDao2
Android ORM框架GreenDao2的基本使用

>作者：邹峰立，微博：zrunker，邮箱：zrunker@yahoo.com，微信公众号：书客创作，个人平台：[www.ibooker.cc](http://www.ibooker.cc)。

>本文选自[书客创作](http://www.ibooker.cc)平台第9篇文章。[阅读原文](http://www.ibooker.cc/article/9/detail) 。

![书客创作](http://upload-images.jianshu.io/upload_images/3480018-a2014a4fe468fa4a..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在了解greenDao之前首要知道什么是SQLite，什么是ORM？
SQLite是一款轻量级嵌入式关系型数据库，也是移动端最为常用的一种关系型数据库。
ORM对象关系映射，简单一点说就是实现数据库表结构与对象一一对印。

![对象关系映射](http://upload-images.jianshu.io/upload_images/3480018-6db15e30ad29bba4..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### greenDao简介
>官网解释：greenDAO: Android ORM for your SQLite database。
greenDAO是Android中对SQLite进行对象关系映射的一个框架。

greenDao之所以很流行，跟它的优点是息息相关的：
- 一个精简的库
- 性能最大化
- 内存开销最小化
- 易于使用的 API
- 对 Android 进行高度优化

Android中ORM框架有很多，如OrmLite，ActiveAndroid等，但是相对而言，greenDao性能更加优秀。
![常用orm框架对比图](http://upload-images.jianshu.io/upload_images/3480018-44d8e9c4450e5132..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### greenDao-Generator2使用
在使用greenDao之前要生成相应的表，Bean，Dao文件等，这个生成过程可以利用Eclipse/MyEclipse/Android Studio等相关工具来实现。
如果使用Eclipse/MyEclipse实现，需要添加greendao-generator:2.1.0和freemaker两个jar。
如果使用Android Studio实现，只要在创建的Java Module中添加greendao-generator:2.1.0即可。
下面将以Android Studio为例，实现这一功能。

**配置：**首先创建一个Android工程，然后添加一个Module（Java Library），最后在Module的build.gradle文件中中添加greendao-generator:2.1.0依赖。
```
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    compile 'de.greenrobot:greendao-generator:2.1.0'
}
```
**使用：**greendao-generator是用来生成相应的表，Bean，Dao文件等。假如要生成一个用户表，一个朋友表，一个用户可以有多个朋友，一个朋友数据最多对应一个用户，那么该如何实现呢？
```
public class ZGenerator {
    public static void main(String args[]) {
        // 创建一个模式Shcema
        // 参数1-版本，参数2-包名
        Schema schema = new Schema(1, "cc.ibooker.daogenerator");

        // 添加Entity-相当于表-用户对象
        // 参数-代表生成对象类名
        Entity user = schema.addEntity("User");
        user.addIdProperty().autoincrement();// 添加ID-自增
        user.addLongProperty("uId");
        user.addStringProperty("uRealname");
        user.addStringProperty("uSex");
        user.addStringProperty("uBirthday");
        user.addFloatProperty("uHeight");
        user.addFloatProperty("uWeight");
        user.addStringProperty("uDomicile");
        user.addLongProperty("uPhone");
        user.addStringProperty("uEmail");
        user.addStringProperty("uWeibo");

        // 朋友表/对象
        Entity friend = schema.addEntity("Friend");
        friend.addIdProperty().autoincrement();// 添加ID-自增
        friend.addStringProperty("fGname");
        // 添加外键-只能关联User主键
        Property fUid = friend.addLongProperty("fUid").getProperty();

        friend.addToOne(user, fUid);// 一对一
        user.addToMany(friend, fUid).setName("friends");// 一对多

        // 生成相关文件
        try {
            // 参数1-schema，参数2-生成路径
            new DaoGenerator().generateAll(schema, "app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
1、创建Schema（设置模式）
```
/**
 * @param version 版本号-可以理解为数据库版本号
 * @param defaultJavaPackage 默认包名-即文件生成路径
 **/
Schema schema = new Schema(int version, String defaultJavaPackage);
```
2、添加表对象
```
/**
 * @param className 生成Bean文件的类名
 **/
Entity entity = schema.addEntity(String className);
// 添加主键-并实现主键自增
entity.addIdProperty().autoincrement();
// 添加字段名-根据添加类型进行设置
entity.addStringProperty(String propertyName);
entity.addLongProperty(String propertyName);
```
添加外键-只能关联主键
```
// 添加外键-只能关联User主键
Property property = entity.addLongProperty(String propertyName).getProperty();
```
一对一关联
```
/**
 * @param target 目标表对象（关联对象）
 * @param fkProperty 外键
 **/
entity.addToOne(Entity target, Property fkProperty);
```
一对多关联
```
/**
 * @param target 目标表对象（关联对象）
 * @param fkProperty 外键
 **/
ToMany tomany = entity.addToMany(Entity target, Property fkProperty);
// 设置外键对象名称
tomany.setName(String name);
```
3、编译执行
```
try {
    // 参数1-schema，参数2-生成根路径
    new DaoGenerator().generateAll(schema, "app/src/main/java");
} catch (Exception e) {
    e.printStackTrace();
}
```
![Generator生成结构图](http://upload-images.jianshu.io/upload_images/3480018-786941cbaa1388bf..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
### greenDao2使用
**配置：**在build.gradle（app）中加入greendao:2.1.0依赖。
```
dependencies {
    compile 'de.greenrobot:greendao:2.1.0'
}
```
**使用：**greendao:2.1.0主要是用来操作数据库，而对于数据库的操作主要是增删改查。
在实现数据库增删改查之前必须对相关Dao文件进行实例化，那么该如何进行实例化呢？
```
// 获取一个可读可写数据库对象，参数1-上下文对象，参数2-数据库名称，参数3-游标工厂CursorFactory
SQLiteDatabase db = new DaoMaster.DevOpenHelper(this, "ibookerdata.db", null).getWritableDatabase();
// 获取DAO模式
DaoMaster daoMaster = new DaoMaster(db);
DaoSession daoSession = daoMaster.newSession();
FriendDao friendDao = daoSession.getFriendDao();
UserDao userDao = daoSession.getUserDao();
```
对Dao对象进行实例化之后，就可以通过相关Dao来操作相关表。
1、插入数据-GreenDao可以实现单一对象插入和批量插入功能。
- 插入一条数据
```
// 参数为User对象
long id = userDao.insert(User user);
```
- 批量插入数据
```
// 参数为User对象集
userDao.insertInTx(List<User> users);
```
2、修改数据-GreenDao可以实现单一对象修改和批量修改功能。
- 修改一条数据
```
// 参数为User对象
userDao.update(User user);
```
- 批量修改数据
```
// 参数为User对象集
userDao.updateInTx(List<User> users);
```
3、删除数据
- 删除一条数据
```
userDao.delete(User user);
```
- 根据主键ID删除数据
```
userDao.deleteByKey(K key);
```
- 批量删除数据
```
userDao.deleteInTx(List<User> users);
```
- 删除所有数据
```
userDao.deleteAll();
```
4、查询数据
- 查询所有数据-三种方式
```
// 方式一
List<User> list = userDao.queryBuilder().list();
// 方式二
List<User> list = new ArrayList<>();
Iterator iterator = userDao.queryBuilder().listIterator();
while (iterator.hasNext()) {
     User user = (User) iterator.next();
     // 一般用来筛选需要的对象
     list.add(user);
}
// 方式三
userDao.loadAll();
```
- 懒加载-记得使用完之后关闭掉close
```
LazyList<User> list = userDao.queryBuilder().listLazy();
list.close();
```
- 条件查询
```
// keyWord为关键字
// 查询相等数据
userDao.queryBuilder().where(UserDao.Properties.URealname.eq(keyWord)).unique();
// 查询相等数据集
userDao.queryBuilder().where(UserDao.Properties.URealname.eq(keyWord)).list();
// 查询不相等数据
userDao.queryBuilder().where(UserDao.Properties.URealname.notEq(keyWord)).list();
// 模糊查询
userDao.queryBuilder().where(UserDao.Properties.UBirthday.like("%" + keyWord + "%")).list();
// 范围查询-minValue最小值，maxValue最大值
userDao.queryBuilder().where(UserDao.Properties.UBirthday.between(minValue, maxValue)).list();
// 查询大于
userDao.queryBuilder().where(UserDao.Properties.UHeight.gt(keyWord)).list();
// 大于等于
userDao.queryBuilder().where(UserDao.Properties.UHeight.ge(keyWord)).list();
// 小于
userDao.queryBuilder().where(UserDao.Properties.UWeight.lt(keyWord)).list();
// 小于等于
userDao.queryBuilder().where(UserDao.Properties.UWeight.le(keyWord)).list();
// 升序查询
userDao.queryBuilder().orderAsc(UserDao.Properties.Id).list();
// 倒序查询
userDao.queryBuilder().orderDesc(UserDao.Properties.Id).list();
```
- 原生SQL查询
```
// 查询有朋友的用户信息
String sql = "_id in (select f_uid from friend)";
userDao.queryBuilder().where(new WhereCondition.StringCondition(sql)).list();
```
- 线程查询
```
final Query query = userDao.queryBuilder().build();
new Thread(new Runnable() {
     @Override
     public void run() {
         query.forCurrentThread().list();
     }
}).start();
```
- 一对一查询
```
List<Friend> list = friendDao.queryBuilder().list();
for (Friend friend : list) {
    User user = friend.getUser();
}
```
- 一对多查询
```
List<User> list = userDao.queryBuilder().list();
for (User user : list) {
    List<Friend> friends = user.getFriends();
}
```
greenDao提供了很多方法，这里只是写了一些常用方法。

[GitHub地址](https://github.com/zrunker/ZGreenDao2)
[阅读原文](http://www.ibooker.cc/article/9/detail)

----------
![微信公众号：书客创作](http://upload-images.jianshu.io/upload_images/3480018-b3f2e43ecc6aa947..jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
