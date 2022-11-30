package wuff.wuff.praktischeuebung03.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KlaeffController {


//    @ModelAttribute("form")
//    Form generateForm(Form oldForm) {
//        if (oldForm == null || oldForm.username() == null) {
//            return new Form("", "");
//        }
//        return new Form(oldForm.username(), "");
//    }


    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @PostMapping("/")
    public String sendMessage(Form form, Model model) {
        model.addAttribute("username", form.username());
        model.addAttribute("text", form.text());
        return "index";
    }


}