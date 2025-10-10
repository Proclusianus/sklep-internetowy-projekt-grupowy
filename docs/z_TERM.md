Wymagania FEAT dla projektu:

F: Wymagania Funkcjonalne
F1. Zarządzanie Kontem Użytkownika
F1.1: System musi umożliwiać rejestrację nowych użytkowników.
F1.2: System musi umożliwiać logowanie i wylogowywanie dla zarejestrowanych użytkowników.
F1.3: System musi udostępniać panel użytkownika z dostępem do historii zamówień, obserwowanych aukcji i danych adresowych.

F2. Moduł Sklepu Internetowego ("Kup Teraz")
F2.1: System musi wyświetlać produkty w podziale na kategorie.
F2.2: System musi udostępniać wyszukiwarkę produktów z opcją filtrowania i sortowania.
F2.3: System musi pozwalać na dodawanie produktów do koszyka i zarządzanie jego zawartością.
F2.4: System musi umożliwiać złożenie zamówienia na produkty z koszyka.
F2.5: Użytkownicy muszą mieć możliwość oceniania i recenzowania zakupionych produktów.

F3. Moduł Aukcyjny
F3.1: Sprzedawcy muszą mieć możliwość wystawiania przedmiotów na aukcję, określając cenę wywoławczą i czas trwania.
F3.2: Klienci muszą mieć możliwość przeglądania aktywnych aukcji.
F3.3: System musi umożliwiać Klientom składanie ofert w licytacjach.
F3.4: System musi automatycznie wysyłać powiadomienia do użytkowników o przebiciu ich oferty oraz o zbliżającym się końcu aukcji.
F3.5: Po zakończeniu aukcji system musi automatycznie wyłonić zwycięzcę i powiadomić go oraz Sprzedawcę.

F4. Moduł Płatności
F4.1: System musi symulować proces podłączenia karty płatniczej do konta użytkownika.
F4.2: System musi symulować obsługę płatności kartą za zamówienia i wygrane aukcje.
F4.3: System musi symulować możliwość wykorzystania kart podarunkowych (giftcardów) jako formy płatności.

F5. Panel Administratora
F5.1: Administrator musi mieć możliwość zarządzania użytkownikami (usuwanie, nakładanie ograniczeń).
F5.2: Administrator musi mieć możliwość zarządzania kategoriami produktów.
F5.3: Administrator musi mieć możliwość monitorowania i moderowania (np. usuwania) aktywnych aukcji i wystawionych produktów.

E: Wymagania Ergonomiczne
E1: Interfejs użytkownika musi być spójny i intuicyjny na obu platformach (webowej i mobilnej).
E2: Aplikacja webowa musi być responsywna, tj. poprawnie wyświetlać się na ekranach o różnej wielkości (desktop, tablet, smartfon).
E3: System musi dostarczać użytkownikowi czytelne komunikaty zwrotne po wykonaniu akcji (np. "Produkt dodany do koszyka", "Twoja oferta jest najwyższa").
E4: Formularze (rejestracji, logowania, składania oferty) muszą być proste w obsłudze i zawierać walidację wprowadzanych danych w czasie rzeczywistym.
E5: Aplikacja mobilna na Androida musi być dostępna do pobrania przez oficjalny Sklep Play.

A: Wymagania Architektoniczne
A1: System musi składać się z aplikacji webowej, natywnej aplikacji mobilnej (Android) oraz centralnego serwera backendowego.
A2: Komunikacja pomiędzy aplikacjami klienckimi (web/mobile) a serwerem musi odbywać się za pomocą API w standardzie REST.
A3: Backend aplikacji musi być zaimplementowany w języku Java z wykorzystaniem frameworka Spring.
A4: Aplikacja mobilna musi być napisana w języku Kotlin.
A5: Logika frontendu aplikacji webowej musi bazować na technologiach HTML i CSS.
A6: Serwer musi korzystać z relacyjnej bazy danych MySQL lub MSSQL.
A7: Aplikacja mobilna musi wykorzystywać lokalną bazę danych SQLite do przechowywania danych tymczasowych.

T: Wymagania Techniczne
T1 (Wydajność): Czas odpowiedzi serwera na typowe żądania API nie powinien przekraczać 500 ms. Czas ładowania kluczowych widoków aplikacji nie powinien przekraczać 3 sekund.
T2 (Bezpieczeństwo): Hasła użytkowników muszą być przechowywane w bazie danych w postaci zaszyfrowanej (hash).
T3 (Bezpieczeństwo): Cała komunikacja między klientem a serwerem musi być szyfrowana za pomocą protokołu HTTPS.
T4 (Niezawodność): System musi zapewniać dostępność na poziomie 99% czasu. Należy wdrożyć mechanizm regularnego tworzenia kopii zapasowych bazy danych.
T5 (Skalowalność): Architektura systemu powinna umożliwiać w przyszłości horyzontalne skalowanie serwera aplikacyjnego w odpowiedzi na rosnące obciążenie.