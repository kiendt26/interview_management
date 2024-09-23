package fa.training.controllers;

import fa.training.entities.Candidate;
import fa.training.enums.Status;
import fa.training.services.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

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
    public String saveCandidate(@Valid @ModelAttribute Candidate candidate,
                                BindingResult result,
                                RedirectAttributes redirectAttributes
//                                @RequestParam("attachment") MultipartFile file
                                ) {
        if (result.hasErrors()) {
            return "candidates/create";
        }

        if (candidate.getSkills() != null) {
            candidate.setSkills(candidate.getSkills());
        }

//        if (!file.isEmpty()) {
//            try {
//                // Lấy tên file và làm sạch để tránh ký tự nguy hiểm
//                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//                // Đường dẫn lưu file vào ổ D
//                String uploadDir = "D:/attachments/"; // Đảm bảo thư mục này đã tồn tại
//                Path uploadPath = Paths.get(uploadDir);
//
//                // Tạo thư mục nếu chưa tồn tại
//                if (!Files.exists(uploadPath)) {
//                    Files.createDirectories(uploadPath);
//                }
//
//                // Lưu file vào thư mục
//                Path filePath = uploadPath.resolve(fileName);
//                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                // Gán tên file vào candidate
//                candidate.setAttachment(fileName);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                redirectAttributes.addFlashAttribute("message", "File upload failed!");
//                return "redirect:/candidates/create";
//            }
//        }

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
