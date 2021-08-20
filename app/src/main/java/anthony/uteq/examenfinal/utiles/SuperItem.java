package anthony.uteq.examenfinal.utiles;

public class SuperItem {

    private String display = "";
    private String value = "";

    public SuperItem() {
    }

    public SuperItem(String value, String display) {
        this.display = display;
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}
