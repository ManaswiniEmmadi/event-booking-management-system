-- Step 1: Create Database
CREATE DATABASE IF NOT EXISTS event_booking_management_system;

-- Step 2: Use the Database
USE event_booking_management_system;

-- Module 1: Authentication & User Profile

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('User', 'Organizer') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login DATETIME DEFAULT NULL
);

CREATE TABLE user_profiles (
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    full_name VARCHAR(100),
    phone_number VARCHAR(20),
    gender VARCHAR(10),
    date_of_birth DATE,
    profile_picture_url VARCHAR(255),
    address TEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Module 2: Event Management (Organizer)

CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    organizer_id INT NOT NULL,
    event_name VARCHAR(150) NOT NULL,
    description TEXT,
    date_time DATETIME NOT NULL,
    location VARCHAR(255),
    capacity INT,
    status ENUM('Upcoming', 'Ongoing', 'Completed', 'Cancelled') DEFAULT 'Upcoming',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organizer_id) REFERENCES users(user_id)
);

-- Module 3: Booking Management (User)

CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    ticket_category_id INT NOT NULL,
    number_of_seats INT NOT NULL,
    booking_status ENUM('Pending', 'Confirmed', 'Cancelled') DEFAULT 'Pending',
    payment_id INT,
    booking_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id)
    -- payment_id will be added after `payments` table creation
);

-- Module 4: Seating & Ticket Types

CREATE TABLE ticket_categories (
    ticket_category_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    category_name VARCHAR(50),
    price DECIMAL(10, 2),
    total_seats INT,
    available_seats INT,
    benefits TEXT,
    FOREIGN KEY (event_id) REFERENCES events(event_id)
);

CREATE TABLE seating_layouts (
    layout_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    seating_map_url VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(event_id)
);

-- Module 5: Payment and Invoicing

CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT,
    user_id INT NOT NULL,
    payment_method ENUM('Card', 'UPI', 'NetBanking') NOT NULL,
    transaction_id VARCHAR(100),
    payment_status ENUM('Success', 'Failed', 'Refunded') DEFAULT 'Success',
    amount DECIMAL(10, 2),
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Add foreign key now that payments table exists
ALTER TABLE bookings ADD FOREIGN KEY (payment_id) REFERENCES payments(payment_id);

-- Module 6: Reports and Dashboards

CREATE TABLE reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    organizer_id INT NOT NULL,
    report_type ENUM('Revenue', 'Booking Count', 'Popular Events') NOT NULL,
    generated_on DATETIME DEFAULT CURRENT_TIMESTAMP,
    filter_applied TEXT,
    data_snapshot TEXT,
    exported_file_url VARCHAR(255),
    FOREIGN KEY (organizer_id) REFERENCES users(user_id)
);
