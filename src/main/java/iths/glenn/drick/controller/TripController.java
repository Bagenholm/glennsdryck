package iths.glenn.drick.controller;

import iths.glenn.drick.entity.TripEntity;
import iths.glenn.drick.exception.DestinationDontExistException;
import iths.glenn.drick.exception.TripAlreadyExistException;
import iths.glenn.drick.exception.TripDontExistException;
import iths.glenn.drick.model.TripModel;
import iths.glenn.drick.service.TripService;
import iths.glenn.drick.trip.UpdateTripRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trip")
public class TripController {

    TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/")
    public List<TripEntity> listAllTrips() {

        try {
            return tripService.listAllTrips();
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }

    @GetMapping("/destination/{destination}")
    public List<TripModel> listAllTripsToDestination(@PathVariable(name = "destination") String destination) {

        try {
            return tripService.listAllTripsToDestination(destination);
        }catch(DestinationDontExistException dex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination not found", dex);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }

    @GetMapping("/{tripId}")
    public TripModel getTripById(@PathVariable(name = "tripId") Map<String, String> tripId) {

        try {
            return tripService.getTripById(tripId);
        }catch(TripDontExistException tdex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found", tdex);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }

    @PostMapping("/")
    public TripModel addTrip(@Valid @RequestBody TripEntity tripEntity) {

        try {
            return tripService.addTrip(tripEntity);
        }catch (TripAlreadyExistException taex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trip already exist", taex);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }

    @DeleteMapping("/{tripId}")
    public Response removeTrip(@PathVariable(name = "tripId") Map<String, String> tripId) {

        try {
            tripService.removeTrip(tripId);
            return Response.ok().build();  //TODO: Använda Response här att returnera???
        }catch(TripDontExistException tdex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found", tdex);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }

    @PutMapping("/{tripId}")
    public TripModel updateTrip(@PathVariable(name = "tripId") Map<String, String> tripId, @Valid @RequestBody TripEntity tripEntity) {

        try {
            return tripService.updateTrip(tripId, tripEntity);
        }catch(TripDontExistException tdex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found", tdex);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }

    @PatchMapping("/{tripId}")
    public TripModel updateTripPartially(@PathVariable(name = "tripId") Map<String, String> tripId, @RequestBody UpdateTripRequest updateTripRequest) {

        try{
            return tripService.updateTripPartially(tripId, updateTripRequest);
        }catch(TripDontExistException tdex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found", tdex);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway", e);
        }
    }
}
