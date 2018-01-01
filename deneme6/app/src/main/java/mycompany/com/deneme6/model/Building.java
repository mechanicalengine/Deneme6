package mycompany.com.deneme6.model;

/**
 * Created by ugur on 28-Dec-17.
 */

public class Building {
    private String buildingId;
    private String buildingName;
    private String buildingAddress;

    public Building(){

    }


    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingLocation) {
        this.buildingAddress = buildingAddress;
    }

    public Building(String buildingId, String buildingName, String buildingAddress){

        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.buildingAddress = buildingAddress;
    }




}
