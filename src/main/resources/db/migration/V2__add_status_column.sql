-- Agregar columna status a la tabla chatgpt
ALTER TABLE chatgpt ADD COLUMN IF NOT EXISTS status CHAR(1) DEFAULT 'A';

-- Actualizar registros existentes
UPDATE chatgpt SET status = 'A' WHERE status IS NULL;

-- Agregar restricción para que status solo pueda ser 'A' o 'I'
ALTER TABLE chatgpt ADD CONSTRAINT chk_status CHECK (status IN ('A', 'I')); 