CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) DEFAULT 'noname user',
    email VARCHAR(512) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(512) DEFAULT 'no description',
    is_booked BOOLEAN DEFAULT false,
    owner_id BIGINT REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS booking (
    id SERIAL PRIMARY KEY,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    booking_state INT DEFAULT 0,
    owner_id BIGINT REFERENCES users(id),
    item_id BIGINT REFERENCES items(id)
    );

CREATE TABLE IF NOT EXISTS item_requests (
    id SERIAL PRIMARY KEY,
    req_item_name VARCHAR(255) NOT NULL,
    owner_id BIGINT REFERENCES users(id),
    resp_item_id BIGINT REFERENCES items(id)
    );

CREATE TABLE IF NOT EXISTS feedbacks (
    id SERIAL PRIMARY KEY,
    description VARCHAR(200) NOT NULL,
    feedback_date TIMESTAMP NOT NULL,
    owner_id BIGINT REFERENCES users(id),
    item_id BIGINT REFERENCES items(id)
    );
