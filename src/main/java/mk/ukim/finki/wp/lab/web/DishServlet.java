package mk.ukim.finki.wp.lab.web;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.service.ChefService;
import mk.ukim.finki.wp.lab.service.DishService;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DishServlet", urlPatterns = "/dish")
public class DishServlet extends HttpServlet {

    private final SpringTemplateEngine templateEngine;
    private final DishService dishService;
    private final ChefService chefService;

    public DishServlet(SpringTemplateEngine templateEngine,
                       DishService dishService,
                       ChefService chefService) {
        this.templateEngine = templateEngine;
        this.dishService = dishService;
        this.chefService = chefService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        String idOfChefStr = req.getParameter("idOfChef");
        if (idOfChefStr == null || idOfChefStr.isEmpty()) {
            resp.sendRedirect("/listChefs"); // fallback ако нема шеф
            return;
        }

        long idOfChef = Long.parseLong(idOfChefStr);
        Chef chef = chefService.findById(idOfChef);

        WebContext context = new WebContext(webExchange);
        context.setVariable("dishes", dishService.listDishes());
        context.setVariable("idOFChef", idOfChef); // важно за hidden input
        context.setVariable("nameOfChef", chef.getFirstName() + " " + chef.getLastName());
        context.setVariable("chef", chef);

        templateEngine.process("dishesList.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idOfChefStr = req.getParameter("idOfChef");
        String idOfDish = req.getParameter("idOfDish");
        String ratingStr = req.getParameter("rating");

        if (idOfChefStr == null || idOfDish == null || ratingStr == null) {
            resp.sendRedirect("/dish?idOfChef=" + idOfChefStr);
            return;
        }

        try {
            Long idOfChef = Long.parseLong(idOfChefStr);
            int rating = Integer.parseInt(ratingStr);

            Dish dish = dishService.findByDishId(idOfDish); // <- ТУКА е промената

            if (dish != null) {
                chefService.addDishToChef(idOfChef, dish.getDishId());
                chefService.addRatingToChef(idOfChef, dish, rating);
            }

            resp.sendRedirect("/chefDetails?idOfChef=" + idOfChefStr);

        } catch (NumberFormatException e) {
            resp.sendRedirect("/dish?idOfChef=" + idOfChefStr);
        }
    }


}

