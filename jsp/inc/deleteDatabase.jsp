<%@ page import="inc.Helper" %>
<%
    String baseName = request.getParameter("database");
    Helper.deleteDatabase(baseName);
    response.sendRedirect("../index.jsp");
%>