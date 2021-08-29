package com.Engineering.jobly;


public class JobOffer implements java.io.Serializable {
    public String Title ="";
    public String Description ="";
    public String Location="";
    public String Budget="";
    public String WorkTime="";
    public String PublisherID="";
    public String PublishDate="";
    public String OfferID="";

    public JobOffer(String Name, String ID){
        Title = Name;
        PublisherID =ID;

    }

    public  JobOffer(){}




}
