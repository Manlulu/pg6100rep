package entity;


import org.eclipse.persistence.oxm.annotations.XmlWriteOnly;

import javax.xml.bind.annotation.*;
import java.util.HashMap;


public class RateIndex {


    private String base;

    private String date;

    private HashMap<String, String > rates;

    private String rate;

    public RateIndex(String base, String date, HashMap<String, String> rates, String rate) {
        this.base = base;
        this.date = date;
        this.rates = rates;
        this.rate = rate;
    }

    public RateIndex() {
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, String> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, String> rates) {
        this.rates = rates;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
