CREATE TABLE client (
	id	 SERIAL,
	nome VARCHAR(512) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE resultstopic (
	amount	 DOUBLE PRECISION NOT NULL,
	currency	 VARCHAR(50) NOT NULL,
	client_id BIGINT NOT NULL
);

ALTER TABLE resultstopic ADD CONSTRAINT resultstopic_fk1 FOREIGN KEY (client_id) REFERENCES client(id);

