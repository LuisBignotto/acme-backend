package br.com.acmeairlines.service;

import br.com.acmeairlines.dto.FlightCreateDTO;
import br.com.acmeairlines.dto.FlightDTO;
import br.com.acmeairlines.dto.FlightUpdateDTO;
import br.com.acmeairlines.model.FlightModel;
import br.com.acmeairlines.repository.FlightRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository repository;

    public FlightDTO createFlight(@Valid FlightCreateDTO flightCreateDTO) {
        FlightModel flightModel = new FlightModel(
                null,
                flightCreateDTO.tag(),
                flightCreateDTO.departureDate(),
                flightCreateDTO.arrivalDate(),
                flightCreateDTO.departureAirport(),
                flightCreateDTO.arrivalAirport(),
                flightCreateDTO.status(),
                flightCreateDTO.airplaneModel());
        FlightModel savedFlight = repository.save(flightModel);
        return new FlightDTO(savedFlight.getId(), savedFlight.getTag(), savedFlight.getDepartureDate(),
                savedFlight.getArrivalDate(), savedFlight.getDepartureAirport(), savedFlight.getArrivalAirport(),
                savedFlight.getStatus(), savedFlight.getAirplaneModel());
    }

    public Optional<FlightDTO> updateFlight(@Valid FlightUpdateDTO flightUpdateDTO, Long id) {
        return repository.findById(id).map(flightModel -> {
            if (flightUpdateDTO.tag() != null) {
                flightModel.setTag(flightUpdateDTO.tag());
            }
            if (flightUpdateDTO.departureDate() != null) {
                flightModel.setDepartureDate(flightUpdateDTO.departureDate());
            }
            if (flightUpdateDTO.arrivalDate() != null) {
                flightModel.setArrivalDate(flightUpdateDTO.arrivalDate());
            }
            if (flightUpdateDTO.departureAirport() != null) {
                flightModel.setDepartureAirport(flightUpdateDTO.departureAirport());
            }
            if (flightUpdateDTO.arrivalAirport() != null) {
                flightModel.setArrivalAirport(flightUpdateDTO.arrivalAirport());
            }
            if (flightUpdateDTO.status() != null) {
                flightModel.setStatus(flightUpdateDTO.status());
            }
            if (flightUpdateDTO.airplaneModel() != null) {
                flightModel.setAirplaneModel(flightUpdateDTO.airplaneModel());
            }
            FlightModel updatedFlight = repository.save(flightModel);
            return new FlightDTO(updatedFlight.getId(), updatedFlight.getTag(), updatedFlight.getDepartureDate(),
                    updatedFlight.getArrivalDate(), updatedFlight.getDepartureAirport(),
                    updatedFlight.getArrivalAirport(), updatedFlight.getStatus(), updatedFlight.getAirplaneModel());
        });
    }

    public Optional<FlightDTO> getFlightById(Long id) {
        return repository.findById(id)
                .map(flightModel -> new FlightDTO(flightModel.getId(), flightModel.getTag(),
                        flightModel.getDepartureDate(), flightModel.getArrivalDate(), flightModel.getDepartureAirport(),
                        flightModel.getArrivalAirport(), flightModel.getStatus(), flightModel.getAirplaneModel()));
    }

    public Optional<FlightDTO> getFlightByTag(String tag) {
        return repository.findByTag(tag)
                .map(flightModel -> new FlightDTO(flightModel.getId(), flightModel.getTag(),
                        flightModel.getDepartureDate(), flightModel.getArrivalDate(), flightModel.getDepartureAirport(),
                        flightModel.getArrivalAirport(), flightModel.getStatus(), flightModel.getAirplaneModel()));
    }

    public Page<FlightDTO> findAllFlights(Pageable pageable) {
        return repository.findAll(pageable)
                .map(flightModel -> new FlightDTO(flightModel.getId(), flightModel.getTag(),
                        flightModel.getDepartureDate(), flightModel.getArrivalDate(), flightModel.getDepartureAirport(),
                        flightModel.getArrivalAirport(), flightModel.getStatus(), flightModel.getAirplaneModel()));
    }

    public void deleteFlight(Long id) {
        repository.deleteById(id);
    }
}