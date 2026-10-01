# Slotbook

System rezerwacji zasobów na sloty czasowe (korty, sale, sprzęt). Projekt do nauki, rozwijany etapami: od prostego REST API do systemu rozproszonego w chmurze.

> Domena jest umowna. Jeśli masz lepszy pomysł (np. z poprzedniej pracy), podmień ją — struktura faz zostaje ta sama.

## Roadmapa

| Faza | Temat | Czas* | Tryb AI | Tag |
|---|---|---|---|---|
| [0](docs/fazy/faza-0.md) | Setup: repo, Git, szkielet Spring Boot, CI | 1 tydz. | tylko tłumacz | `v0` |
| [1](docs/fazy/faza-1.md) | REST API + H2 + testy | 5–6 tyg. | tłumacz i reviewer, **zero generowania kodu** | `v1` |
| [2](docs/fazy/faza-2.md) | Postgres, Docker, migracje, współbieżność | 3 tyg. | nauczyciel | `v2` |
| [3](docs/fazy/faza-3.md) | Drugi serwis + RabbitMQ | 4 tyg. | nauczyciel | `v3` |
| [4](docs/fazy/faza-4.md) | CI/CD + Azure | 3 tyg. | partner | `v4` |
| [5](docs/fazy/faza-5.md) | Architektura i niezawodność | 4–5 tyg. | partner | `v5` |
| [F](docs/fazy/tor-frontend.md) | Frontend z AI — równolegle, od fazy 3 | w tle | generuje | — |

\* Przy ok. 10 h tygodniowo. To orientacja, nie deadline — liczy się zrozumienie, nie tempo.

Jak pracujemy (branche, PR-y, review, zasady AI): [docs/jak-pracujemy.md](docs/jak-pracujemy.md).

## Docelowa architektura (po fazie 5)

```
                    ┌──────────────┐
                    │   frontend   │
                    └──────┬───────┘
                           │ HTTP + JWT          ┌──────────┐
                           ▼                     │ Keycloak │
┌─────────────────┐  HTTP  ┌──────────────────┐  └──────────┘
│ payment-service │◄───────│ booking-service  │──SQL──► PostgreSQL
│    (atrapa)     │        └────────┬─────────┘
└─────────────────┘                 │ zdarzenia (outbox)
                                    ▼
                              ┌──────────┐
                              │ RabbitMQ │
                              └────┬─────┘
                                   ▼
                      ┌──────────────────────┐
                      │ notification-service │──SMTP──► Mailpit
                      └──────────────────────┘
```

## Struktura repo (docelowa)

```
slotbook/
├── README.md
├── CLAUDE.md                    # zasady dla Claude Code (tryb nauczyciela)
├── docs/
│   ├── jak-pracujemy.md
│   ├── fazy/                    # plan faz
│   ├── adr/                     # decyzje architektoniczne (faza 5)
│   └── api/                     # pliki .http z przykładowymi requestami
├── booking-service/             # od fazy 0
├── notification-service/        # od fazy 3
├── payment-service/             # faza 5
├── frontend/                    # tor równoległy
├── infra/                       # faza 4
├── docker-compose.yml           # od fazy 2
└── .github/
    ├── workflows/               # CI od fazy 0, CD od fazy 4
    └── pull_request_template.md
```

## Jak uruchomić

_Uzupełniasz sam w miarę postępów — to część zadania 0.2._

## Poza kodem

- **Po fazie 3:** profil GitHub i LinkedIn, CV z linkiem do tego repo.
- **Po fazie 4:** opcjonalnie certyfikat AZ-900 (tani, w CV juniora czasem pomaga).
- **Po fazie 5:** próbne rozmowy rekrutacyjne z mentorem — techniczna i „opowiedz o projekcie”.
