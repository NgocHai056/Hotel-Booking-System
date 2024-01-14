package com.example.booking.common;


import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientSingleton {

	private static HttpClientBuilder httpclient;
    
    private HttpClientSingleton(){}
    
    public static synchronized HttpClientBuilder getInstance(){
        if(httpclient == null){
        	try {
        		//httpclient = HttpClients.createDefault();
        		httpclient = HttpClientBuilder.create();
    		} catch(Exception ex) {
    			System.out.println(ex.getMessage());
    			httpclient = null;
    		}
        }
        return httpclient;
    }

	
}