<%@ page import="inc.Controller, object.Table" %>
<%
  String baseName = request.getParameter("database");
  Table tab = Controller.listTable(baseName);
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
        <h1
          class="title has-text-grey-dark is-flex is-justify-content-space-between"
          id="title"
        >
          <span>Liste des tables</span>
          <span>Base de donnees : <% out.println(baseName); %></span>
        </h1>
        <table class="table is-striped">
          <thead>
            <tr>
              <th>Numero</th>
              <th>Nom</th>
              <th>Champs</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <% for(int i = 0; i < tab.getValues().size(); i++) { %>
              <tr>
                <td><% out.print(tab.getValues().get(i)[0]); %></td>
                <td><% out.print(tab.getValues().get(i)[1]); %></td>
                <td><% out.print(tab.getValues().get(i)[2]); %></td>
                <td>
                  <div class="buttons">
                    <a href="./table.jsp?table=<% out.print(tab.getValues().get(i)[1]); %>&&database=<% out.println(baseName); %>" class="button is-small is-link is-light">Voir</a>
                    <a href="#" class="button is-small is-danger is-light">
                      Supprimer
                    </a>
                  </div>
                </td>
              </tr>
            <% } %>
          </tbody>
        </table>
        <a href="./index.jsp" class="button is-primary is-outlined mt-5">
          Retour
        </a>
      </div>
    </main>
    <footer class="footer">
      <div class="content has-text-centered">
        <p>
          <strong>UnitySQL</strong> by <a href="">Toky RAKOTOARIVONY</a>
        </p>
        <p>v1.0.0</p>
        <p>
          <em>2023</em>
        </p>
      </div>
    </footer>
  </body>
</html>
