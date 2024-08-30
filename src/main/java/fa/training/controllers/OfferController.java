package fa.training.controllers;

import fa.training.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/offer")
public class OfferController {
    @Autowired
    private OfferService offerService;

    @GetMapping("/list")
    public String listOffers(
        Model model
    ) {
        model.addAttribute("offers", offerService.listOffers());
        return "offer/offer-list";
    }

//    @PostMapping("/add")
//    public String addOffer(
//            @ModelAttribute
//    ){
//
//    }

}
