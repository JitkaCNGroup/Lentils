package dk.cngroup.lentils.repository;

import dk.cngroup.lentils.LentilsApplication;
import dk.cngroup.lentils.config.DataConfig;
import dk.cngroup.lentils.entity.Cypher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest(classes = {LentilsApplication.class, DataConfig.class})
public class CypherRepositoryIntegrationTest {
    @Autowired
    private CypherRepository cypherRepository;

    @Test
    public void findFirstByStageTest() {
        Cypher cypher1 = new Cypher(new Point(1.1,1.1), 1, "bonus1", "descr1", "address1");
        Cypher cypher2 = new Cypher(new Point(1.1,1.1), 2, "bonus2", "descr2", "address2");
        Cypher cypher3 = new Cypher(new Point(1.1,1.1), 3, "bonus3", "descr3", "address3");
        Cypher cypher4 = new Cypher(new Point(1.1,1.1), 4, "bonus4", "descr4", "address4");
        Cypher cypher5 = new Cypher(new Point(1.1,1.1), 5, "bonus5", "descr5", "address5");

        cypherRepository.save(cypher5);
        cypherRepository.save(cypher4);
        cypherRepository.save(cypher3);
        cypherRepository.save(cypher2);
        cypherRepository.save(cypher1);

        assertEquals(cypher1, cypherRepository.findFirstByOrderByStageAsc());
    }
}