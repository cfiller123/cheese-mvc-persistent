package org.launchcode.controllers;

import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "My Menus");

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {

        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return "menu/add";

    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addSubmit(@ModelAttribute @Valid Menu menu, Errors errors, Model model) {


        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
        }

        menuDao.save(menu);
        return "redirect:/view/" + menu.getId();
    }

    @RequestMapping(value = "view/", method = RequestMethod.GET)
    public String viewMenu(@RequestParam int id, Model model) {

        Menu menu = menuDao.findOne(id);
        model.addAttribute(menu);

        return "menu/view";
    }

    @RequestMapping(value = "add-item/", method = RequestMethod.GET)
    public String addItem(Model model, @RequestParam int id) {

        Menu menu = menuDao.findOne(id);
        AddMenuItemForm addMenuItemForm = new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("form", addMenuItemForm);
        model.addAttribute("title", "Add to menu: " + menu.getName());

        return "menu/add-item";
    }

}
