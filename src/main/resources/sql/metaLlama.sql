CREATE TABLE IF NOT EXISTS meta_llama
(
    id  SERIAL PRIMARY KEY NOT NULL,
    question  VARCHAR(200),
    answer VARCHAR(800)
    );