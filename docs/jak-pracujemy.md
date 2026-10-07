# Jak pracujemy

## Rytm pracy

- **Faza = Milestone** na GitHubie. **Krok w fazie = Issue.**
- Każdy krok to osobny branch i osobny, mały PR do `main`.
- Nazwa brancha: `faza-<n>/<nr-kroku>-<krotki-opis>`, np. `faza-1/04-reservations`.
- PR: wypełniony szablon → zielone CI → review mentora → poprawki → merge (squash).
- **Koniec fazy:** tag (`v1`, `v2`, …) i **przegląd fazy** — patrz niżej. Następna faza dopiero po przeglądzie.

### Dlaczego małe PR-y, a nie jeden na fazę

- Feedback po 2 dniach, nie po 5 tygodniach. Złe założenie nie zdąży obrosnąć kodem.
- PR na 300 linii da się porządnie przejrzeć. PR na 3000 — nie.
- Tak się pracuje w firmie. Nawyk małych PR-ów docenią od pierwszego dnia.

## Zasady PR-a

- Do ~400 linii zmian (bez plików generowanych). Większy → podziel.
- Opis według szablonu, łącznie z sekcją „Gdzie użyłem AI”.
- Na każdy komentarz z review odpowiadasz: poprawką albo argumentem. Nie zamykasz wątku bez odpowiedzi.
- Commity po angielsku, w trybie rozkazującym: `Add reservation overlap validation`.

## Zasady korzystania z AI

| Faza | Wolno | Nie wolno |
|---|---|---|
| 0–1 | pytać o pojęcia, błędy, „dlaczego”; poprosić o review gotowego kodu | generować kodu do repo; autouzupełnianie AI w IDE (wyłącz) |
| 2–3 | Claude Code jako nauczyciel: tłumaczy, prowadzi, pisze boilerplate; logikę i konfigurację piszesz ty | akceptować zmian, których nie umiesz wyjaśnić |
| 4–5 | Claude Code jako partner: może pisać kod | merge bez testów; PR, którego nie obronisz linijka po linijce |
| frontend | vibe coding — Claude generuje | udawać, że to ręcznie pisany kod |

**Złota reguła: każdą linię w PR musisz umieć wyjaśnić.** Mentor może zapytać o dowolną.

Ustawienia Claude Code:
- [CLAUDE.md](../../CLAUDE.md) w repo mówi Claude'owi, jak ma się zachowywać. Przy przejściu do nowej fazy zmień w nim linię `FAZA:`.
- Od fazy 2 ustaw w Claude Code styl wypowiedzi (output style) **„Learning”** — wtedy Claude tłumaczy i zostawia ci fragmenty do napisania.

## Gdy utkniesz

1. Przeczytaj błąd do końca. W stack trace szukaj pierwszej linii z **twoim** pakietem.
2. Debugger i breakpoint, nie `System.out.println`.
3. 30 minut samemu. Potem AI jako tłumacz: „wyjaśnij, nie naprawiaj”.
4. Dalej nic → pytanie do mentora w formacie: co chciałem zrobić / co zrobiłem / czego się spodziewałem / co dostałem.

## Przegląd fazy (ok. 45 min)

1. Pokazujesz działającą aplikację (demo).
2. Mentor wybiera 2–3 miejsca w kodzie — tłumaczysz je bez notatek.
3. Przechodzicie listę „Musisz umieć wyjaśnić” z pliku fazy.
4. **Test samodzielności** z pliku fazy — robisz go przed przeglądem, bez AI.
5. Krótka retrospektywa: co szło dobrze, co najdłużej blokowało, co zmienić w następnej fazie.

## Dla mentora

- W review pytaj, zamiast podawać rozwiązanie: „co się stanie, jeśli dwa requesty przyjdą naraz?”.
- Oznaczaj wagę komentarzy: `blocker:` / `sugestia:` / `pytanie:` / `nit:`.
- Raz na fazę daj mu przejrzeć kawałek prawdziwego, większego kodu (open source albo twój) — czytanie cudzego kodu to większość pracy.
