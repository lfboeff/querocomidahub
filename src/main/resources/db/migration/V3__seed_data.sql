-- Passwords (BCrypt, cost 10):
-- luis         → verde123
-- natalia      → azul1234
-- maria.daiana → amarelo1
-- maria.nelci  → laranja1
-- jose.flavio  → roxo1234
INSERT INTO users (type, name, email, login, password, last_login_at, created_at, last_modified_at) VALUES
('RESTAURANT_OWNER',    'Luís',             'luis@email.com',           'luis',         '$2b$10$KwHArZTuxtQ0akgbGXmvAuVT1Jhe9AyCVOd00TzFn0SbK2r0TdpzG',     '2026-05-01T10:23:23',  '2026-04-01T11:14:59',  '2026-04-10T08:52:22'   ),
('RESTAURANT_OWNER',    'Natália',          'natalia@email.com',        'natalia',      '$2b$10$8.BiBUgK6XuqF60lQlDTEeSIWDGT1TYaHymDC3PiyK5DlHHpm7fOu',     '2026-05-02',           '2026-04-02',           '2026-04-02'            ),
('CUSTOMER',            'Maria Daiana',     'maria.daiana@email.com',   'maria.daiana', '$2b$10$HIl4mIBOOel1QlErpFCsYu7Xa7YyBK8L1wadmJVwltie3Znik/Jpq',     '2026-05-01',           '2026-04-06',           '2026-04-06'            ),
('CUSTOMER',            'Maria Nelci',      'maria.nelci@email.com',    'maria.nelci',  '$2b$10$ZiS6/.hHvpSNKwWGhK217.BJDULiHLIBvGVKMc/grXtsJ9mfwZj5y',     '2026-05-03',           '2026-04-08',           '2026-04-15'            ),
('CUSTOMER',            'José Flávio',      'jose.flavio@email.com',    'jose.flavio',  '$2b$10$8XqK8/ChQD2r2pYjm2ewqemmCF.4R.O2NPw8VtXPn4cA1GiSmBkeS',     '2026-05-04',           '2026-04-08',           '2026-04-22'            );

INSERT INTO addresses (user_id, zip_code, country_code, state_code, city, street, number, complement, created_at, last_modified_at) VALUES
(1, '90035-190', 'BR', 'RS', 'Porto Alegre',    'Avenida Feriado',      '1500',     NULL,       '2026-04-01T11:14:59',  '2026-04-10T08:52:22'   ),
(2, '90035-190', 'BR', 'RS', 'Porto Alegre',    'Avenida Feriado',      '723',      NULL,       '2026-04-02',           '2026-04-23'            ),
(3, '93125-000', 'BR', 'RS', 'Novo Hamburgo',   'Rua Sexta Feira',      '20A',      'Apto 501', '2026-04-06',           '2026-04-06'            ),
(4, '93100-000', 'BR', 'RS', 'São Leopoldo',    'Rua Sábado Domingo',   NULL,       NULL,       '2026-04-08',           '2026-04-15'            ),
(5, '93100-000', 'BR', 'RS', 'São Leopoldo',    'Rua Sábado Domingo',   NULL,       NULL,       '2026-04-08',           '2026-04-23'            );
