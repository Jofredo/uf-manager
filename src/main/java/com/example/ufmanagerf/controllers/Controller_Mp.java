package com.example.ufmanagerf.controllers;

import com.example.ufmanagerf.model.Mp;
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
import java.util.List;

@Controller
public class Controller_Mp {

    @Autowired
    private Service_Mp MpService;

    @Autowired
    private Service_Uf UfSerivce;

    @GetMapping("/mps")
    public String index(Model m) {
        m.addAttribute("mps", MpService.getAll());
        return "Mp/index";
    }

    @GetMapping("/mps/create")
    public String create(Model m) {
        m.addAttribute("mp", new Mp());
        m.addAttribute("ufs", UfSerivce.getAllWhereMpIsNull());
        return "Mp/create";
    }

    @PostMapping("/mps/save")
    public String save(@Valid @ModelAttribute Mp mp, BindingResult bindingResult, Model m) {
        if (!bindingResult.hasErrors()) {
            if(MpService.exists(mp.getNomMp())){
                bindingResult.addError(new FieldError("mp", "nomMp", "El nom ja existeix"));
                return "Mp/create";
            }
            MpService.add(mp);
            MpService.addUfs(mp, mp.getUfs());
        } else {
            System.out.println("Validation error");
            m.addAttribute("mp", mp);
            return "Mp/create";
        }
        return "redirect:/mps";
    }

    @GetMapping("/mps/delete")
    public String delete(HttpServletRequest request) {
        MpService.remove(Integer.parseInt(request.getParameter("id")));
        return "redirect:/mps";
    }

    @GetMapping("/mps/edit")
    public String edit(HttpServletRequest request, Model m) {
        Mp mp = MpService.get(Integer.parseInt(request.getParameter("id")));
        m.addAttribute("mp", mp);
        m.addAttribute("ufs", UfSerivce.getAllWhereMpIsNullOrMpIsEquals(mp));
        return "Mp/edit";
    }

    @PostMapping("/mps/editPost")
    public String editPost(@Valid @ModelAttribute Mp mp, BindingResult bindingResult, HttpServletRequest request) {
        if (!bindingResult.hasErrors()) {
            if(MpService.existsEdit(mp.getNomMp(), mp.getIdMp())){
                bindingResult.addError(new FieldError("mp", "nomMp", "El nom ja existeix"));
                return "Mp/edit";
            };
            List<Uf> allUfs = UfSerivce.getAll();
            List<Uf> newUfs = mp.getUfs();
            MpService.edit(mp);
            MpService.removeUfs(mp, allUfs);
            MpService.addUfs(mp, newUfs);
        } else {
            System.out.println("Validation error");
            return "Mp/edit";
        }
        return "redirect:/mps";
    }

    @GetMapping("/mps/ufs")
    public String filter(Model m, HttpServletRequest request) {
        Mp mp = MpService.get(Integer.parseInt(request.getParameter("id")));
        m.addAttribute("mp", mp);
        m.addAttribute("ufs", UfSerivce.filter(mp));
        return "Mp/filter";
    }

    @GetMapping("/mps/ufs/edit")
    public String ufs(Model m, HttpServletRequest request) {
        Mp mp = MpService.get(Integer.parseInt(request.getParameter("id")));
        m.addAttribute("mp", mp);
        m.addAttribute("ufsNoMp", UfSerivce.getAllWhereMpIsNull());
        return "Mp/ufs";
    }

    @PostMapping("/mps/ufs/editPost")
    public String ufsPost(@ModelAttribute Mp mp, Model m, HttpServletRequest request) {
        Mp MP = MpService.get(Integer.parseInt(request.getParameter("id")));
        List<Uf> allUfs = UfSerivce.getAll();
        List<Uf> newUfs = mp.getUfs();
        MpService.removeUfs(MP, allUfs);
        MpService.addUfs(MP, newUfs);
        return "redirect:/mps";
    }
}
