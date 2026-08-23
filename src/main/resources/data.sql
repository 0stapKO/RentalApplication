INSERT INTO items (name, description, category, inventory_number, status)
VALUES ('MacBook Pro', 'Apple laptop 2023', 'ELECTRONICS', 'INV-001', 'AVAILABLE') ON CONFLICT DO NOTHING;

INSERT INTO items (name, description, category, inventory_number, status)
VALUES ('Clean Code', 'Book by Robert Martin', 'BOOKS', 'INV-002', 'AVAILABLE') ON CONFLICT DO NOTHING;

INSERT INTO users (first_name, last_name, email, password, role, created_at)
VALUES ('admin', 'Smith', 'admin@email.com', '$2a$12$MXajzzs/RR2dN7ra3VbnwuMNpaboVOOlqdVaEbCw9BBe4sJ7B9NgW', 'ROLE_ADMIN', CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;

INSERT INTO users (first_name, last_name, email, password, role, created_at)
VALUES ('John', 'Doe', 'john@email.com', '$2a$12$oCjvwB1aVjkIV6zSuZi3KeiH2ZhbZefAgkuSfSCz.JV93UfyD/XNy', 'ROLE_USER', CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;