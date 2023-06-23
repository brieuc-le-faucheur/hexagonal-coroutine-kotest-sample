CREATE TABLE IF NOT EXISTS authors
(
    id         uuid primary key default uuid_generate_v4(),
    first_name text,
    last_name  text
);
