--impossibilita adicionar recompensas a prisioneiros castigados nos ultimos 30 dias
CREATE TRIGGER periodoDeNojo
BEFORE INSERT ON Recompensa
FOR EACH ROW
BEGIN
SELECT CASE
WHEN ( (SELECT COUNT(*) FROM PrisioneiroIncidente, Incidente
WHERE PrisioneiroIncidente.prisioneiro = new.idPrisioneiro AND PrisioneiroIncidente.incidente = Incidente.idIncidente AND Incidente.data >= datetime('now', '-30 days')) != 0)
THEN RAISE(ABORT, 'Prisioneiro não pode ser recompensado pois cometeu infração no ultimo mês')
END;
END;

--remove recompensas quando é aplicada uma penalizaçao
CREATE TRIGGER removeRecompensas
AFTER INSERT ON Penalizacao
BEGIN
DELETE FROM Recompensa WHERE Recompensa.idPrisioneiro = new.idPrisioneiro;
END;

--remove objeto recompensa quando a recompensa correspondente for removida
CREATE TRIGGER removeObjeto
AFTER DELETE ON Recompensa
FOR EACH ROW
BEGIN
DELETE FROM ObjetoPessoal WHERE OLD.idRecompensa = ObjetoPessoal.idRecompensa;
END;

-- remove tempo de recreio quando recompensa correspondente for removida
CREATE TRIGGER removeRecreio
AFTER DELETE ON Recompensa
FOR EACH ROW
BEGIN
DELETE FROM TempoDeRecreio WHERE OLD.idRecompensa = TempoDeRecreio.idRecompensa;
END;

--mostra horas dos recreios
SELECT nome, horaInicio, horaFim FROM Prisioneiro, Recompensa, TempoDeRecreio, Pessoa
WHERE Pessoa.idPessoa = Prisioneiro.idPessoa AND Prisioneiro.idPessoa = Recompensa.idPrisioneiro AND TempoDeRecreio.idRecompensa = Recompensa.idRecompensa;

--Mostra prisioneiros com mais que uma pena
SELECT nome, COUNT(Pena.idPena) as Penas FROM Pena INNER JOIN Pessoa
ON Pessoa.idPessoa = Pena.idPessoa
GROUP BY Pessoa.idPessoa
HAVING COUNT(Pena.idPena) > 1;

--Mostra celas com mais do que um prisioneiro
SELECT Local.nome, Local.funcao, COUNT(Prisioneiro.idPessoa) AS NumeroDePrisioneiros FROM Prisioneiro INNER JOIN Pessoa ON Prisioneiro.idPessoa = Pessoa.idPessoa INNER JOIN Cela ON Prisioneiro.cela = Cela.idLocal INNER JOIN Local ON Local.idLocal = Cela.idLocal
GROUP BY Cela.idLocal
HAVING COUNT(Prisioneiro.idPessoa) > 1;

--Mostra descriçao de todos os incidentes em que cada funcionário foi relator
SELECT Pessoa.nome, Incidente.descricao FROM Pessoa INNER JOIN Funcionario ON Pessoa.idPessoa = Funcionario.idPessoa INNER JOIN Incidente ON Incidente.relator = Funcionario.idPessoa;

--Mostra todos os prisioneiros cuja primeira sentença foi pronunciada este mês
SELECT Pessoa.nome, MIN(Pena.dataDeSentenca) as dataDePrimeiraSentenca, Pena.motivo FROM Pena INNER JOIN Pessoa ON Pena.idPessoa = Pessoa.idPessoa
GROUP BY (Pessoa.idPessoa)
HAVING MIN(Pena.dataDeSentenca) >= datetime('now', '-1 months');