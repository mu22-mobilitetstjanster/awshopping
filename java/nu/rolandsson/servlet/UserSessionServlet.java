package nu.rolandsson.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/auth/*")
public class UserSessionServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String action = req.getPathInfo();

    switch (action) {
      case "/login" -> handleLogin(req, resp);
      case "/logout" -> handleLogout(req, resp);
    }
  }

  private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    req.getSession().invalidate(); //Invalidate - empty the session
    resp.sendRedirect("/");
  }

  private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();

    if(session.getAttribute("username") != null) {
      resp.sendRedirect(req.getContextPath() + "/shoppingList");
    } else {
      String username = req.getParameter("username");
      String password = req.getParameter("password");

      //if(username.equals("bob") && password.equals("123")) {
      if(username != null) {
        session.setMaxInactiveInterval(60 * 30);
        session.setAttribute("username", username);
        resp.sendRedirect("/shoppingList");
      } else {
        resp.setContentType("text/html");
        resp.getWriter().println("Invalid attempt, <a href='/'>try again</a>");
      }
    }

  }
}
