package com.example.ufmanagerf.controllers;

import com.example.ufmanagerf.model.Uf;
import com.example.ufmanagerf.services.Mp.Service_Mp;
import com.example.ufmanagerf.services.Uf.Service_Uf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class Controller_Uf {

    @Autowired
    private Service_Uf UfService;

    @Autowired
    private Service_Mp MpService;

    @GetMapping("/ufs")
    public String index(Model m) {
        m.addAttribute("ufs", UfService.getAll());
        return "Uf/index";
    }

    @GetMapping("/ufs/create")
    public String create(Model m) {
        m.addAttribute("uf", new Uf());
        return "Uf/create";
    }

    @PostMapping("/ufs/save")
    public String save(@Valid @ModelAttribute Uf uf, BindingResult bindingResult, Model m) {
        if (!bindingResult.hasErrors()) {
            if(UfService.exists(uf.getNomUf())){
                bindingResult.addError(new FieldError("uf", "nomUf", "El nom ja existeix"));
                return "Uf/create";
            }
            UfService.add(uf);
        } else {
            System.out.println("Validation error");
            m.addAttribute("uf", uf);
            return "Uf/create";
        }
        return "redirect:/ufs";
    }

    @GetMapping("/ufs/delete")
    public String delete(HttpServletRequest request) {
        UfService.remove(Integer.parseInt(request.getParameter("id")));
        return "redirect:/ufs";
    }

    @GetMapping("/ufs/edit")
    public String edit(HttpServletRequest request, Model m) {
        Uf uf = UfService.get(Integer.parseInt(request.getParameter("id")));
        m.addAttribute("uf", uf);
        return "Uf/edit";
    }

    @PostMapping("/ufs/editPost")
    public String editPost(@Valid @ModelAttribute Uf uf, BindingResult bindingResult, Model m) {
        if (!bindingResult.hasErrors()) {
            if(UfService.existsEdit(uf.getNomUf(), uf.getIdUf())){
                bindingResult.addError(new FieldError("uf", "nomUf", "El nom ja existeix"));
                return "Uf/edit";
            };
            UfService.edit(uf);
        } else {
            System.out.println("Validation error");
            return "Uf/edit";
        }
        return "redirect:/ufs";
    }
}
