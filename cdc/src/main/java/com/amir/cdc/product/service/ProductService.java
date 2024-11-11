package com.amir.cdc.product.service;


import com.amir.cdc.product.entity.Product;

public interface ProductService {

    void handleEvent(String operation, String documentId , String collection , Product product) ;

}
