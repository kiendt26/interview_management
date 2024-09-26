package fa.training.services;

import fa.training.dto.Interview.InterviewSearchByInterviewDTO;
import fa.training.entities.Candidate;
import fa.training.enums.Role;
import fa.training.enums.Status;
import fa.training.repositories.CandidateRepository;
import fa.training.repositories.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    public Page<Candidate> findAll(Pageable pageable) {
        return candidateRepository.findAll(pageable);
    }

    public Optional<Candidate> findById(Long id) {
        return candidateRepository.findById(id);
    }

    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }


    public void deleteById(Long id) {
        candidateRepository.deleteById(id);
    }

    public Page<Candidate> searchCandidates(String keyword, Pageable pageable) {
        if ((keyword == null || keyword.isEmpty())) {
            return findAll(pageable);
        }   else {
            return candidateRepository.findByKeyword(pageable, keyword);
        }
    }

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
}
