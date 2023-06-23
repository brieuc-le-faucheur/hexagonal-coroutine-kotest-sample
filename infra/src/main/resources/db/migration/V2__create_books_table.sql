CREATE TABLE IF NOT EXISTS books
(
    id               uuid primary key default uuid_generate_v4(),
    title            text,
    author_id        uuid,
    is_borrowed      boolean,
    borrowed_instant text
);
