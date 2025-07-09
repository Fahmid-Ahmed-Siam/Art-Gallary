package Entity;

public abstract class ArtRequirement implements ArtItem {
    private String name, author;
    private double price;

    public ArtRequirement(String name, String author, double price) {
        this.name = name;
        this.author = author;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public abstract String getDisplayText();
}