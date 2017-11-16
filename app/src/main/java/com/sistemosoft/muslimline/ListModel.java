package com.sistemosoft.muslimline;

/**
 * Created by appaz on 11/17/16.
 */
public class ListModel {

    private String ProductId = "";
    private String ProductName = "";
    private String ProductContent = "";
    private String ProductPrice = "";
    private String TotalPrice = "";
    private String Count = "";
    private String ProductSize = "";
    private String ProductsSize = "";
    private String ProductColor = "";
    private String ProductsColor = "";
    private String image;


    /***********
     * Set Methods
     ******************/
    public void setImage(String image) {
        this.image = image;
    }

    public void setProductSize(String productSize) {
        ProductSize = productSize;
    }

    public void setProductsSize(String productsSize) {
        ProductsSize = productsSize;
    }

    public void setProductColor(String productColor) {
        ProductColor = productColor;
    }

    public void setProductsColor(String productsColor) {
        ProductsColor = productsColor;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public void setProductContent(String ProductContent) {
        this.ProductContent = ProductContent;
    }

    public void setProductPrice(String ProductPrice) {
        this.ProductPrice = ProductPrice;
    }

    public void setTotalPrice(String TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public void setCount(String Count) {
        this.Count = Count;
    }


    /***********
     * Get Methods
     ****************/

    public String getImage() {
        return image;
    }

    public String getProductSize() {
        return ProductSize;
    }

    public String getProductsSize() {
        return ProductsSize;
    }

    public String getProductColor() {
        return ProductColor;
    }

    public String getProductsColor() {
        return ProductsColor;
    }

    public String getProductName() {
        return this.ProductName;
    }

    public String getProductId() {
        return this.ProductId;
    }

    public String getProductContent() {
        return this.ProductContent;
    }

    public String getProductPrice() {
        return this.ProductPrice;
    }

    public String getTotalPrice() {
        return this.TotalPrice;
    }

    public String getCount() {
        return this.Count;
    }

}