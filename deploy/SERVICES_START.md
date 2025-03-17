# Скрипты для запуска сервисов в отдельном контейнере

### В IntelliJ IDEA есть возможность запустить скрипт прямо из данного файла

### Список сервисов
*  PostgreSQL
*  Kafka
*  Zookeeper
*  Elasticsearch
*  Logstash
*  Kibana
*  Prometheus
*  Grafana

### 1. Запуск PostgreSQL 

**Контейнер:** `postgres:latest`

**Параметры:**

- **Имя контейнера:** `postgres`
- **Порты:** `5432:5432`
- **Переменные окружения:**
    - `POSTGRES_USER=postgres`
    - `POSTGRES_PASSWORD=postgres`
    - `POSTGRES_DB=postgres`
- **Volumes:**
    - Инициализационный скрипт: `./docker-entrypoint/postgres-db-init.sh` - создаёт БД для микросервисов
    - Том баз данных: `postgres_data_qpms`

**Команда запуска:**

```bash
sudo docker run -d \
  --name postgres \
  -p 5432:5432 \
  --network microservices-network \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=postgres \
  -v "$(pwd)/docker-entrypoint/postgres-db-init.sh:/docker-entrypoint-initdb.d/init-db.sh" \
  -v postgres_data_spms:/var/lib/postgresql/data \
  postgres:latest
```

### 2. Kafka

**Контейнер:** `postgres:latest`

**Параметры:**

- **Имя контейнера:** `postgres`
- **Порты:** `5432:5432`
- **Переменные окружения:**
  - `POSTGRES_USER=postgres`
  - `POSTGRES_PASSWORD=postgres`
  - `POSTGRES_DB=postgres`
- **Volumes:**
  - Инициализационный скрипт: `./docker-entrypoint/postgres-db-init.sh` - создаёт БД для микросервисов
  - Том баз данных: `postgres_data_spms`
