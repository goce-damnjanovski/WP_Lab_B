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
        if (idOfChefStr == null || idOfChefStr.isEmpty() || idOfChefStr.equals("null")) {
            resp.sendRedirect("/listChefs"); // fallback ако нема шеф
            return;
        }

        long idOfChef = Long.parseLong(idOfChefStr);
        Chef chef = chefService.findById(idOfChef);

        WebContext context = new WebContext(webExchange);
        context.setVariable("dishes", dishService.listDishes());
        context.setVariable("idOfChef", idOfChef); // важно за hidden input
        context.setVariable("nameOfChef", chef.getFirstName() + " " + chef.getLastName());
        context.setVariable("chef", chef);

        templateEngine.process("dishesList.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idOfChefStr = req.getParameter("idOfChef");
        String idOfDish = req.getParameter("idOfDish");

        // Ако не е избрано јадење – врати се на страната со јадења
        if (idOfDish == null || idOfDish.isEmpty()) {
            resp.sendRedirect("/dish?idOfChef=" + idOfChefStr);
            return;
        }

        Long idOfChef = Long.parseLong(idOfChefStr);
        Dish dish = dishService.findByDishId(idOfDish);

        if (dish != null) {
            chefService.addDishToChef(idOfChef, dish.getDishId());
            // rating се проверува од страна на HTML required, па тука не проверуваме дополнително
            chefService.addRatingToChef(idOfChef, dish, Integer.parseInt(req.getParameter("rating")));
        }

        resp.sendRedirect("/chefDetails?idOfChef=" + idOfChefStr);
    }

}

