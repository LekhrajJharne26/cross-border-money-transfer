CREATE TABLE transaction_number_sequence (
    sequence_key VARCHAR(20) NOT NULL,
    sequence_value BIGINT NOT NULL,
    PRIMARY KEY (sequence_key)
);

INSERT INTO transaction_number_sequence (sequence_key, sequence_value) VALUES ('TXN', 0);

CREATE TABLE transactions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    transaction_number VARCHAR(20) NOT NULL,
    sender_id BIGINT NOT NULL,
    beneficiary_id BIGINT NOT NULL,
    banking_partner_id BIGINT NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    exchange_rate DECIMAL(19,6) NOT NULL,
    destination_amount DECIMAL(19,4) NOT NULL,
    purpose VARCHAR(150) NOT NULL,
    remarks VARCHAR(500) NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_transactions_number UNIQUE (transaction_number),
    CONSTRAINT fk_transactions_sender FOREIGN KEY (sender_id) REFERENCES users (id),
    CONSTRAINT fk_transactions_beneficiary FOREIGN KEY (beneficiary_id) REFERENCES beneficiaries (id),
    CONSTRAINT fk_transactions_partner FOREIGN KEY (banking_partner_id) REFERENCES banking_partners (id),
    INDEX idx_transactions_sender_id (sender_id),
    INDEX idx_transactions_status (status)
);
