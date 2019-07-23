package dk.cngroup.lentils.service;

import dk.cngroup.lentils.dto.TeamFormDTO;
import dk.cngroup.lentils.entity.Team;
import dk.cngroup.lentils.service.convertors.ModelMapperWrapper;
import dk.cngroup.lentils.service.convertors.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ModelMapperWrapperTest {

    private ObjectGenerator generator;
    private ObjectMapper modelMapper;

    @Before
    public void setUp() {
        generator = new ObjectGenerator();
        modelMapper = new ModelMapperWrapper();
    }

    @Test
    public void testConvertTeamToDTO() {
        Team team = generator.generateValidTeam();

        TeamFormDTO teamFormDto = modelMapper.map(team, TeamFormDTO.class);

        assertTrue(teamFormDto.getName().equals(team.getName()));
        assertTrue(teamFormDto.getNumOfMembers().equals(team.getNumOfMembers()));
    }

    @Test
    public void testUpdateTeamWithDTO() {
        Team team = generator.generateValidTeam();
        TeamFormDTO teamFormDto = new TeamFormDTO();
        teamFormDto.setName("general");
        teamFormDto.setNumOfMembers(8);

        modelMapper.map(teamFormDto, team);

        assertTrue(teamFormDto.getName().equals(team.getName()));
        assertTrue(teamFormDto.getNumOfMembers().equals(team.getNumOfMembers()));
    }
}
