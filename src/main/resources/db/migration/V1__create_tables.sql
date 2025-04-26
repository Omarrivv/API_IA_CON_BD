-- Crear tabla para ChatGPT
CREATE TABLE IF NOT EXISTS chatgpt
(
    id      SERIAL PRIMARY KEY NOT NULL,
    content TEXT,
    answer  TEXT
);

-- Crear tabla para Meta Llama
CREATE TABLE IF NOT EXISTS meta_llama
(
    id      SERIAL PRIMARY KEY NOT NULL,
    content TEXT,
    answer  TEXT
); 