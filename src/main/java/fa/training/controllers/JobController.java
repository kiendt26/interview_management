package fa.training.controllers;

import fa.training.dto.JobDTO;
import fa.training.entities.Job;
import fa.training.entities.User;
import fa.training.repositories.JobRepository;
import fa.training.services.JobService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
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
    public String jobList(Model model,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
                          @RequestParam(value = "keyword",required = false) String keyword,
                          @RequestParam(value = "status",required = false) String status) {

        if (keyword != null && !keyword.isEmpty()) {
            keyword = keyword.trim().replaceAll("\\s+", " ");
        }

        Page<JobDTO> jobDTOS = jobService.searchJob(keyword,status,page,size);
        model.addAttribute("jobDTOS", jobDTOS);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        return "job/job-list";
    }

    @GetMapping("/job/job-create")
    public String jobCreate(Model model,@ModelAttribute("dto") JobDTO jobDTO) {
        model.addAttribute("job", new Job());

        return "job/job-create";
    }
    @PostMapping("/job/create")
    public String jobCreate(@Validated @ModelAttribute("dto") JobDTO jobDTO,BindingResult result, Model model) {
        LocalDate localDate = LocalDate.now();

        if(jobDTO.getEndDate().isBefore(localDate)) {
            jobDTO.setStatus("Closed");
            result.rejectValue("endDate", "error.endDate", "End date cannot be in the past");

        }
        if(jobDTO.getStartDate().isAfter(localDate)) {
            jobDTO.setStatus("Open");
            result.rejectValue("startDate", "error.startDate", "Start date cannot be in the past");
        }

        if (result.hasErrors()) {
            return "job/job-create";
        }

        jobDTO.setStatus("Draft");
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
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }
}


