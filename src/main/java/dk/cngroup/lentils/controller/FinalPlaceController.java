package dk.cngroup.lentils.controller;

import dk.cngroup.lentils.dto.FinalPlaceFormDTO;
import dk.cngroup.lentils.entity.FinalPlace;
import dk.cngroup.lentils.service.FinalPlaceService;
import dk.cngroup.lentils.service.convertors.ModelMapperWrapper;
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
    private static final String VIEW_ADMIN_FINALPLACE_FORM = "finalplace/form";
    private static final String REDIRECT_ADMIN_FINALPLACE = "redirect:/admin/finalplace/";

    private static final String TEMPLATE_ATTR_FINALPLACE = "finalPlace";

    private final FinalPlaceService finalPlaceService;
    private final ModelMapperWrapper mapper;

    @Autowired
    public FinalPlaceController(final FinalPlaceService finalPlaceService,
                                final ModelMapperWrapper modelMapperWrapper) {
        this.finalPlaceService = finalPlaceService;
        this.mapper = modelMapperWrapper;
    }

    @GetMapping(value = "/")
    public String finalPlace(final Model model) {
        FinalPlaceFormDTO finalPlaceFormDto = mapper.map(finalPlaceService.getFinalPlace(), FinalPlaceFormDTO.class);
        model.addAttribute(TEMPLATE_ATTR_FINALPLACE, finalPlaceFormDto);
        model.addAttribute(
                "serverTime",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return VIEW_ADMIN_FINALPLACE_FORM;
    }

    @PostMapping(value = "/update")
    public String saveFinalPlace(@Valid @ModelAttribute("finalPlace") final FinalPlaceFormDTO finalPlaceFormDto,
                                 final BindingResult bindingResult,
                                 final Model model) {
        FinalPlace finalPlace = mapper.map(finalPlaceFormDto, FinalPlace.class);
        finalPlaceService.checkFinishTimeBeforeResultsTime(bindingResult, finalPlace);
        if (bindingResult.hasErrors()) {
            model.addAttribute(TEMPLATE_ATTR_FINALPLACE, finalPlaceFormDto);
            return VIEW_ADMIN_FINALPLACE_FORM;
        }
        finalPlaceService.save(finalPlace);
        return REDIRECT_ADMIN_FINALPLACE;
    }
}
