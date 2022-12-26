package object;

import inc.Function;

import java.io.File;
import java.util.Objects;
import java.util.Vector;

public class Database {
    String name;
    Vector<Table> tables = new Vector<>();
    File file;

    public Database(String request) throws Exception {
        this.name = Function.getNomDatabase(request);
        this.file = new File("./data/" + this.name);
    }

    public void create() {
        boolean b = file.mkdir();
    }

    public boolean exists() {
        return file.exists();
    }

    public void use() throws Exception {
        if(!exists()) throw new Exception("Base de données inexistante");

        File[] listFiles = file.listFiles();
        Vector<Table> tables = new Vector<>();
        for (int i = 0; i < Objects.requireNonNull(listFiles).length; i++) {
            Table tab = new Table(listFiles[i], this);
            tab.getData();
            tables.add(tab);
        }
        this.setTables(tables);
    }

    public Table getTable(String name) throws Exception {
        for (int i = 0; i < getTables().size(); i++) {
            if(getTables().get(i).getName().equalsIgnoreCase(name)) {
                return getTables().get(i);
            }
        }
        throw new Exception("Table inexistante");
    }

    public static Table show() {
        Table tab = new Table();

        String[] col = {"database"};
        tab.setColumn(col);

        Vector<String[]> tempVal = new Vector<>();
        String[] temp;

        File file = new File("./data/");
        File[] listFiles = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listFiles).length; i++) {
            temp = new String[1];
            temp[0] = listFiles[i].getName();
            tempVal.add(temp);
        }
        tab.setValues(tempVal);

        return tab;
    }

    public String getName() {
        return name;
    }

    public Vector<Table> getTables() {
        return tables;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTables(Vector<Table> tables) {
        this.tables = tables;
    }

}
