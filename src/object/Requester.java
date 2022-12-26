package object;

import inc.Function;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Requester {
    Database databaseUsed;

    public Requester() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String req;
        do {
            if(databaseUsed == null) {
                System.out.print("USQL> ");
            } else {
                System.out.print(databaseUsed.getName() + "> ");
            }
            req = reader.readLine();
            try {
                treatRequest(req);
            } catch (Exception e) {
                System.out.println("\n" + e.getMessage() + "\n\n");
            }
            System.out.println();
        } while (!req.equalsIgnoreCase("exit") && !req.equalsIgnoreCase("quit"));
    }

    public void treatRequest(String request) throws Exception {
        String req = request.toLowerCase();
        String nomTable;
        Table table;

        if(req.contains("show")) {
            if(req.contains("database")) {
                table = Database.show();
                table.print();

            } else if (req.contains("tables")) {
                table = Table.show(databaseUsed);
                table.print();

            }
        } else if (req.contains("desc") || req.contains("describe")) {
            nomTable = Function.getNomTable(request, "desc");

            int i = Function.getIndiceTable(nomTable, databaseUsed);
            table = databaseUsed.getTables().get(i).desc();
            table.print();

        } else if(req.contains("create")) {
            if(req.contains("database")) {
                new Database(req).create();

            } else if (req.contains("table") && req.contains("with")) {
                new Table(req, databaseUsed).create();

            }
        } else if (req.contains("use database")) {
            databaseUsed = new Database(req);
            try {
                databaseUsed.use();
            } catch (Exception e) {
                databaseUsed = null;
                throw e;
            }

        } else if (req.contains("insert into") && req.contains("values")) {
            nomTable = Function.getNomTable(request, "insert");

            int i = Function.getIndiceTable(nomTable, databaseUsed);
            databaseUsed.getTables().get(i).insert(request);

        } else if (req.contains("select") && req.contains("from")) {

            if(req.contains("where")) {
                nomTable = Function.getNomTable(request, "select");

                int i = Function.getIndiceTable(nomTable, databaseUsed);
                table = databaseUsed.getTables().get(i).selection(request).projection(request);
                table.print();

            } else if (req.contains("union")) {
                nomTable = Function.getNomTable(request, "select");

                int i = Function.getIndiceTable(nomTable, databaseUsed);
                table = databaseUsed.getTables().get(i).union(request, databaseUsed);
                table.print();

            } else if (req.contains("sous")) {
                table = Table.sousRequest(req);
                table.print();

            } else {
                nomTable = Function.getNomTable(request, "select");

                int i = Function.getIndiceTable(nomTable, databaseUsed);
                table = databaseUsed.getTables().get(i).projection(request);
                table.print();

            }
            
        } else if (request.contains("exit") || request.contains("quit")) {
            System.out.println("bye");

        } else if (request.equals("")) {
            System.out.print("");
        } else {
            throw new Exception("Erreur de syntaxe");
        }
    }


}
