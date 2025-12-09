package mk.ukim.finki.wp.lab.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.model.Dish;
import mk.ukim.finki.wp.lab.model.Rating;
import mk.ukim.finki.wp.lab.service.ChefService;
import mk.ukim.finki.wp.lab.service.DishService;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "ChefDetailsServlet", urlPatterns = "/servlet/chefDetails")
public class ChefDetailsServlet extends HttpServlet {
    private final SpringTemplateEngine templateEngine;
    private final ChefService chefService;
    private final DishService dishService;

    public ChefDetailsServlet(SpringTemplateEngine templateEngine, ChefService chefService, DishService dishService) {
        this.templateEngine = templateEngine;
        this.chefService = chefService;
        this.dishService = dishService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        long idOfChef = -1L;

        try{
            idOfChef = Long.parseLong(req.getParameter("idOfChef"));
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        Chef chef = chefService.findById(idOfChef);
        if(chef == null){
            resp.sendRedirect("/listChefs");
            return;
        }

        WebContext context = new WebContext(webExchange);
        context.setVariable("chef", chef);
        context.setVariable("nameOfChef", chef.getFirstName() + " " + chef.getLastName());
        context.setVariable("bioofChef", chef.getBio());
        context.setVariable("dishes", chef.getDishes());
        context.setVariable("ratings", chef.getRatings());
        context.setVariable("idOfChef", chef.getId());

        templateEngine.process("chefDetails.html", context, resp.getWriter());
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idOfChefStr = req.getParameter("idOfChef");
        String idOfDish = req.getParameter("idOfDish");
        String ratingStr = req.getParameter("rating");

        if (idOfChefStr == null || idOfDish == null || ratingStr == null || ratingStr.isEmpty()) {
            resp.sendRedirect("/chefDetails?idOfChef=" + idOfChefStr);
            return;
        }


        long idOfChef = Long.parseLong(idOfChefStr);
        int rating = Integer.parseInt(ratingStr);

        chefService.addDishToChef(idOfChef, idOfDish);
        chefService.addRatingToDish(idOfChef, idOfDish, rating);


        resp.sendRedirect("/chefDetails?idOfChef=" + idOfChef);
    }

}

