#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Data processing for GTA5 vehicles scraperService.
This module handles data validation, cleaning and formatting.

Author: devTASE
"""

import logging
import re
from datetime import datetime
from decimal import Decimal, InvalidOperation

# Configure logging
logger = logging.getLogger(__name__)

def validate_required_fields(vehicle_data):
    """
    Validate that all required fields are present and not empty.
    
    Args:
        vehicle_data (dict): The vehicle data to validate
        
    Returns:
        bool: True if all required fields are valid, False otherwise
    """
    required_fields = [
        'name', 'manufacturer', 'vehicle_class', 'seats', 
        'top_speed', 'acceleration', 'braking', 'handling', 'price'
    ]
    
    for field in required_fields:
        if field not in vehicle_data or vehicle_data[field] is None:
            logger.error(f"Required field '{field}' is missing for vehicle {vehicle_data.get('name', 'unknown')}")
            return False
            
    return True

def clean_and_convert_data(vehicle_data):
    """
    Clean and convert data to the appropriate types.
    
    Args:
        vehicle_data (dict): The raw vehicle data
        
    Returns:
        dict: Cleaned and converted vehicle data
    """
    cleaned_data = {}
    
    # Process string fields (trim whitespace)
    for field in ['name', 'manufacturer', 'vehicle_class']:
        if field in vehicle_data:
            cleaned_data[field] = vehicle_data[field].strip()
    
    # Process numeric fields
    try:
        if 'seats' in vehicle_data:
            cleaned_data['seats'] = int(vehicle_data['seats'])
        
        for field in ['top_speed', 'acceleration', 'braking', 'handling']:
            if field in vehicle_data:
                # Convert percentage or string values to float
                value = vehicle_data[field]
                if isinstance(value, str):
                    # Remove percentage signs and convert to float
                    value = value.replace('%', '').strip()
                cleaned_data[field] = float(value)
        
        # Process price (might include currency symbols or commas)
        if 'price' in vehicle_data:
            price_str = vehicle_data['price']
            if isinstance(price_str, str):
                # Remove currency symbols, commas, and spaces
                price_str = re.sub(r'[$,£€\s]', '', price_str)
            cleaned_data['price'] = Decimal(price_str)
            
    except (ValueError, InvalidOperation) as e:
        logger.error(f"Error converting numeric data for vehicle {vehicle_data.get('name', 'unknown')}: {e}")
        return None
    
    # Process boolean fields
    if 'is_special_vehicle' in vehicle_data:
        value = vehicle_data['is_special_vehicle']
        if isinstance(value, str):
            cleaned_data['is_special_vehicle'] = value.lower() in ['yes', 'true', '1']
        else:
            cleaned_data['is_special_vehicle'] = bool(value)
    else:
        cleaned_data['is_special_vehicle'] = False
    
    # Process date fields
    if 'release_date' in vehicle_data and vehicle_data['release_date']:
        try:
            # Try different date formats
            date_str = vehicle_data['release_date']
            for fmt in ['%Y-%m-%d', '%d/%m/%Y', '%m/%d/%Y', '%B %d, %Y']:
                try:
                    date_obj = datetime.strptime(date_str, fmt).date()
                    cleaned_data['release_date'] = date_obj
                    break
                except ValueError:
                    continue
            
            # If no format matched, set to None
            if 'release_date' not in cleaned_data:
                cleaned_data['release_date'] = None
                
        except Exception as e:
            logger.warning(f"Could not parse release date '{vehicle_data['release_date']}' for vehicle {vehicle_data.get('name', 'unknown')}: {e}")
            cleaned_data['release_date'] = None
    else:
        cleaned_data['release_date'] = None
    
    # Process list fields
    if 'modifications' in vehicle_data and vehicle_data['modifications']:
        if isinstance(vehicle_data['modifications'], list):
            cleaned_data['modifications'] = [mod.strip() for mod in vehicle_data['modifications'] if mod.strip()]
        else:
            # If it's a string, try to split it
            cleaned_data['modifications'] = [mod.strip() for mod in vehicle_data['modifications'].split(',') if mod.strip()]
    else:
        cleaned_data['modifications'] = []
    
    return cleaned_data

def process_vehicle_data(raw_data):
    """
    Process and validate vehicle data.
    
    Args:
        raw_data (dict): Raw vehicle data from scraperService
        
    Returns:
        dict: Processed and validated vehicle data, or None if validation fails
    """
    # Clean and convert data
    processed_data = clean_and_convert_data(raw_data)
    
    # Return None if data conversion failed
    if processed_data is None:
        return None
    
    # Validate required fields
    if not validate_required_fields(processed_data):
        return None
    
    return processed_data

