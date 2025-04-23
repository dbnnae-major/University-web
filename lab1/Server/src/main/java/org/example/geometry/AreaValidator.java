package org.example.geometry;

public class AreaValidator {
    public boolean areaConfirm(int x, double y, int r){
        if ((x >= -r && x <= 0) && (y >= 0 && y <= (double) r /2)){
            return true;
        }
        if ((x >= 0 && x <= r) && (y <= ( (double) -x /2 + (double) r /2) && y >= 0)) { // y < -x/2 + r/2
            return true;
        }
        if ((x*x+y*y) <= (double) (r * r) /2 && y <= 0 && x <= 0){
            return true;
        }
        return false;
    }
}
