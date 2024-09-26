package fa.training.controllers;

import fa.training.entities.Candidate;
import fa.training.enums.Status;
import fa.training.services.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping("/list")
    public String listCandidates(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        if (keyword != null && !keyword.isEmpty()) {
            keyword = keyword.trim().replaceAll("\\s+", " ");
        }

        List<Candidate> candidates = candidateService.searchCandidates(keyword);

        model.addAttribute("candidates", candidates);
        model.addAttribute("keyword", keyword);

        return "candidates/candidate-list";
    }

    @GetMapping("/create")
    public String newCandidateForm(Model model) {
        model.addAttribute("recruiters", candidateService.selectByRecruiter());
        model.addAttribute("candidate", new Candidate());
        return "candidates/candidate-create";
    }

    @PostMapping("/addNew")
    public String saveCandidate(@Valid @ModelAttribute Candidate candidate,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("recruiters", candidateService.selectByRecruiter());
            return "candidates/candidate-create";
        }

        if (candidate.getSkills() != null) {
            candidate.setSkills(candidate.getSkills());
        }

        candidateService.save(candidate);
        return "redirect:/candidates/list";
    }

    @GetMapping("/edit/{id}")
    public String editCandidateForm(@PathVariable Long id, Model model) {
        Optional<Candidate> candidate = candidateService.findById(id);
        if (candidate.isPresent()) {
            Candidate c = candidate.get();
            c.setSkillsAsString(c.getSkillsAsString());
            model.addAttribute("candidate", c);
            model.addAttribute("recruiters", candidateService.selectByRecruiter());
            return "candidates/candidate-edit";
        }
        return "redirect:/candidates/list";
    }

    @PostMapping("/update")
    public String updateCandidate(@Valid @ModelAttribute Candidate candidate,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "candidates/candidate-edit";
        }

        if (candidate.getSkills() != null) {
            candidate.setSkills(candidate.getSkills());
        }

        candidateService.save(candidate);
        return "redirect:/candidates/list";
    }


    @GetMapping("/detail/{id}")
    public String detailCandidateForm(@PathVariable Long id, Model model) {
        Optional<Candidate> candidate = candidateService.findById(id);
        if (candidate.isPresent()) {
            Candidate c = candidate.get();
            c.setSkillsAsString(c.getSkillsAsString());
            model.addAttribute("candidate", c);
            return "candidates/candidate-detail";
        }
        return "redirect:/candidates/list";
    }


    @GetMapping("/delete/{id}")
    public String deleteCandidate(@PathVariable Long id) {
        candidateService.deleteById(id);
        return "redirect:/candidates/list";
    }

    @GetMapping("/candidates/search")
    public String searchCandidates(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<Candidate> candidates = candidateService.searchCandidates(keyword);
        model.addAttribute("candidates", candidates);

        return "candidates/candidate-list";
    }

    @PostMapping("/ban/{id}")
    public String banCandidate(@PathVariable Long id) {
        Optional<Candidate> candidateOpt = candidateService.findById(id);
        if (candidateOpt.isPresent()) {
            Candidate candidate = candidateOpt.get();
            candidate.setStatus(Status.BANNED);
            candidateService.save(candidate);
        }
        return "redirect:/candidates/detail/" + id;
    }

}
