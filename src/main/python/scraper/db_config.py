#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Database configuration and connection handling for the GTA5 vehicles scraperService.
This module handles database connections and provides functions for data persistence.

Author: devTASE
"""

import os
import logging
import psycopg2
from psycopg2 import sql
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()

# Configure logging
logger = logging.getLogger(__name__)

# Database connection parameters
DB_CONFIG = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'port': os.getenv('DB_PORT', '5432'),
    'database': os.getenv('DB_NAME', 'gta5vehicles'),
    'user': os.getenv('DB_USER', 'postgres'),
    'password': os.getenv('DB_PASSWORD', 'password')
}

def get_connection():
    """
    Establish a connection to the PostgreSQL database.
    
    Returns:
        psycopg2.connection: A connection object to the database.
    """
    try:
        conn = psycopg2.connect(**DB_CONFIG)
        logger.info("Database connection established successfully")
        return conn
    except psycopg2.Error as e:
        logger.error(f"Error connecting to the database: {e}")
        raise

def insert_vehicle(vehicle_data):
    """
    Insert a vehicle into the database.
    
    Args:
        vehicle_data (dict): Dictionary containing vehicle data.
        
    Returns:
        bool: True if the insertion was successful, False otherwise.
    """
    # SQL statement to insert vehicle data
    insert_query = """
    INSERT INTO vehicle (
        name, manufacturer, vehicle_class, seats, top_speed, 
        acceleration, braking, handling, price, release_date, 
        is_special_vehicle
    ) VALUES (
        %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
    ) RETURNING id;
    """
    
    # SQL statement to insert modifications
    insert_mods_query = """
    INSERT INTO vehicle_modifications (
        vehicle_id, modifications
    ) VALUES (%s, %s);
    """
    
    conn = None
    try:
        conn = get_connection()
        with conn.cursor() as cursor:
            # Check if vehicle already exists to avoid duplicates
            cursor.execute("SELECT id FROM vehicle WHERE name = %s AND manufacturer = %s", 
                          (vehicle_data['name'], vehicle_data['manufacturer']))
            existing = cursor.fetchone()
            
            if existing:
                logger.info(f"Vehicle {vehicle_data['name']} already exists in the database with ID {existing[0]}")
                return False
            
            # Insert vehicle
            cursor.execute(insert_query, (
                vehicle_data['name'],
                vehicle_data['manufacturer'],
                vehicle_data['vehicle_class'],
                vehicle_data['seats'],
                vehicle_data['top_speed'],
                vehicle_data['acceleration'],
                vehicle_data['braking'],
                vehicle_data['handling'],
                vehicle_data['price'],
                vehicle_data['release_date'],
                vehicle_data['is_special_vehicle']
            ))
            
            vehicle_id = cursor.fetchone()[0]
            
            # Insert modifications if present
            if 'modifications' in vehicle_data and vehicle_data['modifications']:
                for mod in vehicle_data['modifications']:
                    cursor.execute(insert_mods_query, (vehicle_id, mod))
            
            conn.commit()
            logger.info(f"Vehicle {vehicle_data['name']} inserted successfully with ID {vehicle_id}")
            return True
            
    except psycopg2.Error as e:
        if conn:
            conn.rollback()
        logger.error(f"Database error when inserting vehicle {vehicle_data.get('name', 'unknown')}: {e}")
        return False
    finally:
        if conn:
            conn.close()

def bulk_insert_vehicles(vehicles_data):
    """
    Insert multiple vehicles into the database in a single transaction.
    
    Args:
        vehicles_data (list): List of dictionaries containing vehicle data.
        
    Returns:
        tuple: (success_count, fail_count)
    """
    conn = None
    success_count = 0
    fail_count = 0
    
    try:
        conn = get_connection()
        with conn:  # This will automatically handle commit/rollback
            for vehicle_data in vehicles_data:
                if insert_vehicle(vehicle_data):
                    success_count += 1
                else:
                    fail_count += 1
                    
        logger.info(f"Bulk insert completed: {success_count} vehicles added, {fail_count} failed")
        return success_count, fail_count
        
    except psycopg2.Error as e:
        logger.error(f"Error during bulk insert operation: {e}")
        return success_count, fail_count
    finally:
        if conn:
            conn.close()

