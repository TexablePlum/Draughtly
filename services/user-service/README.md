# User Service

Mikroserwis systemu Draughtly, budowany w Java/Spring Boot, korzystający z architektury heksagonalnej, odpowiada za zakładanie i zarządzanie kontami oraz profilami użytkowników.

## Odpowiedzialności (koncept)

- **Rejestracja**
- **Aktywacja konta** przez kod z e-maila, serwis emituje event z kodem, wysyłkę maila robi Notification Service
- **Logowanie**
- **Wylogowanie / unieważnienie tokenu**, blacklista access tokenu w Redis, czytana przez Gateway
- **Reset hasła**
- **Zmiana hasła**
- **Zmiana adresu e-mail** wymaga potwierdzenia na oba adresy (stary i nowy)
- **Usuwanie konta przez anonimizację**
- **Zarządzanie stanem konta**: pending / active / banned / deleted, w tym reakcja na zewnętrzny sygnał banowania z Rating Service
- **Zarządzanie profilem gracza**: dane publiczne profilu

## Dane

PostgreSQL jako relacyjna baza danych. Redis do krótko żyjących danych i blacklisty access tokenów czytanej przez Gateway.

## Eventy (koncept)

### Emitowane

| Event | Kiedy | Konsument |
|---|---|---|
| UserRegistered | rejestracja | Audit |
| ActivationCodeGenerated | wygenerowanie kodu aktywacyjnego | Notification, Audit |
| AccountActivated | potwierdzenie aktywacji | Notification, Audit |
| UserLoggedIn | udane logowanie | Audit |
| LoginFailed | nieudane logowanie | Audit |
| UserLoggedOut | wylogowanie | Audit |
| PasswordResetRequested | żądanie resetu hasła | Notification, Audit |
| PasswordChanged | zmiana lub dokończenie resetu hasła | Notification, Audit |
| EmailChangeRequested / EmailChanged | zmiana adresu e-mail | Notification (oba adresy), Audit |
| AccountDeleted | usunięcie konta, wysyłane PRZED anonimizacją | Notification, Audit |
| AccountBanned | zbanowanie konta | Notification, Audit |
| ProfileUpdated | zmiana danych profilu | Audit |

### Konsumowane

| Event | Źródło | Reakcja |
|---|---|---|
| AbandonThresholdExceeded (nazwa robocza) | Rating Service | BanPolicy zmienia status konta na `banned` |

## Reguły

- Nazwa użytkownika unikalna, zmiana nie częściej niż raz na 6 miesięcy
- Stan konta jako maszyna stanów: pending -> active -> banned / deleted
- Hasło zawsze zahashowane
- Blokada konta po przekroczeniu liczby nieudanych prób logowania
- BanPolicy reaguje na sygnał z Rating Service, User Service sam nie liczy progu porzuceń
- ActivationPolicy: rejestracja generuje kod aktywacyjny z TTL
- PasswordResetPolicy / EmailChangePolicy: dwuetapowy przepływ requested -> confirmed
- Handlery eventów konsumowanych muszą być idempotentne