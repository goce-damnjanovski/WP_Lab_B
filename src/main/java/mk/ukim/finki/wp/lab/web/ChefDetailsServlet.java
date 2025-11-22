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

@WebServlet(name = "ChefDetailsServlet", urlPatterns = "/chefDetails")
public class ChefDetailsServlet extends HttpServlet {

    private final SpringTemplateEngine templateEngine;
    private final ChefService chefService;
    private final DishService dishService; // <- додај го

    public ChefDetailsServlet(SpringTemplateEngine templateEngine, ChefService chefService, DishService dishService) {
        this.templateEngine = templateEngine;
        this.chefService = chefService;
        this.dishService = dishService; // <- инјектирај
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        Long idOfChef = Long.parseLong(req.getParameter("idOfChef"));
        Chef chef = chefService.findById(idOfChef);

        WebContext context = new WebContext(webExchange);
        context.setVariable("chef", chef);
        context.setVariable("nameOfChef", chef.getFirstName() + " " + chef.getLastName());
        context.setVariable("bioofChef", chef.getBio());
        context.setVariable("dishes", chef.getDishes());
        context.setVariable("ratings", chef.getRatings());

        templateEngine.process("chefDetails.html", context, resp.getWriter());
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

            Dish dish = dishService.findByDishId(idOfDish); // сега работи

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
