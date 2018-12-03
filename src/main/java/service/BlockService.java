package service;

import bean.*;
import util.SerializeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class BlockService {

    public ArrayList<Block> list;
    public static BlockService blockService = new BlockService();

    private BlockService() {
        this.list = BlockInit.initBlocks();
    }

    /**
     * 前 0 -- 8 块保存目录文件信息 FCB DCB
     * 第 9 块保存用户信息
     * 前 0 -- 9 块置为使用中
     * @param realRoot
     *
     */
    public void init(DCB realRoot, ArrayList<User> users){
        // 前 0 -- 8 块保存目录文件信息 FCB DCB
        byte[] bytes = SerializeUtil.serialize(realRoot);
        int blocks = bytes.length / 1024;
        if (blocks > 9) {
            throw new RuntimeException("文件索引超过9块");
        }
        for (int i = 0; i < blocks; i++) {
            Block block = list.get(i);
            int index = i*1024;
            for (int j = 0; j < block.data.length; j++) {
                block.data[j] = bytes[index + j];
            }
            block.end = block.data.length;
        }
        // 复制尾部数据
        int already = blocks * 1024;
        for (int i = already; i < bytes.length; i++) {
            list.get(blocks).data[i] = bytes[i];
            list.get(blocks).end = bytes.length - already;
        }


        // 第 9 块保存用户信息
        byte[] usersBytes = SerializeUtil.serialize(users);
        Block block = list.get(9);
        if (usersBytes.length <= 1024) {
            for (int i = 0; i < usersBytes.length; i++) {
                block.data[i] = usersBytes[i];
            }
        }else {
            System.out.println("用户量达到最大");
        }
        block.end = usersBytes.length;

        // 前10块置为使用中
        for (int i = 0; i < 10; i++) {
            list.get(i).used = true;
        }
    }

    public void saveUser(){
        byte[] bytes = SerializeUtil.serialize(User.users);
        if (bytes.length > 1024){
            throw new RuntimeException("user 达到最大限度");
        }
        Block block = list.get(9);
        for (int i = 0; i < bytes.length; i++) {
            block.data[i] = bytes[i];
        }
        block.end = bytes.length;
    }

    /**
     * 把根文件写入控制块中
     * @param realRoot
     */
    public void writeToBlocks(DCB realRoot){
        byte[] bytes = SerializeUtil.serialize(realRoot);
        writeToBlocks(bytes);
    }

    /**
     * 写目录的控制信息到磁盘
     * @param bytes
     */
    public void writeToBlocks(byte[] bytes){
        int blockNum = bytes.length / 1024;
        if (blockNum > 9) {
            throw new RuntimeException("超限");
        }
        for (int i = 0; i < blockNum; i++) {
            Block block = list.get(i);
            for (int j = 0; j < 1024; j++) {
                block.data[j] = bytes[i*1024 + j];
            }
            block.end = 1024;
        }
        // 写尾部数据
        Block block = list.get(blockNum);
        for (int i = blockNum*1024; i < bytes.length; i++) {
            block.data[i] = bytes[blockNum*1024 + i];
        }
        block.end = bytes.length - blockNum*1024;
    }

    public ArrayList<User> getUsers(){
        Block block = list.get(9);
        byte[] userBytes = new byte[block.end];
        for (int i = 0; i < block.end; i++) {
            userBytes[i] = block.data[i];
        }
        Object obj = SerializeUtil.unseralize(userBytes);
        if (obj instanceof ArrayList) {
            ArrayList<User> users = (ArrayList<User>) obj;
            return users;
        }
        throw new RuntimeException("读取用户异常");
    }

    public void showDirec(){
        int blockNum = getBlockNum();
        byte[] bytes = readBytes(blockNum);
        Object obj = SerializeUtil.unseralize(bytes);
        if (!(obj instanceof DCB)) {
            throw new RuntimeException("文件异常");
        }
        DCB realRoot = (DCB) obj;
        // 展示dcb 打印到控制台
        displayDirec(realRoot, 0);

    }



    public void displayDirec(ControlBlock<ControlBlock> ctb, int count){
        if (ctb == null) {
            return;
        }
        for (int i = 0; i < count; i++) {
            System.out.print("  ");
        }
        System.out.printf("%-10s %tT | %tT %s\n", ctb.name, ctb.createDate, ctb.lastUpdateTime, ctb.type);
        count++;
        if (ctb instanceof DCB){
            for (ControlBlock cb : ctb.list) {
                displayDirec(cb, count);
            }
        }
    }

    public int getBlockNum(){
        Integer result = 0;
        for (int i = 0; i < 9; i++) {
            Block block = list.get(i);
            if (block.end != block.data.length) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * 把磁盘的控制信息读取出来
     */
    public byte[] readBytes(int blockNum){
        int lastBlockIndex = list.get(blockNum).end;
        byte[] bytes = new byte[blockNum*1024 + lastBlockIndex];
        for (int i = 0; i <= blockNum; i++) {
            Block block = list.get(i);
            for (int j = 0; j < block.end; j++) {
                bytes[i*1024 + j] = block.data[j];
            }
        }
        return bytes;
    }

    public DCB getRealRoot(){
        int blockNum = getBlockNum();
        byte[] bytes = readBytes(blockNum);
        Object obj = SerializeUtil.unseralize(bytes);
        if (!(obj instanceof DCB)) {
            throw new RuntimeException("文件异常");
        }
        DCB realRoot = (DCB) obj;
        return realRoot;
    }

    /**
     * 把文件内容写入磁盘
     */
    public ArrayList<Integer> writeFileContent(byte[] contentBytes){
        // 存放内存块
        ArrayList<Integer> result = new ArrayList<>();

        // 将内容字节分块
        int blockNum = contentBytes.length / 1024;
        for (int i = 0; i < blockNum; i++) {
            // 将内容随机放入空闲的磁盘块
            int randBlockNumber = getRandBlock();
            result.add(randBlockNumber);
            Block block = list.get(randBlockNumber);
            for (int j = 0; j < 1024; j++) {
                block.data[j] = contentBytes[i*1024 + j];
            }
            block.end = 1024;
            block.used = true;
        }
        // 写入尾部数据
        int randBlockNumber = getRandBlock();
        result.add(randBlockNumber);
        Block block = list.get(randBlockNumber);
        block.used = true;
        for (int i = 0; i < contentBytes.length; i++) {
            block.data[i] = contentBytes[blockNum*1024 + i];
        }

        if (block != null) {
            block.end = contentBytes.length - blockNum*1024;
        }
        return result;
    }

    public int getRandBlock(){
        Random rand = new Random();
        boolean flag = true;
        int count = 0;
        while (true) {
            int i = rand.nextInt(1024);
            count++;
            if (count > 20) {
                break;
            }
            if (list.get(i).used) {
                continue;
            }
            return i;
        }
        throw new RuntimeException("磁盘已满");
    }

    /**
     * 得到文件内容字节
     * @param fcb
     * @return
     */
    public byte[] getFileContentByte(FCB fcb){
        ArrayList<Byte> bytes = new ArrayList<>();
        for (Integer index : fcb.list) {
            Block block = list.get(index);
            for (int i = 0; i < block.end; i++) {
                bytes.add(block.data[i]);
            }
        }

        // 将list byte 转化为byte数组
        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }
        return result;
    }

    /**
     * 删除文件占用的内存块
     * 把文件占用的磁盘块置为没有使用，且 end 置0
     */
    public void restoreBlock(ArrayList<Integer> indexs){
        for (int i = 0; i < indexs.size(); i++) {
            Block block = list.get(indexs.get(i));
            for (int j = 0; j < block.end; j++) {
                block.data[j] = 0;
            }
            block.end = 0;
            block.used = false;
        }
    }

    public List<Block> getFreeBlock(){
        List<Block> free = list.stream().filter(block -> !block.used).collect(Collectors.toList());
        return free;
    }
}
