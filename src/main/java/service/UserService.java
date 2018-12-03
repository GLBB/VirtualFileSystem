package service;

import bean.Block;
import bean.DCB;
import bean.User;
import util.SerializeUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserService {
    BlockService blockService = BlockService.blockService;
    public static UserService userService = new UserService();

    private UserService(){}

    public void addUser(User user, String name, String passwd){
        if (!user.name.equals("root")){
            throw new RuntimeException("只有root 才能创建 user");
        }
        User u1 = new User(name, passwd);
        // 设置用户工作目录
        u1.currentPath = "/"+name;
        User.users.add(u1);


        blockService.saveUser();

        // 从磁盘读出控制信息， 创建用户目录
        int flag = -1;
        for (int i = 0; i < 9; i++) {
            Block block = blockService.list.get(i);
            if (block.end != block.data.length) {
                flag = i;
                break;
            }
            if (i == 8) {
                throw new RuntimeException("内部错误");
            }
        }
        // 创建保存用户信息需要的字节数组
        int length = flag * 1024 + blockService.list.get(flag).end;
        byte[] bytes = new byte[length];

        for (int i = 0; i <= flag; i++) {
            Block block = blockService.list.get(i);
            for (int j = 0; j < block.end; j++) {
                bytes[i*1024 + j] = block.data[j];
            }
        }
        Object obj = SerializeUtil.unseralize(bytes);
        DCB dcb = null;
        if (obj instanceof DCB) {
             dcb = (DCB) obj;
        }else {
            throw new RuntimeException("对象转换错误");
        }
        dcb.list.add(new DCB(name));

        // 创建目录后在将信息写回磁盘
        byte[] reBytes = SerializeUtil.serialize(dcb);
        if (reBytes.length > 1024 * 9) {
            throw new RuntimeException("信息太大");
        }
        blockService.writeToBlocks(reBytes);
    }
    public User getUser(String name){
        ArrayList<User> users = blockService.getUsers();
        User user = users.stream().filter(u -> u.name.equals(name)).findAny().get();
        return user;
    }


}
