package fa.training.controllers;

import fa.training.entities.Candidate;
import fa.training.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CandidateController {
    @Autowired
    private CandidateRepository candidateRepository;

    @GetMapping("/candidate-list")
    public String listCandidates(Model model) {
        List<Candidate> candidates = candidateRepository.findAll();
        model.addAttribute("candidates", candidates);
        return "candidate/candidate-list";
    }
    @GetMapping("/candidate-create")
    public String createCandidateForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        return "candidate/candidate-create";
    }
}
