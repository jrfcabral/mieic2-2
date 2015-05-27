#/bin/bash
echo "A correr o exemplo do segundo trabalho, para acompanhar os registos \"tail -f exemplo.log\""
./bin/balcao /exemplo 5 &
echo "Balcao 1 criado"
sleep 1
./bin/ger_cl /exemplo 25 &
echo "Gerados 25 clientes"
sleep 5
./bin/balcao /exemplo 15 &
echo "Balcao 2 criado"
./bin/ger_cl /exemplo 10 &
echo "Gerados 10 clientes"
./bin/balcao /exemplo 15 &
echo "Balcao 3 criado"
./bin/ger_cl /exemplo 50 &
echo "Gerados 50 clientes"
sleep 15
echo "O exemplo deve ter terminado. Registo em exemplo.log, estatisticas geradas em exemploStatistics.txt"
