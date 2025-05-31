#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Main script for the GTA5 vehicles scraperService.
This script orchestrates the web scraping and database persistence processes.

Author: devTASE
"""

import os
import sys
import logging
import argparse
from datetime import datetime
from dotenv import load_dotenv

# Add the parent directory to sys.path to enable imports
current_dir = os.path.dirname(os.path.abspath(__file__))
parent_dir = os.path.dirname(current_dir)
sys.path.append(parent_dir)

# Import project modules
import scraperService
import data_processor
import db_config

# Load environment variables
load_dotenv()

# Configure logging
log_level = os.getenv('LOG_LEVEL', 'INFO').upper()
logging.basicConfig(
    level=getattr(logging, log_level),
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler(f"gta5_scraper_{datetime.now().strftime('%Y%m%d_%H%M%S')}.log"),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

def parse_args():
    """Parse command line arguments."""
    parser = argparse.ArgumentParser(description='Scrape GTA5 vehicle data and store it in the database.')
    parser.add_argument('--dry-run', action='store_true', help='Run the scraperService without saving to the database')
    parser.add_argument('--source', choices=['all', 'gtabase', 'gtawiki'], default='all',
                        help='Specify which source to scrape data from')
    return parser.parse_args()

def main():
    """Main function to run the scraperService."""
    args = parse_args()
    
    logger.info("Starting GTA5 vehicles scraperService")
    
    # Scrape vehicle data from the specified source(s)
    if args.source == 'all':
        logger.info("Scraping from all sources")
        raw_vehicles = scraperService.scrape_all_sources()
    elif args.source == 'gtabase':
        logger.info("Scraping from GTA Base only")
        raw_vehicles = scraperService.scrape_gta_base()
    elif args.source == 'gtawiki':
        logger.info("Scraping from GTA Wiki only")
        raw_vehicles = scraperService.scrape_gta_wiki()
    
    if not raw_vehicles:
        logger.error("No vehicle data was scraped. Exiting.")
        return
        
    logger.info(f"Scraped {len(raw_vehicles)} raw vehicle records")
    
    # Process and validate the scraped data
    processed_vehicles = []
    for raw_vehicle in raw_vehicles:
        processed_vehicle = data_processor.process_vehicle_data(raw_vehicle)
        if processed_vehicle:
            processed_vehicles.append(processed_vehicle)
    
    logger.info(f"Successfully processed {len(processed_vehicles)} out of {len(raw_vehicles)} vehicles")
    
    # Save to database if not in dry run mode
    if not args.dry_run:
        logger.info("Saving vehicles to the database")
        success_count, fail_count = db_config.bulk_insert_vehicles(processed_vehicles)
        logger.info(f"Database operation completed: {success_count} vehicles added, {fail_count} failed")
    else:
        logger.info("Dry run mode: Not saving to database")
        for vehicle in processed_vehicles[:5]:  # Show first 5 as examples
            logger.info(f"Example vehicle: {vehicle['name']} by {vehicle['manufacturer']}")
    
    logger.info("GTA5 vehicles scraperService completed successfully")

if __name__ == "__main__":
    try:
        main()
    except Exception as e:
        logger.exception(f"An unexpected error occurred: {e}")
        sys.exit(1)

