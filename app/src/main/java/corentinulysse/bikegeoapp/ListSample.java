package corentinulysse.bikegeoapp;

/**
 * Classe Sample qui est utilisé par le SampleAdapter afin d'afficher les différents éléments voulu dans le ListView du ListFragment
 */
public class ListSample {

    private String name = "Chatelet";
    private String status = "OPEN";
    private int bike_stands = 2;
    private int available_bike_stands = 2;
    private int available_bikes = 2;
    private String address = "3 rue de Vouillé";
    private double[] position = {43.344, 2.403};

    /**
     * Constructeur
     * @param status ouvert ou fermé
     * @param bike_stands nombre de supports total
     * @param available_bike_stands nombre de supports disponibles
     * @param available_bikes nombre de vélibs disponibles
     * @param name nom de la station
     * @param position coordonnées géographiques de la station
     */
    public ListSample(String status, int bike_stands, int available_bike_stands, int available_bikes, String name, double[] position) {
        this.status = status;
        this.bike_stands = bike_stands;
        this.available_bike_stands = available_bike_stands;
        this.available_bikes = available_bikes;
        this.name = name;
        this.position = position;
    }

    /*
    Getter et Setter correspondant aux données privées de la classe
     */

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public int getBike_stands() {
        return bike_stands;
    }

    public int getAvailable_bike_stands() {
        return available_bike_stands;
    }

    public int getAvailable_bikes() {
        return available_bikes;
    }

    public String getAddress() {
        return address;
    }

    public double[] getPosition() {
        return position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBike_stands(int bike_stands) {
        this.bike_stands = bike_stands;
    }

    public void setAvailable_bike_stands(int available_bike_stands) {
        this.available_bike_stands = available_bike_stands;
    }

    public void setAvailable_bikes(int available_bikes) {
        this.available_bikes = available_bikes;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }
}
