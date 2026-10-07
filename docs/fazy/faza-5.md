# Faza 5 — Architektura i niezawodność

**Cel:** system odporny na awarie, obserwowalny i zabezpieczony. Umiesz bronić swoich decyzji architektonicznych.
**Czas:** 4–5 tygodni. **Tryb AI:** partner — `FAZA: 5`.

---

### 5.1 Transactional outbox — `faza-5/01-outbox`

- **Udowodnij problem z fazy 3:** zatrzymaj Rabbita i utwórz rezerwację → rezerwacja jest, zdarzenia nie będzie nigdy.
- Napraw wzorcem outbox: zdarzenie zapisywane w tej samej transakcji co rezerwacja (tabela `outbox`), osobny proces publikuje je i oznacza jako wysłane.

**Pojęcia:** dual write; czemu zarówno „najpierw baza, potem Rabbit”, jak i odwrotnie są złe; jak outbox współgra z idempotentnym konsumentem z 3.4.

### 5.2 Bezpieczeństwo — `faza-5/02-security`

- Keycloak w compose. `booking-service` jako OAuth2 Resource Server (JWT).
- Role: `USER` (rezerwuje, widzi i anuluje swoje) i `ADMIN` (zarządza zasobami).
- Właściciel rezerwacji brany z tokena — koniec z `customerName` w body.
- Testy: użytkownik nie anuluje cudzej rezerwacji. W PR: `403` czy `404` — i czemu?

**Pojęcia:** uwierzytelnianie vs autoryzacja, OAuth2/OIDC w skrócie, co jest w JWT (zdekoduj swój), czemu nie pisze się własnego logowania.

### 5.3 Obserwowalność — `faza-5/03-observability`

- Logi w formacie JSON z identyfikatorem, który przechodzi przez REST → Rabbit → `notification-service`.
- Metryki (Micrometer → Prometheus + Grafana w compose): liczba rezerwacji, czasy odpowiedzi, długość kolejki.
- Opcjonalnie: tracing OpenTelemetry — jeden request widoczny w obu serwisach.

**Pojęcia:** logi vs metryki vs trace; na co ustawiłbyś alert.

### 5.4 Odporność na awarie zależności — `faza-5/04-resilience`

- Nowy, mały `payment-service` (atrapa). Rezerwacja przechodzi w stan `PENDING_PAYMENT`, `booking-service` woła płatności synchronicznie. Atrapa losowo odpowiada wolno albo błędem.
- Timeouty, ponawianie z backoffem, circuit breaker (np. Resilience4j).
- Klucz idempotencji przy płatności.

**Pojęcia:** kaskadowe awarie, czemu timeout jest obowiązkowy, kiedy ponawianie szkodzi (płatność bez klucza idempotencji!).

### 5.5 Test obciążeniowy — `faza-5/05-load-test`

- Skrypt k6: równoległe rezerwacje i zapytania o dostępność.
- Znajdź wąskie gardło (pula połączeń? brak indeksu? N+1?), popraw, zmierz ponownie. Wyniki przed i po w opisie PR.

### 5.6 Dokumentacja architektury — `faza-5/06-adr-c4`

- Diagram C4 (poziomy Context i Container) w README, np. w Mermaidzie.
- 4–5 ADR-ów w `docs/adr/`: monorepo, RabbitMQ vs Kafka vs REST, outbox, model bezpieczeństwa, wybór chmury.
- Uczciwa sekcja „Czego nie zrobiłbym w prawdziwym projekcie tej skali”. Mikroserwisy przy jednym zespole to tu celowe przeuczenie — żeby poznać ich problemy.

---

## Przegląd końcowy = próbna rozmowa rekrutacyjna

- opowiedz o systemie w 5 minut, rysując architekturę
- 3 najtrudniejsze problemy i jak je rozwiązałeś (podwójna rezerwacja, duplikaty, dual write)
- monolit vs mikroserwisy — kiedy który; czemu twój projekt jest celowo „za bardzo” rozbity
- co zepsuje się jako pierwsze przy 100 razy większym ruchu

**Tag:** `v5` — projekt gotowy do CV.
