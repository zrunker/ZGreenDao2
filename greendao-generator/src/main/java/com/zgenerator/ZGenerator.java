package com.zgenerator;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * GreenDao-Generator工具类
 * create by 邹峰立
 */
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
        user.addToMany(friend, fUid);// 一对多

        // 生成相关文件
        try {
            // 参数1-schema，参数2-生成路径
            new DaoGenerator().generateAll(schema, "app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
