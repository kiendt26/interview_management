package fa.training.controllers;

import fa.training.entities.Candidate;
import fa.training.services.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public String listCandidates(Model model) {
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates/list";
    }

    @GetMapping("/new")
    public String newCandidateForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        return "candidates/form";
    }

    @PostMapping
    public String saveCandidate(@Valid @ModelAttribute Candidate candidate, BindingResult result) {
        if (result.hasErrors()) {
            return "candidates/form";
        }
        candidateService.save(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/edit/{id}")
    public String editCandidateForm(@PathVariable Long id, Model model) {
        Optional<Candidate> candidate = candidateService.findById(id);
        if (candidate.isPresent()) {
            model.addAttribute("candidate", candidate.get());
            return "candidates/form";
        }
        return "redirect:/candidates";
    }

    @GetMapping("/delete/{id}")
    public String deleteCandidate(@PathVariable Long id) {
        candidateService.deleteById(id);
        return "redirect:/candidates";
    }
}
