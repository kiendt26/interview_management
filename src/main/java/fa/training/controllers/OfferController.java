package fa.training.controllers;

import fa.training.dto.AddOfferDTO;
import fa.training.entities.Offer;
import fa.training.repositories.OfferRepository;
import fa.training.services.OfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/offer")
public class OfferController {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerService;

    @GetMapping("/list")
    public String listOffers(
            Model model
    ) {
        model.addAttribute("offers", offerRepository.findAll());
        return "offer/offer-list";
    }

    @GetMapping("/add")
    public String addOffer(
            Model model
    ) {
        model.addAttribute("data", new Offer());
        return "offer/add-offer";
    }

    @PostMapping("/add")
    public String addOffer(
            @Valid @ModelAttribute Offer offer,
            Model model,
            BindingResult result
    ) {
        model.addAttribute("data", offer);
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors().toString());
            return "redirect:/add";
        }

        if (offer.noDataInRequired()) {
            model.addAttribute("error", "Please provide data in required fields");
            return "redirect:/add";
        }

        offer.setOfferDate(LocalDate.now());

        offerRepository.save(offer);
        return "redirect:/list";
    }

    @GetMapping("/edit/{id}")
    public String editOffer(
            @PathVariable("id") Long id,
            Model model
    ) {
        Offer offer = offerRepository.findById(id).orElse(null);
        if (offer == null) {
            return "redirect:/list";
        }
        model.addAttribute("data", offer);
        return "offer/edit-offer";
    }

    @PostMapping("/edit")
    public String editOffer(
            @Valid @ModelAttribute AddOfferDTO offer,
            Model model,
            BindingResult result
    ) {
        model.addAttribute("data", offer);

        if (offer.getOfferId() == null) {
            model.addAttribute("error", "Offer not found");
            return "redirect:/list";
        }

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors().toString());
            return "redirect:/edit/" + offer.getOfferId();
        }

        if(!offerService.update(offer)){
            model.addAttribute("errors", "Could not update offer, offer not found.");
            return "redirect:/list";
        }

        return "redirect:/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteOffer(
            @PathVariable("id") Long id,
            Model model
    ) {
        Offer offer = offerRepository.findById(id).orElse(null);

        if (offer == null) {
            model.addAttribute("error", "No offer found");
            return "redirect:/list";
        }

        offerRepository.delete(offer);
        return "redirect:/list";
    }
}
