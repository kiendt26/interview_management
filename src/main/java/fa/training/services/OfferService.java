package fa.training.services;

import fa.training.entities.Offer;
import fa.training.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {
    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> listOffers() {
        return offerRepository.findAll();
    }
}
