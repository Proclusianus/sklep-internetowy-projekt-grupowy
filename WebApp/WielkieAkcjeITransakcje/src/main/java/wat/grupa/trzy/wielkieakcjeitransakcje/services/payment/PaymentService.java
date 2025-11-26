package wat.grupa.trzy.wielkieakcjeitransakcje.services.payment;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.OrderItem;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.ShopOrder;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.TransactionData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.BankAccountData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.CardData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.GiftCardData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.TRANSACTION_TYPE;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.payment.BankAccountValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.payment.CardValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.ShopOrderRepository; // WAŻNY IMPORT
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.payment.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Service
public class PaymentService {

    private final WalletRepository walletRepository;
    private final TransactionDataRepository transactionDataRepository;
    private final GiftCardDataRepository giftCardDataRepository;
    private final CardRepository cardRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ShopOrderRepository shopOrderRepository; // WAŻNE: Dodajemy to repozytorium

    public PaymentService(WalletRepository walletRepository,
                          TransactionDataRepository transactionDataRepository,
                          GiftCardDataRepository giftCardDataRepository,
                          CardRepository cardRepository,
                          BankAccountRepository bankAccountRepository,
                          ShopOrderRepository shopOrderRepository) { // Wstrzykujemy w konstruktorze
        this.walletRepository = walletRepository;
        this.transactionDataRepository = transactionDataRepository;
        this.giftCardDataRepository = giftCardDataRepository;
        this.cardRepository = cardRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.shopOrderRepository = shopOrderRepository;
    }

    public Wallet getUserWallet(String username) {
        return walletRepository.findByUserData_Email(username)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono portfela dla użytkownika: " + username));
    }

    @Transactional
    public void redeemGiftCard(String userEmail, String code) {
        Wallet wallet = getUserWallet(userEmail);
        GiftCardData giftCard = giftCardDataRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Podany kod nie istnieje!"));

        boolean alreadyUsed = transactionDataRepository.existsByWallet_IdAndUsedGiftCard_Id(
                wallet.getId(),
                giftCard.getId()
        );
        if (alreadyUsed) { throw new IllegalStateException("Już wykorzystałeś ten kod promocyjny!"); }

        TransactionData transaction = new TransactionData();
        transaction.setAmount(giftCard.getAmountToAdd());
        transaction.setDesc("Doładowanie kodem: " + code);
        transaction.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        transaction.setBalanceAfter(wallet.getBalance() + giftCard.getAmountToAdd());
        transaction.setTransactionType(TRANSACTION_TYPE.TOP_UP_GIFT_CARD);
        transaction.setUsedGiftCard(giftCard);
        transaction.setWallet(wallet);
        if (giftCard.getTransactions() == null) { giftCard.setTransactions(new ArrayList<TransactionData>());}
        giftCard.getTransactions().add(transaction);

        wallet.addTransaction(transaction);
        walletRepository.save(wallet);
    }

    @Transactional
    public void addCreditCard(String userEmail, String rawCardNumber) {
        CardValidator.validate(rawCardNumber);
        Wallet wallet = getUserWallet(userEmail);
        String cleanNumber = rawCardNumber.replaceAll("[\\s-]", "");
        String maskedNumber = "************" + cleanNumber.substring(cleanNumber.length() - 4);

        CardData newCard = new CardData();
        newCard.setMaskedNumber(maskedNumber);
        newCard.setWallet(wallet);
        wallet.getOwnedCards().add(newCard);

        walletRepository.save(wallet);
    }

    @Transactional
    public void addBankAccount(String userEmail, String rawIban) {
        BankAccountValidator.validate(rawIban);
        Wallet wallet = getUserWallet(userEmail);
        String cleanIban = rawIban.replaceAll("[\\s-]", "").toUpperCase();
        boolean exists = wallet.getOwnedBankAccounts().stream()
                .anyMatch(acc -> acc.getIban().equals(cleanIban));
        if (exists) {
            throw new IllegalArgumentException("To konto bankowe jest już dodane.");
        }
        BankAccountData newAccount = new BankAccountData();
        newAccount.setIban(cleanIban);
        newAccount.setWallet(wallet);
        wallet.getOwnedBankAccounts().add(newAccount);
        walletRepository.save(wallet);
    }

    @Transactional
    public void topUpWallet(String userEmail, Long amount, String methodIdRaw) {
        if (amount == null || amount <= 0) { throw new IllegalArgumentException("Kwota doładowania musi być dodatnia!"); }
        if (methodIdRaw == null || methodIdRaw.isEmpty()) { throw new IllegalArgumentException("Wybierz metodę płatności!"); }

        Wallet wallet = getUserWallet(userEmail);
        String[] parts = methodIdRaw.split("_");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Nieprawidłowy identyfikator metody płatności.");
        }
        String type = parts[0];
        Long id = Long.parseLong(parts[1]);

        TransactionData transaction = new TransactionData();
        transaction.setAmount(amount);
        transaction.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        transaction.setBalanceAfter(wallet.getBalance() + amount);
        transaction.setWallet(wallet);
        if ("card".equals(type)) {
            CardData card = cardRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Wybrana karta nie istnieje."));

            if (!card.getWallet().getId().equals(wallet.getId())) {
                throw new SecurityException("Próba użycia cudzej karty!");
            }

            transaction.setTransactionType(TRANSACTION_TYPE.TOP_UP_CARD);
            transaction.setDesc("Doładowanie kartą: " + card.getMaskedNumber());
            transaction.setUsedCard(card);

        } else if ("bank".equals(type)) {
            BankAccountData bank = bankAccountRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Wybrane konto nie istnieje."));

            if (!bank.getWallet().getId().equals(wallet.getId())) {
                throw new SecurityException("Próba użycia cudzego konta!");
            }

            transaction.setTransactionType(TRANSACTION_TYPE.TOP_UP_BANK_ACC);
            transaction.setDesc("Doładowanie z konta: " + bank.getIban());
            transaction.setUsedBankAccount(bank);

        } else {
            throw new IllegalArgumentException("Nieznany typ metody płatności: " + type);
        }

