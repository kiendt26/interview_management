package fa.training.controllers;

import fa.training.dto.OfferDTO;
import fa.training.entities.Candidate;
import fa.training.entities.Offer;
import fa.training.entities.User;
import fa.training.enums.Role;
import fa.training.enums.Status;
import fa.training.exception.EnumMismatchException;
import fa.training.repositories.CandidateRepository;
import fa.training.repositories.OfferRepository;
import fa.training.repositories.UserRepository;
import fa.training.services.OfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/offer")
public class OfferController {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferService offerService;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public String getOffers(
            Model model
    ) {
        model.addAttribute("offers", offerRepository.findAll());
        return "offer/offer-list";
    }

    @GetMapping("/list/{id}")
    public String getOffer(
            @PathVariable("id") Long id,
            Model model
    ){
        Offer offer = offerRepository.findById(id).orElse(null);

        //TODO: get user's role for edit managing

        if(offer == null) return "/list";

        model.addAttribute("data", offer);
        return "offer/offer-detail";
    }

    @GetMapping("/add")
    public String addOffer(
            Model model
    ) {
        model.addAttribute("data", new Offer());

        List<Candidate> candidates = candidateRepository.findAll();

        model.addAttribute("candidates", candidates);

        List<User> approvers = userRepository.findByRoleOrRole(Role.ADMIN, Role.HR);

        model.addAttribute("approver", approvers);

        List<User> allUsers = userRepository.findAll();

        model.addAttribute("users", allUsers);

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
            @Valid @ModelAttribute OfferDTO offer,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("data", offer);
            model.addAttribute("errors", result.getAllErrors().toString());
            return "offer/edit-offer";
        }

        if (!offerService.update(offer)) {
            redirectAttributes.addFlashAttribute("errors", "Could not update offer, offer not found.");
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

    @PostMapping("/update/{id}/{status}")
    public String updateOfferStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") String status,
            Model model
    ) {
        Offer offer =  offerRepository.findById(id).orElse(null);

        if(offer == null) {
            return "redirect:/list";
        }

        try{
            offer.setStatus(Status.valueOf(status));
        } catch (IllegalArgumentException e){
            throw new EnumMismatchException("Invalid status");
        }

        return "/list/" + offer.getOfferId();
    }
}
