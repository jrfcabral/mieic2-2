PRAGMA foreign_keys=ON;

CREATE TABLE Pessoa(
idPessoa INTEGER PRIMARY KEY,
nome NVARCHAR(50) NOT NULL,
dataNascimento NVARCHAR(10),
nacionalidade NVARCHAR(50));

CREATE TABLE Prisioneiro(
idPessoa INTEGER PRIMARY KEY REFERENCES Pessoa(idPessoa),
dataDeEntrada NVARCHAR(10) NOT NULL,
dataDeSaida NVARCHAR(10) NOT NULL,
cela INTEGER REFERENCES Cela(numero) NOT NULL,
CHECK(julianday(dataDeEntrada) < julianday(dataDeSaida)));

CREATE TABLE Funcionario(
idPessoa INTEGER PRIMARY KEY REFERENCES Pessoa(idPessoa),
cargo NVARCHAR(50) NOT NULL,
horaDeEntrada NVARCHAR(8),
horaDeSaida NVARCHAR(8),
dataContratacao NVARCHAR(10) NOT NULL,
vencimento INTEGER NOT NULL,
CHECK(julianday(horaDeEntrada) < julianday(horaDeSaida)));

CREATE TABLE TempoDeRecreio(
idRecompensa INTEGER PRIMARY KEY REFERENCES Recompensa(idRecompensa),
horaInicio NVARCHAR(8) NOT NULL,
horaFim NVARCHAR(8) NOT NULL);

CREATE TABLE ObjetoPessoal(
idRecompensa INTEGER PRIMARY KEY REFERENCES Recompensa(idRecompensa),
descricao NVARCHAR(50));

CREATE TABLE Local(
idLocal INTEGER PRIMARY KEY,
nome NVARCHAR(50),
funcao NVARCHAR(100));

CREATE TABLE Pena(
idPena INTEGER PRIMARY KEY,
dataDeSentenca NVARCHAR(10),
motivo NVARCHAR(100),
idPessoa INTEGER REFERENCES Prisioneiro(idPessoa));

CREATE TABLE Cela(
numero INTEGER PRIMARY KEY,
idLocal INTEGER REFERENCES Local(idLocal));

CREATE TABLE Incidente(
idIncidente INTEGER PRIMARY KEY,
descricao NVARCHAR(100),
data NVARCHAR(10) NOT NULL,
idLocal INTEGER REFERENCES Local(idLocal),
relator INTEGER REFERENCES Funcionario(idPessoa) NOT NULL);

CREATE TABLE PrisioneiroIncidente(
prisioneiro INTEGER REFERENCES Prisioneiro(idPessoa) NOT NULL,
incidente INTEGER REFERENCES Incidente(idIncidente) NOT NULL,
PRIMARY KEY(prisioneiro, incidente));

CREATE TABLE Penalizacao(
idPena INTEGER REFERENCES Pena(idPena),
idIncidente INTEGER REFERENCES Incidente(idIncidente),
idPessoa INTEGER REFERENCES Prisioneiro(idPessoa),
descricao NVARCHAR(100),
PRIMARY KEY(idPena, idPessoa));

CREATE TABLE Recompensa(
idRecompensa INTEGER PRIMARY KEY,
motivo NVARCHAR(100),
idPrisioneiro INTEGER REFERENCES Prisioneiro(idPessoa)),
idPessoa INTEGER REFERENCES Prisioneiro(idPessoa);

