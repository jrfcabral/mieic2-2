--impossibilita que se adicione recompensas a prisioneiros castigados nos ultimos 30 dias
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

--remove todas as recompensas de um quando lhe é aplicada uma penalizaçao
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
SELECT Local.nome, Local.funcao, COUNT(Prisioneiro.idPessoa) AS NumeroDePrisioneiros FROM Prisioneiro INNER JOIN Pessoa ON Prisioneiro.idPessoa = Pessoa.idPessoa INNER JOIN Cela ON Prisioneiro.cela = Cela.numero INNER JOIN Local ON Local.idLocal = Cela.idLocal
GROUP BY Cela.idLocal
HAVING COUNT(Prisioneiro.idPessoa) > 1;

--Mostra descriçao de todos os incidentes em que cada funcionário foi relator
SELECT Pessoa.nome, Incidente.descricao FROM Pessoa INNER JOIN Funcionario ON Pessoa.idPessoa = Funcionario.idPessoa INNER JOIN Incidente ON Incidente.relator = Funcionario.idPessoa
ORDER BY Pessoa.nome;

--Mostra todos os prisioneiros cuja primeira sentença foi pronunciada este mês
SELECT Pessoa.nome, MIN(Pena.dataDeSentenca) as dataDePrimeiraSentenca, Pena.motivo FROM Pena INNER JOIN Pessoa ON Pena.idPessoa = Pessoa.idPessoa
GROUP BY (Pessoa.idPessoa)
HAVING MIN(Pena.dataDeSentenca) >= datetime('now', '-1 months'); --max???

--Mostrar numero de objetos pessoais que os presos estao autorizados a ter em cada cela
SELECT Cela.numero AS NumeroCela, COUNT(ObjetoPessoal.idRecompensa) AS NumeroDeObjetos FROM Cela INNER JOIN Prisioneiro ON Prisioneiro.cela = Cela.numero INNER JOIN Recompensa ON Recompensa.idPrisioneiro = Prisioneiro.idPessoa INNER JOIN ObjetoPessoal ON ObjetoPessoal.idRecompensa = Recompensa.idRecompensa
GROUP BY (Cela.numero);

--Mostrar celas em que pelo menos 2 prisioneiros estiveram envolvidos no mesmo incidente
SELECT Cela.numero, COUNT(Pessoa.idPessoa) AS NumeroDePrisioneirosEnvolvidosNoMesmoIncidente FROM Pessoa INNER JOIN Prisioneiro ON Pessoa.idPessoa = Prisioneiro.idPessoa INNER JOIN Cela ON Prisioneiro.cela = Cela.numero INNER JOIN PrisioneiroIncidente ON PrisioneiroIncidente.prisioneiro = Prisioneiro.idPessoa 
INNER JOIN Incidente ON Incidente.idIncidente = PrisioneiroIncidente.incidente
GROUP BY idIncidente, cela
HAVING COUNT(Pessoa.idPessoa) > 1;

--Mostra todos os prisioneiros que não se envolvem num incidente há pelo menos um ano
SELECT Pessoa.nome FROM Pessoa INNER JOIN Prisioneiro ON Pessoa.idPessoa = Prisioneiro.idPessoa LEFT OUTER JOIN PrisioneiroIncidente ON PrisioneiroIncidente.prisioneiro = Pessoa.idPessoa LEFT OUTER JOIN Incidente ON Incidente.idIncidente = PrisioneiroIncidente.incidente
GROUP BY Pessoa.idPessoa
HAVING MIN(Incidente.data) < (datetime('now', '-1 years')) OR Incidente.data IS NULL; --max????

--Mostra todos os funcionarios cujo rendimento está acima da média do rendimento dos funcionarios da prisão e por quanto
SELECT Pessoa.nome, Funcionario.vencimento, Funcionario.cargo, Funcionario.vencimento - (SELECT AVG(Funcionario.vencimento) FROM Funcionario) AS VencimentoAcimaDaMedia FROM Pessoa INNER JOIN Funcionario ON Pessoa.idPessoa = Funcionario.idPessoa
WHERE (SELECT AVG(Funcionario.vencimento) FROM Funcionario) < Funcionario.vencimento;

--Mostrar todas as penas a que está associado um incidente ocorrido na prisão
SELECT Pena.idPena, Pessoa.nome, Pena.motivo FROM Pessoa INNER JOIN Prisioneiro ON Pessoa.idPessoa = Prisioneiro.idPessoa INNER JOIN PrisioneiroIncidente ON PrisioneiroIncidente.prisioneiro = Prisioneiro.idPessoa INNER JOIN Pena ON Pena.idPessoa = Prisioneiro.idPessoa INNER JOIN Penalizacao ON Penalizacao.idPessoa = Prisioneiro.idPessoa AND Penalizacao.idPena = Pena.idPena;

--mostrar todos os pares de prisioneiros que estao na cela 0
SELECT DISTINCT a.nome, b.nome FROM Pessoa a, Pessoa b, Prisioneiro
WHERE a.idPessoa < b.idPessoa AND ( SELECT cela FROM Prisioneiro WHERE a.idPessoa = idPessoa) == 0 AND ( SELECT cela FROM Prisioneiro WHERE b.idPessoa = idPessoa) = 0;


