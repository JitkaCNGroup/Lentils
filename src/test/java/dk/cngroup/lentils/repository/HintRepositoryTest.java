package dk.cngroup.lentils.repository;

import dk.cngroup.lentils.LentilsApplication;
import dk.cngroup.lentils.config.DataConfig;
import dk.cngroup.lentils.entity.Cypher;
import dk.cngroup.lentils.entity.Hint;
import dk.cngroup.lentils.entity.HintTaken;
import dk.cngroup.lentils.entity.Team;
import dk.cngroup.lentils.service.CypherService;
import dk.cngroup.lentils.service.ObjectGenerator;
import dk.cngroup.lentils.service.TeamService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dk.cngroup.lentils.service.ObjectGenerator.CODEWORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = {LentilsApplication.class, DataConfig.class, ObjectGenerator.class})
public class HintRepositoryTest {
    private static final int TESTED_STAGE = 3;
    private static final Point TEST_LOCATION = new Point(59.9090442, 10.7423389);
    private static final String TEST_EMPTY_NAME = "";
    private static final String TEST_CYPHER_NAME = "CypherName";
    private static final String TEST_HINT_TEXT = "HintText";

    @Autowired
    private HintRepository hintRepository;

    @Autowired
    private HintTakenRepository hintTakenRepository;

    @Autowired
    private CypherService cypherService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ObjectGenerator generator;

    private List<Cypher> cyphers;

    @Before
    public void setUp() {
        cypherService.saveAll(generator.generateCypherList());
        cyphers = cypherService.getAll();
    }

    @Test
    public void saveAllTest() {
        hintRepository.saveAll(generator.generateHintsForCypher(cyphers));

        int expected = ObjectGenerator.NUMBER_OF_CYPHERS * ObjectGenerator.NUMBER_OF_HINTS_FOR_CYPHER;
        assertEquals(expected, hintRepository.count());
    }

    @Test
    public void addTest() {
        Cypher cypher = new Cypher(TEST_CYPHER_NAME, TESTED_STAGE, TEST_LOCATION, CODEWORD);
        Cypher cypher1 = cypherService.save(cypher);
        Hint hint = new Hint(TEST_HINT_TEXT, 5, cypher1);
        Hint hint1 = hintRepository.save(hint);

        assertNotNull(hint1);
        assertEquals(1, hintRepository.count());
    }

    @Test
    public void deleteAllTest() {
        hintRepository.saveAll(generator.generateHintsForCypher(cyphers));
        hintRepository.deleteAll();

        assertEquals(0, hintRepository.count());
    }

    @Test
    public void getAllTest() {
        hintRepository.saveAll(generator.generateHintsForCypher(cyphers));
        List<Hint> hints = hintRepository.findAll();

        int expected = ObjectGenerator.NUMBER_OF_CYPHERS * ObjectGenerator.NUMBER_OF_HINTS_FOR_CYPHER;
        assertEquals(expected, hints.size());
    }

