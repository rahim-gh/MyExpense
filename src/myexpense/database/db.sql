/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  rahim
 * Created: 3 Dec 2024
 */

-- Create the database for the Expense Application
CREATE DATABASE IF NOT EXISTS MyExpense;

-- Use the created database
USE MyExpense;

-- Create a table for user accounts
CREATE TABLE
    IF NOT EXISTS Accounts (
        account_id INT AUTO_INCREMENT PRIMARY KEY,
        email VARCHAR(255) NOT NULL UNIQUE,
        username VARCHAR(255) NOT NULL UNIQUE,
        password_hash VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Create a table for profiles associated with each account
CREATE TABLE
    IF NOT EXISTS Profiles (
        profile_id INT AUTO_INCREMENT PRIMARY KEY,
        account_id INT NOT NULL,
        profile_name VARCHAR(100) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (account_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
    );

-- Create a table for transactions related to each profile
CREATE TABLE
    IF NOT EXISTS Transactions (
        transaction_id INT AUTO_INCREMENT PRIMARY KEY,
        profile_id INT NOT NULL,
        transaction_type ENUM ('income', 'expense') NOT NULL,
        amount DECIMAL(10, 2) NOT NULL,
        transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (profile_id) REFERENCES Profiles (profile_id) ON DELETE CASCADE
    );