CREATE TABLE IF NOT EXISTS borrowings
(
    id      uuid primary key default uuid_generate_v4(),
    book_id uuid,
    instant text
);
