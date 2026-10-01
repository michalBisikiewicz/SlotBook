# Faza 0 — Setup

**Cel:** działające repo z pierwszym endpointem, CI i ustalonym sposobem pracy. Jeszcze bez logiki — chodzi o narzędzia.
**Czas:** ok. 1 tydzień. **Tryb AI:** tylko tłumacz.

---

### 0.1 Repo i narzędzia — commit bezpośrednio do `main` (jedyny raz)

- Konto GitHub, publiczne repo `slotbook` — to twoje portfolio.
- Zainstaluj: JDK 25 (LTS; 21, jeśli coś nie współpracuje), IntelliJ IDEA, Git, Docker Desktop (na razie tylko sprawdź `docker run hello-world`).
- Git skonfigurowany (`user.name`, `user.email`), klucz SSH dodany do GitHuba.
- Pierwszy commit: pliki planu (README, `docs/`, `CLAUDE.md`, `.github/pull_request_template.md`).
- Ochrona gałęzi `main`: merge tylko przez PR.
- Milestone'y „Faza 0” … „Faza 5” i issues dla kroków fazy 0.
- Wyłącz autouzupełnianie AI w IDE.

### 0.2 Szkielet Spring Boot — `faza-0/02-spring-boot`

- Projekt z [start.spring.io](https://start.spring.io) w katalogu `booking-service/`: Maven, najnowsza stabilna wersja Spring Boot. Zależności: Spring Web, Spring Data JPA, H2, Validation.
- Bez Lomboka — rekordy Javy wystarczą, a zobaczysz, co Lombok by ukrył.
- Endpoint `GET /api/hello` zwracający JSON.
- Testy: `@WebMvcTest` dla endpointu + `@SpringBootTest`, że kontekst wstaje.
- `./mvnw verify` przechodzi lokalnie.
- Plik `docs/api/hello.http` (HTTP Client w IntelliJ) z requestem.
- README: sekcja „Jak uruchomić”.

**Pojęcia:** autokonfiguracja Spring Boot, wbudowany Tomcat, cykl życia Mavena (`compile` → `test` → `package` → `verify`), po co jest `mvnw`.

### 0.3 CI — `faza-0/03-ci`

- `.github/workflows/ci.yml`: na każdy PR i push do `main` → checkout, setup Javy, `./mvnw -B verify` w `booking-service/`.
- Ochrona `main`: wymagany zielony check CI.
- Sprawdź, że celowo zepsuty test blokuje merge. Potem go napraw.

**Pojęcia:** CI, runner, składnia YAML, cache zależności Mavena.

---

## Musisz umieć wyjaśnić

- commit / push / branch / PR / merge — czym się różnią
- co się dzieje po wpisaniu `./mvnw verify`
- jak request HTTP trafia do twojej metody w kontrolerze (ogólnie)
- co robi `ci.yml`, krok po kroku

## Test samodzielności

Na pustym katalogu: wygeneruj nowy projekt, dodaj endpoint i test, wypchnij na nowy branch i otwórz PR — bez zaglądania do notatek.

**Tag:** `v0`
