package bean;

import util.SerializeUtil;

import java.io.Serializable;

public class DCB extends ControlBlock<ControlBlock> implements Serializable {

    long size;

    public DCB(String name) {
        this.name = name;
        this.size = 0;
        type = FileType.Directory;
    }
}
