# Draughtly

*Platforma do gry w warcaby online.*

Draughtly umożliwiać będzie 2 główne tryby rozgrywki PvP i PvAI, wiele wariantów growych i czasowych, podstawowy system rankingowy, matchmaking oparty na poziomie umiejętności dobieranych graczy. Budowany w architekturze mikroserwisowej.

## Mikroserwisy (koncept)

- **API Gateway** - jedyny punkt wejścia do systemu, routing do serwisów, walidacja JWT, rate limiting (Spring Cloud Gateway)
- **User Service** - tożsamość, dostęp i publiczny profil gracza
- **Notification Service** - wielokanałowe powiadomienia, konsumuje eventy z pozostałych serwisów
- **Validation Service** - wewnętrzny, bezstanowy, waliduje legalne ruchy w oparciu o py-draughts, jedyne źródło prawdy reguł gry, komunikacja po gRPC
- **AI Engine Service** - wewnętrzny, bezstanowy, generuje ruchy bota w trybie PvAI w oparciu o py-draughts, komunikacja po gRPC
- **Matchmaking Service** - adaptacyjny dobór graczy wg poziomu umiejętności i tworzenie gier po sparowaniu
- **Game Service** - orkiestruje rozgrywkę przez WebSocket, zarządza stanem partii, korzysta z Validation i AI Engine
- **Rating Service** - liczy ranking ELO i statystyki gracza po otrzymaniu eventu końca gry
- **Archive Service** - archiwum zakończonych partii, do wglądu dla graczy i celów analitycznych
- **Audit Service** - audyt bezpieczeństwa kont

## Zewnętrzne Zależności

- [py-draughts](https://github.com/miskibin/py-draughts) - zewnętrzna biblioteka odpowiedzialna za całą logikę gry w warcaby, współdzielona przez Validation Service i AI Engine Service jako jedyne źródło prawdy dla logiki gry
 