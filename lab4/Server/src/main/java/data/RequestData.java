package data;

import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "Requests")
public class RequestData {
    public RequestData(double x, double y, double r, boolean flag, String owner) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.flag = flag;
        this.owner = owner;
    }


    public RequestData() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x")
    private double x;
    @Column(name = "y")
    private double y;
    @Column(name = "r")
    private double r;
    @Column(name = "flag")
    private boolean flag;
    @Column(name = "owner")
    private String owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", flag=" + flag +
                ", owner='" + owner + '\'' +
                '}';
    }
}   