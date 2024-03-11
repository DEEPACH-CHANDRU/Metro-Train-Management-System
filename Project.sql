-- Create database
CREATE DATABASE IF NOT EXISTS metro_db;

-- Switch to the newly created database
USE metro_db;

-- Create passengers table
CREATE TABLE IF NOT EXISTS passengers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    source VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME,
    exit_status CHAR(1) DEFAULT 'N'
);
