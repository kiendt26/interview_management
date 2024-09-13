package fa.training.services;

import fa.training.entities.Candidate;
import fa.training.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    public List<Candidate> findAll() {
        return candidateRepository.findAll();
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

    public List<Candidate> searchCandidates(String keyword, String status) {
        if ((keyword == null || keyword.isEmpty()) && (status == null || status.isEmpty())) {
            return candidateRepository.findAll();
        } else if (status == null || status.isEmpty()) {
            return candidateRepository.findByKeyword(keyword);
        } else if (keyword == null || keyword.isEmpty()) {
            return candidateRepository.findByStatus(status);
        } else {
            return candidateRepository.findByKeywordAndStatus(keyword, status);
        }
    }
}
