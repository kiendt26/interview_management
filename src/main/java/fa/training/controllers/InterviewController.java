package fa.training.controllers;

import fa.training.entities.Interview;
import fa.training.entities.InterviewSchedule;
import fa.training.repositories.InterviewRepository;
import fa.training.repositories.InterviewScheduleRepository;
import fa.training.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class InterviewController {
//    @GetMapping("/list-interview")
//    public String viewController() {
//        return "interviewer/interview-list";
//    }

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @RequestMapping(value = "/list-interview", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewContent(
            Model model
//            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
//            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword

    ) {
        List<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findName();
        model.addAttribute("interviews", interviewRepository.findAll());
        model.addAttribute("users", usersRepository.findAll());
        model.addAttribute("interviewSchedules", interviewSchedules);
        model.addAttribute("interviewSchedules", interviewScheduleRepository.findAll());
//        int pageSize = 10;
////        Sort sort = Sort.by(Sort.Direction.ASC, "title");
//        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
//
//        Page<Interview> page = null;
//
//        if (keyword.isBlank()) {
//            page = interviewRepository.findAll(pageable);
//        } else {
////            page = interviewRepository.findByTitleLike("%"+keyword+"%", pageable);
//        }


//        // Tinhs list page number
//        int totalPages = page.getTotalPages();
//        List<Integer> pageNums = new ArrayList<>();
//        for (int i = 1; i <= totalPages; i++) {
//            pageNums.add(i);
//        }
//        model.addAttribute("pageNums", pageNums);
//        model.addAttribute("page", page);
        return "interviewer/interview-list";
    }
}
