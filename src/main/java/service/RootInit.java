package service;

import bean.DCB;
import bean.User;

public class RootInit {


    public void initRoot(){
        // 创建 / 挂载目录
        DCB realRoot = new DCB("/");

        DCB dcb = new DCB("root");
        realRoot.list.add(dcb);

        // 创建root 用户
        User root = new User("root", "root");
        root.currentPath = "/";
        root.own.add(realRoot);

        User.users.add(root);

        BlockService blockService = BlockService.blockService;
        blockService.init(realRoot, User.users);
    }

    public static void main(String[] args) {
        new RootInit().initRoot();
    }

}
