package codedeploy.bean;

import sun.security.provider.MD5;

public class Code {
    private int cno;
    private String filename;
    private boolean isBackup;
    private String md5;
    private int ono;
    public Code(){};
    public Code(int cno,String filename,boolean isBackup,String md5,int ono)
    {
        this.cno=cno;
        this.filename=filename;
        this.isBackup=isBackup;
        this.md5=md5;
        this.ono=ono;
}
    public int getCno() {
        return cno;
    }

    public int getOno() {
        return ono;
    }

    public String getMd5() {
        return md5;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isBackup() {
        return isBackup;
    }

    public void setBackup(boolean backup) {
        isBackup = backup;
    }

    public void setCno(int cno) {
        this.cno = cno;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setOno(int ono) {
        this.ono = ono;
    }

}
