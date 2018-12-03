package service;

import bean.ControlBlock;
import bean.DCB;
import bean.FCB;
import bean.User;
import util.SerializeUtil;

import java.util.ArrayList;

public class FileDirectoryService {

    public static FileDirectoryService fileDirectoryService = new FileDirectoryService();

    private FileDirectoryService(){}

    BlockService blockService = BlockService.blockService;

    /**
     * 创建文件
     * @param user
     * @param fileName
     */
    public void addFile(User user, String fileName){
        FCB fcb = new FCB();
        fcb.name = fileName;

        // 等到目录的控制信息
        DCB realRoot = blockService.getRealRoot();
        // 得到用户的工作目录
        DCB currentDirec = getWorkingDirec(user.currentPath, realRoot);
        // 添加到当前目录下
        currentDirec.list.add(fcb);
        // 将根目录重新写回磁盘
        blockService.writeToBlocks(realRoot);

    }

    /**
     * 得到当前工作目录
     * @param workingDirec
     * @param dcb
     * @return
     */
    public DCB getWorkingDirec(String workingDirec, DCB dcb){
        if (dcb != null) {
            if (workingDirec.equals(dcb.name)) {
                return dcb;
            }else {
                for (ControlBlock cb : dcb.list) {
                    if (cb instanceof DCB) {
                        DCB result = getWorkingDirec(workingDirec, (DCB) cb);
                        if (result != null){
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

    public FCB getFCB(String fileName, DCB dcb) {
        if (dcb != null) {
            for (ControlBlock cb : dcb.list) {
                if (cb instanceof FCB) {
                    if (fileName.equals(cb.name)) {
                        return (FCB) cb;
                    }
                }
                var result = getFCB(fileName, (DCB) cb);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 将文件内容写入文件
     * @param fileName
     * @param content
     */
    public void writeFile(String fileName, String content) {
        // 得到根文件
        DCB realRoot = blockService.getRealRoot();
        // 根据文件名得到文件
        FCB fcb = getFCB(fileName, realRoot);
        // 将内容序列化为字节
        byte[] contentBytes = SerializeUtil.serialize(content);
        // 将内容字节存入磁盘
        ArrayList<Integer> blocksNumber = blockService.writeFileContent(contentBytes);
        // 将使用的的内容块号码写入 fcb 中
        for (Integer index : blocksNumber) {
            fcb.list.add(index);
        }
        // 将根目录重新写入控制块中
        blockService.writeToBlocks(realRoot);
    }

    /**
     * 查看文件内容
     * @param fileName
     */
    public String catFile(String fileName){
        // 得到根文件
        DCB realRott = blockService.getRealRoot();
        // 根据文件名得到 FCB
        FCB fcb = getFCB(fileName, realRott);
        // 得到文件内容
        byte[] fileContentByte = blockService.getFileContentByte(fcb);
        Object obj = SerializeUtil.unseralize(fileContentByte);
        if (obj instanceof String) {
            return (String) obj;
        }
        throw new RuntimeException("读取错误");
    }



    public DCB getParent(ControlBlock cb, DCB dcb){
        if (dcb.list.contains(cb)){
            return dcb;
        }
        for (ControlBlock cb1 : dcb.list) {
            if (cb1 instanceof DCB) {
                DCB result = getParent(cb, (DCB) cb1);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 删除文件
     * @param
     */
    public void deleteFile(String fileName){
        // 得到根目录
        DCB realRoot = blockService.getRealRoot();
        // 找到文件
        FCB fcb = getFCB(fileName, realRoot);
        // 把文件占用的磁盘块置为没有使用，且 end 置0
        blockService.restoreBlock(fcb.list);
        // 得到该文件的父文件
        DCB parent = getParent(fcb, realRoot);
        // 从父目录中删除
        parent.list.remove(fcb);
        // 将控制块写入磁盘
        blockService.writeToBlocks(realRoot);
    }

    /**
     * 删除目录
     * @param directoryName
     */
    public void deleteDirec(String directoryName){
        // 得到根目录
        DCB realRoot = blockService.getRealRoot();
        // 得到要输出的目录
        DCB dcb = getWorkingDirec(directoryName, realRoot);
        // 判断该目录是否为空
        if (dcb.list.size() != 0) {
            System.out.println("目录中还有文件不能删除");
        }
        // 得到该目录的父目录
        DCB parent = getParent(dcb, realRoot);
        if (parent == null) {
            System.out.println("删除失败");
        }
        parent.list.remove(dcb);
        // 写回磁盘
        blockService.writeToBlocks(realRoot);
    }




}
