import action.UserLogin;
import bean.User;
import javafx.application.Application;
import javafx.application.Platform;
import service.BlockService;
import service.FileDirectoryService;
import service.RootInit;
import service.UserService;
import view.BlockView;

import java.util.Map;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static UserLogin userLogin = new UserLogin();
    static RootInit rootInit = new RootInit();
    static BlockService blockService = BlockService.blockService;
    static UserService userService = UserService.userService;
    static FileDirectoryService fileDirectoryService = FileDirectoryService.fileDirectoryService;

    public static void main(String[] args) {
        Main main = new Main();
        rootInit.initRoot();
        boolean login = false;
        while (!login) {
            String[] msg = main.inNamePasswd();
            boolean temp = userLogin.rootLogin(msg[0], msg[1]);
            login = temp;
        }
        System.out.println("登陆成功");
        String name = "root";
        var flag = true;
        while (flag) {
            User user = userService.getUser(name);
            System.out.print(user.name + "@" + user.currentPath + ": ");
            String cmd = scanner.nextLine();
            switch (cmd) {
                case "q":
                    flag = false;
                    break;
                case "showdirec":
                    blockService.showDirec();
                    break;
                case "useradd":
                    String[] namepasswd = main.inUserAdd();
                    userService.addUser(user, namepasswd[0], namepasswd[1]);
                    break;
                case "touch":
                    String fileName = main.inFileName();
                    fileDirectoryService.addFile(user, fileName);
                    break;
                case "write":
                    String fileName1 = main.inFileName();
                    fileDirectoryService.writeFile(fileName1, "money not evail");
                    break;
                case "cat":
                    String fileName2 = main.inFileName();
                    String content = fileDirectoryService.catFile(fileName2);
                    System.out.println(content);
                    break;
                case "rmfile":
                    String fileName3 = main.inFileName();
                    fileDirectoryService.deleteFile(fileName3);
                    break;
                case "rmdirec":
                    String direcName = main.inFileName();
                    fileDirectoryService.deleteDirec(direcName);
                    break;
                case "showblocks":
                    int count = 0;
                    new Thread(()->{
                        if (count == 0){
//                            count++;
                            Application.launch(BlockView.class, args);
                        }else {
                            BlockView.setNewScene();
                        }

                    }).start();
                    break;
            }
        }
        System.out.println("退出成功");
    }

    public String inFileName(){
        System.out.print("请输入文件名: ");
        String fileName = scanner.nextLine();
        return fileName;
    }

    public String[] inUserAdd(){
        System.out.println("用户注册");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String passwd = scanner.nextLine();
        String[] str = new String[2];
        str[0] = username;
        str[1] = passwd;
        return str;
    }

    public String[] inNamePasswd(){
        System.out.println("root用户登陆");
        System.out.print("用户名：");
        String name = scanner.nextLine();
        System.out.print("密码: ");
        String passwd = scanner.nextLine();
        String[] loginMessage = new String[2];
        loginMessage[0] = name;
        loginMessage[1] = passwd;
        return loginMessage;
    }
}
