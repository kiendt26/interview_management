package fa.training.services;

import fa.training.dto.Interview.InterviewDTO;
import fa.training.dto.Interview.InterviewSearchByInterviewDTO;
import fa.training.entities.*;
import fa.training.enums.ResultInterview;
import fa.training.enums.Role;
import fa.training.enums.Status;
import fa.training.enums.StatusInterview;
import fa.training.exception.InvalidTokenException;
import fa.training.repositories.CandidateRepository;
import fa.training.repositories.Interview.InterviewScheduleRepository;
import fa.training.repositories.Interview.PasswordResetTokenRepository;
import fa.training.repositories.InterviewRepository;
import fa.training.repositories.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
@Service
public class InterviewServce {
    private final InterviewRepository interviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository userRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final CandidateRepository candidateRepository;
    private final JavaMailSender mailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public  InterviewServce(
            InterviewRepository interviewRepository,
            PasswordEncoder passwordEncoder,
            UsersRepository userRepository,
            InterviewScheduleRepository interviewScheduleRepository,
            CandidateRepository candidateRepository,
            JavaMailSender mailSender,
            PasswordResetTokenRepository passwordResetTokenRepository
    ){
        this.interviewRepository = interviewRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.interviewScheduleRepository = interviewScheduleRepository;
        this.candidateRepository = candidateRepository;
        this.mailSender = mailSender;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }
    //read all
    public Page<InterviewDTO> getAllInterviews(int pageNumber, int pageSize ) {

        Pageable pageable = PageRequest.of(pageNumber-1,pageSize);
        List<Object[]> rawResults = interviewRepository.findAllInterviewsSchedule();
        Map<String, InterviewDTO> interviewMap = new LinkedHashMap<>();

        for (Object[] rawResult : rawResults) {
            Long interviewId = (Long) rawResult[0];
            String interviewTitle = (String) rawResult[1];
            String candidateName = (String) rawResult[2];
            String interviewerName = (String) rawResult[3];
            LocalDate scheduleDate = (LocalDate) rawResult[4];
            LocalTime scheduleFrom = (LocalTime) rawResult[5];
            LocalTime scheduleTo = (LocalTime) rawResult[6];
            ResultInterview result =( ResultInterview ) rawResult[7] ;
            StatusInterview status = (StatusInterview) rawResult[8];
            String jobTitle = (String) rawResult[9];

            // Tạo khóa cho nhóm dựa trên thông tin của Interview
            String key = interviewId + interviewTitle + candidateName  + scheduleDate + scheduleFrom + scheduleTo + result + status + jobTitle;

            if (!interviewMap.containsKey(key)) {
                // Tạo một đối tượng DTO mới nếu chưa có
                InterviewDTO interviewDTO = new InterviewDTO(
                        interviewId,
                        candidateName,
                        jobTitle,
                        result,
                        scheduleDate,
                        scheduleFrom,
                        scheduleTo,
                        status,
                        interviewTitle,
                        new ArrayList<>()
                );
                interviewMap.put(key, interviewDTO);
            }

            // Thêm tên người dùng vào danh sách trong DTO
            InterviewDTO existingDTO = interviewMap.get(key);
            if (existingDTO != null) {
                existingDTO.getUserNames().add(interviewerName);
            }
        }

        List<InterviewDTO> interviewDTOList = new ArrayList<>(interviewMap.values());

        // Xác định các chỉ số start và end dựa trên Pageable
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), interviewDTOList.size());

        // Tạo đối tượng Page<InterviewDTO> bằng cách sử dụng PageImpl
        List<InterviewDTO> pageList = interviewDTOList.subList(start, end);
        return new PageImpl<>(pageList, pageable, interviewDTOList.size());

    }

    //read one
    public InterviewDTO findByInterviewId(Long interviewId) {
        // Lấy kết quả raw từ repository dựa theo interviewId
        List<Object[]> rawResults = interviewRepository.findScheduleByScheduleId(interviewId);

        if (rawResults.isEmpty()) {
            return null; // Nếu không có kết quả, trả về null hoặc có thể ném ngoại lệ tùy yêu cầu
        }

        // Tạo DTO cho buổi phỏng vấn
        InterviewDTO interviewDTO = null;

        for (Object[] rawResult : rawResults) {
            String interviewTitle = (String) rawResult[1];
            String candidateName = (String) rawResult[2];
            String interviewerName = (String) rawResult[3];
            LocalDate scheduleDate = (LocalDate) rawResult[4];
            LocalTime scheduleFrom = (LocalTime) rawResult[5];
            LocalTime scheduleTo = (LocalTime) rawResult[6];
            ResultInterview result = (ResultInterview) rawResult[7];
            StatusInterview status = (StatusInterview) rawResult[8];
            String jobTitle = (String) rawResult[9];
            String meetingId = (String) rawResult[10];
            String note = (String) rawResult[11];
            String location = (String) rawResult[12];
            String recruiter = (String) rawResult[13];

            // Nếu interviewDTO chưa được khởi tạo, tạo đối tượng mới
            if (interviewDTO == null) {
                interviewDTO = new InterviewDTO(
                        interviewId,
                        candidateName,
                        jobTitle,
                        result,
                        scheduleDate,
                        scheduleFrom,
                        scheduleTo,
                        status,
                        interviewTitle,
                        new ArrayList<>(),
                        meetingId,
                        note,
                        location,
                        recruiter
                );
            }

            // Thêm tên người phỏng vấn vào danh sách trong DTO
            interviewDTO.getUserNames().add(interviewerName);
        }

        return interviewDTO; // Trả về đối tượng DTO
    }


    public Page<InterviewDTO> getAllInterviewsBySearch(Long interviewer, String status, int pageNumber, int pageSize) {

        StatusInterview statusEnum = null;
        if (status != null && !status.isEmpty()) {
                statusEnum = StatusInterview.valueOf(status);
        }

        Pageable pageable = PageRequest.of(pageNumber-1,pageSize);
        List<Object[]> rawResults = interviewRepository.findAllScheduleByIntereviewAndStatus(interviewer, statusEnum);
        Map<String, InterviewDTO> interviewMap = new LinkedHashMap<>();

        for (Object[] rawResult : rawResults) {
            Long interviewId = (Long) rawResult[0];
            String interviewTitle = (String) rawResult[1];
            String candidateName = (String) rawResult[2];
            String interviewerName = (String) rawResult[3];
            LocalDate scheduleDate = (LocalDate) rawResult[4];
            LocalTime scheduleFrom = (LocalTime) rawResult[5];
            LocalTime scheduleTo = (LocalTime) rawResult[6];
            ResultInterview result =( ResultInterview ) rawResult[7] ;
            statusEnum = (StatusInterview) rawResult[8];
            String jobTitle = (String) rawResult[9];

            // Tạo khóa cho nhóm dựa trên thông tin của Interview
            String key = interviewId + interviewTitle + candidateName + scheduleDate + scheduleFrom + scheduleTo + result + status + jobTitle;

            if (!interviewMap.containsKey(key)) {
                // Tạo một đối tượng DTO mới nếu chưa có
                InterviewDTO interviewDTO = new InterviewDTO(
                        interviewId,
                        candidateName,
                        jobTitle,
                        result,
                        scheduleDate,
                        scheduleFrom,
                        scheduleTo,
                        statusEnum,
                        interviewTitle,
                        new ArrayList<>()
                );
                interviewMap.put(key, interviewDTO);
            }

            // Thêm tên người dùng vào danh sách trong DTO
            InterviewDTO existingDTO = interviewMap.get(key);
            if (existingDTO != null) {
                existingDTO.getUserNames().add(interviewerName);
            }
        }

        List<InterviewDTO> interviewDTOList = new ArrayList<>(interviewMap.values());

        // Xác định các chỉ số start và end dựa trên Pageable
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), interviewDTOList.size());

        // Tạo đối tượng Page<InterviewDTO> bằng cách sử dụng PageImpl
        List<InterviewDTO> pageList = interviewDTOList.subList(start, end);

        return new PageImpl<>(pageList, pageable, interviewDTOList.size());

    }





    // selection interview
    public List<InterviewSearchByInterviewDTO> searchByInterview() {
        List<Object[]> rawResults = interviewRepository.searchByInterview(Role.Interview);
        List<InterviewSearchByInterviewDTO> userDTOs = new ArrayList<>();

        for (Object[] result : rawResults) {
            Long userId = (Long) result[0];
            String username = (String) result[1];
            userDTOs.add(new InterviewSearchByInterviewDTO(userId, username));
        }

        return userDTOs;
    }

    // selection candidate
    public List<InterviewSearchByInterviewDTO> selectByCandidate() {
        List<Object[]> rawResults = interviewRepository.searchByCandidate(Status.OPEN);
        List<InterviewSearchByInterviewDTO> candidateDTOs = new ArrayList<>();

        for (Object[] result : rawResults) {
            Long candidateId = (Long) result[0];
            String fullName = (String) result[1];
            candidateDTOs.add(new InterviewSearchByInterviewDTO(candidateId, fullName));
        }

        return candidateDTOs;
    }

    // selection job
    public List<InterviewSearchByInterviewDTO> selectByJob() {
        String statusJob = "Open";
        List<Object[]> rawResults = interviewRepository.selectByJob(statusJob);
        List<InterviewSearchByInterviewDTO> jobDTOs = new ArrayList<>();

        for (Object[] result : rawResults) {
            Long jobId = (Long) result[0];
            String jobTitle = (String) result[1];
            jobDTOs.add(new InterviewSearchByInterviewDTO(jobId, jobTitle));
        }

        return jobDTOs;
    }

    // selection recruiter
    public List<InterviewSearchByInterviewDTO> selectByRecruiter() {
        List<Object[]> rawResults = interviewRepository.selectByRecruiter(Role.Recruiter);
        List<InterviewSearchByInterviewDTO> userDTOs = new ArrayList<>();

        for (Object[] result : rawResults) {
            Long userId = (Long) result[0];
            String username = (String) result[1];
            userDTOs.add(new InterviewSearchByInterviewDTO(userId, username));
        }

        return userDTOs;
    }

    public Page<InterviewDTO> getAllInterviewsBySearchAll(String keyword, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber-1,pageSize);
        List<Object[]> rawResults = interviewRepository.findAllScheduleByKeyword(keyword);
        Map<String, InterviewDTO> interviewMap = new LinkedHashMap<>();

        for (Object[] rawResult : rawResults) {
            Long interviewId = (Long) rawResult[0];
            String interviewTitle = (String) rawResult[1];
            String candidateName = (String) rawResult[2];
            String interviewerName = (String) rawResult[3];
            LocalDate scheduleDate = (LocalDate) rawResult[4];
            LocalTime scheduleFrom = (LocalTime) rawResult[5];
            LocalTime scheduleTo = (LocalTime) rawResult[6];
            ResultInterview result =( ResultInterview ) rawResult[7] ;
            StatusInterview statusEnum = (StatusInterview) rawResult[8];
            String jobTitle = (String) rawResult[9];

            // Tạo khóa cho nhóm dựa trên thông tin của Interview
            String key = interviewId + interviewTitle + candidateName + scheduleDate + scheduleFrom + scheduleTo + result + statusEnum + jobTitle;

            if (!interviewMap.containsKey(key)) {
                // Tạo một đối tượng DTO mới nếu chưa có
                InterviewDTO interviewDTO = new InterviewDTO(
                        interviewId,
                        candidateName,
                        jobTitle,
                        result,
                        scheduleDate,
                        scheduleFrom,
                        scheduleTo,
                        statusEnum,
                        interviewTitle,
                        new ArrayList<>()
                );
                interviewMap.put(key, interviewDTO);
            }

            // Thêm tên người dùng vào danh sách trong DTO
            InterviewDTO existingDTO = interviewMap.get(key);
            if (existingDTO != null) {
                existingDTO.getUserNames().add(interviewerName);
            }
        }

        List<InterviewDTO> interviewDTOList = new ArrayList<>(interviewMap.values());

        // Xác định các chỉ số start và end dựa trên Pageable
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), interviewDTOList.size());

        // Tạo đối tượng Page<InterviewDTO> bằng cách sử dụng PageImpl
        List<InterviewDTO> pageList = interviewDTOList.subList(start, end);

        return new PageImpl<>(pageList, pageable, interviewDTOList.size());

    }


    public void createNewSchedule(Schedule newSchedule, List<Long> interviewIds) {
        // Thêm schedule mới
        Schedule savedSchedule =   interviewRepository.save(newSchedule);
        savedSchedule.setResult(ResultInterview.NA);
        savedSchedule.setStatus(StatusInterview.New);

         interviewRepository.save(savedSchedule);

        Candidate updateStatusCandidate = candidateRepository.findById(savedSchedule.getCandidate().getCandidateId()).orElse(null);
        updateStatusCandidate.setStatus(Status.WAITING_FOR_INTERVIEW);
        candidateRepository.save(updateStatusCandidate);

        List<Long> interviewDTO= interviewIds;
        for (Long interviewId : interviewDTO) {
            InterviewSchedule interviewSchedule = new InterviewSchedule();
            interviewSchedule.setSchedule(savedSchedule);

            User user = userRepository.findById(interviewId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            interviewSchedule.setInterview(user);
            interviewScheduleRepository.save(interviewSchedule);



        }

    }

    //edit
    public void updateSchedule(Schedule newSchedule, List<Long> interviewIds) {
        if (newSchedule != null){
            newSchedule.setStatus(StatusInterview.New);
            Candidate updateStatusCandidate = candidateRepository.findById(newSchedule.getCandidate().getCandidateId()).orElse(null);
            updateStatusCandidate.setStatus(Status.WAITING_FOR_INTERVIEW);
            if (newSchedule.getResult() != ResultInterview.NA) {
                newSchedule.setStatus(StatusInterview.Interviewed);
                if (newSchedule.getResult().equals(ResultInterview.FAILDED)){
                    updateStatusCandidate.setStatus(Status.FAILED_INTERVIEW);

                }
                if (newSchedule.getResult().equals(ResultInterview.PASS)){
                    updateStatusCandidate.setStatus(Status.PASSED_INTERVIEW);

                }
            }
            interviewRepository.save(newSchedule);

            candidateRepository.save(updateStatusCandidate);
        }

        //delete List interview cũ để add lại list mới
        List<InterviewSchedule>  interviewScheduleList = interviewScheduleRepository.findBySchedule(newSchedule);
        if(interviewScheduleList != null){
            interviewScheduleRepository.deleteAll(interviewScheduleList);
        }


        List<Long> interviewDTO= interviewIds;
        for (Long interviewId : interviewDTO) {
            InterviewSchedule interviewSchedule = new InterviewSchedule();
            interviewSchedule.setSchedule(newSchedule);

            User user = userRepository.findById(interviewId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            interviewSchedule.setInterview(user);

            interviewScheduleRepository.save(interviewSchedule);

        }

    }
    //submit
    @Validated
    public void submitSchedule(InterviewDTO newSchedule){
        if (newSchedule != null){
            Schedule schedule = interviewRepository.findById(newSchedule.getInterviewId()).orElse(null);
            if (schedule != null){
                schedule.setResult(newSchedule.getResult());
                schedule.setNote(newSchedule.getNote());
                schedule.setStatus(StatusInterview.New);
                if (schedule.getResult() != ResultInterview.NA) {
                    schedule.setStatus(StatusInterview.Interviewed);
                }
                interviewRepository.save(schedule);

                Candidate updateStatusCandidate = candidateRepository.findById(schedule.getCandidate().getCandidateId()).orElse(null);
                if (newSchedule.getResult().equals(ResultInterview.FAILDED)){
                    updateStatusCandidate.setStatus(Status.FAILED_INTERVIEW);
                }
                if (newSchedule.getResult().equals(ResultInterview.PASS)){
                    updateStatusCandidate.setStatus(Status.PASSED_INTERVIEW);

                }
                candidateRepository.save(updateStatusCandidate);
            }
        }
    }

    //cancel
    public void cancelSchedule(InterviewDTO schedule) {
        if (schedule != null){
            Schedule scheduleDB = interviewRepository.findById(schedule.getInterviewId()).orElse(null);
            if (scheduleDB != null){
                scheduleDB.setStatus(StatusInterview.Cancelled);
                interviewRepository.save(scheduleDB);
                Candidate updateStatusCandidate = candidateRepository.findById(scheduleDB.getCandidate().getCandidateId()).orElse(null);
                if (updateStatusCandidate != null){
                    updateStatusCandidate.setStatus(Status.CANCELLED_INTERVIEW);
                    candidateRepository.save(updateStatusCandidate);
                }

            }


        }

    }


    //send mail to interviewer


    public void sendEmail(List<String> interviewer, Long interviewID) {

        List<String> emailList = userRepository.findEmailByUserNameIn(interviewer);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Schedule Interview");

        String linkSchedule = "Link view schedule detail: http://localhost:8080/interview/detail?id="+interviewID;
        message.setText(linkSchedule);
//        message.setFrom("recruiter1.com"); // Địa chỉ email của bạn

        // Chuyển đổi danh sách người nhận thành mảng
        String[] recipientArray = emailList.toArray(new String[0]);
        message.setTo(recipientArray);

        mailSender.send(message);

        // Cập nhật status
        Schedule schedule = interviewRepository.findById(interviewID).orElse(null);
        if (schedule != null){
            schedule.setStatus(StatusInterview.Invited);
            interviewRepository.save(schedule);
        }

    }

    //tao token để lấy lại mật khẩu và gửi link để lấy lại mat khau
    public void initiatePasswordReset(String email){
        User user = userRepository.findByEmail(email);
        

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusDays(1));

        passwordResetTokenRepository.save(passwordResetToken);

        String resetLink = "http://localhost:8080/reset/reset-password/" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Reset Password");
        message.setText("To reset your password, click the link below:\n" + resetLink);
        mailSender.send(message);

    }


    //set lại password
    public void resetPassword(String token, String newPass){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        User userDB = userRepository.findById(passwordResetToken.getUser().getUserId()).orElse(null);
        String encodePass = passwordEncoder.encode(newPass);
        if (userDB!= null){
            userDB.setPasswordHash(encodePass);
            userRepository.save(userDB);
        }


        passwordResetToken.setExpiryDate(LocalDateTime.now().minusDays(1));
        passwordResetTokenRepository.save(passwordResetToken);
    }
}

