package nu.rolandsson.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nu.rolandsson.model.ShoppingItem;
import nu.rolandsson.model.ShoppingList;
import nu.rolandsson.service.ShoppingListService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/shoppingList")
public class ShoppingListServlet extends HttpServlet {

  //private Map<String, ShoppingList> shoppingRepos;
  private ShoppingListService listService;

  @Override
  public void init() throws ServletException {
    listService = new ShoppingListService();
  }

  public void printUserShoppingListContent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String username = (String) req.getSession().getAttribute("username");
    ShoppingList userShoppingList = listService.getShoppingList(username);

    PrintWriter writer = resp.getWriter();

    for (ShoppingItem item : userShoppingList.getItemList()) {
      writer.println(item + "<br>");
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    resp.getWriter().println("Shopping list is activeeee! - Servlet");

    req.getRequestDispatcher("/shoppingList.jsp").include(req, resp);

    printUserShoppingListContent(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String username = (String) req.getSession().getAttribute("username");

    ShoppingItem item = new ShoppingItem();
    item.setName(req.getParameter("shopping-item"));
    item.setQuantity(Integer.parseInt(req.getParameter("shopping-quantity")));

    listService.addShoppingItem(username, item);

    resp.sendRedirect("/shoppingList");
  }
}
