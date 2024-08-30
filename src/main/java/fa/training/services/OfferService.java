package fa.training.services;

import fa.training.dto.AddOfferDTO;
import fa.training.entities.Candidate;
import fa.training.entities.Offer;
import fa.training.entities.User;
import fa.training.repositories.CandidateRepository;
import fa.training.repositories.OfferRepository;
import fa.training.repositories.UserRepository;
import fa.training.utils.DataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean update(AddOfferDTO update) {

        Offer offer = offerRepository.findById(update.getOfferId()).orElse(null);

        if (offer == null) return false;

        User recruiteOwner =  userRepository.findByUserName(update.getRecruiterOwner());

        Candidate candidate = null;
        if (!Objects.equals(offer.getCandidate().getCandidateId(), update.getCandidateId())) {
            candidate = candidateRepository.findById(update.getCandidateId()).orElse(null);
        }

        String formattedInterviewNote = helper.format(update.getInterviewNote());
        String formattedNote = helper.format(update.getNote());

        // Set fields          |Call to DataHelper  |Old Data                   |New Data
        offer.setCandidate(     helper.updateHelper( offer.getCandidate(),       candidate));
        offer.setPosition(      helper.updateHelper( offer.getPosition(),        update.getPosition()));
        offer.setInterviewInfo( helper.updateHelper( offer.getInterviewInfo(),   update.getInterviewInfo()));
        offer.setContractFrom(  helper.updateHelper( offer.getContractFrom(),    update.getContractFrom()));
        offer.setContractTo(    helper.updateHelper( offer.getContractTo(),      update.getContractTo()));
        offer.setInterviewNote( helper.updateHelper( offer.getInterviewNote(),   formattedInterviewNote));
        offer.setContractType(  helper.updateHelper( offer.getContractType(),    update.getContractType()));
        offer.setLevel(         helper.updateHelper( offer.getLevel(),           update.getLevel()));
        offer.setDepartment(    helper.updateHelper( offer.getDepartment(),      update.getDepartment()));
        offer.setRecruiterOwner(helper.updateHelper( offer.getRecruiterOwner(),  recruiteOwner.getUserName()));
        offer.setDueDate(       helper.updateHelper( offer.getDueDate(),         update.getDueDate()));
        offer.setBasicSalary(   helper.updateHelper( offer.getBasicSalary(),     update.getBasicSalary()));
        offer.setNote(          helper.updateHelper( offer.getNote(),            formattedNote));

        offerRepository.save(offer);

        return true;
    }
}
