CREATE TABLE gemini (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    answer TEXT NOT NULL,
    status VARCHAR(1) NOT NULL DEFAULT 'A'
); 