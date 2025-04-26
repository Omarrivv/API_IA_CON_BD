-- Crear tabla para ChatGPT
CREATE TABLE IF NOT EXISTS chatgpt
(
    id      SERIAL PRIMARY KEY NOT NULL,
    content VARCHAR(200),
    answer  VARCHAR(800)
);

-- Crear tabla para Meta Llama
CREATE TABLE IF NOT EXISTS meta_llama
(
    id      SERIAL PRIMARY KEY NOT NULL,
    content VARCHAR(200),
    answer  VARCHAR(800)
); 