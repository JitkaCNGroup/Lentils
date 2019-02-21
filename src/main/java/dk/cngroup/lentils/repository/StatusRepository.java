package dk.cngroup.lentils.repository;

import dk.cngroup.lentils.entity.Cypher;
import dk.cngroup.lentils.entity.Status;
import dk.cngroup.lentils.entity.StatusKey;
import dk.cngroup.lentils.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, StatusKey> {
    public Status findByTeamAndCypher(Team team, Cypher cypher);

    public Status findByTeamIdAndCypherId(Long teamId, Long cypherId);

    public List<Status> findByTeam(Team team);
}
