# Faza 2 — Postgres, Docker, współbieżność

**Cel:** aplikacja działa na prawdziwej bazie w kontenerach, schemat jest zarządzany migracjami, równoległe rezerwacje nie psują danych.
**Czas:** ok. 3 tygodnie. **Tryb AI:** nauczyciel — w `CLAUDE.md` ustaw `FAZA: 2`, w Claude Code styl „Learning”.

---

### 2.1 Dockerfile — `faza-2/01-dockerfile`

- Wieloetapowy Dockerfile dla `booking-service`: etap budowania (Maven) + etap uruchomieniowy (samo JRE).
- `.dockerignore`.
- Obraz się uruchamia: `docker run -p 8080:8080 slotbook/booking-service`.
- W opisie PR: rozmiar obrazu jednoetapowego vs wieloetapowego.

**Pojęcia:** obraz vs kontener, warstwy i cache, po co multi-stage.

### 2.2 Postgres + docker-compose — `faza-2/02-postgres-compose`

- `docker-compose.yml` w głównym katalogu: `postgres` + `booking-service`, wolumen na dane.
- Konfiguracja przez zmienne środowiskowe. Aplikacja uruchamiana z IDE też łączy się z Postgresem z compose.
- Healthcheck dla bazy, `depends_on` z warunkiem.
- Testy na razie zostają na H2 (zmiana w 2.4).

**Pojęcia:** sieć w compose (czemu host to `postgres`, a nie `localhost`), wolumeny.

### 2.3 Migracje Flyway — `faza-2/03-flyway`

- `spring.jpa.hibernate.ddl-auto=validate` — Hibernate już nie tworzy tabel.
- `V1__init.sql` napisany ręcznie.
- `V2__...` — indeks pod zapytania o kolizje i dostępność. Pokaż `EXPLAIN` przed i po.
- `V3__...` — nowa kolumna `NOT NULL` (np. `price_per_hour`) na tabeli, w której są już dane.

**Pojęcia:** czemu wykonanej migracji się nie edytuje; co zrobić, gdy jest w niej błąd.

### 2.4 Testcontainers — `faza-2/04-testcontainers`

- Testy integracyjne na prawdziwym Postgresie w kontenerze. H2 usunięte z projektu.
- CI dalej zielone.

**Pojęcia:** czemu testy na H2 potrafią kłamać; koszt czasowy testów integracyjnych.

### 2.5 Podwójna rezerwacja — `faza-2/05-concurrency`

To jest typowy błąd produkcyjny. Na rozmowie rekrutacyjnej opowiedz tę historię.

- **Najpierw udowodnij błąd:** test, w którym kilka wątków naraz rezerwuje ten sam slot (`ExecutorService` + `CountDownLatch`). Powstaje więcej niż jedna rezerwacja.
- **Potem napraw.** Rozważ co najmniej dwa podejścia i opisz w PR, czemu wybrałeś jedno:
  - blokada pesymistyczna (`SELECT ... FOR UPDATE` na zasobie),
  - blokada optymistyczna (`@Version`),
  - ograniczenie w bazie: `EXCLUDE USING gist` na przedziale czasu (rozszerzenie `btree_gist`),
  - poziom izolacji `SERIALIZABLE`.
- Test z pierwszego punktu przechodzi. Klient dostaje czytelne `409`.

**Pojęcia:** race condition, check-then-act, poziomy izolacji transakcji.

---

## Musisz umieć wyjaśnić

- obraz vs kontener; twój Dockerfile linijka po linijce
- jak kontenery w compose się widzą
- czemu migracji się nie edytuje i co zrobić z błędną
- skąd się brała podwójna rezerwacja i czemu twoja poprawka działa
- czym jest indeks i jak sprawdzić, czy zapytanie go używa

## Test samodzielności

Napisz od zera `Dockerfile` i `docker-compose.yml` — bez podglądania.

**Tag:** `v2`
