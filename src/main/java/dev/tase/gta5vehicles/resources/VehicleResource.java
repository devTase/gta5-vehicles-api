package dev.tase.gta5vehicles.resources;

import dev.tase.gta5vehicles.control.VehicleService;
import dev.tase.gta5vehicles.entities.Vehicle;
import dev.tase.gta5vehicles.repositories.VehicleRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Path("/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Vehicle Resource", description = "Operations for GTA5 vehicles management")
public class VehicleResource {

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleService vehicleService;

    @GET
    @Operation(summary = "Get all vehicles", description = "Returns a list of all vehicles in the database")
    @APIResponse(
            responseCode = "200",
            description = "List of vehicles",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                              schema = @Schema(implementation = Vehicle.class)))
    public List<Vehicle> getAllVehicles() {
        return Vehicle.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get vehicle by ID", description = "Returns a vehicle by its ID")
    @APIResponse(
            responseCode = "200",
            description = "Vehicle found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                              schema = @Schema(implementation = Vehicle.class)))
    @APIResponse(
            responseCode = "404",
            description = "Vehicle not found")
    public Response getVehicleById(
            @Parameter(description = "Vehicle ID", required = true) 
            @PathParam("id") Long id) {
        Vehicle vehicle = Vehicle.findById(id);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(vehicle).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new vehicle", description = "Creates a new vehicle entry in the database")
    @APIResponse(
            responseCode = "201",
            description = "Vehicle created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                              schema = @Schema(implementation = Vehicle.class)))
    @APIResponse(
            responseCode = "400",
            description = "Invalid vehicle data")
    public Response createVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicle.name == null || vehicle.manufacturer == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
       vehicleService.createVehicle(vehicle);
        return Response.status(Response.Status.CREATED).entity(vehicle).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Update an existing vehicle", description = "Updates an existing vehicle by its ID")
    @APIResponse(
            responseCode = "200",
            description = "Vehicle updated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                              schema = @Schema(implementation = Vehicle.class)))
    @APIResponse(
            responseCode = "404",
            description = "Vehicle not found")
    @APIResponse(
            responseCode = "400",
            description = "Invalid vehicle data")
    public Response updateVehicle(
            @Parameter(description = "Vehicle ID", required = true) 
            @PathParam("id") Long id, 
            Vehicle updatedVehicle) {
        Vehicle vehicle = Vehicle.findById(id);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        if (updatedVehicle == null || updatedVehicle.name == null || updatedVehicle.manufacturer == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        // Update vehicle fields
        vehicle.name = updatedVehicle.name;
        vehicle.manufacturer = updatedVehicle.manufacturer;
        vehicle.vehicleClass = updatedVehicle.vehicleClass;
        vehicle.seats = updatedVehicle.seats;
        vehicle.topSpeed = updatedVehicle.topSpeed;
        vehicle.acceleration = updatedVehicle.acceleration;
        vehicle.braking = updatedVehicle.braking;
        vehicle.price = updatedVehicle.price;
        vehicle.persist();
        return Response.ok(vehicle).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a vehicle", description = "Deletes a vehicle by its ID")
    @APIResponse(
            responseCode = "204",
            description = "Vehicle deleted")
    @APIResponse(
            responseCode = "404",
            description = "Vehicle not found")
    public Response deleteVehicle(
            @Parameter(description = "Vehicle ID", required = true) 
            @PathParam("id") Long id) {
        Vehicle vehicle = Vehicle.findById(id);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        vehicle.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    @Operation(summary = "Search for vehicles", description = "Search for vehicles with various filter criteria")
    @APIResponse(
            responseCode = "200",
            description = "List of filtered vehicles",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                              schema = @Schema(implementation = Vehicle.class)))
    public List<Vehicle> searchVehicles(
            @Parameter(description = "Vehicle name fragment") 
            @QueryParam("name") String name,
            
            @Parameter(description = "Vehicle manufacturer") 
            @QueryParam("manufacturer") String manufacturer,
            
            @Parameter(description = "Vehicle class (Sports, Super, Muscle, etc.)") 
            @QueryParam("vehicleClass") String vehicleClass,
            
            @Parameter(description = "Minimum number of seats") 
            @QueryParam("minSeats") Integer minSeats,
            
            @Parameter(description = "Minimum top speed") 
            @QueryParam("minTopSpeed") Double minTopSpeed,
            
            @Parameter(description = "Maximum price") 
            @QueryParam("maxPrice") BigDecimal maxPrice,
            
            @Parameter(description = "Is special vehicle") 
            @QueryParam("isSpecialVehicle") Boolean isSpecialVehicle,
            
            @Parameter(description = "Has modification") 
            @QueryParam("modification") String modification) {
        
        // Handle different search combinations
        if (name != null) {
            return vehicleRepository.findByNameContaining(name);
        }
        
        if (manufacturer != null) {
            return Vehicle.findByManufacturer(manufacturer);
        }
        
        if (vehicleClass != null && minSeats != null && maxPrice != null) {
            return vehicleRepository.findByMultipleCriteria(vehicleClass, minSeats, maxPrice);
        }
        
        if (vehicleClass != null) {
            return Vehicle.findByVehicleClass(vehicleClass);
        }
        
        if (minTopSpeed != null) {
            return vehicleRepository.findByTopSpeedGreaterThan(minTopSpeed);
        }
        
        if (isSpecialVehicle != null && isSpecialVehicle && modification != null) {
            return vehicleRepository.findSpecialVehiclesWithModification(modification);
        }
        
        // Default to all vehicles if no filters are applied
        return Vehicle.listAll();
    }
}

