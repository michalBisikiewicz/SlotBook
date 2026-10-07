# Faza 1 — REST API + H2

**Cel:** REST API rezerwacji z prawdziwą logiką biznesową i testami. **Napisane samodzielnie.**
**Czas:** 5–6 tygodni. **Tryb AI:** tłumacz i reviewer — zero generowania kodu.

## Model domeny

**Resource** (zasób): `id`, `name`, `type` (`COURT`, `ROOM`, `EQUIPMENT`), `openingTime`, `closingTime`, `active`.

**Reservation**: `id`, `resource`, `customerName`, `customerEmail`, `startTime`, `endTime`, `status` (`CONFIRMED`, `CANCELLED`), `createdAt`.

## Reguły biznesowe

1. Rezerwacja tylko w przyszłości.
2. Długość od 30 min do 3 h, w wielokrotnościach 30 min.
3. Tylko w godzinach otwarcia zasobu.
4. Nie może nachodzić na inną potwierdzoną rezerwację tego samego zasobu. Przedziały są `[start, end)` — koniec jednej równy początkowi drugiej jest OK.
5. Anulować można najpóźniej 2 h przed startem.
6. Nieaktywnego zasobu nie da się zarezerwować.

## Definition of Done każdego PR-a w tej fazie

- testy do nowej logiki, CI zielone
- plik `.http` w `docs/api/` zaktualizowany
- w sekcji „Gdzie użyłem AI” — tylko pytania i wyjaśnienia

---

### 1.1 CRUD zasobów — `faza-1/01-resources-crud`

- `POST /api/resources`, `GET /api/resources`, `GET /api/resources/{id}`, `PUT /api/resources/{id}`, `DELETE /api/resources/{id}`.
- Warstwy: controller → service → repository (Spring Data JPA), baza H2 w pamięci.
- Dane startowe (2–3 zasoby) ładowane przy starcie aplikacji.
- Testy: `@WebMvcTest` dla kontrolera.

**Pojęcia:** `@RestController`, `@Service`, wstrzykiwanie przez konstruktor, `@Entity`, `@Id`, konsola H2.

### 1.2 DTO, walidacja, kody HTTP — `faza-1/02-dto-validation`

- Osobne klasy request/response (rekordy). Encja nie wychodzi poza warstwę serwisu. Mapowanie ręczne.
- Bean Validation: `@NotBlank`, `@NotNull`, `@Size`…; `@Valid` w kontrolerze.
- Kody: `201` + nagłówek `Location` przy tworzeniu, `204` przy usunięciu, `404` gdy brak, `400` przy błędnych danych.

**Pojęcia:** czemu nie zwracać encji z API; idempotentność — `PUT` vs `POST`.

### 1.3 Obsługa błędów — `faza-1/03-error-handling`

- `@RestControllerAdvice`, odpowiedzi błędów w formacie `ProblemDetail` (RFC 9457 — Spring ma wbudowane wsparcie).
- Własne wyjątki domenowe → odpowiednie kody (`404`, `409`, `422`).
- Żaden stack trace nie trafia do klienta.

### 1.4 Rezerwacje — `faza-1/04-reservations`

- `POST /api/reservations`, `GET /api/reservations/{id}`, `POST /api/reservations/{id}/cancel`.
  W opisie PR: czemu anulowanie to `POST .../cancel`, a nie `DELETE`?
- Reguły 1–6. Logika w serwisie albo w encji — nie w kontrolerze.
- Usunięcie zasobu, który ma przyszłe rezerwacje → `409`.
- Testy jednostkowe **każdej** reguły, z przypadkami brzegowymi (rezerwacje stykające się końcami, granice godzin otwarcia).
- Na teraz „teraz” bierz z `Clock` wstrzykiwanego do serwisu — inaczej nie przetestujesz reguł 1 i 5.

**Pojęcia:** `@Transactional`, `@ManyToOne`, `LAZY` vs `EAGER`, problem N+1 (na razie tylko przeczytaj).

### 1.5 Dostępność — `faza-1/05-availability`

- `GET /api/resources/{id}/availability?date=2026-11-03` → lista wolnych 30-minutowych slotów w godzinach otwarcia.
- Algorytm samodzielnie. Ile zapytań do bazy robi twoje rozwiązanie?
- Testy: dzień pusty, dzień pełny, rezerwacja na granicy godzin otwarcia, anulowana rezerwacja nie blokuje slotu.

### 1.6 Wyszukiwanie i stronicowanie — `faza-1/06-search-pagination`

- `GET /api/reservations?resourceId=&from=&to=&status=&page=&size=&sort=`
- `Pageable` ze Spring Data, własne zapytanie (`@Query`) dla filtrów.
- To samo zapytanie napisz ręcznie w SQL w konsoli H2 — wklej do opisu PR.

**Pojęcia:** JPQL vs SQL, po co stronicowanie.

### 1.7 Dokumentacja API i test end-to-end — `faza-1/07-openapi`

- springdoc-openapi → Swagger UI.
- Jeden test `@SpringBootTest` z pełnym scenariuszem przez HTTP: utwórz zasób → zarezerwuj → sprawdź dostępność → anuluj → slot znów wolny.
- README: krótki opis API i reguł biznesowych.

---

## Musisz umieć wyjaśnić

- metody HTTP i kody odpowiedzi; czym różni się `400` od `409` i `422`
- co robi `@Transactional` i co się dzieje, gdy w środku poleci wyjątek
- wykrywanie nakładania się przedziałów — narysuj na kartce
- co Hibernate wysyła do bazy (włącz logowanie SQL i pokaż)
- test jednostkowy vs `@WebMvcTest` vs `@SpringBootTest` — kiedy który
- przejdź debuggerem jeden request od kontrolera do repozytorium
- napisz z pamięci SQL znajdujący kolidujące rezerwacje

## Test samodzielności

Usuń implementację reguły nakładania się rezerwacji i napisz ją od nowa — bez AI i bez zaglądania. Testy mają przejść.

**Tag:** `v1`
