Ścieżki wylogowania:  
SF.1.1 - Ścieżka podstawowa - Stan początkowy: Użytkownik jest zalogowany. -> Input (Akcja użytkownika): Użytkownik wysyła request wylogowania. -> Output (System): Wyloguj użytkownika. Prześlij stronę główną użytkownikowi.  
SF.1.2 - Ścieżka alternatywna - Stan początkowy: Użytkownik jest zalogowany. -> Input (System): Stan aktywności użytkownika. -> Decyzja (System): Czy użytkownik jest nieaktywny dłużej niż 2 godziny? ->  
Jeśli TAK: Output (System): Wyloguj użytkownika. Prześlij stronę główną użytkownikowi.  
Jeśli NIE: Czytaj status użytkownika.  
SF.1.3 - Ścieżka błędu - Stan początkowy: Użytkownik jest zalogowany. -> Input (System): Brak połączenia z użytkownikiem (użytkownik utracił połączenie z internetem). -> Output (System): Wyloguj użytkownika.  
  
SF.2.1 - Ścieżka podstawowa - Logowanie użytkownika:  
Stan początkowy: Użytkownik chce się zalogować. -> Input (Akcja użytkownika): request użytkownika o logowanie. -> Output (System): system wyświetla stronę logowania. -> Input (Akcja użytkownika): użytkownik wpisuje dane logowania. -> Decyzja (System): Czy dane logowania są poprawne? ->  
Jeśli TAK: Output (System): Prześle wersję strony dla zalogowanego użytkownika.  
Jeśli NIE: Czytaj Ścieżka błędu.  
SF.2.2 - Ścieżka błędu - Nieudane logowanie:  
Stan początkowy: Użytkownik wpisuje dane logowania. -> Decyzja (System): Czy dane logowania są poprawne? ->  
Jeśli NIE: Decyzja (System): Czy to czwarta z rzędu nieudana próba logowania? ->  
Jeśli NIE: Output (System): system wyświetla błąd wskazujący, że wprowadzono błędne informacje. -> Input (Akcja użytkownika): użytkownik wpisuje dane logowania (ponownie).
Jeśli TAK: Output (System): system wyświetla informację o zablokowaniu możliwości logowania na 5 minut. -> Akcja (System): Zablokowanie logowania na 5 minut. -> Input (Akcja użytkownika): użytkownik wpisuje dane logowania (ponownie).  