        wallet.addTransaction(transaction);
        walletRepository.save(wallet);
    }

    @Transactional
    public void deleteCard(String userEmail, Long cardId) {
        CardData card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Karta nie istnieje."));

        String ownerEmail = card.getWallet().getUserData().getEmail();
        if (!ownerEmail.equals(userEmail)) {
            throw new SecurityException("Nie masz uprawnień do usunięcia tej karty!");
        }

        card.getWallet().getOwnedCards().remove(card);
        cardRepository.delete(card);
    }

    @Transactional
    public void deleteBankAccount(String userEmail, Long accountId) {
        BankAccountData account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Konto nie istnieje."));

        String ownerEmail = account.getWallet().getUserData().getEmail();
        if (!ownerEmail.equals(userEmail)) {
            throw new SecurityException("Nie masz uprawnień do usunięcia tego konta!");
        }

        account.getWallet().getOwnedBankAccounts().remove(account);
        bankAccountRepository.delete(account);
    }

    public Page<TransactionData> getUserTransactions(String userEmail, int pageNumber) {
        Wallet wallet = getUserWallet(userEmail);
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());

        return transactionDataRepository.findAllByWallet_Id(wallet.getId(), pageable);
    }

    @Transactional
    public void processPayment(Long orderId, String methodIdRaw) { // Przyjmuje ID zamiast obiektu
        // 1. Załaduj zamówienie wewnątrz transakcji (będzie "Managed")
        ShopOrder order = shopOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Zamówienie nie istnieje!"));

        Long cost = order.getTotalAmount().longValue();

        // Dla kupującego
        Wallet walletBuyer = order.getBuyer().getWallet();
        if (walletBuyer == null) { throw new IllegalStateException("Kupujący nie posiada portfela!"); }
        if (walletBuyer.getBalance() < cost) { throw new IllegalStateException("Niewystarczające środki w portfelu. Brakuje: " + (cost - walletBuyer.getBalance())); }

        // Walidacja metody
        if (methodIdRaw == null || methodIdRaw.isEmpty()) {
            throw new IllegalArgumentException("Musisz wybrać metodę płatności (Kartę lub Konto) aby autoryzować transakcję.");
        }

        String[] parts = methodIdRaw.split("_");
        if (parts.length != 2) { throw new IllegalArgumentException("Nieprawidłowy identyfikator metody."); }

        String type = parts[0];
        Long id = Long.parseLong(parts[1]);

        TransactionData transactionBuyer = new TransactionData();

        // Wybór metody autoryzacji
        if ("card".equals(type)) {
            CardData card = cardRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Karta nie istnieje"));

            if (!card.getWallet().getId().equals(walletBuyer.getId())) {
                throw new SecurityException("Próba użycia cudzej karty!");
            }
            transactionBuyer.setUsedCard(card);
            transactionBuyer.setDesc("Zakup opłacony (autoryzacja kartą: " + card.getMaskedNumber() + ") zamówienie nr: " + order.getId());

        } else if ("bank".equals(type)) {
            BankAccountData bank = bankAccountRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Konto nie istnieje"));

            if (!bank.getWallet().getId().equals(walletBuyer.getId())) {
                throw new SecurityException("Próba użycia cudzego konta!");
            }
            transactionBuyer.setUsedBankAccount(bank);
            transactionBuyer.setDesc("Zakup opłacony (autoryzacja kontem: " + bank.getIban() + ") zamówienie nr: " + order.getId());

        } else {
            throw new IllegalArgumentException("Nieznany typ metody płatności.");
        }

        transactionBuyer.setAmount(cost);
        transactionBuyer.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        transactionBuyer.setTransactionType(TRANSACTION_TYPE.PURCHASE);
        transactionBuyer.setBalanceAfter(walletBuyer.getBalance() - cost);

        // Powiązanie z zamówieniem (teraz zadziała, bo order jest Managed)
        transactionBuyer.setShopOrder(order);
        transactionBuyer.setWallet(walletBuyer);

        walletBuyer.addTransaction(transactionBuyer);
        walletRepository.save(walletBuyer);

        // Dla sprzedającego
        for (OrderItem item : order.getOrderItems()) {
            Wallet walletSeller = item.getProduct().getSeller().getWallet();
            if (walletSeller == null) continue;

            Long itemCost = item.getPriceAtPurchase().longValue();

            TransactionData transactionSeller = new TransactionData();
            transactionSeller.setAmount(itemCost);
            transactionSeller.setDesc("Wpływ ze sprzedaży: " + item.getProduct().getName());
            transactionSeller.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
            transactionSeller.setTransactionType(TRANSACTION_TYPE.SALE);
            transactionSeller.setBalanceAfter(walletSeller.getBalance() + itemCost);
            transactionSeller.setShopOrder(order);
            transactionSeller.setWallet(walletSeller);
            walletSeller.addTransaction(transactionSeller);

            walletRepository.save(walletSeller);
        }

        // Zmiana statusu
        order.setStatus("PAID");
        shopOrderRepository.save(order);
    }
}