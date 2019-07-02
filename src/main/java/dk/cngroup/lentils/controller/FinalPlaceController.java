package dk.cngroup.lentils.controller;

import dk.cngroup.lentils.entity.FinalPlace;
import dk.cngroup.lentils.service.FinalPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/finalplace")
public class FinalPlaceController {
    private static final String VIEW_FINALPLACE_FORM = "finalplace/form";
    private static final String REDIRECT_FINALPLACE_FORM = "redirect:/admin/finalplace/";

    private FinalPlaceService finalPlaceService;

    @Autowired
    public FinalPlaceController(final FinalPlaceService finalPlaceService) {
        this.finalPlaceService = finalPlaceService;
    }

    @GetMapping(value = "/")
    public String finalPlace(final Model model) {
        model.addAttribute("finalPlace", finalPlaceService.getFinalPlace());
        model.addAttribute(
                "serverTime",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return VIEW_FINALPLACE_FORM;
    }

    @PostMapping(value = "/update")
    public String saveFinalPlace(@Valid @ModelAttribute final FinalPlace finalPlace,
                                 final BindingResult bindingResult,
                                 final Model model) {
        finalPlaceService.checkFinishTimeBeforeResultsTime(bindingResult, finalPlace);
        if (bindingResult.hasErrors()) {
            model.addAttribute("finalPlace", finalPlace);
            return VIEW_FINALPLACE_FORM;
        }
        finalPlaceService.save(finalPlace);
        return REDIRECT_FINALPLACE_FORM;
    }
}
