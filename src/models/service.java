package models;

public class service {
    private static service instance;
    private int service_id;
    private String service_name;
    private double price;

    private service() {
        // Private constructor prevents instantiation
    }

    public static synchronized service getInstance() {
        if (instance == null) {
            instance = new service();
        }
        return instance;
    }

    public service(int service_id, String service_name, double price) {
        this.service_id = service_id;
        this.service_name = service_name;
        this.price = price;
    }
    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}