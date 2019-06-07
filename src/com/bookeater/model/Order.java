package com.bookeater.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    private String Books;
    private String FirstName;
    private String LastName;
    private String Phone;
    private String Address;
    private String ShipMethod;
    private String CcardName;
    private String CcardNumber;
    private String CcardDate;
    private int CcardCode;
    private String CcardZip;

    public String getBooks() { return Books; }
    public void setBooks(String books) { Books = books; }

    public String getFirstName() { return FirstName; }
    public void setFirstName(String firstName) { FirstName = firstName; }

    public String getLastName() { return LastName; }
    public void setLastName(String lastName) { LastName = lastName; }

    public String getPhone() { return Phone; }
    public void setPhone(String phone) { Phone = phone; }

    public String getAddress() { return Address; }
    public void setAddress(String address) { Address = address; }

    public String getShipMethod() { return ShipMethod; }
    public void setShipMethod(String shipMethod) { ShipMethod = shipMethod; }

    public String getCcardName() { return CcardName; }
    public void setCcardName(String ccardName) { CcardName = ccardName; }

    public String getCcardNumber() { return CcardNumber; }
    public void setCcardNumber(String ccardNumber) { CcardNumber = ccardNumber; }

    public String getCcardDate() { return CcardDate; }
    public void setCcardDate(String ccardDate) { CcardDate = ccardDate; }

    public int getCcardCode() { return CcardCode; }
    public void setCcardCode(int ccardCode) { CcardCode = ccardCode; }

    public String getCcardZip() { return CcardZip; }
    public void setCcardZip(String ccardZip) { CcardZip = ccardZip; }
}
