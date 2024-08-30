package fa.training.controllers;

import fa.training.entities.Job;
import fa.training.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class JobController {
    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/job-list")
    public String jobList(Model model) {
        List<Job> jobs = jobRepository.findAll();
        model.addAttribute("listJobs",jobs);
        return "job/job-list";
    }

    @GetMapping("/job-create")
    public String jobCreate(Model model) {
        model.addAttribute("job", new Job());
        return "job/job-create";
    }
}
