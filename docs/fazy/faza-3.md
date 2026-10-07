# Faza 3 — Asynchroniczność: drugi serwis i RabbitMQ

**Cel:** drugi serwis, komunikacja zdarzeniami przez RabbitMQ, świadomość tego, co psuje się w systemach rozproszonych.
**Czas:** ok. 4 tygodnie. **Tryb AI:** nauczyciel — `FAZA: 3`.

## Kontekst

Po utworzeniu lub anulowaniu rezerwacji klient dostaje e-mail. `booking-service` nie wysyła maili sam — publikuje zdarzenie, a `notification-service` je konsumuje.

Zanim napiszesz kod: czemu zdarzenie przez kolejkę, a nie po prostu wywołanie REST? Odpowiedź w opisie PR 3.1.

---

### 3.1 RabbitMQ i publikacja zdarzeń — `faza-3/01-publish-events`

- RabbitMQ w compose (z panelem management).
- `booking-service` publikuje `ReservationConfirmed` i `ReservationCancelled` na exchange typu topic `slotbook.reservations`.
- Zdarzenie to JSON z `eventId` (UUID), `occurredAt`, typem i danymi rezerwacji.
- W panelu Rabbita sprawdź, że wiadomości wychodzą (tymczasowa kolejka podpięta ręcznie).

**Pojęcia:** exchange / queue / binding / routing key; typy exchange; nadawca nie zna odbiorcy.

### 3.2 notification-service — `faza-3/02-notification-service`

- Nowy projekt Spring Boot w `notification-service/` (Spring AMQP, Spring Mail).
- Konsumuje zdarzenia z własnej kolejki i wysyła e-mail przez SMTP do Mailpita (kontener w compose, podgląd maili w przeglądarce).
- Własny Dockerfile, wpis w compose, CI buduje i testuje oba serwisy.

**Do przemyślenia (w PR):** współdzielić klasy zdarzeń między serwisami (wspólna biblioteka) czy duplikować? Plusy i minusy.

### 3.3 Gdy coś pójdzie nie tak — `faza-3/03-retry-dlq`

- Zasymuluj awarie: SMTP nie działa; przychodzi „zatruta” wiadomość (zły JSON).
- Ponawianie z backoffem dla błędów przejściowych, dead letter queue dla wiadomości, których nie da się przetworzyć.
- W README: jak ręcznie ponowić wiadomość z DLQ.

**Pojęcia:** błąd przejściowy vs trwały, ack/nack, dostarczenie at-least-once.

### 3.4 Idempotentny konsument — `faza-3/04-idempotency`

- **Najpierw udowodnij problem:** ta sama wiadomość dostarczona dwa razy → dwa maile (duplikat wyślij ręcznie z panelu).
- Napraw: `notification-service` zapamiętuje przetworzone `eventId` we **własnej** bazie lub schemacie. Serwisy nie dzielą tabel.

**Pojęcia:** czemu duplikaty są normalne, a nie wyjątkowe; czemu „exactly-once” to (prawie) mit.

### 3.5 Ewolucja zdarzeń — `faza-3/05-event-evolution`

- Dodaj pole do zdarzenia (np. `resourceName`) tak, żeby konsument w starszej wersji się nie wysypał.

**Pojęcia:** kompatybilność wsteczna kontraktu, tolerant reader.

### 3.6 (Opcjonalnie) Kafka — eksperyment, bez merge'a

- Na osobnym branchu przepisz publikację i konsumpcję na Kafkę (tryb KRaft w compose).
- W notatce porównaj: log vs kolejka, partycje, consumer group, retencja. Kiedy wybrałbyś które?

---

## Musisz umieć wyjaśnić

- komunikacja synchroniczna vs asynchroniczna — co zyskaliśmy, co straciliśmy
- exchange, kolejki i bindingi — narysuj dla swojego systemu
- skąd się biorą duplikaty i jak się przed nimi bronisz
- co się dzieje z wiadomością, której nie da się przetworzyć
- eventual consistency na przykładzie: rezerwacja już jest, maila jeszcze nie ma
- **Pytanie na przegląd:** co, jeśli zapis rezerwacji do bazy się uda, a publikacja do Rabbita nie? (Rozwiązanie w fazie 5 — na razie wystarczy zauważyć problem.)

## Test samodzielności

Dodaj nowe zdarzenie `ReservationReminder` (np. wyzwalane endpointem administracyjnym) z obsługą w `notification-service` — od publikacji po mail w Mailpicie, bez AI.

**Tag:** `v3`
