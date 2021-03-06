package dk.cngroup.lentils.service;

import dk.cngroup.lentils.entity.Team;
import dk.cngroup.lentils.entity.User;
import dk.cngroup.lentils.exception.ResourceNotFoundException;
import dk.cngroup.lentils.repository.HintTakenRepository;
import dk.cngroup.lentils.repository.TeamRepository;
import dk.cngroup.lentils.repository.UserRepository;
import dk.cngroup.lentils.util.UsernameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TeamService {

    private static final int PIN_LENGTH = 4;
    private static final String PIN_CHARACTERS = "0123456789";
    private static final String UNIQUE_TEAM_NAME_ERROR = "Zadané jméno už existuje.";
    private static final String TEAM_ENTITY = "team";
    private static final String NAME_PROPERTY = "name";

    private final TeamRepository teamRepository;
    private final HintTakenRepository hintTakenRepository;
    private final StatusService statusService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final Random random;

    @Autowired
    public TeamService(final TeamRepository teamRepository,
                       final HintTakenRepository hintTakenRepository,
                       final StatusService statusService,
                       final PasswordEncoder passwordEncoder,
                       final RoleService roleService,
                       final UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.hintTakenRepository = hintTakenRepository;
        this.statusService = statusService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userRepository = userRepository;

        random = new Random();
    }

    public Team update(final Team team) {
        User user = team.getUser();
        user.setUsername(UsernameUtils.generateUsername(team.getName()));
        return teamRepository.save(team);
    }

    public Team save(final Team team) {
        team.setPin(getUniquePin());
        User user = new User();
        user.setPassword(passwordEncoder.encode(team.getPin()));
        user.setUsername(UsernameUtils.generateUsername(team.getName()));
        user.setRoles(roleService.setRole("USER"));
        user.setTeam(team);
        team.setUser(user);
        return teamRepository.save(team);
    }

    public List<Team> searchTeams(final String searchString) {
        return teamRepository.findByNameIgnoreCaseContaining(searchString);
    }

    public Team getTeam(final Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            return team.get();
        }
        throw new ResourceNotFoundException(Team.class.getSimpleName(), id);
    }

    @Transactional
    public void delete(final Long id) {
        statusService.deleteAllByTeamId(id);
        hintTakenRepository.deleteAllByTeamTeamId(id);
        teamRepository.deleteById(id);
    }

    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    private String getUniquePin() {
        String pin = getPIN(PIN_LENGTH);

        while (!isPINUnique(pin)) {
            pin = getPIN(PIN_LENGTH);
        }
        return pin;
    }

    private boolean isPINUnique(final String pin) {
        List<Team> teamList = teamRepository.findAll();

        for (Team team : teamList) {
            if (team.getPin().equals(pin)) {
                return false;
            }
        }
        return true;
    }

    private String getPIN(final int len) {
        String numbers = PIN_CHARACTERS;
        char[] pin = new char[len];
        for (int i = 0; i < len; i++) {
            pin[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return String.copyValueOf(pin);
    }

    public boolean isUsernameUnique(final Team team) {
        String checkedUsername = UsernameUtils.generateUsername(team.getName());
        Optional<User> userInDb = userRepository.findByUsername(checkedUsername);
        if (team.getTeamId() == null) {
            return !userInDb.isPresent();
        } else {
            return !userInDb.isPresent() || userInDb.get().getTeam().getTeamId().equals(team.getTeamId());
        }
    }

    public void checkUsernameIsUnique(final Team team, final BindingResult bindingResult) {
        if (!isUsernameUnique(team)) {
            FieldError error = new FieldError(
                    TEAM_ENTITY,
                    NAME_PROPERTY,
                    team.getName(),
                    true,
                    null,
                    null,
                    UNIQUE_TEAM_NAME_ERROR);
            bindingResult.addError(error);
        }
    }
}
