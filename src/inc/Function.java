package inc;

import object.Database;
import object.Table;

import java.util.Vector;

public class Function {

    public static String getNomTable(String request, String refer) throws Exception {
        try {
            switch (refer) {
                case "create", "insert":
                    return request.split("\s+")[2];

                case "select":
                    return request.split("from")[1].trim().split("\s+")[0];

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

    public static String getKeyOrValue(String request, String toGet) {
        String[] rep = new String[2];
        String[] str = request.toLowerCase().split("where")[1].split("=");

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

        if(toGet.equals("key")) {
            return rep[0];
        } else if (toGet.equals("value")) {
            return rep[1];
        }
        return null;
    }

    public static Table[] getArrayTableByRequest(String request, Database databaseUsed, String separator) throws Exception {
        String[] reqs = request.split(separator);

        Table[] tables = new Table[reqs.length];
        for (int i = 0; i < reqs.length; i++) {
            tables[i] = databaseUsed.getTable(Function.getNomTable(reqs[i].trim(), "select"));
            tables[i] = tables[i].selection(reqs[i].trim()).projection(reqs[i].trim());
        }
        return tables;
    }

    public static String concatArray(String[] array) {
        StringBuilder builder = new StringBuilder();
        for (String str : array) {
            builder.append(str);
        }
        return builder.toString();
    }

    public static Vector<String[]> deleteDoublon(Vector<String[]> values) {
        Vector<String[]> rep = new Vector<>();
        Vector<String> hist = new Vector<>();

        boolean exist = false;
        for (String[] value : values) {
            for (String h : hist) {
                if (h.equals(Function.concatArray(value))) {
                    exist = true;
                }
            }

            if (!exist) rep.add(value);
            hist.add(Function.concatArray(value));
            exist = false;
        }
        return rep;
    }

    public static boolean areSameCol(Table[] tables) {
        for (int i = 0; i < tables.length; i++) {
            try {
                if (tables[i].getColumn().length != tables[i+1].getColumn().length) return false;

                for (int j = 0; j < tables[i].getColumn().length; j++) {
                    if(!tables[i].getColumn()[j].equalsIgnoreCase(tables[i+1].getColumn()[j])) return false;

                }
            } catch (Exception e) {
                break;
            }
        }
        return true;
    }

    public static String[] concatColonne(Table tab1, Table tab2) {
        String[] cols = new String[tab1.getColumn().length + tab2.getColumn().length];
        int i = 0;
        for (int j = 0; j < tab1.getColumn().length; j++, i++) cols[i] = tab1.getColumn()[j];
        for (int j = 0; j < tab2.getColumn().length; j++, i++) cols[i] = tab2.getColumn()[j];

        return cols;
    }

    public static Vector<String[]> produitCartesien(Table tab1, Table tab2) {
        Vector<String[]> rep = new Vector<>();

        int size = tab1.getColumn().length + tab2.getColumn().length;

        String[] temp;
        for (int i = 0; i < tab2.getValues().size(); i++) {
            for (int j = 0; j < tab1.getValues().size(); j++) {
                temp = new String[size];
                int k = 0;

                for (int l = 0; l < tab1.getValues().get(j).length; l++, k++) temp[k] = tab1.getValues().get(j)[l];
                for (int l = 0; l < tab2.getValues().get(i).length; l++, k++) temp[k] = tab2.getValues().get(i)[l];

                rep.add(temp);
            }
        }
        return rep;
    }

    public static Table concatArrayTable(Table[] tables) {
        String[] cols;
        Vector<String[]> tempValues;
        Table tab = tables[0];

        for (int i = 0; i < tables.length; i++) {
            try {
                tempValues = Function.produitCartesien(tab, tables[i + 1]);
                tab.setValues(tempValues);

                cols = Function.concatColonne(tab, tables[i + 1]);
                tab.setColumn(cols);

            } catch (Exception ignored) { }
        }

        return tab;
    }

    public static String[] getColonneCommune(Table table) throws Exception {
        String[] colTable = table.getColumn();

        Vector<String> temp = new Vector<>();
        for (int i = 0; i < colTable.length; i++) {
            for (int j = i; j < colTable.length; j++) {
                if(colTable[i].equals(colTable[j]) && i != j) {
                    temp.add(colTable[i]);
                }
            }
        }

        if(temp.size() == 0) throw new Exception("Aucune colonne commune");

        String[] colonneCommune = new String[temp.size()];
        for (int i = 0; i < colonneCommune.length; i++) {
            colonneCommune[i] = temp.get(i);
        }
        return colonneCommune;
    }

    public static void removeNotMatch(Table table) throws Exception {
        String[] colonneCommune = Function.getColonneCommune(table);

        Table temp = new Table();
        temp.setColumn(table.getColumn());
        temp.setValues(table.getValues());

        for (String colComm : colonneCommune) {
            Vector<String[]> values = new Vector<>();

            for (int j = 0; j < temp.getValues().size(); j++) {
                String[] val = new String[2];

                for (int k = 0, l = 0; k < temp.getValues().get(j).length; k++) {
                    if (temp.getColumn()[k].equals(colComm)) {
                        val[l] = temp.getValues().get(j)[k];
                        l++;
                    }
                }
                if (val[0].equals(val[1])) {
                    values.add(temp.getValues().get(j));
                }
            }
            temp.setValues(values);
        }

        table.setValues(temp.getValues());
    }

}
