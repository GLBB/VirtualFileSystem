package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {
    public String name;

    public String passwd;

    public Date createDate; // 创建用户的时间

    public String currentPath; // 当前工作目录

    public ArrayList<DCB> own;  // 对于哪些目录拥有读写权限

    public static ArrayList<User> users = new ArrayList<>();

    public User() {
        name = "unknown";
        createDate = new Date();
        own = new ArrayList<>();
    }


    public User(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
        createDate = new Date();
        own = new ArrayList<>();
    }
}
