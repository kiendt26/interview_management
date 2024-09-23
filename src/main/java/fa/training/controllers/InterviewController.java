package fa.training.controllers;

import fa.training.dto.Interview.InterviewDTO;
import fa.training.entities.InterviewSchedule;
import fa.training.entities.PasswordResetToken;
import fa.training.entities.Schedule;
import fa.training.entities.User;
import fa.training.enums.ResultInterview;
import fa.training.enums.StatusInterview;
import fa.training.repositories.Interview.InterviewScheduleRepository;
import fa.training.repositories.Interview.PasswordResetTokenRepository;
import fa.training.repositories.InterviewRepository;
//import fa.training.repositories.Interview.InterviewScheduleRepository;
import fa.training.repositories.UsersRepository;
import fa.training.services.InterviewServce;
import jakarta.validation.Valid;
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

// show list interview schedule
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
        int pageSize = 1;

        Page<InterviewDTO> interviewDTOPage = getInterviewListPage(keyword, interviewSearch, statusSearch, pageNumber, pageSize);

        // Tinhs list page number
        int totalPages = interviewDTOPage.getTotalPages();
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNums.add(i);
        }
        model.addAttribute("pageNums", pageNums);
        model.addAttribute("interviewListPage", interviewDTOPage);
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

            @Valid
            @ModelAttribute(name = "addInterview") Schedule schedule,
            BindingResult result,
            Model model,
            @RequestParam(name = "idInterviewer", required = false) List<Long> idInterviewer,
            RedirectAttributes redirectAttributes

    ){
        if (idInterviewer == null || idInterviewer.isEmpty()) {
            result.rejectValue("interviewScheduleList", "error.interviewScheduleList", "*Must selected interviewer");
        }
        if (schedule.getCandidate().getCandidateId() == null) {
            result.rejectValue("candidate", "error.candidate", "*Candidate must be selected.");
        }
        if (schedule.getJob().getJobId() == null) {
            result.rejectValue("job", "error.job", "*Job must be selected.");
        }
        if (schedule.getRecruiter().getUserId() == null) {
            result.rejectValue("recruiter", "error.recruiter", "*Recruiter must be selected.");
        }

        if(result.hasErrors() ) {
            model.addAttribute("recruiters", interviewServce.selectByRecruiter());
            model.addAttribute("jobs", interviewServce.selectByJob());
            model.addAttribute("candidateName", interviewServce.selectByCandidate());
            model.addAttribute("idInterviewer", interviewServce.searchByInterview());
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

    //edit schedule
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
            @Valid
            @ModelAttribute("scheduleDetail") Schedule schedule,
            BindingResult bindingResult,
            @RequestParam(name = "userNames", required = false) List<Long> idInterviewer,
            RedirectAttributes attributes,
            Model model

    ) {
        if (idInterviewer == null || idInterviewer.isEmpty()) {
            bindingResult.rejectValue("interviewScheduleList", "error.interviewScheduleList", "*Must selected interviewer");
        }
        if (schedule.getCandidate().getFullname() == null) {
            bindingResult.rejectValue("candidate", "error.candidate", "*Candidate must be selected.");
        }
        if (schedule.getJob().getJobId() == null) {
            bindingResult.rejectValue("job", "error.job", "*Job must be selected.");
        }
        if (schedule.getRecruiter().getUserId() == null) {
            bindingResult.rejectValue("recruiter", "error.recruiter", "*Recruiter must be selected.");
        }

        if (bindingResult.hasErrors()){
            Schedule scheduleDB = interviewRepository.findById(schedule.getScheduleId()).orElse(null);

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
        interviewServce.updateSchedule(schedule, idInterviewer);
        attributes.addFlashAttribute("message", "Interview Schedule edit success");

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
        attributes.addFlashAttribute("message", "Interview Schedule submit result success");

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
        attributes.addFlashAttribute("message","Status schedule change cancel done");
        return "redirect:/list-interview";
    }

    @GetMapping("/send-email")
    public String sendEmail(
            @RequestParam(name = "userNames") List<String> userNames,
            @RequestParam(name = "interviewId") Long interviewID,
            RedirectAttributes redirect

    ) {
        interviewServce.sendEmail(userNames, interviewID);
        redirect.addFlashAttribute("message","Send reminder to Interviewer success! ");
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
    //gửi mail lấy link để điền mk mới
    @PostMapping("/send")
    public String sendForgot(
            @RequestParam(name = "email") String email
    ){
       interviewServce.initiatePasswordReset(email);
        return "redirect:/login";
    }

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            model.addAttribute("error", "Token không tồn tại.");
            return "reset-password"; // Trả về trang với thông báo lỗi
        }

        // Kiểm tra xem token có hết hạn không
//        if (userService.isTokenExpired(passwordResetToken)) {
//            model.addAttribute("error", "Token đã hết hạn.");
//            return "reset-password"; // Trả về trang với thông báo lỗi
//        }

        // Nếu token hợp lệ, truyền token đến view để sử dụng khi đặt lại mật khẩu
        model.addAttribute("token", token);
        return "login"; // Trả về trang nơi người dùng có thể nhập mật khẩu mới
    }
}
