package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 既可以表示文件
 */
public class FCB extends ControlBlock<Integer> implements Serializable {

    long length; // 字节数

    public FCB(){
        type = FileType.File;
        length = 0;
    }

}
