<%@ page import="inc.Helper" %>
<%
    String tabName = request.getParameter("table");
    String baseName = request.getParameter("database");
    Helper.deleteTable(tabName, baseName);
    response.sendRedirect("../liste_table.jsp?table=" + tabName + "&&database=" + baseName);
%>