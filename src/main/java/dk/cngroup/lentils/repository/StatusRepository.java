package dk.cngroup.lentils.repository;

import dk.cngroup.lentils.entity.Cypher;
import dk.cngroup.lentils.entity.Status;
import dk.cngroup.lentils.entity.StatusKey;
import dk.cngroup.lentils.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface StatusRepository extends JpaRepository<Status, StatusKey> {
    List<Status> findAllByCypher(Cypher cypher);
    Status findByTeamAndCypher(Team team, Cypher cypher);
    List<Status> findByTeam(Team team);
}
