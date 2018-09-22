package in.bookarcade.app.model;

public class Address {

    private String address_id;
    private String name;
    private String address1;
    private String address2;
    private String landmark;
    private String phone;
    private String pincode;
    private String city;
    private String state;

    public Address(String address_id, String name, String address1, String address2, String landmark, String phone, String pincode, String city, String state) {
        this.address_id = address_id;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.landmark = landmark;
        this.phone = phone;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
    }

    public String getAddressId() {
        return address_id;
    }

    public void setAddressId(String address_id) {
        this.address_id = address_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
