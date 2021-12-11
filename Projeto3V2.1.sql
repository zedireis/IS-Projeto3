CREATE TABLE client (
	id SERIAL UNIQUE,
	email VARCHAR(512) UNIQUE,
	nome	 VARCHAR(512) NOT NULL,
	manager_email VARCHAR(512) NOT NULL,
	PRIMARY KEY(email)
);

CREATE TABLE manager (
	id SERIAL UNIQUE,
	email	 VARCHAR(512),
	nome	 VARCHAR(512) NOT NULL,
	PRIMARY KEY(email)
);

CREATE TABLE client_payments (
	amount	 DOUBLE PRECISION NOT NULL,
	client_email VARCHAR(512) UNIQUE NOT NULL
);

CREATE TABLE client_credits (
	amount	 DOUBLE PRECISION NOT NULL,
	client_email VARCHAR(512) UNIQUE NOT NULL
);

CREATE TABLE total (
	client_email VARCHAR(512),
	amount DOUBLE PRECISION NOT NULL,
	PRIMARY KEY(client_email)
);

CREATE TABLE balance (
	amount	 DOUBLE PRECISION NOT NULL,
	client_email VARCHAR(512) UNIQUE NOT NULL
);

CREATE TABLE last_month_bill (
	amount	 DOUBLE PRECISION NOT NULL,
	client_email VARCHAR(512) UNIQUE NOT NULL
);

CREATE TABLE manager_revenue (
	amount	 DOUBLE PRECISION NOT NULL,
	client_email VARCHAR(512) UNIQUE NOT NULL
);

CREATE TABLE currency (
	id	 SERIAL,
	name	 VARCHAR(50) UNIQUE,
	conversion DOUBLE PRECISION NOT NULL,
	PRIMARY KEY(id)
);



ALTER TABLE client_payments ADD CONSTRAINT client_payments_fk1 FOREIGN KEY (client_email) REFERENCES client(email);
ALTER TABLE client_credits ADD CONSTRAINT client_credits_fk1 FOREIGN KEY (client_email) REFERENCES client(email);
ALTER TABLE balance ADD CONSTRAINT balance_fk1 FOREIGN KEY (client_email) REFERENCES client(email);
ALTER TABLE last_month_bill ADD CONSTRAINT last_month_bill_fk1 FOREIGN KEY (client_email) REFERENCES client(email);
ALTER TABLE manager_revenue ADD CONSTRAINT manager_revenue_fk1 FOREIGN KEY (client_email) REFERENCES manager(email);
ALTER TABLE client ADD CONSTRAINT cliente_fk1 FOREIGN KEY (manager_email) REFERENCES manager(email);

