package rs.carapp.CarAppDemo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillRequirements {


    private CustomerInfo customerInfo;
    private List<Items> items;
    private String terminalID;


    public class CustomerInfo {
        @JsonProperty("isLock")
        private boolean isLock;
        private String name;
        private String taxID;
        private String street;
        @JsonIgnore
        public boolean isLock() {
            return isLock;
        }

        public void setLock(boolean lock) {
            isLock = lock;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTaxID() {
            return taxID;
        }

        public void setTaxID(String taxID) {
            this.taxID = taxID;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }

    public class Items {
        public String name;
        private int priceType;
        private long price;
        private int quantity;
        private int taxPercent;



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPriceType() {
            return priceType;
        }

        public void setPriceType(int priceType) {
            this.priceType = priceType;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getTaxPercent() {
            return taxPercent;
        }

        public void setTaxPercent(int taxPercent) {
            this.taxPercent = taxPercent;
        }

        public int getTaxType() {
            return taxType;
        }

        public void setTaxType(int taxType) {
            this.taxType = taxType;
        }

        private int taxType;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItemsList(List<Items> items) {
        this.items = items;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

}
