# Tor równoległy — frontend z AI

**Kiedy:** od fazy 3 (wcześniej API jeszcze mocno się zmienia).
**Tryb AI:** Claude generuje kod (vibe coding). Ty jesteś product ownerem: piszesz wymagania, sprawdzasz efekt, zgłaszasz poprawki.

## Po co

Nie żeby zostać frontendowcem, tylko żeby:
1. mieć klikalne demo do portfolio,
2. nauczyć się pracy z Claude Code jako wykonawcą,
3. zrozumieć styk frontend–backend.

## Kroki

Stack: React + TypeScript + Vite, w katalogu `frontend/`. Każdy krok to osobny PR.

- **F.1** Lista zasobów i kalendarz dostępności.
- **F.2** Formularz rezerwacji z ładną obsługą błędów z API (`409` → „termin zajęty”).
- **F.3** Moje rezerwacje + anulowanie.
- **F.4** Logowanie przez Keycloak (po 5.2).
- **F.5** Deploy na Azure Static Web Apps (po fazie 4).

## Co musisz rozumieć sam, niezależnie od tego, kto pisał kod

- CORS — czemu przeglądarka blokuje request, a `curl` nie
- jak frontend wywołuje API i gdzie trafia token
- DevTools → zakładka Network: status, nagłówki, body odpowiedzi

## Jak pracować z Claude Code — to jest tu właściwa lekcja

- Zaczynaj od planu (plan mode), zanim pozwolisz cokolwiek pisać.
- Małe kroki. Po każdym sprawdź efekt w przeglądarce.
- Gdy coś się psuje, opisz objaw konkretnie: co kliknąłeś, co widzisz, co jest w konsoli.
- Przeglądaj diff przed akceptacją. Nawet jeśli nie rozumiesz wszystkiego, zauważysz, że zmienia 20 plików zamiast 2.

W `frontend/README.md` napisz wprost, że frontend powstał z pomocą Claude Code.
