<%@ page import="inc.Controller, object.Table" %>
<% Table tab = Controller.listDatabase(); %>
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
    <script src="./assets/js/script.js" defer></script>
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
        <h1 class="title has-text-grey-dark">Liste des bases de donnees</h1>
        <table class="table is-striped">
          <thead>
            <tr>
              <th>Numero</th>
              <th>Nom</th>
              <th>Nombre de tables</th>
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
                  <a
                    href="./liste_table.jsp?database=<% out.print(tab.getValues().get(i)[1]); %>"
                    class="button is-small is-link is-light"
                  >
                    Voir
                  </a>
                  <a
                    href="./inc/deleteDatabase.jsp"
                    class="js-modal-trigger button is-small is-danger is-light"
                    data-target="suppression"
                    id="database=<% out.print(tab.getValues().get(i)[1]); %>"
                  >
                    Supprimer
                  </a>
                </div>
              </td>
            </tr>
            <% } %>
          </tbody>
        </table>
      </div>
    </main>
    <footer class="footer">
      <div class="content has-text-centered">
        <p>
          <strong>UnitySQL</strong> by
          <a href="https://github.com/RJToky">Toky RAKOTOARIVONY</a>
        </p>
        <p>v1.0.0</p>
        <p>
          <em>2023</em>
        </p>
      </div>
    </footer>

    <div id="suppression" class="modal">
      <div class="modal-background"></div>
      <div class="modal-content">
        <div class="box">
          <p class="content">Voulez-vous vraiment supprimer?</p>
          <div class="buttons">
            <a href="#" class="button is-danger is-light" id="supprimer">Supprimer</a>
            <a href="#" class="button is-light cancel">Annuler</a>
          </div>
        </div>
      </div>
      <button class="modal-close is-large" aria-label="close"></button>
    </div>
  </body>
</html>
