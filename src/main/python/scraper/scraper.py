#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Web scraperService for GTA5 vehicles.
This module handles the extraction of vehicle data from various websites.

Author: devTASE
"""

import logging
import re
import time
import requests
from bs4 import BeautifulSoup
from datetime import datetime

# Configure logging
logger = logging.getLogger(__name__)

# Headers to mimic a browser request
HEADERS = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
    'Accept-Language': 'en-US,en;q=0.9',
}

def scrape_gta_base():
    """
    Scrape vehicle data from GTA Base.
    
    Returns:
        list: List of dictionaries containing vehicle data
    """
    url = "https://www.gta-db.com/gta-5-cars/"
    vehicles = []

    page_info = soup.select_one("div.page")
    if page_info:
        text = page_info.text.strip()
        match = re.search(r"Found (\d+) posts", text)
        if match:
            total_vehicles = int(match.group(1))
            print(f"Total de veículos: {total_vehicles}")

    try:
        logger.info(f"Scraping vehicles from {url}")
        response = requests.get(url, headers=HEADERS, timeout=30)
        response.raise_for_status()
        
        soup = BeautifulSoup(response.text, 'html.parser')
        
        # Find vehicle table or container
        vehicle_containers = soup.select('.item.clickable')
        
        for container in vehicle_containers:
            try:
                # Extract basic information
                name_element = container.select_one('h4')
                name = name_element.text.strip() if name_element else "Unknown"
                
                # Extract detailed information from the vehicle page
                detail_url = container.select_one('a')['href']
                vehicle_data = scrape_vehicle_details(detail_url)
                
                if vehicle_data:
                    vehicles.append(vehicle_data)
                    logger.info(f"Extracted data for vehicle: {name}")
                
                # Respect the website by adding a delay between requests
                time.sleep(1)
                
            except Exception as e:
                logger.error(f"Error scraping vehicle container: {e}")
                continue
                
        logger.info(f"Successfully scraped {len(vehicles)} vehicles from GTA Base")
        return vehicles
        
    except requests.RequestException as e:
        logger.error(f"Error fetching GTA Base: {e}")
        return []



def scrape_vehicle_details(detail_url):
    """
    Scrape detailed information about a vehicle from its detail page.
    
    Args:
        detail_url (str): URL of the vehicle detail page
        
    Returns:
        dict: Dictionary containing vehicle details
    """
    try:
        logger.info(f"Scraping details from {detail_url}")
        response = requests.get(detail_url, headers=HEADERS, timeout=30)
        response.raise_for_status()
        
        soup = BeautifulSoup(response.text, 'html.parser')
        
        # Initialize vehicle data
        vehicle_data = {
            'name': 'Unknown',
            'manufacturer': 'Unknown',
            'vehicle_class': 'Unknown',
            'seats': 0,
            'top_speed': 0.0,
            'acceleration': 0.0,
            'braking': 0.0,
            'handling': 0.0,
            'price': 0,
            'release_date': None,
            'is_special_vehicle': False,
            'modifications': []
        }
        
        # Extract name
        name_element = soup.select_one('h1')
        if name_element:
            vehicle_data['name'] = name_element.text.strip()
        
        # Extract specifications from the details table
        specs_table = soup.select_one('.table-responsive table')
        if specs_table:
            rows = specs_table.select('tr')
            for row in rows:
                columns = row.select('td')
                if len(columns) >= 2:
                    label = columns[0].text.strip().lower()
                    value = columns[1].text.strip()
                    
                    if 'manufacturer' in label:
                        vehicle_data['manufacturer'] = value
                    elif 'class' in label:
                        vehicle_data['vehicle_class'] = value
                    elif 'seats' in label or 'capacity' in label:
                        try:
                            vehicle_data['seats'] = int(re.search(r'\d+', value).group())
                        except (AttributeError, ValueError):
                            vehicle_data['seats'] = 0
                    elif 'top speed' in label:
                        try:
                            vehicle_data['top_speed'] = float(re.search(r'\d+(\.\d+)?', value).group())
                        except (AttributeError, ValueError):
                            vehicle_data['top_speed'] = 0.0
                    elif 'acceleration' in label:
                        try:
                            vehicle_data['acceleration'] = float(re.search(r'\d+(\.\d+)?', value).group())
                        except (AttributeError, ValueError):
                            vehicle_data['acceleration'] = 0.0
                    elif 'braking' in label:
                        try:
                            vehicle_data['braking'] = float(re.search(r'\d+(\.\d+)?', value).group())
                        except (AttributeError, ValueError):
                            vehicle_data['braking'] = 0.0
                    elif 'handling' in label:
                        try:
                            vehicle_data['handling'] = float(re.search(r'\d+(\.\d+)?', value).group())
                        except (AttributeError, ValueError):
                            vehicle_data['handling'] = 0.0
                    elif 'price' in label or 'cost' in label:
                        price_str = re.sub(r'[^\d]', '', value)
                        try:
                            vehicle_data['price'] = int(price_str) if price_str else 0
                        except ValueError:
                            vehicle_data['price'] = 0
                    elif 'release' in label or 'date' in label:
                        vehicle_data['release_date'] = value
                    elif 'special' in label:
                        vehicle_data['is_special_vehicle'] = 'yes' in value.lower()
        
        # Extract modifications
        mods_section = soup.find('h3', text=re.compile(r'Modifications', re.I))
        if mods_section:
            mod_list = mods_section.find_next('ul')
            if mod_list:
                mod_items = mod_list.select('li')
                vehicle_data['modifications'] = [mod.text.strip() for mod in mod_items]
        
        return vehicle_data
        
    except requests.RequestException as e:
        logger.error(f"Error fetching vehicle details from {detail_url}: {e}")
        return None

def scrape_gta_wiki():
    """
    Scrape vehicle data from GTA Wiki.
    
    Returns:
        list: List of dictionaries containing vehicle data
    """
    url = "https://gta.fandom.com/wiki/Vehicles_in_GTA_V"
    vehicles = []
    
    try:
        logger.info(f"Scraping vehicles from {url}")
        response = requests.get(url, headers=HEADERS, timeout=30)
        response.raise_for_status()
        
        soup = BeautifulSoup(response.text, 'html.parser')
        
        # Find vehicle links
        # Look for tables with vehicle lists
        vehicle_tables = soup.select('table.wikitable')
        
        for table in vehicle_tables:
            # Find links in the table rows
            vehicle_links = table.select('tr td a')
            
            for link in vehicle_links:
                try:
                    vehicle_name = link.text.strip()
                    detail_url = "https://gta.fandom.com" + link['href']
                    
                    # Extract detailed information from the vehicle page
                    vehicle_data = scrape_wiki_vehicle_details(detail_url)
                    
                    if vehicle_data:
                        vehicles.append(vehicle_data)
                        logger.info(f"Extracted data for vehicle: {vehicle_name}")
                    
                    # Respect the website by adding a delay between requests
                    time.sleep(1)
                    
                except Exception as e:
                    logger.error(f"Error processing vehicle link: {e}")
                    continue
        
        logger.info(f"Successfully scraped {len(vehicles)} vehicles from GTA Wiki")
        return vehicles
        
    except requests.RequestException as e:
        logger.error(f"Error fetching GTA Wiki: {e}")
        return []

def scrape_wiki_vehicle_details(detail_url):
    """
    Scrape detailed information about a vehicle from GTA Wiki.
    
    Args:
        detail_url (str): URL of the vehicle detail page
        
    Returns:
        dict: Dictionary containing vehicle details
    """
    try:
        logger.info(f"Scraping details from {detail_url}")
        response = requests.get(detail_url, headers=HEADERS, timeout=30)
        response.raise_for_status()
        
        soup = BeautifulSoup(response.text, 'html.parser')
        
        # Initialize vehicle data
        vehicle_data = {
            'name': 'Unknown',
            'manufacturer': 'Unknown',
            'vehicle_class': 'Unknown',
            'seats': 0,
            'top_speed': 0.0,
            'acceleration': 0.0,
            'braking': 0.0,
            'handling': 0.0,
            'price': 0,
            'release_date': None,
            'is_special_vehicle': False,
            'modifications': []
        }
        
        # Extract name
        name_element = soup.select_one('h1.page-header__title')
        if name_element:
            vehicle_data['name'] = name_element.text.strip()
        
        # Extract information from infobox
        infobox = soup.select_one('aside.portable-infobox')
        if infobox:
            # Extract manufacturer
            manufacturer_label = infobox.find('h3', text=re.compile(r'manufacturer', re.I))
            if manufacturer_label:
                manufacturer_value = manufacturer_label.find_next('div', class_='pi-data-value')
                if manufacturer_value:
                    vehicle_data['manufacturer'] = manufacturer_value.text.strip()
            
            # Extract vehicle class
            class_label = infobox.find('h3', text=re.compile(r'class|vehicle type', re.I))
            if class_label:
                class_value = class_label.find_next('div', class_='pi-data-value')
                if class_value:
                    vehicle_data['vehicle_class'] = class_value.text.strip()
            
            # Extract seats
            seats_label = infobox.find('h3', text=re.compile(r'capacity', re.I))
            if seats_label:
                seats_value = seats_label.find_next('div', class_='pi-data-value')
                if seats_value:
                    try:
                        vehicle_data['seats'] = int(re.search(r'\d+', seats_value.text).group())
                    except (AttributeError, ValueError):
                        pass
            
            # Extract price
            price_label = infobox.find('h3', text=re.compile(r'price|cost', re.I))
            if price_label:
                price_value = price_label.find_next('div', class_='pi-data-value')
                if price_value:
                    price_str = re.sub(r'[^\d]', '', price_value.text)
                    try:
                        vehicle_data['price'] = int(price_str) if price_str else 0
                    except ValueError:
                        pass
        
        # Extract performance data from tables or sections
        performance_section = soup.find('span', id=re.compile(r'performance', re.I))
        if performance_section:
            # Try to find stats in the paragraphs after the section
            paragraph = performance_section.parent.find_next('p')
            if paragraph:
                text = paragraph.text.lower()
                
                # Extract top speed
                speed_match = re.search(r'top speed.*?(\d+(\.\d+)?)', text)
                if speed_match:
                    try:
                        vehicle_data['top_speed'] = float(speed_match.group(1))
                    except ValueError:
                        pass
                
                # Extract acceleration
                accel_match = re.search(r'acceleration.*?(\d+(\.\d+)?)', text)
                if accel_match:
                    try:
                        vehicle_data['acceleration'] = float(accel_match.group(1))
                    except ValueError:
                        pass
                
                # Extract braking
                brake_match = re.search(r'braking.*?(\d+(\.\d+)?)', text)
                if brake_match:
                    try:
                        vehicle_data['braking'] = float(brake_match.group(1))
                    except ValueError:
                        pass
                
                # Extract handling
                handling_match = re.search(r'handling.*?(\d+(\.\d+)?)', text)
                if handling_match:
                    try:
                        vehicle_data['handling'] = float(handling_match.group(1))
                    except ValueError:
                        pass
        
        # Extract modifications
        modifications_section = soup.find(['span', 'h2', 'h3'], id=re.compile(r'modification|customization', re.I)) or soup.find(['h2', 'h3'], text=re.compile(r'modification|customization', re.I))
        if modifications_section:
            mod_list = modifications_section.parent.find_next('ul')
            if mod_list:
                vehicle_data['modifications'] = [li.text.strip() for li in mod_list.find_all('li')]
        
        # Check if it's a special vehicle
        if 'special' in soup.text.lower() or 'exclusive' in soup.text.lower():
            vehicle_data['is_special_vehicle'] = True
        
        return vehicle_data
        
    except requests.RequestException as e:
        logger.error(f"Error fetching vehicle details from {detail_url}: {e}")
        return None

def scrape_all_sources():
    """
    Scrape vehicle data from all available sources and merge the results.
    
    Returns:
        list: Combined list of vehicle data from all sources
    """
    all_vehicles = []
    
    # Scrape from GTA Base
    gta_base_vehicles = scrape_gta_base()
    if gta_base_vehicles:
        all_vehicles.extend(gta_base_vehicles)
    
    # Scrape from GTA Wiki
    gta_wiki_vehicles = scrape_gta_wiki()
    if gta_wiki_vehicles:
        # Merge with existing vehicles to avoid duplicates
        existing_names = {v['name'].lower() for v in all_vehicles}
        for vehicle in gta_wiki_vehicles:
            if vehicle['name'].lower() not in existing_names:
                all_vehicles.append(vehicle)
            else:
                # If vehicle exists, merge missing data
                for existing_vehicle in all_vehicles:
                    if existing_vehicle['name'].lower() == vehicle['name'].lower():
                        for key, value in vehicle.items():
                            if not existing_vehicle[key] and value:
                                existing_vehicle[key] = value
    
    logger.info(f"Total unique vehicles scraped: {len(all_vehicles)}")
    return all_vehicles

