package fa.training.controllers;

import fa.training.dto.JobDTO;
import fa.training.entities.Job;
import fa.training.repositories.JobRepository;
import fa.training.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@Controller
public class JobController {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }




    @GetMapping("/job/job-list")
    public String jobList(Model model) {
        List<JobDTO> jobs = jobService.getAll();
//        jobs.forEach(j -> j.setSkillsDTO(new String[]{Arrays.toString(j.getSkillsDTO())}));
//        jobs.forEach(j -> j.setLevelDTO(new String[]{Arrays.toString(j.getLevelDTO())}));
//        jobs.forEach(j -> j.setBenefitDTO(new String[]{Arrays.toString(j.getBenefitDTO())}));


        model.addAttribute("listJobs",jobs);
        return "job/job-list";
    }

    @GetMapping("/job/job-create")
    public String jobCreate(Model model) {
        model.addAttribute("job", new Job());
        return "job/job-create";
    }
    @PostMapping("/job/create")
    public String jobCreate(@Validated @ModelAttribute("dto") JobDTO jobDTO,BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "job/job-create";
        }
        jobService.save(jobDTO);
        return "redirect:/job/job-list";
    }
    @GetMapping("/job/job-delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        JobDTO jobDTO =  jobService.delete(id);
        return "redirect:/job/job-list";
    }

    @GetMapping("/job/job-detail/{id}")
    public String edit(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        JobDTO jobDTO =  jobService.findById(id);
        model.addAttribute("dto",jobDTO);
        return "/job/job-detail";
    }

    @GetMapping("/job/job-edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        JobDTO jobDTO =  jobService.findById(id);
        model.addAttribute("dto",jobDTO);
        return "job/job-edit";
    }
    @PostMapping("/job/job-edit")
    public String edit(@Validated @ModelAttribute("dto") JobDTO jobDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "job/job-edit";
        }
        jobService.save(jobDTO);
        return "redirect:/job/job-list";
    }
}


