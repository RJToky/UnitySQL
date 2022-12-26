package inc;

import object.Database;
import object.Table;

public class Function {

    public static String getNomTable(String request, String refer) throws Exception {
        try {
            switch (refer) {
                case "create", "insert":
                    return request.split("\s+")[2];

                case "select":
                    return request.split(" from ")[1].split("\s+")[0];

                case "union":
                    return request.toLowerCase().split(" union ")[1].split("\s+")[0];

                case "desc", "describe":
                    return request.split("\s+")[1];

            }
        } catch (Exception e) {
            throw new Exception("Erreur de syntaxe");
        }
        throw new Exception("Erreur de syntaxe");
    }

    public static String[] getNomColonne(String request, String refer) throws Exception {
        String[] str, column;

        if (refer.equals("create")) {
            str = request.split("\s+");
            column = new String[str.length-4];

            System.arraycopy(str, 4, column, 0, column.length);
            return column;

        } else if (refer.equals("select")) {
            str = request.split("from")[0].split("\s+");
            column = new String[str.length-1];

            System.arraycopy(str, 1, column, 0,  column.length);
            return column;

        }
        throw new Exception("Erreur de syntax");
    }

    public static String getNomDatabase(String request) throws Exception {
        if (request.contains("create") && request.split("\s+").length == 3) {
            return request.split("\s+")[2];

        } else if (request.contains("use") && request.split("\s+").length == 3) {
            return request.split("\s+")[2];

        }
        throw new Exception("Erreur de syntax");
    }

    public static int getIndiceTable(String name, Database database) throws Exception {
        if(database == null) throw new Exception("Aucune base de données selectionnée");

        for (int i = 0; i < database.getTables().size(); i++) {
            if(database.getTables().get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        throw new Exception("Table inexistante");
    }

    public static int getIndiceColonne(Table table, String name) throws Exception {
        for (int i = 0; i < table.getColumn().length; i++) {
            if(table.getColumn()[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        throw new Exception("Colonne inexistante");
    }

    public static String getKeyOrValue(String req, String alaina) {
        String[] rep = new String[2];
        String[] str = req.toLowerCase().split("where")[1].split("=");

        String[] temps;
        int a = 0;
        for (String s : str) {
            temps = s.split("\s+");
            for (String temp : temps) {
                if (!temp.equals("")) {
                    rep[a] = temp;
                    a++;
                }
            }
        }

        if(alaina.equals("key")) {
            return rep[0];
        } else if (alaina.equals("value")) {
            return rep[1];
        }

        return null;
    }

}
