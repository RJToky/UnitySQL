<%@ page import="inc.Controller" %>
<%
    String baseName = request.getParameter("database");
    Controller.deleteDatabase(baseName);
    response.sendRedirect("../index.jsp");
%>