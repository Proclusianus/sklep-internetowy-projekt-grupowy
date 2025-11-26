### Przypadek Użycia: Zarządzaj płatnościami

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-PAY-01 |
| **Nazwa** | Zarządzaj płatnościami |
| **Autor** | Dawid Niedziński |
| **Priorytet** | Średni |
| **Krytyczność** | Niska |
| **Źródło** | Wymagania biznesowe |
| **Osoba odpowiedzialna** | Dawid Niedziński |
| **Opis** | Umożliwia zalogowanemu użytkownikowi symulację dodawania i usuwania zapisanych metod dodawania symulowanej waluty strony „Bobux” (przez karty kredytowe, numer konta bankowego i giftcardy). |
| **Wyzwalacze** | Użytkownik wybiera opcję "Płatności" lub "Moje metody płatności" w ustawieniach swojego konta. |
| **Aktorzy** | Zalogowany Użytkownik |
| **Warunki wstępne** | Użytkownik musi być zalogowany w systemie. |
| **Warunki końcowe** | Zmiany w zapisanych metodach płatności zostają trwale zapisane w systemie. |
| **Rezultat** | Użytkownik ma zaktualizowaną listę metod płatności, co ułatwia przyszłe zakupy. |
| **Główny scenariusz** | 1. Użytkownik przechodzi do sekcji zarządzania płatnościami. <br> 2. System wyświetla listę zapisanych metod płatności oraz historię transakcji. <br> 3. Użytkownik wybiera opcję "Dodaj nową metodę doładowań". <br> 4. System wyświetla bezpieczny formularz do wprowadzania danych metody doładowań. <br> 5. Użytkownik wprowadza dane i zatwierdza. <br> 6. System zapisuje nową metodę płatności. <br> 7. Użytkownik za pośrednictwem dodanej metody doładowuje walutę „Bobux” do swojego konta. |
| **Scenariusze alternatywne** | **2.A Użytkownik chce usunąć metodę płatności:** <br> 1. Użytkownik wybiera opcję "Usuń" przy wybranej metodzie płatności. <br> 2. System prosi o potwierdzenie operacji. <br> 3. Po potwierdzeniu, system usuwa metodę płatności. <br> 4. Scenariusz kończy się sukcesem. |
| **Scenariusze wyjątków** | **Zdarzenie: Walidacja nowej metody płatności nie powiodła się.** <br> 1. System otrzymuje informację o błędzie od wewnętrznych walidatorów. <br> 2. System wyświetla użytkownikowi komunikat o niepoprawnych danych karty lub odrzuceniu transakcji weryfikacyjnej. <br> 3. Scenariusz wraca do punktu 4 scenariusza głównego. |
| **Wymagania jakościowe** | WJ.25 (Szybkość odpowiedzi systemu płatności < 3 sekundy) |

---

