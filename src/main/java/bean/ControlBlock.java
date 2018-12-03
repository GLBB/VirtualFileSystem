package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ControlBlock<T> implements Serializable {

    public String name;

    public FileType type;

    public Date createDate;

    public ArrayList<T> list;

    public Date lastUpdateTime;

    public ControlBlock() {
        list = new ArrayList<T>();
        createDate = new Date();
        lastUpdateTime = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
