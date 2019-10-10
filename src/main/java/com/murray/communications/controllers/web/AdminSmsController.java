package com.murray.communications.controllers.web;

import com.murray.communications.adapters.web.SmsMessageAdapter;
import com.murray.communications.dtos.web.SMSMessageDto;
import com.murray.communications.dtos.web.SmsSearchResults;
import com.murray.communications.dtos.web.NewSMSMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
public class AdminSmsController {


    private final SmsMessageAdapter smsMessageAdapter;

    public AdminSmsController(SmsMessageAdapter smsMessageAdapter) {
        this.smsMessageAdapter = smsMessageAdapter;
    }

    @GetMapping({"/sms", "/sms-overview.html"})
    public String smsOverview(HttpServletRequest request, Model model) {
        log.warn("TBC get the latest results for message sent per status last 10days");

        int page = 0;
        int size = 10;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
            if (page < 0) {
                page = 0;
            }
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        SmsSearchResults results = smsMessageAdapter.findAllMessages(page, size);

        log.info("Found {} messages", results.getTotalElements());

        model.addAttribute("results", results);

        return "sms/overview";
    }

    @GetMapping({"/sms/new-sms.html"})
    public String newSms(NewSMSMessageDto newSMSMessageDto) {

        return "sms/new-sms";
    }

    @PostMapping("/sms/save")
    public String saveSms(@Valid NewSMSMessageDto newSMSMessageDto, BindingResult bindingResult,
                          Model model) {

        log.info("saving newSMSMessageDto:{}", newSMSMessageDto);

        if (bindingResult.hasErrors()) {
            return "sms/new-sms";
        }

        log.info("saving newSMSMessageDto:{}", newSMSMessageDto);


        SMSMessageDto smsMessage = smsMessageAdapter.saveSms(newSMSMessageDto);

        log.info("New sms id:{}", smsMessage.getId());


        model.addAttribute("smsMessage", smsMessage);


        return "redirect:/sms-overview.html";
    }

    @GetMapping("/sms-edit/{id}")
    public String showEditSmsForm(@PathVariable("id") Long id, Model model) {

        SMSMessageDto smsMessage = smsMessageAdapter.findMessageBy(id);

        model.addAttribute("smsMessage", smsMessage);
        return "sms/edit-sms";
    }

    @PostMapping("/sms/update")
    public String updateSms(@Valid @ModelAttribute("smsMessage") SMSMessageDto smsMessage,
                          BindingResult bindingResult,
                          Model model) {

        log.info("updating sms:{}", smsMessage);
        if (bindingResult.hasErrors()) {
            log.warn("updating sms validation errors");
            return "sms/edit-sms";
        }

        SMSMessageDto smsMessageDto = smsMessageAdapter.update(smsMessage);

        model.addAttribute("smsMessage", smsMessageDto);

        return "redirect:/sms-overview.html";
    }

    @GetMapping("/sms-delete/{id}")
    public String deleteSms(@PathVariable("id") Long id, Model model) {

        log.warn("deleting sms :{}",id);

        smsMessageAdapter.deleteById(id);

        return "redirect:/sms-overview.html";
    }


    @GetMapping("/sms-send/{id}")
    public String sendSms(@PathVariable("id") Long id, Model model) {

        log.warn("sending sms :{}",id);

        smsMessageAdapter.sendMessage(id);

        return "redirect:/sms-overview.html";
    }



}
