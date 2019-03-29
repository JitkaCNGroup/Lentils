package dk.cngroup.lentils.service;

import dk.cngroup.lentils.entity.Cypher;
import dk.cngroup.lentils.entity.Hint;
import dk.cngroup.lentils.entity.HintTaken;
import dk.cngroup.lentils.entity.Team;
import dk.cngroup.lentils.repository.HintTakenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HintTakenService {

    private HintTakenRepository hintTakenRepository;

    @Autowired
    public HintTakenService(final HintTakenRepository hintTakenRepository) {
        this.hintTakenRepository = hintTakenRepository;
    }

    public void takeHint(final Team team, final Hint hint) {
        hintTakenRepository.save(new HintTaken(team, hint));
    }

    public void revertHint(final Team team, final Hint hint) {
        HintTaken hintTaken = hintTakenRepository.findByTeamAndHint(team, hint);
        hintTakenRepository.delete(hintTaken);
    }

    public int getHintScore(final Team team, final Cypher cypher) {
        int hintScore = 0;
        final List<Hint> hints = cypher.getHints();
        for (Hint hint: hints) {
            HintTaken hintTaken = hintTakenRepository.findByTeamAndHint(team, hint);
            if (hintTaken != null) {
                hintScore += hint.getValue();
            }
        }
        return hintScore;
    }

    public List<HintTaken> getTakenHintsOfTeam(final Team team) {
        return hintTakenRepository.findByTeam(team);
    }

    public List<HintTaken> getAllByCypher(Cypher cypher){
        List<HintTaken> allHintsTaken = hintTakenRepository.findAll();
        List<HintTaken> hintTakenByCypher = allHintsTaken
                .stream()
                .filter(hintTaken -> cypher.getCypherId().equals(hintTaken.getHint().getCypher().getCypherId()))
                .collect(Collectors.toList());
        return hintTakenByCypher;
    }

    public List<HintTaken> getAllByTeam(Team team) {
        return  hintTakenRepository.findByTeam(team);
    }

    public List<HintTaken> getAllByTeamAndCypher(Team team, Cypher cypher) {
    List<HintTaken> hintsTakenByTeamAndCypher = hintTakenRepository.findByTeam(team)
            .stream()
            .filter(hintTaken -> cypher.getCypherId().equals(hintTaken.getHint().getCypher().getCypherId()))
            .collect(Collectors.toList());
    return hintsTakenByTeamAndCypher;
    }
    public List<HintTaken> getAll(){
        return hintTakenRepository.findAll();
    }
}
