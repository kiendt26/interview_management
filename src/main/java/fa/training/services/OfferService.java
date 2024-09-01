package fa.training.services;

import fa.training.dto.OfferAddDTO;
import fa.training.dto.OfferDTO;
import fa.training.entities.Candidate;
import fa.training.entities.Offer;
import fa.training.entities.User;
import fa.training.enums.Status;
import fa.training.repositories.CandidateRepository;
import fa.training.repositories.OfferRepository;
import fa.training.repositories.UserRepository;
import fa.training.utils.DataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class OfferService {
    @Autowired
    private DataHelper helper;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean add(OfferAddDTO add) {
        Candidate candidate = candidateRepository.findById(add.getCandidateId()).orElse(null);

        if (candidate == null) {
            return false;
        }

        User approver = userRepository.findById(add.getApprover()).orElse(null);

        if (approver == null) return false;

        User recruiterOwner = userRepository.findById(add.getRecruiterOwnerId()).orElse(null);

        if (recruiterOwner == null) return false;

        if (add.getContractFrom().isAfter(add.getContractTo())) return false;

        if (add.getBasicSalary().doubleValue() < 0) return false;

        Offer offer = Offer.builder()
                .candidate(candidate)
                .approvalBy(approver)
                .recruiterOwner(recruiterOwner.getUserName())
                .position(add.getPosition())
                .department(add.getDepartment())
                .level(add.getLevel())
                .contractFrom(add.getContractFrom())
                .contractTo(add.getContractTo())
                .dueDate(add.getDueDate())
                .interviewNote(add.getInterviewNote())
                .basicSalary(add.getBasicSalary())
                .note(add.getNote())
                .offerDate(LocalDate.now())
                .status(Status.WAITING)
                .build();

        offerRepository.save(offer);

        return true;
    }

    public boolean update(OfferDTO update) {

        if (update.getContractFrom().isAfter(update.getContractTo()))
            return false;

        Offer offer = offerRepository.findById(update.getOfferId()).orElse(null);

        if (offer == null ||
                offer.getStatus().equals(Status.REJECTED) ||
                offer.getStatus().equals(Status.APPROVED))
            return false;

        User recruiteOwner = userRepository.findByUserName(update.getRecruiterOwner());

        Candidate candidate = null;
        if (!Objects.equals(offer.getCandidate().getCandidateId(), update.getCandidateId())) {
            candidate = candidateRepository.findById(update.getCandidateId()).orElse(null);
        }

        String formattedNote = helper.format(update.getNote());

        // Set fields          |Call to DataHelper  |Old Data                   |New Data
        offer.setCandidate(helper.updateHelper(offer.getCandidate(), candidate));
        offer.setPosition(helper.updateHelper(offer.getPosition(), update.getPosition()));
        offer.setInterviewInfo(helper.updateHelper(offer.getInterviewInfo(), update.getInterviewInfo()));
        offer.setContractFrom(helper.updateHelper(offer.getContractFrom(), update.getContractFrom()));
        offer.setContractTo(helper.updateHelper(offer.getContractTo(), update.getContractTo()));
        offer.setContractType(helper.updateHelper(offer.getContractType(), update.getContractType()));
        offer.setLevel(helper.updateHelper(offer.getLevel(), update.getLevel()));
        offer.setDepartment(helper.updateHelper(offer.getDepartment(), update.getDepartment()));
        offer.setRecruiterOwner(helper.updateHelper(offer.getRecruiterOwner(), recruiteOwner.getUserName()));
        offer.setDueDate(helper.updateHelper(offer.getDueDate(), update.getDueDate()));
        offer.setBasicSalary(helper.updateHelper(offer.getBasicSalary(), update.getBasicSalary()));
        offer.setNote(helper.updateHelper(offer.getNote(), formattedNote));

        offer.getCandidate().setStatus(Status.WAITING);

        offerRepository.save(offer);

        return true;
    }
}