### Przypadek Użycia: Wyświetl Rachunki Za Dokonane Transakcje

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-PAY-02 |
| **Nazwa** | Wyświetl Rachunki Za Dokonane Transakcje |
| **Autorzy** | Dawid Niedziński |
| **Priorytet** | Niski |
| **Krytyczność** | Niska |
| **Źródło** | Wymagania biznesowe |
| **Osoba odpowiedzialna** | Dawid Niedziński |
| **Opis** | Umożliwia zalogowanemu użytkownikowi przeglądanie historii swoich transakcji w serwisie oraz generowanie i pobieranie rachunków lub faktur za dokonane zakupy. |
| **Wyzwalacze** | Użytkownik wybiera opcję "Historia transakcji", "Moje zakupy" lub "Rachunki" w panelu swojego konta. |
| **Aktorzy** | Zalogowany Użytkownik |
| **Warunki wstępne** | 1. Użytkownik jest zalogowany w systemie. <br> 2. Użytkownik dokonał co najmniej jednej transakcji w przeszłości. |
| **Warunki końcowe** | Użytkownik pobrał żądany dokument (rachunek/fakturę) lub zapoznał się z historią transakcji. |
| **Rezultat** | Użytkownik posiada dokumentację swoich transakcji do celów księgowych lub informacyjnych. |
| **Główny scenariusz** | 1. Użytkownik przechodzi do sekcji historii transakcji. <br> 2. System wyświetla listę wszystkich dokonanych transakcji w porządku chronologicznym (np. data, nazwa przedmiotu, kwota, sprzedawca). <br> 3. Użytkownik znajduje na liście interesującą go transakcję. <br> 4. Użytkownik klika przycisk "Pobierz rachunek" lub "Generuj fakturę" powiązany z daną transakcją. <br> 5. System generuje dokument zawierający szczegóły transakcji. <br> 6. System inicjuje pobieranie wygenerowanego pliku w przeglądarce użytkownika. |
| **Scenariusze alternatywne** | **2.A Użytkownik chce filtrować lub wyszukać transakcje:** <br> 1. Użytkownik korzysta z dostępnych filtrów (np. zakres dat, kategoria) lub pola wyszukiwania. <br> 2. System odświeża listę, wyświetlając tylko transakcje spełniające podane kryteria. <br> 3. Scenariusz jest kontynuowany od kroku 3. w głównym scenariuszu. |
| **Scenariusze wyjątków** | **Zdarzenie: Użytkownik nie posiada żadnych transakcji.** <br> 1. W kroku 2. system stwierdza, że historia transakcji użytkownika jest pusta. <br> 2. System wyświetla komunikat informacyjny, np. "Nie dokonałeś jeszcze żadnych transakcji". <br> 3. Przypadek użycia kończy się. <br><br> **Zdarzenie: Generowanie dokumentu nie powiodło się.** <br> 1. W kroku 5. system napotyka wewnętrzny błąd i nie jest w stanie utworzyć pliku. <br> 2. System wyświetla komunikat o błędzie, np. "Wystąpił błąd podczas generowania rachunku. Prosimy spróbować ponownie później". <br> 3. Użytkownik pozostaje na stronie z listą transakcji. |
| **Wymagania jakościowe** | WJ.28 (Czas generowania dokumentu < 2 sekundy) <br> WJ.30 (Dokładność i integralność danych na rachunku) |

---

### Przypadek Użycia: Utwórz Konto

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-ACC-01 |
| **Nazwa** | Utwórz Konto |
| **Autorzy** | Dawid Niedziński |
| **Priorytet** | Krytyczny |
| **Krytyczność** | Średnia |
| **Źródło** | Wymagania biznesowe |
| **Osoba odpowiedzialna** | Dawid Niedziński |
| **Opis** | Umożliwia nowemu użytkownikowi (Gościowi) założenie konta w systemie, co jest niezbędne do dokonywania zakupów, sprzedaży i korzystania ze spersonalizowanych funkcji serwisu. |
| **Wyzwalacze** | Gość klika przycisk "Zarejestruj się" lub "Załóż konto" na stronie głównej lub podczas procesu zakupu. |
| **Aktorzy** | Gość |
| **Warunki wstępne** | Użytkownik nie jest zalogowany. |
| **Warunki końcowe** | Konto użytkownika zostaje utworzone w bazie danych. Użytkownik jest automatycznie zalogowany. |
| **Rezultat** | Utworzenie nowego konta użytkownika w systemie i rozpoczęcie sesji. |
| **Główny scenariusz** | 1. Gość inicjuje proces tworzenia konta. <br> 2. System wyświetla formularz rejestracyjny. <br> 3. Gość wprowadza adres e-mail, hasło oraz inne wymagane dane. <br> 4. Gość akceptuje regulamin serwisu. <br> 5. Gość zatwierdza formularz. <br> 6. System waliduje poprawność i unikalność danych. <br> 7. System tworzy nowe konto. <br> 8. System wysyła e-mail z linkiem aktywacyjnym. <br> 9. System loguje użytkownika i przekierowuje go do jego panelu. |
| **Scenariusze alternatywne** | **Punkt rozszerzenia (Extend Point): "Rejestracja Firmy"** <br> W kroku 3. Gość może zaznaczyć opcję "Chcę założyć konto firmowe", co uruchamia przypadek użycia PU-ACC-02 Zarejestruj Dane Firmy. |
| **Scenariusze wyjątków** | **Zdarzenie: Podany adres e-mail już istnieje w systemie.** <br> 1. W kroku 6. system wykrywa, że adres e-mail jest już zajęty. <br> 2. System wyświetla komunikat o błędzie i sugeruje zalogowanie się lub użycie innego adresu. <br> 3. Scenariusz wraca do punktu 3 scenariusza głównego. <br><br> **Zdarzenie: Hasło nie spełnia polityki bezpieczeństwa.** <br> 1. W kroku 6. system wykrywa, że hasło jest zbyt słabe. <br> 2. System wyświetla wymagania dotyczące hasła. <br> 3. Scenariusz wraca do punktu 3. |
| **Wymagania jakościowe** | WJ.08 (Walidacja formularza w czasie rzeczywistym) <br> WJ.18 (Bezpieczeństwo przechowywania haseł) |

