<%@ page import="inc.Controller" %>
<%
    String tabName = request.getParameter("table");
    String baseName = request.getParameter("database");
    Controller.deleteTable(tabName, baseName);
    response.sendRedirect("../liste_table.jsp?table=" + tabName + "&&database=" + baseName);
%>