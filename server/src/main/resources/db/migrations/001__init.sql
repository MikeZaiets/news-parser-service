CREATE TABLE IF NOT EXISTS news
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    headline         VARCHAR(100)  NOT NULL,
    description      VARCHAR(1000) NOT NULL,
    publication_time TIMESTAMP     NOT NULL
);
