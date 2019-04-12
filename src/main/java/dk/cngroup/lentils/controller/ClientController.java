package dk.cngroup.lentils.controller;

import dk.cngroup.lentils.entity.Cypher;
import dk.cngroup.lentils.entity.CypherStatus;
import dk.cngroup.lentils.entity.formEntity.Codeword;
import dk.cngroup.lentils.service.CypherGameInfoService;
import dk.cngroup.lentils.service.CypherService;
import dk.cngroup.lentils.service.HintService;
import dk.cngroup.lentils.service.HintTakenService;
import dk.cngroup.lentils.service.StatusService;
import dk.cngroup.lentils.service.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class ClientController {

    private static final String CLIENT_VIEW_CYPHER_LIST = "client/cypher/list";
    private static final String CLIENT_VIEW_CYPHER_DETAIL = "client/cypher/detail";
    private static final String CLIENT_VIEW_HINT_LIST = "client/hint/list";
    private static final String CLIENT_TRAP_SCREEN = "client/cypher/trap";
    private static final String REDIRECT_TO_CLIENT_CYPHER_DETAIL = "redirect:/cypher/";
    private static final String REDIRECT_TO_TRAP_SCREEN = "redirect:/cypher/lets-play-a-game";

    private CypherService cypherService;
    private HintService hintService;
    private HintTakenService hintTakenService;
    private StatusService statusService;
    private TeamService teamService;
    private CypherGameInfoService cypherGameInfoService;

    @Autowired
    public ClientController(final CypherService cypherService,
                            final TeamService teamService,
                            final StatusService statusService,
                            final HintService hintService,
                            final HintTakenService hintTakenService,
                            final CypherGameInfoService cypherGameInfoService) {
        this.cypherService = cypherService;
        this.hintService = hintService;
        this.hintTakenService = hintTakenService;
        this.statusService = statusService;
        this.teamService = teamService;
        this.cypherGameInfoService = cypherGameInfoService;
    }

    @GetMapping(value = "cypher")
    public String listAllCyphers(final Model model) {
        model.addAttribute("cypherGameInfos", cypherGameInfoService.getAll());
        model.addAttribute("team", teamService.getTeam(2L));
        return CLIENT_VIEW_CYPHER_LIST;
    }

    @GetMapping(value = "cypher/{id}")
    public String cypherDetail(@PathVariable("id") final Long id, final Model model) {
        Cypher cypher = cypherService.getCypher(id);
        String status = statusService.getStatusNameByTeamAndCypher(teamService.getTeam(2L), cypher);
        model.addAttribute("team", teamService.getTeam(2L));
        model.addAttribute("cypher", cypher);
        model.addAttribute("status", status);
        model.addAttribute("hintsTaken",
                hintTakenService.getAllByTeamAndCypher(teamService.getTeam(2L), cypher));
        model.addAttribute("nextCypher", cypherService.getNext(cypher.getStage()));
        Codeword codeword = new Codeword();
        model.addAttribute("codeword", codeword);
        return CLIENT_VIEW_CYPHER_DETAIL;
    }

    @GetMapping(value = "hint")
    public String listAllAvailableHintsForCypher(final Cypher cypher, final Model model) {
        model.addAttribute("cypher", cypher);
        return CLIENT_VIEW_HINT_LIST;
    }

    @PostMapping(value = "cypher/verify/{id}")
    public String verifyCodeword(@PathVariable("id") final Long id,
                                 @Valid final Codeword codeword,
                                 final BindingResult result,
                                 final Model model) {
        Cypher cypher = cypherService.getCypher(id);
        if (cypherService.checkCodeword(cypher, codeword.getGuess())) {
            statusService.markCypher(cypherService.getCypher(cypher.getCypherId()),
                    teamService.getTeam(2L),
                    CypherStatus.SOLVED);
            return REDIRECT_TO_CLIENT_CYPHER_DETAIL + cypher.getCypherId();
        }

        if (cypherService.checkTrapCodeword(cypher, codeword.getGuess())) {
            return REDIRECT_TO_TRAP_SCREEN;
        }

        FieldError error = new FieldError("codeword", "guess", "Špatné řešení, zkuste se víc zamyslet :-)");
        result.addError(error);
        String status = statusService.getStatusNameByTeamAndCypher(teamService.getTeam(2L), cypher);
        model.addAttribute("team", teamService.getTeam(2L));
        model.addAttribute("cypher", cypher);
        model.addAttribute("status", status);
        model.addAttribute("hintsTaken",
                hintTakenService.getAllByTeamAndCypher(teamService.getTeam(2L), cypher));
        model.addAttribute("nextCypher", cypherService.getNext(cypher.getStage()));
        model.addAttribute("codeword", codeword);
        return CLIENT_VIEW_CYPHER_DETAIL;
    }

    @GetMapping("cypher/lets-play-a-game")
    public String trapScreen() {
        return CLIENT_TRAP_SCREEN;
    }

    @PostMapping(value = "cypher/takeHint/{hintId}")
    public String getHint(@PathVariable("hintId") final Long id, final Cypher cypher) {
        hintTakenService.takeHint(teamService.getTeam(2L),
                hintService.getHint(id));
        return REDIRECT_TO_CLIENT_CYPHER_DETAIL + cypher.getCypherId();
    }

    @PostMapping(value = "cypher/giveUp")
    public String skipCypher(final Cypher cypher) {
        if (statusService.getStatusNameByTeamAndCypher(teamService.getTeam(2L),
                cypherService.getCypher(cypher.getCypherId())).equals("PENDING")) {
            statusService.markCypher(cypherService.getCypher(cypher.getCypherId()),
                    teamService.getTeam(2L),
                    CypherStatus.SKIPPED);
        }
        return REDIRECT_TO_CLIENT_CYPHER_DETAIL + cypherService.getNext(cypher.getStage()).getCypherId();
    }
}
