package wat.grupa.trzy.wielkieakcjeitransakcje.services.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.TransactionData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.BankAccountData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.CardData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.GiftCardData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.TRANSACTION_TYPE;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.payment.*;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private WalletRepository walletRepository;
    @Mock private TransactionDataRepository transactionDataRepository;
    @Mock private GiftCardDataRepository giftCardDataRepository;
    @Mock private CardRepository cardRepository;
    @Mock private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private PaymentService paymentService;

    // --- TESTY GIFT CARD ---

    @Test
    @DisplayName("redeemGiftCard: Powinien doładować portfel, gdy kod jest poprawny i nieużyty")
    void redeemGiftCard_ShouldSuccess() {
        // GIVEN
        String email = "user@test.pl";
        String code = "FREE100";
        Wallet wallet = createWalletWithUser(1L, email, 0L);
        GiftCardData giftCard = new GiftCardData();
        giftCard.setId(10L);
        giftCard.setCode(code);
        giftCard.setAmountToAdd(100L);
        giftCard.setTransactions(new ArrayList<>());

        when(walletRepository.findByUserData_Email(email)).thenReturn(Optional.of(wallet));
        when(giftCardDataRepository.findByCode(code)).thenReturn(Optional.of(giftCard));
        when(transactionDataRepository.existsByWallet_IdAndUsedGiftCard_Id(1L, 10L)).thenReturn(false);

        // WHEN
        paymentService.redeemGiftCard(email, code);

        // THEN
        assertEquals(100L, wallet.getBalance());
        verify(walletRepository).save(wallet); // Sprawdzamy czy zapisano portfel
        assertEquals(1, wallet.getTransactionDataList().size()); // Czy dodano transakcję do listy
        assertEquals(TRANSACTION_TYPE.TOP_UP_GIFT_CARD, wallet.getTransactionDataList().get(0).getTransactionType());
    }

    @Test
    @DisplayName("redeemGiftCard: Powinien rzucić wyjątek, gdy kod był już użyty przez ten portfel")
    void redeemGiftCard_ShouldThrow_WhenAlreadyUsed() {
        // GIVEN
        String email = "user@test.pl";
        String code = "USED100";
        Wallet wallet = createWalletWithUser(1L, email, 50L);
        GiftCardData giftCard = new GiftCardData();
        giftCard.setId(10L);
        giftCard.setCode(code);

        when(walletRepository.findByUserData_Email(email)).thenReturn(Optional.of(wallet));
        when(giftCardDataRepository.findByCode(code)).thenReturn(Optional.of(giftCard));
        when(transactionDataRepository.existsByWallet_IdAndUsedGiftCard_Id(1L, 10L)).thenReturn(true);

        // WHEN & THEN
        assertThrows(IllegalStateException.class, () -> paymentService.redeemGiftCard(email, code));
        verify(walletRepository, never()).save(any()); // Nie powinno zapisać zmian
    }

    // --- TESTY TOP UP (DOŁADOWANIE) ---

    @Test
    @DisplayName("topUpWallet: Powinien doładować kartą, gdy dane są poprawne")
    void topUpWallet_Card_ShouldSuccess() {
        // GIVEN
        String email = "user@test.pl";
        Long amount = 500L;
        String methodId = "card_123"; // ID karty 123
        Wallet wallet = createWalletWithUser(1L, email, 100L);

        CardData card = new CardData();
        card.setId(123L);
        card.setMaskedNumber("************1234");
        card.setWallet(wallet); // Karta należy do tego portfela

        when(walletRepository.findByUserData_Email(email)).thenReturn(Optional.of(wallet));
        when(cardRepository.findById(123L)).thenReturn(Optional.of(card));

        // WHEN
        paymentService.topUpWallet(email, amount, methodId);

        // THEN
        assertEquals(600L, wallet.getBalance()); // 100 + 500
        verify(walletRepository).save(wallet);
    }

    @Test
    @DisplayName("topUpWallet: Powinien rzucić SecurityException przy próbie użycia cudzej karty")
    void topUpWallet_ShouldThrowSecurityException_WhenCardNotOwned() {
        // GIVEN
        String emailHacker = "hacker@test.pl";
        Wallet hackerWallet = createWalletWithUser(1L, emailHacker, 0L);

        String emailVictim = "victim@test.pl";
        Wallet victimWallet = createWalletWithUser(2L, emailVictim, 1000L);

        CardData victimCard = new CardData();
        victimCard.setId(999L);
        victimCard.setWallet(victimWallet); // Karta należy do ofiary

        when(walletRepository.findByUserData_Email(emailHacker)).thenReturn(Optional.of(hackerWallet));
        when(cardRepository.findById(999L)).thenReturn(Optional.of(victimCard));

        // WHEN & THEN
        assertThrows(SecurityException.class, () ->
                paymentService.topUpWallet(emailHacker, 100L, "card_999")
        );
    }

    @Test
    @DisplayName("topUpWallet: Powinien doładować kontem bankowym")
    void topUpWallet_Bank_ShouldSuccess() {
        // GIVEN
        String email = "user@test.pl";
        Wallet wallet = createWalletWithUser(1L, email, 0L);
        BankAccountData bank = new BankAccountData();
        bank.setId(55L);
        bank.setIban("PL123456");
        bank.setWallet(wallet);

        when(walletRepository.findByUserData_Email(email)).thenReturn(Optional.of(wallet));
        when(bankAccountRepository.findById(55L)).thenReturn(Optional.of(bank));

        // WHEN
        paymentService.topUpWallet(email, 200L, "bank_55");

        // THEN
        assertEquals(200L, wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    // --- TESTY ZARZĄDZANIA METODAMI PŁATNOŚCI ---

    @Test
    @DisplayName("addBankAccount: Nie powinien pozwolić na dodanie duplikatu IBAN w tym samym portfelu")
    void addBankAccount_ShouldThrow_WhenDuplicate() {
        // GIVEN
        String email = "user@test.pl";
        String iban = "PL 11 1111 1111 1111 1111 1111 1111"; // Spacje zostaną usunięte w serwisie
        String cleanIban = "PL11111111111111111111111111";

        Wallet wallet = createWalletWithUser(1L, email, 0L);
        BankAccountData existingAccount = new BankAccountData();
        existingAccount.setIban(cleanIban);
        wallet.getOwnedBankAccounts().add(existingAccount);

        when(walletRepository.findByUserData_Email(email)).thenReturn(Optional.of(wallet));

        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () ->
                paymentService.addBankAccount(email, iban)
        );
    }

    // Helper method
    private Wallet createWalletWithUser(Long id, String email, Long balance) {
        Wallet w = new Wallet();
        w.setId(id);
        try {
            java.lang.reflect.Field balanceField = Wallet.class.getDeclaredField("balance");
            balanceField.setAccessible(true);
            balanceField.set(w, balance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserData u = new UserData();
        u.setEmail(email);
        w.setUserData(u);
        w.setTransactionDataList(new ArrayList<>());
        w.setOwnedCards(new ArrayList<>());
        w.setOwnedBankAccounts(new ArrayList<>());
        return w;
    }
}