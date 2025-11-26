package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty; // WAŻNY IMPORT

public class DTO_LoginResponse {

    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("accountType")
    private String accountType;

    // Konstruktor
    public DTO_LoginResponse(String sessionId, Long userId, String username, String accountType) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.username = username;
        this.accountType = accountType;
    }

    // Gettery (wymagane przez Jacksona)
    public String getSessionId() { return sessionId; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getAccountType() { return accountType; }
}