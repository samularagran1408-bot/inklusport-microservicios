CREATE DATABASE IF NOT EXISTS sports_events_ms;
USE sports_events_ms;

CREATE TABLE sport (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE disability (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sport_disability (
    id CHAR(36) PRIMARY KEY,
    sport_id CHAR(36) NOT NULL,
    disability_id CHAR(36) NOT NULL,
    FOREIGN KEY (sport_id) REFERENCES sport(id) ON DELETE CASCADE,
    FOREIGN KEY (disability_id) REFERENCES disability(id) ON DELETE CASCADE,
    UNIQUE KEY uk_sport_disability (sport_id, disability_id)
);

CREATE TABLE event (
    id CHAR(36) PRIMARY KEY,
    sport_id CHAR(36) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    max_participants INT NOT NULL,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sport_id) REFERENCES sport(id)
);

CREATE TABLE event_registration (
    id CHAR(36) PRIMARY KEY,
    event_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    status VARCHAR(50) DEFAULT 'CONFIRMED',
    registered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    UNIQUE KEY uk_event_user (event_id, user_id)
);

CREATE TABLE event_attendance (
    id CHAR(36) PRIMARY KEY,
    registration_id CHAR(36) NOT NULL,
    attended_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (registration_id) REFERENCES event_registration(id) ON DELETE CASCADE
);

CREATE TABLE waitlist (
    id CHAR(36) PRIMARY KEY,
    event_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    position INT NOT NULL,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    UNIQUE KEY uk_waitlist_event_user (event_id, user_id)
);
