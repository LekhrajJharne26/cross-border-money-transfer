CREATE TABLE banking_partners (
    id BIGINT NOT NULL AUTO_INCREMENT,
    partner_code VARCHAR(50) NOT NULL,
    partner_name VARCHAR(150) NOT NULL,
    api_url VARCHAR(500) NOT NULL,
    active BOOLEAN NOT NULL,
    country_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_banking_partners_code UNIQUE (partner_code),
    CONSTRAINT fk_banking_partners_country FOREIGN KEY (country_id) REFERENCES countries (id),
    INDEX idx_banking_partners_country_active (country_id, active)
);

INSERT INTO banking_partners (partner_code, partner_name, api_url, active, country_id)
SELECT 'IN_HDFC', 'HDFC Bank', 'https://api.hdfcbank.example', TRUE, id FROM countries WHERE code = 'IN'
UNION ALL SELECT 'IN_ICICI', 'ICICI Bank', 'https://api.icicibank.example', TRUE, id FROM countries WHERE code = 'IN'
UNION ALL SELECT 'NP_NABIL', 'Nabil Bank', 'https://api.nabilbank.example', TRUE, id FROM countries WHERE code = 'NP'
UNION ALL SELECT 'NP_GIME', 'Global IME', 'https://api.globalime.example', TRUE, id FROM countries WHERE code = 'NP'
UNION ALL SELECT 'PH_BDO', 'BDO', 'https://api.bdo.example', TRUE, id FROM countries WHERE code = 'PH'
UNION ALL SELECT 'PH_METROBANK', 'Metrobank', 'https://api.metrobank.example', TRUE, id FROM countries WHERE code = 'PH'
UNION ALL SELECT 'US_WF', 'Wells Fargo', 'https://api.wellsfargo.example', TRUE, id FROM countries WHERE code = 'US'
UNION ALL SELECT 'US_BOFA', 'Bank of America', 'https://api.bankofamerica.example', TRUE, id FROM countries WHERE code = 'US';