---

### Przypadek Użycia: Zarejestruj Dane Firmy

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-ACC-02 |
| **Nazwa** | Zarejestruj Dane Firmy |
| **Autorzy** | Dawid Niedziński |
| **Priorytet** | Wysoki |
| **Krytyczność** | Średnia |
| **Źródło** | Wymagania biznesowe |
| **Osoba odpowiedzialna** | Dawid Niedziński |
| **Opis** | Rozszerza (<<Extend>>) przypadek użycia "Utwórz Konto". Pozwala użytkownikowi na dodanie do swojego konta danych firmowych (NIP, nazwa, adres), aby móc działać w serwisie jako sprzedawca firmowy. |
| **Wyzwalacze** | Użytkownik w trakcie realizacji PU "Utwórz Konto" zaznacza opcję założenia konta firmowego. |
| **Aktorzy** | Firma (w tym kontekście, Gość stający się Firmą) |
| **Warunki wstępne** | Przypadek użycia "Utwórz Konto" osiągnął punkt rozszerzenia "Rejestracja Firmy". |
| **Warunki końcowe** | Dane firmy zostały zapisane i powiązane z nowo tworzonym kontem. Konto zostaje oznaczone jako "firmowe". |
| **Rezultat** | Utworzenie konta z profilem firmowym, gotowego do weryfikacji i rozpoczęcia sprzedaży. |
| **Główny scenariusz** | *Scenariusz jest kontynuacją kroku 3. z PU-ACC-01* <br> 1. System wyświetla dodatkowe pola formularza przeznaczone na dane firmowe (NIP, KRS, nazwa, adres siedziby). <br> 2. Użytkownik wprowadza dane swojej firmy. <br> 3. Użytkownik zatwierdza dane. <br> 4. System dokonuje podstawowej walidacji formatu wprowadzonych danych (np. format NIP, KRS). <br> 5. Scenariusz wraca do kroku 4. w PU-ACC-01, kontynuując proces tworzenia konta. |
| **Scenariusze alternatywne** | **3.A Użytkownik rezygnuje z podawania danych firmy:** <br> 1. Użytkownik odznacza opcję "Chcę założyć konto firmowe". <br> 2. System ukrywa pola na dane firmowe. <br> 3. Scenariusz wraca do głównego przepływu PU "Utwórz Konto". |
| **Scenariusze wyjątków** | **Zdarzenie: Wprowadzony NIP jest niepoprawny.** <br> 1. W kroku 4. system wykrywa, że suma kontrolna numeru NIP jest nieprawidłowa. <br> 2. System wyświetla komunikat o błędzie przy polu NIP. <br> 3. Scenariusz wraca do punktu 2 niniejszego scenariusza. |
| **Wymagania jakościowe** | WJ.22 (Czas automatycznej weryfikacji danych firmy < 10 sekund) |

---

### Przypadek Użycia: Przeglądaj Produkty

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-01 |
| **Nazwa** | Przeglądanie Produktów |
| **Autorzy** | Bartek |
| **Priorytet** | Wysoki |
| **Krytyczność** | Niska (niepowodzenie nie powoduje utraty danych) |
| **Źródło** | Wymaganie funkcjonalne systemu e-commerce |
| **Osoba odpowiedzialna**| Właściciel Produktu |
| **Opis** | Opisuje, w jaki sposób użytkownik (zarówno zalogowany, jak i gość) może przeglądać listę dostępnych produktów, korzystając z funkcji wyszukiwania i filtrowania po kategoriach. |
| **Wyzwalacze** | Użytkownik przechodzi na stronę "Kupno i Sprzedaż". |
| **Aktorzy** | Kupujący (zalogowany), Gość (niezalogowany). |
| **Warunki wstępne** | W bazie danych istnieje co najmniej jeden produkt. |
| **Warunki końcowe** | 1. System wyświetla listę produktów zgodną z wybranymi kryteriami. <br> 2. System wyświetla listę dostępnych kategorii. |
| **Rezultat** | Użytkownik może zapoznać się z ofertą produktową sklepu. |
| **Główny scenariusz** | 1. Aktor wchodzi na stronę "Kupno i Sprzedaż". <br> 2. System pobiera i wyświetla listę wszystkich produktów oraz panel z kategoriami i wyszukiwarką. <br> 3. Aktor wybiera kategorię z listy. <br> 4. System odświeża stronę, wyświetlając tylko produkty z wybranej kategorii. <br> 5. Aktor wpisuje frazę w pole wyszukiwania i zatwierdza. <br> 6. System odświeża stronę, wyświetlając tylko produkty, których nazwa pasuje do wpisanej frazy. |
| **Scenariusze alternatywne** | **A. Brak produktów spełniających kryteria:** <br> 1. W kroku 4 lub 6, system nie znajduje pasujących produktów. <br> 2. System wyświetla komunikat o braku wyników. <br><br> **B. Brak jakichkolwiek produktów:** <br> 1. W kroku 2, system nie znajduje żadnych produktów w bazie. <br> 2. System wyświetla komunikat, że obecnie nie ma żadnych dostępnych produktów. |
| **Scenariusze wyjątków** | **Zdarzenie: Błąd połączenia z bazą danych.** <br> 1. System traci połączenie z bazą danych podczas pobierania danych. <br> 2. System wyświetla ogólną stronę błędu (np. Błąd 500). |
| **Wymagania jakościowe** | 1. Czas ładowania strony z produktami < 2 sekundy. <br> 2. Interfejs musi być responsywny. |

