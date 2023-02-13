<%@ page import="inc.Helper, object.Table" %>
<%
  String tabName = request.getParameter("table");
  String baseName = request.getParameter("database");
  Table tab = Helper.selectAll(tabName, baseName);
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="./assets/bulma/bulma.min.css" />
    <link rel="stylesheet" href="./assets/bulma/bulma-helpers.min.css" />
    <link rel="stylesheet" href="./assets/css/style.css" />
    <script src="./assets/bulma/bulma.js" defer></script>
    <title>UnitySQL</title>
  </head>
  <body>
    <header class="navbar has-shadow">
      <div class="container">
        <div class="navbar-brand">
          <a href="./" class="navbar-item title has-text-primary">UnitySQL</a>
        </div>
      </div>
    </header>
    <main class="content py-5">
      <div class="container">
        <h1 class="title has-text-grey-dark">Table : <% out.println(tabName); %></h1>
        <table class="table is-striped">
          <thead>
            <tr>
              <% for(int i = 0; i < tab.getColumn().length; i++) { %>
                <th><% out.println(tab.getColumn()[i]); %></th>
              <% } %>
            </tr>
          </thead>
          <tbody>
            <% for(int i = 0; i < tab.getValues().size(); i++) { %>
              <tr>
                <% for(int j = 0; j < tab.getValues().get(i).length; j++) { %>
                  <td><% out.println(tab.getValues().get(i)[j]); %></td>
                <% } %>
              </tr>
            <% } %>
          </tbody>
        </table>
        <a href="./liste_table.jsp?database=<% out.print(baseName); %>" class="button is-primary is-outlined mt-5">
          Retour
        </a>
      </div>
    </main>
    <footer class="footer">
      <div class="content has-text-centered">
        <p>
          <strong>UnitySQL</strong> by <a href="https://github.com/RJToky">Toky RAKOTOARIVONY</a>
        </p>
        <p>v1.0.0</p>
        <p>
          <em>2023</em>
        </p>
      </div>
    </footer>
  </body>
</html>
