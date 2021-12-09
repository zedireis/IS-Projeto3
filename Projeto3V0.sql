CREATE TABLE client (
	id	 SERIAL,
	nome VARCHAR(512) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE client_payments (
	amount	 DOUBLE PRECISION NOT NULL,
	client_id BIGINT NOT NULL
);

CREATE TABLE client_credits (
	amount	 DOUBLE PRECISION NOT NULL,
	client_id BIGINT NOT NULL
);

CREATE TABLE total_payments (
	id	 BIGINT,
	amount DOUBLE PRECISION NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE total_credits (
	id	 BIGINT,
	amount DOUBLE PRECISION NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE balance (
	amount	 DOUBLE PRECISION NOT NULL,
	client_id BIGINT NOT NULL
);

CREATE TABLE last_month_bill (
	amount	 DOUBLE PRECISION NOT NULL,
	client_id BIGINT NOT NULL
);

CREATE TABLE currency (
	id	 SERIAL,
	name	 VARCHAR(50) UNIQUE,
	conversion DOUBLE PRECISION NOT NULL,
	PRIMARY KEY(id)
);

ALTER TABLE client_payments ADD CONSTRAINT client_payments_fk1 FOREIGN KEY (client_id) REFERENCES client(id);
ALTER TABLE client_credits ADD CONSTRAINT client_credits_fk1 FOREIGN KEY (client_id) REFERENCES client(id);
ALTER TABLE balance ADD CONSTRAINT balance_fk1 FOREIGN KEY (client_id) REFERENCES client(id);
ALTER TABLE last_month_bill ADD CONSTRAINT last_month_bill_fk1 FOREIGN KEY (client_id) REFERENCES client(id);

