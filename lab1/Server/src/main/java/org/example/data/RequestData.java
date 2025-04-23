package org.example.data;

import org.json.JSONObject;

public class RequestData {
    public RequestData(int x, double y, int r, boolean flag) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.flag = flag;
    }
    private int x;
    private double y;
    private int r;
    private boolean flag;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    @Override
    public String toString(){
        JSONObject jsonData = new JSONObject();
        jsonData.put("x",x);
        jsonData.put("y",y);
        jsonData.put("r",r);
        jsonData.put("flag",flag);
        return jsonData.toString();
    }
}
