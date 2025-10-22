package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.FirmPayment;
import java.util.List;

public class BusinessData {
    private String firmName;
    private Address hqAddress;
    private String nip;
    private String krs_id;
    private ContactPerson contactPerson;
    private List<Address> deliveryAddresses;
    private FirmPayment paymentInformation;

    public BusinessData(String firmName, Address hqAddress, String nip, String krs_id, ContactPerson contactPerson, List<Address> deliveryAddresses, FirmPayment paymentInformation) {
        this.firmName = firmName;
        this.hqAddress = hqAddress;
        this.nip = nip;
        this.krs_id = krs_id;
        this.contactPerson = contactPerson;
        this.deliveryAddresses = deliveryAddresses;
        this.paymentInformation = paymentInformation;
    }

    // Getters
    public String getFirmName() { return firmName; }
    public Address getHqAddress() { return hqAddress; }
    public String getNip() { return nip; }
    public String getKrs_id() { return krs_id; }
    public ContactPerson getContactPerson() { return contactPerson; }
    public List<Address> getDeliveryAddresses() { return deliveryAddresses; }
    public FirmPayment getPaymentInformation() { return paymentInformation; }

    // Setters
    public void setFirmName(String firmName) { this.firmName = firmName; }
    public void setHqAddress(Address hqAddress) { this.hqAddress = hqAddress; }
    public void setNip(String nip) { this.nip = nip; }
    public void setKrs_id(String krs_id) { this.krs_id = krs_id; }
    public void setContactPerson(ContactPerson contactPerson) { this.contactPerson = contactPerson; }
    public void setDeliveryAddresses(List<Address> deliveryAddresses) { this.deliveryAddresses = deliveryAddresses; }
    public void setPaymentInformation(FirmPayment paymentInformation) { this.paymentInformation = paymentInformation; }
}
