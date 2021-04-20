package org.acme;


public class WorkOrder {
    public String workOrderId;
    public String userId;
    public String title;
    public String description;
    public String type;
    public String telephone;
    public String addressCity;
    public String addressDistrict;
    public String openAddress;
    public String workerId;
    public int status;

    public WorkOrder(String workOrderId, String userId, String title, String description, String type, String telephone, String addressCity, String addressDistrict, String openAddress, String workerId, int status) {
        this.workOrderId = workOrderId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.type = type;
        this.telephone = telephone;
        this.addressCity = addressCity;
        this.addressDistrict = addressDistrict;
        this.openAddress = openAddress;
        this.workerId = workerId;
        this.status = status;
    }

    public WorkOrder() {
    }

    @Override
    public String toString() {
        return "WorkOrder{" +
                "workOrderId='" + workOrderId + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", telephone='" + telephone + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", addressDistrict='" + addressDistrict + '\'' +
                ", openAddress='" + openAddress + '\'' +
                ", workerId='" + workerId + '\'' +
                ", status=" + status +
                '}';
    }
}
