public class MyOperation {
    private int in = 0;
    private int out = 0;
    private String id;
    private String pid;
    private String name;
    private int visiable;

    public MyOperation(String myid, String pid, String name, int visiable) {
        this.id = myid;
        this.pid = pid;
        this.name = name;
        this.visiable = visiable;
    }

    public int getVisiable() {
        return visiable;
    }

    public String getPid() {
        return pid;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
