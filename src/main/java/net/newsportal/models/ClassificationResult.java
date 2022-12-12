package net.newsportal.models;

import java.util.ArrayList;

public class ClassificationResult {
    public ArrayList<ClassificationResultItem> items;

    public ArrayList<ClassificationResultItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ClassificationResultItem> items) {
        this.items = items;
    }
}
