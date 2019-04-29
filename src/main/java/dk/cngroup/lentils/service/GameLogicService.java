package dk.cngroup.lentils.service;

import dk.cngroup.lentils.entity.CypherStatus;
import dk.cngroup.lentils.entity.FinalPlace;
import dk.cngroup.lentils.entity.Status;
import dk.cngroup.lentils.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameLogicService {

    private final FinalPlaceService finalPlaceService;
    private final StatusService statusService;

    @Autowired
    public GameLogicService(final FinalPlaceService finalPlaceService,
                            final StatusService statusService) {
        this.finalPlaceService = finalPlaceService;
        this.statusService = statusService;
    }

    public boolean isGameInProgress() {
        final FinalPlace finalPlace = finalPlaceService.getFinalPlace();

        return finalPlace.getOpeningTime() != null && finalPlace.getOpeningTime().isAfter(LocalDateTime.now());
    }

    public boolean allowPlayersToViewFinalPlace(Team team) {
        if (passedTimeToViewFinalPlace() || passedAllCyphers(team)) {
            return true;
        }
        return false;
    }

    public boolean passedAllCyphers(Team team) {
        List<Status> statusesOfTeam = statusService.getAllByTeam(team);
        Long numberOfStatusPendingByTeam = statusesOfTeam.stream()
                .filter(status -> status.getCypherStatus().equals(CypherStatus.PENDING)).count();
        if (numberOfStatusPendingByTeam > 0) {
            return false;
        }
        return true;
    }

    public boolean passedTimeToViewFinalPlace() {
        LocalDateTime finalPlaceOpeningTime = finalPlaceService.getFinalPlace().getOpeningTime();
        return finalPlaceOpeningTime.isBefore(LocalDateTime.now().plusHours(1));
    }
}
