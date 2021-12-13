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
	client_email VARCHAR(512) UNIQUE NOT NULL,
	modified TIMESTAMP
);

CREATE TABLE two_month_payments (
	amount	 DOUBLE PRECISION NOT NULL,
	client_email VARCHAR(512) UNIQUE NOT NULL,
	modified TIMESTAMP
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
ALTER TABLE two_month_payments ADD CONSTRAINT two_month_payments_fk1 FOREIGN KEY (client_email) REFERENCES client(email);
ALTER TABLE client ADD CONSTRAINT cliente_fk1 FOREIGN KEY (manager_email) REFERENCES manager(email);

create or replace function add_timestamp() returns trigger
language 'plpgsql'
as $$
BEGIN
    NEW.modified = now();
    RETURN NEW;   
END;
$$;

CREATE TRIGGER update_customer_bill BEFORE INSERT OR UPDATE ON last_month_bill FOR EACH ROW EXECUTE PROCEDURE  add_timestamp();
CREATE TRIGGER update_customer_payments BEFORE INSERT OR UPDATE ON two_month_payments FOR EACH ROW EXECUTE PROCEDURE  add_timestamp();

