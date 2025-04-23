package services;

import data.RequestData;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "areaCheck", eager = true)
@SessionScoped
public class AreaCheckBean {
    private String result;
    public AreaCheckBean() {}
    public RequestData checkArea() {
        RequestData requestData = (RequestData) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("requestData");

        double x = requestData.getX();
        double y = requestData.getY();
        double r = requestData.getR();
        boolean flag = areaConfirm(x, y, r);
        requestData.setFlag(flag);

        if (flag) {result = "POPAL";} else {result = "NE POPAL";}

        return requestData;
    }

    public String getResult() {
        return result;
    }

    public boolean areaConfirm(Double x, Double y, Double r) {
        if ((x >= -r && x <= 0) && (y >= 0 && y <= (double) r / 2)) {
            return true;
        }
        if ((x >= 0 && x <= r) && (y <= ((double) -x / 2 + (double) r / 2) && y >= 0)) {
            return true;
        }
        if ((x * x + y * y) <= (double) (r * r) / 4 && y <= 0 && x <= 0) {
            return true;
        }
        return false;
    }
}