---

### Przypadek Użycia: Zaloguj

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-02 |
| **Nazwa** | Logowanie Użytkownika |
| **Autorzy** | Bartek |
| **Priorytet** | Krytyczny |
| **Krytyczność** | Wysoka (niepowodzenie uniemożliwia dostęp do kluczowych funkcji) |
| **Źródło** | Wymaganie bezpieczeństwa systemu |
| **Osoba odpowiedzialna**| Właściciel Produktu |
| **Opis** | Opisuje proces uwierzytelniania użytkownika w systemie za pomocą jego adresu email i hasła. |
| **Wyzwalacze** | Użytkownik próbuje uzyskać dostęp do zabezpieczonego zasobu lub klika przycisk "Zaloguj". |
| **Aktorzy** | Gość |
| **Warunki wstępne** | 1. Aktor posiada zarejestrowane konto w systemie. <br> 2. Aktor nie jest aktualnie zalogowany. |
| **Warunki końcowe** | 1. System tworzy nową, unikalną sesję dla Aktora, zapisaną w bazie danych. <br> 2. System przekierowuje Aktora do panelu użytkownika lub strony docelowej. |
| **Rezultat** | Aktor jest uwierzytelniony i uzyskuje dostęp do funkcji dla zalogowanych użytkowników. |
| **Główny scenariusz** | 1. Aktor wchodzi na stronę logowania. <br> 2. Aktor wprowadza zarejestrowany email i poprawne hasło. <br> 3. Aktor zatwierdza formularz. <br> 4. System weryfikuje istnienie użytkownika i poprawność hasła. <br> 5. System tworzy i zapisuje nową sesję w bazie danych. <br> 6. System przekierowuje Aktora na stronę panelu użytkownika. |
| **Scenariusze alternatywne** | **A. Nieprawidłowe dane logowania:** <br> 1. W kroku 2, Aktor wprowadza błędny email lub hasło. <br> 2. W kroku 4, weryfikacja kończy się niepowodzeniem. <br> 3. System odświeża stronę logowania, wyświetlając komunikat o błędnych danych. |
| **Scenariusze wyjątków** | **Zdarzenie: Aktor jest już zalogowany.** <br> 1. Zalogowany użytkownik próbuje wejść na stronę `/login`. <br> 2. System natychmiast przekierowuje go do panelu użytkownika, bez wyświetlania formularza. |
| **Wymagania jakościowe** | 1. Proces logowania musi odbywać się przez HTTPS (w wersji produkcyjnej). <br> 2. Hasła nie mogą być przechowywane w formie czystego tekstu. |

---

