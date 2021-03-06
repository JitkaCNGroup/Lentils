package dk.cngroup.lentils.controller;

import dk.cngroup.lentils.entity.Team;
import dk.cngroup.lentils.entity.view.TeamScoreDetail;
import dk.cngroup.lentils.service.PdfExportService;
import dk.cngroup.lentils.service.ScoreService;
import dk.cngroup.lentils.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
@RequestMapping("/game/score")
public class ScoreController {
    private static final String VIEW_ORGANIZER_SCORE_LIST = "score/list";
    private static final String VIEW_ORGANIZER_SCORE_TEAM = "score/team";

    private final TeamService teamService;
    private final ScoreService scoreService;
    private final PdfExportService exportService;

    @Autowired
    public ScoreController(final TeamService teamService,
                           final ScoreService scoreService,
                           final PdfExportService exportService) {
        this.teamService = teamService;
        this.scoreService = scoreService;
        this.exportService = exportService;
    }

    @GetMapping
    public String listScore(final Model model) {
        model.addAttribute("teamsWithScores", scoreService.getAllTeamsWithScores());
        return VIEW_ORGANIZER_SCORE_LIST;
    }

    @GetMapping(value = "/team")
    public String viewDetailScoreForTeam(final @RequestParam("teamId") Long teamId,
                                         final Model model) {
        Team team = teamService.getTeam(teamId);
        List<TeamScoreDetail> teamScoreDetails = scoreService.getTeamWithDetailScores(team);
        model.addAttribute("team", team);
        model.addAttribute("teamWithDetailScores", teamScoreDetails);
        return VIEW_ORGANIZER_SCORE_TEAM;
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> exportScores() {
        ByteArrayInputStream bis = exportService.getScoresStream();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
