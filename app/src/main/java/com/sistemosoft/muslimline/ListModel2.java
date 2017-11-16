package com.sistemosoft.muslimline;

/**
 * Created by appaz on 11/17/16.
 */
public class ListModel2 {

    private  String ProductId="";
    private  String ProductName="";
    private  String ProductPrice="";
    private  String ProductCount="";
    private  String ProductSize="";
    private  String ProductsColor = "";
    private  String Comments = "";


    /*********** Set Methods ******************/
    public void setComments(String comments) {
        Comments = comments;
    }

    public void setProductSize(String productSize) {
        ProductSize = productSize;
    }

    public void setProductsColor(String productsColor) {
        ProductsColor = productsColor;
    }

    public void setProductName(String ProductName)
    {
        this.ProductName = ProductName;
    }

    public void setProductId(String ProductId)
    {
        this.ProductId = ProductId;
    }

    public void setProductCount(String productCount)
    {
        this.ProductCount = productCount;
    }

    public void setProductPrice(String ProductPrice)
    {
        this.ProductPrice = ProductPrice;
    }

    /*********** Get Methods ****************/
    public String getComments() {
        return Comments;
    }

    public String getProductSize() {
        return ProductSize;
    }

    public String getProductsColor() {
        return ProductsColor;
    }

    public String getProductName()
    {
        return this.ProductName;
    }

    public String getProductId()
    {
        return this.ProductId;
    }

    public String getProductCount()
    {
        return this.ProductCount;
    }

    public String getProductPrice()
    {
        return this.ProductPrice;
    }

}