### Przypadek Użycia: Sprzedaj Produkt

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-03 |
| **Nazwa** | Wystawienie Produktu na Sprzedaż |
| **Autorzy** | Bartek |
| **Priorytet** | Wysoki |
| **Krytyczność** | Średnia |
| **Źródło** | Główna funkcjonalność biznesowa platformy |
| **Osoba odpowiedzialna**| Właściciel Produktu |
| **Opis** | Opisuje proces, w którym zalogowany użytkownik (Sprzedawca) tworzy nową ofertę sprzedaży produktu. |
| **Wyzwalacze** | Sprzedawca wybiera opcję "Sprzedaj" lub "Wystaw przedmiot". |
| **Aktorzy** | Sprzedawca (zalogowany użytkownik) |
| **Warunki wstępne** | Aktor jest zalogowany. |
| **Warunki końcowe** | 1. W bazie danych zostaje utworzony nowy rekord produktu, powiązany z kontem Sprzedawcy. <br> 2. Nowy produkt jest widoczny na liście produktów. |
| **Rezultat** | Nowa oferta sprzedaży jest dostępna w systemie. |
| **Główny scenariusz** | 1. Aktor wybiera opcję wystawienia nowego produktu. <br> 2. System wyświetla formularz dodawania produktu. <br> 3. Aktor wypełnia wymagane pola (nazwa, cena, kategoria, opis). <br> 4. Aktor zatwierdza formularz. <br> 5. System waliduje dane i tworzy nowy produkt w bazie danych. <br> 6. System przekierowuje Aktora na stronę nowo utworzonego produktu. |
| **Scenariusze alternatywne** | **A. Błędy walidacji formularza:** <br> 1. W kroku 3, Aktor wprowadza niepoprawne dane. <br> 2. W kroku 5, walidacja kończy się niepowodzeniem. <br> 3. System ponownie wyświetla formularz z komunikatami o błędach. |
| **Scenariusze wyjątków** | **Zdarzenie: Użytkownik niezalogowany.** <br> 1. Niezalogowany użytkownik próbuje uzyskać dostęp do formularza sprzedaży. <br> 2. System przechwytuje żądanie i przekierowuje go na stronę logowania. |
| **Wymagania jakościowe** | Formularz musi jasno wskazywać, które pola są wymagane. |

---

### Przypadek Użycia: Kup Produkt

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-04 |
| **Nazwa** | Dokonanie Zakupu Produktu |
| **Autorzy** | Bartek |
| **Priorytet** | Krytyczny |
| **Krytyczność** | Krytyczna |
| **Źródło** | Główna funkcjonalność biznesowa platformy |
| **Osoba odpowiedzialna**| Właściciel Produktu |
| **Opis** | Opisuje proces od dodania produktu do koszyka, przez finalizację zakupu, aż do utworzenia zamówienia. |
| **Wyzwalacze** | Użytkownik klika przycisk "Dodaj do koszyka" lub "Kup Teraz". |
| **Aktorzy** | Kupujący, Gość |
| **Warunki wstępne** | Co najmniej jeden produkt jest dostępny do sprzedaży. |
| **Warunki końcowe** | System tworzy nowy rekord zamówienia w bazie danych, powiązany z kontem Kupującego. |
| **Rezultat** | Produkt zostaje zakupiony przez użytkownika. |
| **Główny scenariusz** | 1. Użytkownik na stronie produktu klika "Dodaj do koszyka". <br> 2. System dodaje produkt do koszyka w sesji. <br> 3. Użytkownik przechodzi do strony koszyka i klika "KUP TERAZ". <br> 4. System wyświetla stronę podsumowania zamówienia. <br> 5. Użytkownik potwierdza zakup. <br> 6. System tworzy zamówienie w bazie danych i wyświetla stronę z potwierdzeniem. |
| **Scenariusze alternatywne** | **A. Zakup przez Gościa:** <br> 1. Gość wykonuje kroki 1-3 scenariusza głównego. <br> 2. W kroku 4, zamiast podsumowania, system przekierowuje Gościa na stronę logowania. <br> 3. Po zalogowaniu, system przywraca koszyk i kontynuuje od kroku 4. |
| **Scenariusze wyjątków** | **Zdarzenie: Produkt niedostępny.** <br> 1. Użytkownik próbuje dodać do koszyka produkt, który został wyprzedany. <br> 2. System wyświetla komunikat o niedostępności produktu. |
| **Wymagania jakościowe** | Proces zakupu musi być transakcyjny – jeśli krok zawiedzie, cała operacja musi zostać wycofana. |

---

