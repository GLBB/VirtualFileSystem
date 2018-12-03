package action;

import bean.User;
import service.BlockService;

import java.util.ArrayList;

public class UserLogin {
    BlockService blockService = BlockService.blockService;

    public boolean rootLogin(String name, String passwd){
        ArrayList<User> users = blockService.getUsers();
        User user = users.get(0);
        if (user.name.equals(name) && user.passwd.equals(passwd)){
            return true;
        }else {
            return false;
        }
    }

}
