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

CREATE TRIGGER removeRecompensas
AFTER INSERT ON Penalizacao
BEGIN
DELETE FROM Recompensa WHERE Recompensa.idPrisioneiro = new.idPrisioneiro;
END;

CREATE TRIGGER removeObjeto
AFTER DELETE ON Recompensa
FOR EACH ROW
BEGIN
DELETE FROM ObjetoPessoal WHERE OLD.idRecompensa = ObjetoPessoal.idRecompensa;
END;

CREATE TRIGGER removeRecreio
AFTER DELETE ON Recompensa
FOR EACH ROW
BEGIN
DELETE FROM TempoDeRecreio WHERE OLD.idRecompensa = TempoDeRecreio.idRecompensa;
END;

SELECT nome, horaInicio, horaFim FROM Prisioneiro, Recompensa, TempoDeRecreio, Pessoa
WHERE Pessoa.idPessoa = Prisioneiro.idPessoa AND Prisioneiro.idPessoa = Recompensa.idPrisioneiro AND TempoDeRecreio.idRecompensa = Recompensa.idRecompensa;

--Reincidentes
SELECT nome, COUNT(Pena.idPena) as Penas FROM Pena INNER JOIN Pessoa
ON Pessoa.idPessoa = Pena.idPessoa
GROUP BY Pessoa.idPessoa
HAVING COUNT(Pena.idPena) > 1;

--