### Przypadek Użycia: Wyświetlaj Status i Historię Zamówień

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-05 |
| **Nazwa** | Monitorowanie Zamówień |
| **Autorzy** | Bartek |
| **Priorytet** | Wysoki |
| **Krytyczność** | Średnia |
| **Źródło** | Wymaganie funkcjonalne panelu użytkownika |
| **Osoba odpowiedzialna**| Właściciel Produktu |
| **Opis** | Opisuje, w jaki sposób zalogowany użytkownik może przeglądać listę swoich zamówień i śledzić ich status. |
| **Wyzwalacze** | Użytkownik przechodzi na stronę "Historia zamówień" w swoim panelu. |
| **Aktorzy** | Kupujący (zalogowany użytkownik) |
| **Warunki wstępne** | 1. Aktor jest zalogowany. <br> 2. Aktor dokonał co najmniej jednego zakupu. |
| **Warunki końcowe** | System wyświetla listę zamówień należących do Aktora, posortowaną od najnowszych. |
| **Rezultat** | Kupujący ma wgląd w historię swoich transakcji i status bieżących zamówień. |
| **Główny scenariusz** | 1. Aktor wchodzi na stronę "Historia zamówień". <br> 2. System identyfikuje Aktora i wyszukuje w bazie wszystkie jego zamówienia. <br> 3. System pobiera powiązane informacje i sortuje zamówienia chronologicznie. <br> 4. System wyświetla sformatowaną listę zamówień. <br> 5. Aktor klika na szczegóły zamówienia. <br> 6. System wyświetla stronę ze szczegółami wybranego zamówienia. |
| **Scenariusze alternatywne** | **A. Brak historii zamówień:** <br> 1. W kroku 2, system nie znajduje żadnych zamówień. <br> 2. System wyświetla komunikat o braku historii zamówień. |
| **Scenariusze wyjątków** | **Zdarzenie: Użytkownik niezalogowany.** <br> 1. Niezalogowany użytkownik próbuje uzyskać dostęp do strony. <br> 2. System przechwytuje żądanie i przekierowuje na stronę logowania. |
| **Wymagania jakościowe** | 1. Status zamówienia musi być aktualizowany w czasie zbliżonym do rzeczywistego. <br> 2. Lista musi być paginowana przy dużej liczbie transakcji. |

---

### Przypadek Użycia: Generuj Raporty Sprzedaży

| Sekcja | Treść |
| :--- | :--- |
| **Oznaczenie** | PU-06 |
| **Nazwa** | Generowanie Raportów Sprzedaży |
| **Autorzy** | Bartek |
| **Priorytet** | Średni |
| **Krytyczność** | Średnia |
| **Źródło** | Wymaganie funkcjonalne dla kont firmowych |
| **Osoba odpowiedzialna**| Właściciel Produktu |
| **Opis** | Opisuje proces, w którym użytkownik z kontem "Firma" może generować raporty dotyczące swojej sprzedaży. |
| **Wyzwalacze** | Użytkownik firmowy przechodzi na stronę "Statystyki Firmowe". |
| **Aktorzy** | Sprzedawca (zalogowany użytkownik z kontem "Firma") |
| **Warunki wstępne** | 1. Aktor jest zalogowany. <br> 2. Konto Aktora jest typu FIRMA. <br> 3. Aktor sprzedał co najmniej jeden produkt. |
| **Warunki końcowe** | 1. System wyświetla stronę z raportami. <br> 2. (Opcjonalnie) System generuje plik z raportem do pobrania. |
| **Rezultat** | Sprzedawca uzyskuje wgląd w wyniki swojej działalności handlowej. |
| **Główny scenariusz** | 1. Aktor wchodzi na stronę "Statystyki Firmowe". <br> 2. System weryfikuje, czy typ konta to FIRMA. <br> 3. Aktor wybiera zakres dat i typ raportu. <br> 4. System wyszukuje i agreguje dane o transakcjach. <br> 5. System wyświetla dane w formie tabel i wykresów. <br> 6. (Opcjonalnie) Aktor klika "Pobierz jako PDF", a system generuje i udostępnia plik. |
| **Scenariusze alternatywne** | **A. Brak danych do raportu:** <br> 1. W kroku 4, system nie znajduje transakcji dla wybranego okresu. <br> 2. System wyświetla komunikat o braku danych. |
| **Scenariusze wyjątków** | **Zdarzenie: Użytkownik nie jest firmą.** <br> 1. Użytkownik z kontem OSOBA_FIZYCZNA próbuje wejść na stronę. <br> 2. System przechwytuje żądanie i przekierowuje do głównego panelu. |
| **Wymagania jakościowe** | 1. Generowanie raportów nie powinno blokować interfejsu. <br> 2. Dane w raportach muszą być spójne i dokładne. <br> 3. Sprzedawca widzi tylko dane dotyczące jego własnej sprzedaży. |