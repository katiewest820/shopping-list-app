package hello;

public class GroceryList {

    public String name;
    public boolean active;
    public int itemCount;

    public GroceryList(String name, boolean active, int itemCount) {
        this.name = name;
        this.active = active;
        this.itemCount = itemCount;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public int getItemCount() {
        return itemCount;
    }
}
