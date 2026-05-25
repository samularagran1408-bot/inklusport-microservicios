CREATE DATABASE reports_db;
USE reports_db;

CREATE TABLE report (
    id CHAR(36) PRIMARY KEY,
    report_type VARCHAR(50) NOT NULL,
    date_range VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);
