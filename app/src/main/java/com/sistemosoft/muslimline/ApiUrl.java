package com.sistemosoft.muslimline;

/**
 * Created by appaz on 12/10/16.
 */

public class ApiUrl {
    //public final static String BASE_URL = "http://192.168.1.34:3000/";
    public final static String Base_Url = "http://www.muslimlineru.ru/";
    public final static String REGISTRATION = Base_Url + "api/mobile/registrations";
    public final static String SIGN_IN = Base_Url + "api/mobile/authentications/sign_in";
    public final static String CHECKOUT = Base_Url + "api/mobile/checkouts/cart";
    public final static String CHECKOUTV1 = Base_Url + "api/mobile/v11/checkouts/cart";
    public final static String PRODUCT_GALLERIES = Base_Url + "api/mobile/products/gallery?id=";
    public final static String PRODUCT_DATA = Base_Url + "api/mobile/products/index?id=4&page=";
    public final static String VIEWPAGER_DATA = Base_Url + "api/mobile/v10/products/category";

}