    @Test
    public void getAllForCypherTest() {
        Cypher cypher = cypherService.getByStage(TESTED_STAGE);
        hintRepository.saveAll(generator.generateHintsForCypher(cypher));
        List<Hint> hints = hintRepository.findByCypher(cypher);

        assertEquals(ObjectGenerator.NUMBER_OF_HINTS_FOR_CYPHER, hints.size());
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void hintWithEmptyTextTest() {
        Cypher cypher = cypherService.save(new Cypher(TEST_CYPHER_NAME, TESTED_STAGE, TEST_LOCATION, CODEWORD));
        Hint hint = new Hint(TEST_EMPTY_NAME, 5, cypher);
        hintRepository.saveAndFlush(hint);
    }

    @Test(expected = javax.validation.ConstraintViolationException.class)
    public void hintWithNotAllowedValueTest() {
        Cypher cypher = cypherService.save(new Cypher(TEST_CYPHER_NAME, TESTED_STAGE, TEST_LOCATION, CODEWORD));
        Hint hint = new Hint(TEST_HINT_TEXT, 0, cypher);
        hintRepository.saveAndFlush(hint);
    }

    @Test
    public  void countHintsNotTakenByTeamWhenOneHintFromThreeTakenNoOtherTeamTookHints() {
        Team team1 = teamService.save(new Team("aaa", 4, "eeee"));
        Cypher cypher = cyphers.get(0);
        List<Hint> hints = generateAndSaveHints(cypher);
        createAndSaveHintTaken(hints, team1, 1);
        List<Hint> notTakenHints = hintRepository.findHintsNotTakenByTeam(team1.getTeamId(), cypher.getCypherId());
        generator.generateHintsForCypher(cyphers);
        assertEquals(2, notTakenHints.size());
    }

    @Test
    public  void countHintsNotTakenByTeamWhenOneHintFromThreeTakenOneOtherTeamTookHints() {
        Team team1 = teamService.save(new Team("aaa", 4, "eeee"));
        Team team2 = teamService.save(new Team("bbb", 2, "uuuu"));
        Cypher cypher = cyphers.get(0);
        List<Hint> hints = generateAndSaveHints(cypher);
        createAndSaveHintTaken(hints, team1, 1);
        createAndSaveHintTaken(hints, team2, 1);
        createAndSaveHintTaken(hints, team2, 2);
        List<Hint> notTakenHints = hintRepository.findHintsNotTakenByTeam(team1.getTeamId(), cypher.getCypherId());
        generator.generateHintsForCypher(cyphers);
        assertEquals(2, notTakenHints.size());
    }

    @Test
    public  void countHintsNotTakenByTeamWhenNoHintsFromThreeTakenTwoOtherTeams() {
        Team team1 = teamService.save(new Team("aaa", 4, "eeee"));
        Team team2 = teamService.save(new Team("bbb", 2, "uuuu"));
        Team team3 = teamService.save(new Team("ccc", 3, "oooo"));
        Cypher cypher = cyphers.get(0);
        List<Hint> hints = generateAndSaveHints(cypher);
        createAndSaveHintTaken(hints, team2, 0);
        createAndSaveHintTaken(hints, team2, 1);
        createAndSaveHintTaken(hints, team2, 2);
        createAndSaveHintTaken(hints, team3, 0);
        createAndSaveHintTaken(hints, team3, 1);
        List<Hint> notTakenHints = hintRepository.findHintsNotTakenByTeam(team1.getTeamId(), cypher.getCypherId());
        generator.generateHintsForCypher(cyphers);
        assertEquals(3, notTakenHints.size());
    }

    @Test
    public  void countHintsNotTakenByTeamWhenTwoHintsFromThreeTakenTwoOtherTeams() {
        Team team1 = teamService.save(new Team("aaa", 4, "eeee"));
        Team team2 = teamService.save(new Team("bbb", 2, "uuuu"));
        Team team3 = teamService.save(new Team("ccc", 3, "oooo"));
        Cypher cypher = cyphers.get(0);
        List<Hint> hints = generateAndSaveHints(cypher);
        createAndSaveHintTaken(hints, team1, 0);
        createAndSaveHintTaken(hints, team1, 1);
        createAndSaveHintTaken(hints, team2, 2);
        createAndSaveHintTaken(hints, team2, 1);
        createAndSaveHintTaken(hints, team3, 2);
        createAndSaveHintTaken(hints, team3, 1);
        List<Hint> notTakenHints = hintRepository.findHintsNotTakenByTeam(team1.getTeamId(), cypher.getCypherId());
        generator.generateHintsForCypher(cyphers);
        assertEquals(1, notTakenHints.size());
    }

    private void createAndSaveHintTaken(List<Hint> hints, Team team, int index) {
        HintTaken hintTaken = new HintTaken(team, hints.get(index));
        hintTakenRepository.save(hintTaken);
    }

    private List<Hint> generateAndSaveHints(Cypher cypher) {
        List<Hint> hints = generator.generateHintsForCypher(cypher);
        hintRepository.saveAll(hints);
        return hints;
    }
}