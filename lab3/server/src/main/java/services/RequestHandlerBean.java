package services;

import data.RequestData;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean(name = "requestHandler", eager = true)
@SessionScoped
public class RequestHandlerBean {
    @Inject
    private AreaCheckBean areaCheckBean;
    @Inject
    private DataBaseManagerBean dataBaseManagerBean;
    private String result;

    public RequestHandlerBean() {
    }

    public String handleRequest() {
        String requestType = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("requestType");

        if (requestType == null){
            System.out.println("requestType is null");
            result = "GG";
        }
        else {
            if (requestType.equals("AREA_CHECK")) {
                dataBaseManagerBean.addRequestData(areaCheckBean.checkArea());
                result = areaCheckBean.getResult();
            } else if (requestType.equals("GET_ALL_REQUESTS")) {
                result = dataBaseManagerBean.getAllRequests();
            } else if (requestType.equals("DROP_ALL_RESULTS")) {
                result = dataBaseManagerBean.dropAllRequests();
            } else if (requestType.equals("GO_TO_MAIN")) {
                result = "goToNextPage";
            } else {
                result = "Неизвестный тип запроса";
            }
        }

        System.out.println(result + "-------------------------");
        return result;
    }

    public String getResult() {
        return result;
    }
}