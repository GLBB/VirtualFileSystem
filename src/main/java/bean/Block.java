package bean;

public class Block {
    public static int generator = 0;

    public final int id;

    public Block() {
        this.id = generator++;
        used = false; // 未被使用
        start = 0;
        end = 0;
    }

    public byte[] data = new byte[1024];
    public int start;
    public int end;
    public boolean used;

}
