package com.personal_game.masoi.object;

public class Request_ChangPass {
    public Request_ChangPass(String tk, String passOld, String passNew) {
        Tk = tk;
        PassOld = passOld;
        PassNew = passNew;
    }

    public String getTk() {
        return Tk;
    }

    public void setTk(String tk) {
        Tk = tk;
    }

    public String getPassOld() {
        return PassOld;
    }

    public void setPassOld(String passOld) {
        PassOld = passOld;
    }

    public String getPassNew() {
        return PassNew;
    }

    public void setPassNew(String passNew) {
        PassNew = passNew;
    }

    private String Tk;
    private String PassOld;
    private String PassNew;
}
