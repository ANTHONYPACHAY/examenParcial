package anthony.uteq.examenfinal.utiles;

public class ObjectLocation {

    private String Name = "";
    private String direction = "";
    private String authority = "";
    private String contact = "";
    private String logo = "";
    private String url = "";
    private double lat = 0;
    private double lng = 0;
    private boolean isCampus = false;

    public ObjectLocation() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isCampus() {
        return isCampus;
    }

    public void setCampus(boolean campus) {
        isCampus = campus;
    }

    @Override
    public String toString() {
        return Name;
    }
}
