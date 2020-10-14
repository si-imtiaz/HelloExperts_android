package com.HelloExperts.HelloExpert;

public class Categories_data {
    private String category_name;
    private int category_count;
    private int category_id;

    public Categories_data(String category_name, int category_count, int category_id) {
        this.category_name = category_name;
        this.category_count = category_count;
        this.category_id = category_id;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name() {
        return this.category_name;
    }

    public void setCategory_count(int category_count) {
        this.category_count = category_count;
    }

    public int getCategory_count() {
        return this.category_count;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getCategory_id() {
        return category_id;
    }
}
