package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "business_data")
public class BusinessData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firm_name")
    private String firmName;

    @Column(name = "nip")
    private String nip;

    @Column(name = "krs_id")
    private String krsId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hq_address_id")
    private Address hqAddress;

    @OneToMany(mappedBy = "businessDataDelivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> deliveryAddresses;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_person_id")
    private ContactPerson contactPersonFirm;

    public BusinessData() {}

    // Getters
    public String getFirmName() { return firmName; }
    public String getNip() { return nip; }
    public String getKrsId() { return krsId; }
    public Address getHqAddress() { return hqAddress; }
    public List<Address> getDeliveryAddresses() { return deliveryAddresses; }
    public ContactPerson getContactPerson() { return contactPersonFirm; }

    // Setters
    public void setFirmName(String firmName) { this.firmName = firmName; }
    public void setNip(String nip) { this.nip = nip; }
    public void setKrsId(String krsId) { this.krsId = krsId; }
    public void setHqAddress(Address hqAddress) { this.hqAddress = hqAddress; }
    public void setDeliveryAddresses(List<Address> deliveryAddresses) { this.deliveryAddresses = deliveryAddresses; }
    public void setContactPersonFirm(ContactPerson contactPersonFirm) { this.contactPersonFirm = contactPersonFirm; }
}
