package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.BusinessData;

public class DTO_BusinessData {
    // Dane Firmy
    private String companyName;
    private String nip;
    private String krs;

    // Dane Osoby Kontaktowej
    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;
    private String contactPhone;

    public DTO_BusinessData() {}
    public DTO_BusinessData(BusinessData data) {
        this.companyName = data.getFirmName();
        this.nip = data.getNip();
        this.krs = data.getKrsId();

        if (data.getContactPerson() != null) {
            this.contactFirstName = data.getContactPerson().getName();
            this.contactLastName = data.getContactPerson().getSurname();
            this.contactEmail = data.getContactPerson().getEmail();
            this.contactPhone = data.getContactPerson().getPhoneNumber();
        }
    }

    // GETTERY I SETTERY
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }

    public String getKrs() { return krs; }
    public void setKrs(String krs) { this.krs = krs; }

    public String getContactFirstName() { return contactFirstName; }
    public void setContactFirstName(String contactFirstName) { this.contactFirstName = contactFirstName; }

    public String getContactLastName() { return contactLastName; }
    public void setContactLastName(String contactLastName) { this.contactLastName = contactLastName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
}