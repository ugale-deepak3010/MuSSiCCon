package com.UssicConMuSSiCCon.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.UssicConMuSSiCCon.entity.Song;
import com.UssicConMuSSiCCon.entity.User;
import com.UssicConMuSSiCCon.enums.PaypalPaymentIntent;
import com.UssicConMuSSiCCon.enums.PaypalPaymentMethod;
import com.UssicConMuSSiCCon.service.SongService;
import com.UssicConMuSSiCCon.service.UserService;
import com.UssicConMuSSiCCon.service.impl.PaypalService;
import com.UssicConMuSSiCCon.utils.PaypalUtils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import lombok.extern.log4j.Log4j2;

@Secured("ROLE_USER")
@Controller
@Log4j2
public class PaypalController {
	
    // Paypal will send request with these two default URLs
    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private UserService userService;

    @Autowired
    private SongService songService;

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("songId") Long songId, Model model) {
        Song purchasedSong = songService.getById(songId);
        model.addAttribute("purchasedSong", purchasedSong);
        return "payment";
    }

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @RequestParam("songId") Long songId, @AuthenticationPrincipal User user) {
        String cancelUrl = PaypalUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = PaypalUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        try {
            Payment payment = paypalService.createPayment(
                    10.0,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment for song",
                    cancelUrl,
                    successUrl);
            userService.purchaseSong(user.getId(), songId);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "payment_fail";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "payment_success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/manual-cancel")
    public String manualCancel() {
        return "redirect:/";
    }
}
