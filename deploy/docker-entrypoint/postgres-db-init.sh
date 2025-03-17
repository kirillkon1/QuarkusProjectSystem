#!/bin/bash
set -e

# Список баз данных, которые необходимо создать
databases=(
"tasks_db"
"projects_db"
"users_db"
"analytics_db"
)

# Подключаемся к основной базе данных (заданной в POSTGRES_DB)
MAIN_DB=${POSTGRES_DB:-postgres}

for db in "${databases[@]}"; do
    # Проверяем, существует ли база данных
    exists=$(psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$MAIN_DB" -tAc "SELECT 1 FROM pg_database WHERE datname='$db'")
    if [ "$exists" != "1" ]; then
        echo "CREATING DATABASE '$db'..."
        psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$MAIN_DB" -c "CREATE DATABASE \"$db\";"
    else
        echo "DATABASE '$db' ALREADY EXISTS."
    fi
done
