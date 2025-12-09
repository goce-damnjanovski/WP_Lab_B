package mk.ukim.finki.wp.lab.web.servlet;

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
import java.util.List;

@WebServlet(name = "DishServlet", urlPatterns = "/servlet/dish")
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
        long idOfChef = -1L;

        if (idOfChefStr == null || idOfChefStr.isEmpty()) {
            resp.sendRedirect("/listChefs");
            return;
        }

        try {
            idOfChef = Long.parseLong(idOfChefStr);
        } catch (NumberFormatException ex) {
            resp.sendRedirect("/listChefs");
            return;
        }

        Chef chef = chefService.findById(idOfChef);
        if (chef == null) {
            resp.sendRedirect("/listChefs");
            return;
        }


        List<Dish> availableDishes = dishService.listDishes().stream()
                .filter(dish -> chef.getDishes().stream()
                        .noneMatch(chefDish -> chefDish.getDishId().equals(dish.getDishId())))
                .toList();



        WebContext context = new WebContext(webExchange);
        context.setVariable("dishes", availableDishes);
        context.setVariable("idOfChef", idOfChef);
        context.setVariable("nameOfChef", chef.getFirstName() + " " + chef.getLastName());

        templateEngine.process("dishesList.html", context, resp.getWriter());
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idOfChefStr = req.getParameter("idOfChef");
        String idOfDish = req.getParameter("idOfDish");
        String ratingStr = req.getParameter("rating");

        if (idOfChefStr != null && (idOfDish == null || idOfDish.isEmpty())) {
            long idOfChef;
            try {
                idOfChef = Long.parseLong(idOfChefStr);
            } catch (NumberFormatException e) {
                resp.sendRedirect("/listChefs");
                return;
            }

            Chef chef = chefService.findById(idOfChef);
            if (chef == null) {
                resp.sendRedirect("/listChefs");
                return;
            }

            IWebExchange webExchange = JakartaServletWebApplication
                    .buildApplication(getServletContext())
                    .buildExchange(req, resp);

            WebContext context = new WebContext(webExchange);
            context.setVariable("dishes", dishService.listDishes());
            context.setVariable("idOfChef", idOfChef);
            context.setVariable("nameOfChef", chef.getFirstName() + " " + chef.getLastName());

            templateEngine.process("dishesList.html", context, resp.getWriter());
            return;
        }

        if (idOfChefStr == null || idOfDish == null || idOfChefStr.isEmpty() || idOfDish.isEmpty()) {
            resp.sendRedirect("/listChefs");
            return;
        }

        try {
            long chefId = Long.parseLong(idOfChefStr);
            Dish dish = dishService.findByDishId(idOfDish);

            if (dish != null) {
                chefService.addDishToChef(chefId, idOfDish);

                if (ratingStr != null && !ratingStr.isEmpty()) {
                    int rating = Integer.parseInt(ratingStr);
                    chefService.addRatingToDish(chefId, idOfDish, rating);
                }
            }


            resp.sendRedirect("/chefDetails?idOfChef=" + chefId);

        } catch (NumberFormatException e) {
            resp.sendRedirect("/listChefs");
        }
    }

    }



