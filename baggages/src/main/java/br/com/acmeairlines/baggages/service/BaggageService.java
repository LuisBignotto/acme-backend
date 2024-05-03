package br.com.acmeairlines.baggages.service;

import br.com.acmeairlines.baggages.dto.BaggageDTO;
import br.com.acmeairlines.baggages.dto.BaggageTrackerDTO;
import br.com.acmeairlines.baggages.dto.CreateBaggageDTO;
import br.com.acmeairlines.baggages.dto.StatusDTO;
import br.com.acmeairlines.baggages.model.BaggageModel;
import br.com.acmeairlines.baggages.model.BaggageTrackerModel;
import br.com.acmeairlines.baggages.model.StatusModel;
import br.com.acmeairlines.baggages.repository.BaggageRepository;
import br.com.acmeairlines.baggages.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaggageService {

    private final BaggageRepository baggageRepository;
    private final StatusRepository statusRepository;

    public BaggageService(BaggageRepository baggageRepository, StatusRepository statusRepository) {
        this.baggageRepository = baggageRepository;
        this.statusRepository = statusRepository;
    }

    @Transactional
    public BaggageDTO createBaggage(CreateBaggageDTO createBaggageDTO) {
        StatusModel status = statusRepository.findById(createBaggageDTO.statusId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid status ID"));

        BaggageModel baggageModel = new BaggageModel();
        baggageModel.setUserId(createBaggageDTO.userId());
        baggageModel.setTag(createBaggageDTO.tag());
        baggageModel.setColor(createBaggageDTO.color());
        baggageModel.setWeight(createBaggageDTO.weight());
        baggageModel.setStatus(status);
        baggageModel.setLastLocation(createBaggageDTO.lastLocation());
        baggageModel.setFlightId(createBaggageDTO.flightId());

        List<BaggageTrackerModel> trackers = createBaggageDTO.trackers().stream()
                .map(trackerDTO -> {
                    BaggageTrackerModel trackerModel = new BaggageTrackerModel();
                    trackerModel.setBaggage(baggageModel);
                    trackerModel.setTrackerUserId(trackerDTO.trackerUserId());
                    return trackerModel;
                })
                .collect(Collectors.toList());

        baggageModel.setTrackers(trackers);

        BaggageModel savedBaggage = baggageRepository.save(baggageModel);
        return convertToDTO(savedBaggage);
    }

    @Transactional(readOnly = true)
    public List<BaggageDTO> getBaggagesByUserId(Long userId) {
        List<BaggageModel> baggageModels = baggageRepository.findByUserId(userId);
        return baggageModels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BaggageDTO> getBaggagesByFlightId(Long flightId) {
        List<BaggageModel> baggageModels = baggageRepository.findByFlightId(flightId);
        return baggageModels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BaggageDTO getBaggageByTag(String tag) {
        BaggageModel baggageModel = baggageRepository.findByTag(tag);
        return baggageModel != null ? convertToDTO(baggageModel) : null;
    }

    private BaggageDTO convertToDTO(BaggageModel baggageModel) {
        StatusDTO statusDTO = new StatusDTO(baggageModel.getStatus().getId(), baggageModel.getStatus().getStatus());

        List<BaggageTrackerDTO> trackerDTOs = baggageModel.getTrackers().stream()
                .map(trackerModel -> new BaggageTrackerDTO(trackerModel.getId(), trackerModel.getBaggage().getId(), trackerModel.getTrackerUserId()))
                .collect(Collectors.toList());

        return new BaggageDTO(
                baggageModel.getId(),
                baggageModel.getUserId(),
                baggageModel.getTag(),
                baggageModel.getColor(),
                baggageModel.getWeight(),
                statusDTO,
                baggageModel.getLastLocation(),
                baggageModel.getFlightId(),
                trackerDTOs
        );
    }
}
