# GTA5 Vehicles API

A comprehensive GTA5 vehicle management API built with Quarkus and PostgreSQL. This application provides a complete database solution for storing and managing detailed information about vehicles from Grand Theft Auto 5.

## Features

- Complete CRUD operations for GTA5 vehicles
- Detailed vehicle specifications including performance metrics
- PostgreSQL database with Panache ORM
- RESTful API with OpenAPI documentation
- Advanced search functionality with multiple criteria
- Comprehensive vehicle attributes (manufacturer, class, performance stats, etc.)

## Technical Stack

- Quarkus 3.x
- PostgreSQL
- Hibernate ORM with Panache
- RESTEasy Reactive
- OpenAPI (Swagger UI)
- Jakarta EE 10

## Prerequisites

- JDK 21 or later
- Maven 3.8.6+
- PostgreSQL 13+
- Git

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/devTASE/gta5-vehicles-api.git
cd gta5-vehicles-api
```

### Database Setup

1. Install PostgreSQL if you haven't already
2. Create a new database:

```bash
createdb gta5vehicles
```

3. Configure your database credentials in `src/main/resources/application.properties`:

```properties
quarkus.datasource.username=your_username
quarkus.datasource.password=your_password
```

### Running the Application

#### Development Mode

```bash
./mvnw compile quarkus:dev
```

This will start the application in development mode with hot reload enabled.

#### Production Mode

Build the application:

```bash
./mvnw package
```

Run the JAR file:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## API Documentation

Once the application is running, you can access the Swagger UI at:

```
http://localhost:8080/swagger-ui
```

This provides interactive documentation of all available endpoints and allows you to test the API directly from your browser.

## API Endpoints

The API provides the following endpoints:

- `GET /vehicles` - Retrieve all vehicles
- `GET /vehicles/{id}` - Get a specific vehicle by ID
- `POST /vehicles` - Create a new vehicle
- `PUT /vehicles/{id}` - Update an existing vehicle
- `DELETE /vehicles/{id}` - Delete a vehicle
- `GET /vehicles/search` - Search for vehicles with multiple filter criteria

### Search Parameters

The search endpoint supports the following query parameters:

- `name` - Search by vehicle name (partial match)
- `manufacturer` - Filter by manufacturer
- `vehicleClass` - Filter by vehicle class (Sports, Super, Muscle, etc.)
- `minSeats` - Filter by minimum number of seats
- `minTopSpeed` - Filter by minimum top speed
- `maxPrice` - Filter by maximum price
- `isSpecialVehicle` - Filter special vehicles only
- `modification` - Filter by available modification

## Example Usage

### Creating a New Vehicle

```bash
curl -X 'POST' \
  'http://localhost:8080/vehicles' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Adder",
  "manufacturer": "Truffade",
  "vehicleClass": "Super",
  "seats": 2,
  "topSpeed": 220.0,
  "acceleration": 9.2,
  "braking": 8.5,
  "handling": 8.3,
  "price": 1000000,
  "releaseDate": "2013-09-17",
  "isSpecialVehicle": false,
  "modifications": [
    "Engine Upgrade",
    "Turbo",
    "Custom Spoiler"
  ]
}'
```

### Searching for Vehicles

```bash
# Get all vehicles from a specific manufacturer
curl -X 'GET' 'http://localhost:8080/vehicles/search?manufacturer=Truffade'

# Get all super cars with at least 2 seats and max price of 2 million
curl -X 'GET' 'http://localhost:8080/vehicles/search?vehicleClass=Super&minSeats=2&maxPrice=2000000'
```

## Database Schema

The Vehicle entity contains the following fields:

- `id` (Long): Primary key
- `name` (String): Vehicle name
- `manufacturer` (String): Vehicle manufacturer
- `vehicleClass` (String): Vehicle class (Sports, Super, Muscle, etc.)
- `seats` (Integer): Number of seats
- `topSpeed` (Double): Top speed in mph
- `acceleration` (Double): Acceleration rating (0-10)
- `braking` (Double): Braking rating (0-10)
- `handling` (Double): Handling rating (0-10)
- `price` (BigDecimal): Vehicle price in GTA$ 
- `releaseDate` (LocalDate): Date when the vehicle was released
- `isSpecialVehicle` (Boolean): Whether it's a special vehicle
- `modifications` (List<String>): Available modifications

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
