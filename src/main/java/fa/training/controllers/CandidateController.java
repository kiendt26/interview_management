package fa.training.controllers;

import fa.training.entities.Candidate;
import fa.training.enums.Status;
import fa.training.services.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping("/list")
    public String listCandidates(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model)
    {
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        if (keyword != null && !keyword.isEmpty()) {
            keyword = keyword.trim().replaceAll("\\s+", " ");
        }

        Page<Candidate> candidates = candidateService.searchCandidates(keyword, pageable);

        int totalPages = candidates.getTotalPages();
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNums.add(i);
        }
        model.addAttribute("pageNums", pageNums);
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
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            Model model)
    {
        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        Page<Candidate> candidates = candidateService.searchCandidates(keyword, pageable);
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
