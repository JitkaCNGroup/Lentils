package dk.cngroup.lentils.repository;

import dk.cngroup.lentils.LentilsApplication;
import dk.cngroup.lentils.config.DataConfig;
import dk.cngroup.lentils.entity.Team;
import dk.cngroup.lentils.service.ObjectGenerator;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(classes = {LentilsApplication.class, DataConfig.class, ObjectGenerator.class})
@Ignore
public class TeamRepositoryTest {
    private static final int TESTED_TEAM = 5;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ObjectGenerator generator;

    @Test
    public void countEmptyDatabaseTest() {
        assertEquals(0, teamRepository.count());
    }

    @Test
    public void addTest() {
        Team team = new Team(generator.TEAM_NAME + TESTED_TEAM, 5, "1234");
        teamRepository.save(team);

        assertEquals(1, teamRepository.count());
    }

    @Test
    public void findByIdTest() {
        List<Team> teams = getAllSavedTeams();
        String teamName = generator.TEAM_NAME + TESTED_TEAM;
        Team team = teamRepository.findByName(teamName);
        Optional<Team> teamById = teamRepository.findById(team.getTeamId());

        assertNotNull(teamById.isPresent());
        assertEquals(team, teamById.get());
    }

    @Test
    public void findAllTest() {
        getAllSavedTeams();
        List<Team> teamsFound = teamRepository.findAll();

        assertEquals(generator.NUMBER_OF_TEAMS, teamsFound.size());
    }

    @Test
    public void deleteAllTest() {
        getAllSavedTeams();
        teamRepository.deleteAll();

        assertEquals(0, teamRepository.count());
    }

    @Test
    public void deleteOneTest() {
        Team team = teamRepository.save(new Team(generator.TEAM_NAME + TESTED_TEAM, 5, "1234"));
        teamRepository.deleteById(team.getTeamId());

        assertEquals(0, teamRepository.count());
        assertFalse(teamRepository.findById(team.getTeamId()).isPresent());
    }

    private Team getAnySaved() {
        String teamName = generator.TEAM_NAME + TESTED_TEAM;
        return teamRepository.findByName(teamName);
    }

    private List<Team> getAllSavedTeams() {
        return teamRepository.saveAll(generator.generateTeamList());
    }

}