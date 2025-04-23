package data;

import org.json.JSONObject;

public class RequestData {
    public RequestData(double x, double y, double r, boolean flag) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.flag = flag;
    }
    private double x;
    private double y;
    private double r;
    private boolean flag;


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public boolean getFlag() {
        return flag;
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