package fa.training.controllers;

import fa.training.dto.Interview.InterviewDTO;
import fa.training.dto.Interview.ResetPassDTO;
import fa.training.entities.InterviewSchedule;
import fa.training.entities.PasswordResetToken;
import fa.training.entities.Schedule;
import fa.training.entities.User;
import fa.training.enums.ResultInterview;
import fa.training.enums.StatusInterview;
import fa.training.enums.StatusUser;
import fa.training.repositories.Interview.InterviewScheduleRepository;
import fa.training.repositories.Interview.PasswordResetTokenRepository;
import fa.training.repositories.InterviewRepository;
//import fa.training.repositories.Interview.InterviewScheduleRepository;
import fa.training.repositories.UsersRepository;
import fa.training.services.InterviewServce;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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
            @RequestParam(value = "interview", required = false, defaultValue = "") Long interviewSearch,
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
        int pageSize = 2;

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


    private Page<InterviewDTO> getInterviewListPage(String search, Long interviewSearch, String status, int page, int pageSize ) {

        if(interviewSearch != null || !status.isBlank()){
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
        redirectAttributes.addFlashAttribute("message", "Interview Schedule created successfully!");

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
            model.addAttribute("scheduleDetail", schedule);
            model.addAttribute("jobs", interviewServce.selectByJob());
            model.addAttribute("recruiters", interviewServce.selectByRecruiter());

            return "/interviewer/edit-detail";
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
            RedirectAttributes attributes
    ) {

        interviewServce.submitSchedule(schedule);
        attributes.addFlashAttribute("message", "Interview Schedule submit result success!");

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
        attributes.addFlashAttribute("message","Status schedule change cancel done!");
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
            @RequestParam(name = "email") String email,
            RedirectAttributes attributes,
            Model model
    ){
        User user = usersRepository.findByEmail(email);
        if (user == null) {
            model.addAttribute("emailError", "Email  not exist!");
            model.addAttribute("email", email);
            return "forgot-password";
        }
        if(!user.getStatus().equals(StatusUser.Active) || user.getStatus() == null){
            model.addAttribute("emailError", "User use this email is not active!");
            model.addAttribute("email", email);
            return "forgot-password";
        }

       interviewServce.initiatePasswordReset(email);
       attributes.addFlashAttribute("message","Send link reset password done, check your email");
        return "redirect:/login";
    }

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
//    @GetMapping("/test/{token}")
//    public String test(
//            @PathVariable("token") String token,
//            Model model
//    ){
//        model.addAttribute("token", token);
//        return "test";
//    }

    // thay doi pass moi
    @GetMapping("/reset/reset-password/{token}")
    public String showResetPasswordPage(
            @PathVariable("token") String token,
            Model model,
            @ModelAttribute("reset") ResetPassDTO reset,
            RedirectAttributes attributes
    ) {
        reset.setToken(token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null){
            attributes.addFlashAttribute("error", "Không có đường dẫn này để reset mật khẩu của bạn!");
            return "redirect:/forgot-password";
        }

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            attributes.addFlashAttribute("error", "Đường dẫn này đã hết hạn sử dụng!");
            return "redirect:/forgot-password";
        }
        model.addAttribute("reset", reset);
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset/reset-password-done")
    public String resetPassword(
            Model model,
           @ModelAttribute("reset") ResetPassDTO reset,
            BindingResult result,
            RedirectAttributes attributes
    ){
        //chua it nhat 1 chu cai va 1 so va co it nhat 7 ky tu
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d).{7,}$";
        boolean isValid = Pattern.matches(regex, reset.getNewPass());

        if (reset.getNewPass().isEmpty()) {
            result.rejectValue("newPass", "error.newPass", "*Must fill newPass");
        }
        if (reset.getReNewPass().isEmpty()) {
            result.rejectValue("reNewPass", "error.reNewPass", "*Must fill reNewPass");
        }
        if (!reset.getNewPass().isEmpty() && !reset.getReNewPass().isEmpty() && !reset.getNewPass().equals(reset.getReNewPass())) {
            result.rejectValue("reNewPass", "error.equal", "*Passwords do not match");
        }
        if (!isValid) {
            result.rejectValue("newPass", "error.validatePass", "*Password does not meet criteria");
        }

        if (result.hasErrors()){
            model.addAttribute("token", reset.getToken());
            return "reset-password";
        }

        interviewServce.resetPassword(reset.getToken(),reset.getNewPass());
        attributes.addFlashAttribute("message","Reset password done!");
        return "redirect:/login";
    }
}
