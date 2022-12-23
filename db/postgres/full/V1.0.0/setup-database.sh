#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE cafeteria_db;
    GRANT ALL PRIVILEGES ON DATABASE cafeteria_db TO cafeteria_admin;
EOSQL

psql -U cafeteria_admin cafeteria_db < /tmp/psql/user/user.sql
psql -U cafeteria_admin cafeteria_db < /tmp/psql/food/food.sql
psql -U cafeteria_admin cafeteria_db < /tmp/psql/order/order.sql
