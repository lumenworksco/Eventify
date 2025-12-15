-- =========================
-- Cities
-- =========================
CREATE TABLE IF NOT EXISTS cities (
    city_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    region VARCHAR(100),
    country VARCHAR(100) NOT NULL
);

-- =========================
-- Users
-- Each user follows exactly ONE city
-- =========================
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(150),
    event_preference VARCHAR(150),
    city_id INTEGER NOT NULL,
    CONSTRAINT fk_user_city
        FOREIGN KEY (city_id)
        REFERENCES cities(city_id)
        ON DELETE RESTRICT
);

-- =========================
-- Venues
-- Each venue belongs to ONE city
-- =========================
CREATE TABLE IF NOT EXISTS venues (
    venue_id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(200),
    capacity INTEGER CHECK (capacity > 0),
    city_id INTEGER NOT NULL,
    CONSTRAINT fk_venue_city
        FOREIGN KEY (city_id)
        REFERENCES cities(city_id)
        ON DELETE CASCADE
);

-- =========================
-- Events
-- =========================
CREATE TABLE IF NOT EXISTS events (
    event_id SERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    event_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CONSTRAINT chk_event_time
        CHECK (end_time > start_time)
);

-- =========================
-- Event Descriptions (1:1)
-- =========================
CREATE TABLE IF NOT EXISTS event_descriptions (
    description_id SERIAL PRIMARY KEY,
    event_id INTEGER NOT NULL UNIQUE,
    event_type VARCHAR(100),
    featured_artists TEXT,
    ticket_purchase_link TEXT,
    extra_description TEXT,
    CONSTRAINT fk_description_event
        FOREIGN KEY (event_id)
        REFERENCES events(event_id)
        ON DELETE CASCADE
);

-- =========================
-- Event ↔ Venue (Many-to-Many)
-- An event MUST have at least one venue (enforced logically)
-- =========================
CREATE TABLE IF NOT EXISTS event_venues (
    event_id INTEGER NOT NULL,
    venue_id INTEGER NOT NULL,
    PRIMARY KEY (event_id, venue_id),
    CONSTRAINT fk_ev_event
        FOREIGN KEY (event_id)
        REFERENCES events(event_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ev_venue
        FOREIGN KEY (venue_id)
        REFERENCES venues(venue_id)
        ON DELETE CASCADE
);
