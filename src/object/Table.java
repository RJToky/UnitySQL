package object;

import inc.Function;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class Table {
    String name;
    String[] column;
    Vector<String[]> values = new Vector<>();
    Database database;
    File file;

    public Table() {}

    public Table(File file, Database database) {
        this.file = file;
        this.database = database;
    }

    public Table(String request, Database database) throws Exception {
        if(database == null) throw new Exception("Aucune base de données selectionnée");

        this.name = Function.getNomTable(request, "create");
        this.column = Function.getNomColonne(request, "create");
        this.database = database;
        this.file = new File("./data/" + database.getName() + "/" + this.name + ".txt");

        database.getTables().add(this);
    }

    public void print() {
        int len = 0;
        int[] maxLength = new int[column.length];

        System.out.println();
        for (int i = 0; i < column.length; i++) {
            maxLength[i] = column[i].length();

            for (String[] value : values) {
                if (maxLength[i] < value[i].length()) {
                    maxLength[i] = value[i].length();
                }
            }
            System.out.print(column[i].toUpperCase());
            for (int j = 0; j < maxLength[i]-column[i].length(); j++) {
                System.out.print(" ");
                len += 1;
            }
            System.out.print("   ");
            len += column[i].length() + 2;
        }
        System.out.println();

        for (int i = 0; i < len; i++) {
            System.out.print("-");
        }
        System.out.println();

        for (String[] value : values) {
            for (int i = 0; i < value.length; i++) {
                System.out.print(value[i]);
                for (int j = 0; j < maxLength[i]-value[i].length(); j++) {
                    System.out.print(" ");
                }
                System.out.print("   ");
            }
            System.out.println();
        }
        System.out.println("(" + values.size() + ((values.size() <= 1) ? " ligne)" : " lignes)"));
    }

    public void getData() throws IOException {
        this.setName(file.getName().split("\\.")[0]);

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        String[] cols = line.split("/");
        this.setColumn(cols);

        Vector<String[]> values = new Vector<>();
        String[] temp;
        while (br.ready()) {
            line = br.readLine();
            temp = line.split("/");
            values.add(temp);
        }
        this.setValues(values);

        fr.close();
        br.close();
    }

    public void create() throws Exception {
        if(file.exists()) throw new Exception("Table existante");

        if(file.createNewFile()) {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String col : column) {
                bw.write(col + "/");
            }
            bw.newLine();
            fw.flush();
            bw.flush();
            fw.close();
            bw.close();
        }
    }

    public static Table show(Database database) throws Exception {
        if(database == null) throw new Exception("Aucune base de données selectionnée");

        Table tab = new Table();

        String[] col = {"tables"};
        tab.setColumn(col);

        Vector<String[]> tempVal = new Vector<>();
        String[] temp;

        File file = new File("./data/" + database.getName() + "/");
        File[] listFiles = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listFiles).length; i++) {
            temp = new String[1];
            temp[0] = listFiles[i].getName().split("\\.")[0];
            tempVal.add(temp);
        }
        tab.setValues(tempVal);

        return tab;
    }

    public Table desc() throws Exception {
        if(database == null) throw new Exception("Aucune base de données selectionnée");

        Table tab = new Table();

        String[] col = {"describe " + this.name};
        tab.setColumn(col);

        Vector<String[]> tempVal = new Vector<>();
        String[] temp;

        for (String c : column) {
            temp = new String[1];
            temp[0] = c;
            tempVal.add(temp);
        }
        tab.setValues(tempVal);

        return tab;
    }

    public void insert(String request) throws Exception {
        if(request.split("\s+").length < 5) throw new Exception("Erreur de syntaxe");

        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);

        String[] values = request.split("values")[1].trim().split("\s+");
        String[] temp = new String[column.length];

        for (int i = 0; i < column.length; i++) {
            try {
                temp[i] = values[i];
                bw.append(temp[i]);
            } catch (Exception e) {
                temp[i] = null;
                bw.append(temp[i]);
            }
            bw.append("/");
        }
        this.values.add(temp);

        bw.newLine();
        fw.flush();
        bw.flush();
        fw.close();
        bw.close();
    }

    public Table selection(String request) throws Exception {
        if(!request.contains("where")) return this;

        String key = Function.getKeyOrValue(request, "key"), value = Function.getKeyOrValue(request, "value");
        Table tab = new Table();
        tab.setColumn(column);

        Vector<String[]> tempVal = new Vector<>();
        for (String[] val : values) {
            if (val[Function.getIndiceColonne(this, key)].equalsIgnoreCase(value)) {
                tempVal.add(val);
            }
        }
        tab.setValues(tempVal);

        return tab;
    }

    public Table projection(String request) throws Exception {
        String[] cols = Function.getNomColonne(request, "select");

        return projection(cols);
    }

    public Table projection(String[] cols) throws Exception {
        Table tab = new Table();

        tab.setColumn(cols);
        if(cols[0].equalsIgnoreCase("*")) {
            tab.setColumn(column);
        }

        Vector<String[]> tempVal = new Vector<>();
        String[] temp;
        for (String[] value : values) {
            temp = new String[tab.getColumn().length];
            for (int j = 0; j < tab.getColumn().length; j++) {
                temp[j] = value[Function.getIndiceColonne(this, tab.getColumn()[j])];
            }
            tempVal.add(temp);
        }
        tab.setValues(tempVal);

        return tab;
    }

    public static Table union(String request, Database databaseUsed) throws Exception {
        if(databaseUsed == null) throw new Exception("Aucune base de données selectionnée");

        Table[] tables = Function.getArrayTableByRequest(request, databaseUsed, "union");

        if(Function.areSameCol(tables)) {
            Table tab = new Table();
            tab.setColumn(tables[0].getColumn());

            Vector<String[]> tempValues = new Vector<>();
            for (Table table : tables) {
                tempValues.addAll(table.getValues());
            }

            Vector<String[]> finalValues = Function.deleteDoublon(tempValues);
            tab.setValues(finalValues);

            return tab;

        } else {
            throw new Exception("Impossible de faire l'opération");
        }
    }

    public static Table intersect(String request, Database databaseUsed) throws Exception {
        if(databaseUsed == null) throw new Exception("Aucune base de données selectionnée");

        Table[] tables = Function.getArrayTableByRequest(request, databaseUsed, "intersect");

        if(Function.areSameCol(tables)) {
            Vector<String[]> tempValues;
            Table tab = tables[0];

            for (int i = 1; i < tables.length; i++) {
                tempValues = new Vector<>();

                for (int j = 0; j < tab.getValues().size(); j++) {
                    for (int k = 0; k < tables[i].getValues().size(); k++) {
                        if(Arrays.equals(tab.getValues().get(j), tables[i].getValues().get(k))) {
                            tempValues.add(tab.getValues().get(j));
                        }
                    }
                }
                tempValues = Function.deleteDoublon(tempValues);
                tab.setValues(tempValues);
            }

            return tab;

        } else {
            throw new Exception("Impossible de faire l'opération");
        }
    }

    public static Table difference(String request, Database databaseUsed) throws Exception {
        if(databaseUsed == null) throw new Exception("Aucune base de données selectionnée");

        Table[] tables = Function.getArrayTableByRequest(request, databaseUsed, "not in");

        if(Function.areSameCol(tables)) {
            Vector<String[]> tempValues;
            Table tab = tables[0];

            boolean match;
            for (int i = 1; i < tables.length; i++) {
                tempValues = new Vector<>();

                for (int j = 0; j < tab.getValues().size(); j++) {
                    match = false;

                    for (int k = 0; k < tables[i].getValues().size(); k++) {
                        if (Arrays.equals(tab.getValues().get(j), tables[i].getValues().get(k))) {
                            match = true;
                            break;
                        }
                    }
                    if(!match) {
                        tempValues.add(tab.getValues().get(j));
                    }
                }
                tempValues = Function.deleteDoublon(tempValues);
                tab.setValues(tempValues);
            }

            return tab;

        } else {
            throw new Exception("Impossible de faire l'opération");
        }
    }

    public static Table product(String request, Database databaseUsed) throws Exception {
        if(databaseUsed == null) throw new Exception("Aucune base de données selectionnée");

        Table[] tables = Function.getArrayTableByRequest(request, databaseUsed, "product");

        Table tab = Function.concatArrayTable(tables);
        tab = tab.projection(Function.getNomColonne(request, "select"));

        return tab;
    }

    public static Table join(String request, Database databaseUsed, String type) throws Exception {
        if(databaseUsed == null) throw new Exception("Aucune base de données selectionnée");

        Table tab;

        if(request.contains("natural join") && type.equals("natural join")) {
            Table[] tables = Function.getArrayTableByRequest(request, databaseUsed, type);

            tab = Function.concatArrayTable(tables);
            try {
                Function.removeNotMatch(tab);

            } catch (Exception ignored) { }
            tab = tab.projection(Function.getNomColonne(request, "select"));

        } else {
            throw new Exception("Erreur de syntaxe");
        }

        return tab;
    }

    public static Table sous(String request, Database databaseUsed) throws Exception {
        if(databaseUsed == null) throw new Exception("Aucune base de données selectionnée");

        return new Table();
    }

    public String getName() {
        return name;
    }

    public String[] getColumn() {
        return column;
    }

    public Vector<String[]> getValues() {
        return values;
    }

    public Database getDatabase() {
        return database;
    }

    public File getFile() {
        return file;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColumn(String[] column) {
        this.column = column;
    }

    public void setValues(Vector<String[]> values) {
        this.values = values;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
