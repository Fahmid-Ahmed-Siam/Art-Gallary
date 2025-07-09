package Entity;

public class Painting extends ArtRequirement {
    public Painting(String name, String author, double price) {
        super(name, author, price);
    }

    @Override
    public String getDisplayText() {
        return getName() + " by " + getAuthor() + " - BDT" + getPrice();
    }
}