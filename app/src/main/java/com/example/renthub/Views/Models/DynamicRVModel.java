package com.example.renthub.Views.Models;

public class DynamicRVModel {
    String name;
    String price;
    String DocumentUID;

    public DynamicRVModel(String name, String price, String DocumentUID) {
        this.name = name;
        this.price = price;
        this.DocumentUID = DocumentUID;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDocumentUID() {
        return DocumentUID;
    }

    public void setDocumentUID(String documentUID) {
        DocumentUID = documentUID;
    }

    public String getName() {
        return name;
    }
}
