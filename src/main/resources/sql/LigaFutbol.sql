DROP DATABASE IF EXISTS LigaFutbol;
CREATE DATABASE LigaFutbol;
Use LigaFutbol;

CREATE TABLE Equipos(
                        idequipo int auto_increment primary key,
                        codigo varchar(7),
                        nombre varchar(30) not null,
                        ciudad varchar(30) not null,
                        presupuesto int not null
);

INSERT INTO Equipos(codigo, nombre, ciudad, presupuesto) VALUES
                                                             ('EQ-001', 'Real Madrid', 'Madrid', 800000000),
                                                             ('EQ-002', 'FC Barcelona', 'Barcelona', 750000000),
                                                             ('EQ-003', 'Real Valladolid', 'Valladolid', 55000000);

CREATE TABLE Jugadores(
                          idjugador int auto_increment primary key,
                          idequipo int,
                          nombre varchar(50),
                          posicion varchar(20),
                          FOREIGN KEY (idequipo) REFERENCES Equipos(idequipo) ON DELETE CASCADE
);

INSERT INTO Jugadores(idequipo, nombre, posicion) VALUES
                                                      (1, 'Vinícius Júnior', 'Delantero'),
                                                      (1, 'Jude Bellingham', 'Centrocampista'),
                                                      (2, 'Robert Lewandowski', 'Delantero'),
                                                      (2, 'Pedri', 'Centrocampista'),
                                                      (3, 'Jordi Masip', 'Portero'),
                                                      (3, 'Mamadou Sylla', 'Delantero');

CREATE TABLE Partidos(
                         idpartido int unsigned auto_increment primary key,
                         idequipo int,
                         fechapartido date,
                         rival varchar(50),
                         espectadores int unsigned,
                         jugado varchar(2),
                         FOREIGN KEY (idequipo) REFERENCES Equipos(idequipo) ON DELETE CASCADE
);

INSERT INTO Partidos VALUES
                         (1, 1, '2025/10/25', 'Sevilla FC', 70000, 'Si'),
                         (2, 1, '2025/10/30', 'Valencia CF', 65000, 'No'),
                         (3, 2, '2025/06/30', 'Athletic Club', 80000, 'Si'),
                         (4, 3, '2025/11/03', 'Getafe CF', 20000, 'No');
