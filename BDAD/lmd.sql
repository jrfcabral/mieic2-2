PRAGMA foreign_keys=ON;

INSERT INTO "Pessoa" VALUES(0, 'Antonio Fortes', '1994-05-22', 'cabo-verdiana');
INSERT INTO "Pessoa" VALUES(1, 'Nicomedes Lemos', '1996-10-14','cabo-verdiana');
INSERT INTO "Pessoa" VALUES(2, 'Joao Corca', '1979-02-12', 'portuguesa');
INSERT INTO "Pessoa" VALUES(3, 'Pedro Reco', '1985-11-04', 'portuguesa');
INSERT INTO "Pessoa" VALUES(4, 'Sara Garcia', '1980-08-29', 'espanhola');
INSERT INTO "Pessoa" VALUES(5, 'Julio Magalhaes', '1980-09-28', 'portuguesa');
INSERT INTO "Pessoa" VALUES(6, 'Luisa Silvä', '1960-09-17', 'dinamarquesa');
INSERT INTO "Pessoa" VALUES(7, 'Joel Matos', '1990-01-12', 'portuguesa');
INSERT INTO "Pessoa" VALUES(8, 'Jacinto Moniz', '1990-01-12', 'portuguesa');
INSERT INTO "Pessoa" VALUES(9, 'Passos Dias', '1980-01-13', 'portuguesa');
INSERT INTO "Pessoa" VALUES(10, 'Aguiar Mota', '1990-01-14', 'portuguesa');

INSERT INTO "Funcionario" VALUES(2, 'Guarda Prisional', '12:00:00', '20:00:00', '1992-04-20', 950);
INSERT INTO "Funcionario" VALUES(3, 'Funcionario de Limpeza', '09:00:00', '17:00:00', '2010-06-03', 800);
INSERT INTO "Funcionario" VALUES(4, 'Diretora Prisional', '09:30:00', '16:00:00', '2008-10-24', 2350);
INSERT INTO "Funcionario" VALUES(5, 'Guarda Prisional', '20:00:00', '04:00:00', '2000-10-22', 1450);
INSERT INTO "Funcionario" VALUES(6, 'Guarda Prisional', '04:00:00', '12:00:00', '1987-01-12', 1830);



INSERT INTO "Local" VALUES(0, 'Cela 0', 'Manter os prisioneiros seguros');
INSERT INTO "Local" VALUES(1, 'Cantina', 'Local onde os prisioneiros comem');
INSERT INTO "Local" VALUES(2, 'Cela 1', 'Manter mais prisioneiros seguros');
INSERT INTO "Local" VALUES(3, 'Cela 2', 'Manter ainda outros prisioneiros seguros');
INSERT INTO "Local" VALUES(4, 'Cela 3', 'Manter restantes prisioneiros seguros');

INSERT INTO "Cela" VALUES(0, 0);
INSERT INTO "Cela" VALUES(1,2);
INSERT INTO "Cela" VALUES(2,3);
INSERT INTO "Cela" VALUES(3,4);


INSERT INTO "Prisioneiro" VALUES(0, '2014-09-20', '2018-04-20', 0);
INSERT INTO "Prisioneiro" VALUES(1, '2014-09-20', '2016-07-20', 0);
INSERT INTO "Prisioneiro" VALUES(7, '2004-06-03', '2029-05-03', 3);
INSERT INTO "Prisioneiro" VALUES(8, '2014-06-03', '2019-05-03', 3);
INSERT INTO "Prisioneiro" VALUES(9, '2013-02-12', '2016-03-29', 2);
INSERT INTO "Prisioneiro" VALUES(10, '2013-02-12', '2016-03-29', 2);

INSERT INTO "Pena" VALUES(0, '2015-02-10', 'Tentativa de Assalto agravado', 0);
INSERT INTO "Pena" VALUES(1, '2015-02-10', 'Tentativa de Assalto agravado', 1);
INSERT INTO "Pena" VALUES(3, '2004-05-03', 'Fogo Posto no parque da FEUP', 7);
INSERT INTO "Pena" VALUES(4, '2013-05-03', 'Elaborou lista VIP no Fisco', 8);
INSERT INTO "Pena" VALUES(5, '2013-01-13', 'Atropelamento e fuga', 9);
INSERT INTO "Pena" VALUES(6, '2013-01-13', 'Atropelamento e fuga', 10);

INSERT INTO "Recompensa" VALUES(0, 'Tempo de Recreio', 1);
INSERT INTO "Recompensa" VALUES(1, 'Objeto Pessoal', 0);
INSERT INTO "Recompensa" VALUES(2, 'Objeto Pessoal', 7);
INSERT INTO "Recompensa" VALUES(3, 'Tempo de Recreio', 7); 

INSERT INTO "TempoDeRecreio" VALUES(0, '14:30:00', '15:30:00');
INSERT INTO "TempoDeRecreio" VALUES(3, '12:30:00', '13:30:00');

INSERT INTO "ObjetoPessoal" VALUES(1, 'Moeda de 2 euros');
INSERT INTO "ObjetoPessoal" VALUES(2, 'Caixa de fosforos');

INSERT INTO "Incidente" VALUES(0, 'Prisioneiro Fortes tentou dar uma facada a Sr. Reco', '2015-02-14', 1, 2);
INSERT INTO "Incidente" VALUES(1, 'Prisioneiro Matos pegou fogo à Cantina, senhor Julio apagou', '2014-02-12', 1, 5);
INSERT INTO "Incidente" VALUES(2, 'Prisioneiros Mota e Dias passaram toda a noite a imitar um motociclo', '2014-03-22', 2, 2);

INSERT INTO "PrisioneiroIncidente" VALUES(0, 0);
INSERT INTO "PrisioneiroIncidente" VALUES(9,2);
INSERT INTO "PrisioneiroIncidente" VALUES(10,2);
INSERT INTO "PrisioneiroIncidente" VALUES(7,1);

INSERT INTO "Pena" VALUES(2, '2015-02-02', 'Tentativa de Agressao', 0);
INSERT INTO "Pena" VALUES(7, '2015-03-03', 'Fogo Posto na prisao', 7);

INSERT INTO "Penalizacao" VALUES(2, 0, 0, 'Por tentativa de agressao, o prisioneiro Fortes esta condenado meio ano de prisao');
INSERT INTO "Penalizacao" VALUES(7, 1, 7, 'Por nova tentativa de fogo posto, condenado a mais dois meses de prisao e proibido de comprar fosforos para o resto da vida');


