package fa.training.controllers;

import fa.training.dto.Interview.InterviewDTO;
import fa.training.entities.InterviewSchedule;
import fa.training.entities.Schedule;
import fa.training.entities.User;
import fa.training.enums.ResultInterview;
import fa.training.enums.StatusInterview;
import fa.training.repositories.Interview.InterviewScheduleRepository;
import fa.training.repositories.InterviewRepository;
//import fa.training.repositories.Interview.InterviewScheduleRepository;
import fa.training.repositories.UsersRepository;
import fa.training.services.InterviewServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class InterviewController {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    private InterviewServce interviewServce;


    @RequestMapping(value = "/list-interview", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewContent(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "interview", required = false, defaultValue = "") String interviewSearch,
            @RequestParam(value = "status", required = false, defaultValue = "") String statusSearch,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String search


    ) {
        model.addAttribute("interviewSearch", interviewServce.searchByInterview());
        String keyword = "%" + search + "%";
        model.addAttribute("keyword", keyword);
        List<StatusInterview> statuses = Arrays.asList(StatusInterview.values());
        model.addAttribute("statuses", statuses);


        Page<InterviewDTO> page = null;
        // số hàng hiện trên 1 trang
        int pageSize = 5;

        Page<InterviewDTO> interviewDTOPage = getInterviewListPage(keyword, interviewSearch, statusSearch, pageNumber, pageSize);

        // Tinhs list page number
        int totalPages = interviewDTOPage.getTotalPages();
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNums.add(i);
        }
        model.addAttribute("pageNums", pageNums);

        model.addAttribute("interviewListPage", interviewDTOPage);

//
        return "interviewer/interview-list";
    }


    private Page<InterviewDTO> getInterviewListPage(String search, String interviewSearch, String status, int page, int pageSize ) {

        if(!interviewSearch.isBlank() || !status.isBlank()){
            return interviewServce.getAllInterviewsBySearch(interviewSearch, status, page, pageSize);
        }
        if(!"%%".equals(search)){
            return interviewServce.getAllInterviewsBySearchAll(search, page, pageSize);
        }

        return interviewServce.getAllInterviews(page, pageSize);
    }



//Create interview schedule
@GetMapping("/create-interview")
public String listInterviews(
        Model model

) {
    model.addAttribute("recruiters", interviewServce.selectByRecruiter());
    model.addAttribute("jobs", interviewServce.selectByJob());
    model.addAttribute("candidateName", interviewServce.selectByCandidate());
    model.addAttribute("idInterviewer", interviewServce.searchByInterview());
    model.addAttribute("addInterview",new Schedule());
    return "interviewer/create-interview";

}
    @PostMapping("/create-interview")
    public String createInterview(
            Model model,
            @Validated()
            @ModelAttribute(name = "addInterview") Schedule schedule,
            @RequestParam(name = "idInterviewer") List<Long> idInterviewer,
            BindingResult result,
            RedirectAttributes redirectAttributes

    ){
        if(result.hasErrors()) {
            return "interviewer/create-interview";
        }
        interviewServce.createNewSchedule(schedule, idInterviewer);
        redirectAttributes.addFlashAttribute("message", "Interview Schedule created successfully");

        return "redirect:/list-interview";
    }




    //Show detail interview
    @GetMapping("/interview/detail")
    public String detailInterview(
            @RequestParam("id") Long idInterviewSchedule,
            Model model
    ){
       InterviewDTO scheduleDB = interviewServce.findByInterviewId(idInterviewSchedule);
        model.addAttribute("idInterviewer", interviewServce.searchByInterview());
        model.addAttribute("scheduleDetail", scheduleDB);

        return "interviewer/interview-detail";
    }

    @GetMapping("/interview/edit")
    public String editInterview(
            @RequestParam("id") Long idInterviewSchedule,
            Model model
    ){


        Schedule scheduleDB = interviewRepository.findById(idInterviewSchedule).orElse(null);

        List<String> userNames = new ArrayList<>();
        for (InterviewSchedule interview : scheduleDB.getInterviewScheduleList()) {
            userNames.add(interview.getInterview().getUserName());
        }

        List<ResultInterview> result = Arrays.asList(ResultInterview.values());

        model.addAttribute("results", result);
        model.addAttribute("userNames", userNames);
        model.addAttribute("idInterviewer", interviewServce.searchByInterview());
        model.addAttribute("candidateName", interviewServce.selectByCandidate());
        model.addAttribute("scheduleDetail", scheduleDB);
        model.addAttribute("jobs", interviewServce.selectByJob());
        model.addAttribute("recruiters", interviewServce.selectByRecruiter());
        return "interviewer/edit-detail";
    }

    @PostMapping("/interview/edit-schedule")
    public String edit(
            @ModelAttribute("scheduleDetail") Schedule schedule,
            @RequestParam(name = "userNames") List<Long> idInterviewer,
            RedirectAttributes attributes,
            BindingResult result
    ) {

        interviewServce.updateSchedule(schedule, idInterviewer);
//        redirectAttributes.addFlashAttribute("message", "Interview Schedule created successfully");

        return "redirect:/list-interview";
    }


    @GetMapping("/interview/submit")
    public String submitInterview(
            @RequestParam("id") Long idInterviewSchedule,
            Model model
    ){
        List<ResultInterview> result = Arrays.asList(ResultInterview.values());
        model.addAttribute("results", result);
        InterviewDTO scheduleDB = interviewServce.findByInterviewId(idInterviewSchedule);
        model.addAttribute("idInterviewer", interviewServce.searchByInterview());
        model.addAttribute("scheduleDetail", scheduleDB);

        return "interviewer/submit-result";
    }

    @PostMapping("/interview/submit-schedule")
    public String submit(
            @ModelAttribute("scheduleDetail") InterviewDTO schedule,
            RedirectAttributes attributes,
            BindingResult result
    ) {

        interviewServce.submitSchedule(schedule);
//        redirectAttributes.addFlashAttribute("message", "Interview Schedule created successfully");

        return "redirect:/list-interview";
    }

    @GetMapping("/interview/cancel")
    public String cancelInterview(

            @RequestParam("id") Long id,
            RedirectAttributes attributes
    ){
        InterviewDTO schedule = new InterviewDTO();
        schedule.setInterviewId(id);
        interviewServce.cancelSchedule(schedule);
        return "redirect:/list-interview";
    }

    @GetMapping("/send-email")
    public String sendEmail(
            @RequestParam(name = "userNames") List<String> userNames) {
        interviewServce.sendEmail(userNames);
        return "redirect:/list-interview";
    }

    @GetMapping("/login")
    public String login(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    //forgot-pass
    @GetMapping("/forgot-password")
    public String forgot(
           Model model,
           String email
    ){

        model.addAttribute("email", email);
        return "forgot-password";
    }

    @PostMapping("/send")
    public String sendForgot(
            @RequestParam(name = "email") String email
    ){
       interviewServce.initiatePasswordReset(email);
        return "redirect:/login";
    }
}
