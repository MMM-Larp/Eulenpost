package net.gothax.larp.larpweb.model;

public enum House {
    GRYFFINDOR("Gryffindor"),
    HUFFLEPUFF("Hufflepuff"),
    RAVENCLAW("Ravenclaw"),
    SLYTHERIN("Slytherin");

    private String title;

    House(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
