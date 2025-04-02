--mysql
CREATE TABLE IF NOT EXISTS technology (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(90) NOT NULL,
    UNIQUE (name)
);
--postgres
CREATE TABLE  IF NOT EXISTS technology(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(90) NOT NULL,
    UNIQUE (name)
);