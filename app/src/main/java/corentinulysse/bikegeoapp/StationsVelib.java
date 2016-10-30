package corentinulysse.bikegeoapp;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;


public class StationsVelib {

    @SerializedName("name")
    private String name;
    @SerializedName("coordinates")
    private double[] coordinates;
    @SerializedName("status")
    private String status;
    @SerializedName("bike_stands")
    private int bike_stands;
    @SerializedName("available_bike_stands")
    private int available_bike_stands;
    @SerializedName("available_bikes")
    private int available_bikes;
    @SerializedName("address")
    private String address;
    @SerializedName("position")
    private double[] position;


    public StationsVelib() {
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBike_stands() {
        return bike_stands;
    }

    public void setBike_stands(int bike_stands) {
        this.bike_stands = bike_stands;
    }

    public int getAvailable_bike_stands() {
        return available_bike_stands;
    }

    public void setAvailable_bike_stands(int available_bike_stands) {
        this.available_bike_stands = available_bike_stands;
    }

    public int getAvailable_bikes() {
        return available_bikes;
    }

    public void setAvailable_bikes(int available_bikes) {
        this.available_bikes = available_bikes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "StationsVelib{" +
                "name='" + name + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                ", status='" + status + '\'' +
                ", bike_stands=" + bike_stands +
                ", available_bike_stands=" + available_bike_stands +
                ", available_bikes=" + available_bikes +
                ", address='" + address + '\'' +
                ", position=" + Arrays.toString(position) +
                '}';
    }

}
