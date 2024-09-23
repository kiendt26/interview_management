package fa.training.controllers;

import fa.training.entities.Candidate;
import fa.training.services.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        if (keyword != null && !keyword.isEmpty()) {
            keyword = keyword.trim().replaceAll("\\s+", " ");
        }

        List<Candidate> candidates = candidateService.searchCandidates(keyword, status);

        model.addAttribute("candidates", candidates);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        return "candidates/list";
    }

    @GetMapping("/create")
    public String newCandidateForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        return "candidates/create";
    }

    @PostMapping("/addNew")
    public String saveCandidate(@Valid @ModelAttribute Candidate candidate, BindingResult result) {


        if (result.hasErrors()) {
            return "candidates/create";
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
            return "candidates/create";
        }
        return "redirect:/candidates/list";
    }

    @GetMapping("/detail/{id}")
    public String detailCandidateForm(@PathVariable Long id, Model model) {
        Optional<Candidate> candidate = candidateService.findById(id);
        if (candidate.isPresent()) {
            Candidate c = candidate.get();
            c.setSkillsAsString(c.getSkillsAsString());
            model.addAttribute("candidate", c);
            model.addAttribute("readonly", true); // Thêm thuộc tính để hiển thị form chỉ đọc
            return "candidates/detail"; // Sử dụng template riêng cho detail
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
            @RequestParam(value = "status", required = false) String status,
            Model model) {

        List<Candidate> candidates = candidateService.searchCandidates(keyword, status);
        model.addAttribute("candidates", candidates);

        return "candidates/list";
    }


}
