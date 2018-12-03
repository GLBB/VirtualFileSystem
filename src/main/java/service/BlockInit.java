package service;

import bean.Block;

import java.util.ArrayList;

public class BlockInit {

    public static ArrayList<Block> initBlocks(){ // 初始化磁盘空间 1M 磁盘空间
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (int i = 0; i < 1024; i++) {
            blocks.add(new Block());
        }
        return blocks;
    }



}
