# Faza 4 — CI/CD i Azure

**Cel:** każdy merge do `main` automatycznie trafia do chmury. Rozumiesz podstawowe usługi i koszty.
**Czas:** ok. 3 tygodnie. **Tryb AI:** partner — `FAZA: 4`.

> ⚠️ **Zanim cokolwiek założysz:** budżet z alertem mailowym na niską kwotę. Środowisko stawiasz i usuwasz świadomie — w README zapisz, jak zrobić jedno i drugie. Warunki darmowego konta Azure sprawdź na stronie Azure (zmieniają się).

---

### 4.1 Obrazy w rejestrze — `faza-4/01-images`

- CI buduje obrazy obu serwisów. Po merge do `main` wypycha je do GitHub Container Registry (`ghcr.io`), tag = SHA commita.

**Pojęcia:** rejestr obrazów; czemu tag = SHA, a nie tylko `latest`.

### 4.2 Infrastruktura w Azure — `faza-4/02-azure-infra`

- Resource group, środowisko Azure Container Apps, Azure Database for PostgreSQL (Flexible Server, najmniejszy tier).
- Najpierw ręcznie (Portal / `az` CLI) — dla zrozumienia. Kroki zapisz w `infra/README.md`.
- Sekrety (hasło do bazy) jako sekrety Container Apps albo w Key Vault — nigdy w repo.
- **RabbitMQ w chmurze — twoja decyzja, opisana w PR:**
  - kontener w Container Apps,
  - zarządzany RabbitMQ zewnętrznego dostawcy,
  - zamiana na Azure Service Bus (inna biblioteka — kosztowne, ale uczy, czym jest vendor lock-in).

**Pojęcia:** IaaS / PaaS / SaaS, region, resource group, baza zarządzana vs baza w kontenerze.

### 4.3 Deploy z GitHub Actions — `faza-4/03-cd`

- `cd.yml`: po merge do `main` → wdrożenie nowych obrazów do Container Apps.
- Logowanie do Azure przez OIDC (federated credentials) — bez kluczy w sekretach GitHuba.
- Opcjonalnie: environment `production` w GitHubie z ręcznym zatwierdzeniem.

**Pojęcia:** continuous delivery vs continuous deployment, OIDC, rollback (rewizje w Container Apps).

### 4.4 Zdrowie i logi — `faza-4/04-health-logs`

- Spring Boot Actuator: `/actuator/health` z liveness i readiness, podpięte jako sondy w Container Apps.
- Logi aplikacji w Log Analytics. Umiesz znaleźć błąd z konkretnego requestu prostym zapytaniem KQL.

### 4.5 (Opcjonalnie) Infrastruktura jako kod — `faza-4/05-iac`

- Całe środowisko w Bicepie albo Terraformie, w katalogu `infra/`. Postawienie od zera i usunięcie — po jednej komendzie.

---

## Jeśli zamiast Azure wybierzesz AWS

| Azure | AWS |
|---|---|
| Container Apps | ECS Fargate |
| Database for PostgreSQL | RDS for PostgreSQL |
| Service Bus | Amazon MQ / SQS |
| Log Analytics | CloudWatch Logs |
| Key Vault | Secrets Manager |
| AZ-900 | AWS Cloud Practitioner |

---

## Musisz umieć wyjaśnić

- droga commita od `git push` do działającej aplikacji w chmurze — krok po kroku
- ile kosztuje twoje środowisko miesięcznie i co jest w nim najdroższe
- czemu OIDC, a nie klucz w sekretach
- jak wycofać złe wdrożenie
- PaaS vs IaaS na przykładzie twojej bazy

## Test samodzielności

Usuń całe środowisko i postaw je od nowa według własnej dokumentacji z `infra/`.

**Tag:** `v4`
