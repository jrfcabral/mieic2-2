PRAGMA foreign_keys=ON;

INSERT INTO "Pessoa" VALUES(0, 'Antonio Fortes', '1994-05-22', 'cabo-vardiana');
INSERT INTO "Pessoa" VALUES(1, 'Nicomedes Lemos', '1996-10-14','cabo-verdiana');
INSERT INTO "Pessoa" VALUES(2, 'Joao Corca', '1979-02-12', 'portuguesa');
INSERT INTO "Pessoa" VALUES(3, 'Pedro Reco', '1985-11-04', 'portuguesa');
INSERT INTO "Pessoa" VALUES(4, 'Sara Garcia', '1980-08-29', 'espanhola');

INSERT INTO "Funcionario" VALUES(2, 'Guarda Prisional', '12:00:00', '20:00:00', '1992-04-20', 950);
INSERT INTO "Funcionario" VALUES(3, 'Funcionario de Limpeza', '09:00:00', '17:00:00', '2010-06-03', 800);
INSERT INTO "Funcionario" VALUES(4, 'Diretora Prisional', '09:30:00', '16:00:00', '2008-10-24', 2350);

INSERT INTO "Local" VALUES(0, 'Cela 0', 'Manter os meliantes seguros');
INSERT INTO "Local" VALUES(1, 'Cantina', 'Local onde os prisioneiros comem');

INSERT INTO "Cela" VALUES(0, 0);

INSERT INTO "Prisioneiro" VALUES(0, '2014-09-20', '2018-04-20', 0);
INSERT INTO "Prisioneiro" VALUES(1, '2014-09-20', '2016-07-20', 0);

INSERT INTO "Pena" VALUES(0, '2015-02-10', 'Tentativa de Assalto agravado', 0);
INSERT INTO "Pena" VALUES(1, '2015-02-10', 'Tentativa de Assalto agravado', 1);

INSERT INTO "Recompensa" VALUES(0, 'Tempo de Recreio', 1);
INSERT INTO "Recompensa" VALUES(1, 'Objeto Pessoal', 0);

INSERT INTO "TempoDeRecreio" VALUES(0, '14:30:00', '15:30:00');

INSERT INTO "ObjetoPessoal" VALUES(1, 'Moeda de 2 euros');

INSERT INTO "Incidente" VALUES(0, 'Prisioneiro Fortes tentou dar facada a Sr. Reco', '2015-02-14', 1, 2);

INSERT INTO "PrisioneiroIncidente" VALUES(0, 0);

INSERT INTO "Pena" VALUES(2, '2015-14-02', 'Tentativa de Agrassao', 0);

INSERT INTO "Penalizacao" VALUES(0, 0, 0, 'Por tentativa de agrassao, o prisioneiro Fortes esta condenado meio ano de prisao');

