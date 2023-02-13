package inc;

import object.Table;

import java.io.*;
import java.util.Objects;
import java.util.Vector;

public class Helper {

    public static Table listDatabase() {
        Table tab = new Table();

        String[] col = {"Numero", "Nom", "Nombre de tables"};
        tab.setColumn(col);

        Vector<String[]> tempVal = new Vector<>();
        String[] temp;

        File file = new File("./data/");
        File[] listFiles = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listFiles).length; i++) {
            temp = new String[3];

            temp[0] = String.valueOf(i+1);
            temp[1] = listFiles[i].getName();
            temp[2] = String.valueOf(Objects.requireNonNull(new File("./data/" + temp[1]).listFiles()).length);
            tempVal.add(temp);
        }
        tab.setValues(tempVal);

        return tab;
    }

    public static Table listTable(String baseName) throws IOException {
        Table tab = new Table();

        String[] col = {"Numero", "Nom", "Champs"};
        tab.setColumn(col);

        Vector<String[]> tempVal = new Vector<>();
        String[] temp;

        File file = new File("./data/" + baseName);
        File[] listFiles = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listFiles).length; i++) {
            temp = new String[3];

            temp[0] = String.valueOf(i+1);
            temp[1] = listFiles[i].getName().split("\\.")[0];
            temp[2] = Helper.ArrayToString(Helper.getChamps(temp[1], baseName));
            tempVal.add(temp);
        }
        tab.setValues(tempVal);

        return tab;
    }

    private static String[] getChamps(String tabName, String baseName) throws IOException {

        File file = new File("./data/" + baseName + "/" + tabName + ".txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        String[] cols = line.split("/");

        fr.close();
        br.close();

        return cols;
    }

    private static String ArrayToString(String[] arr) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            builder.append(arr[i].toUpperCase());
            if(i != arr.length-1) builder.append(", ");
        }

        return builder.toString();
    }

    public static Table selectAll(String tabName, String baseName) throws IOException {
        Table tab = new Table();
        File file = new File("./data/" + baseName + "/" + tabName + ".txt");

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        String[] cols = line.split("/");
        tab.setColumn(cols);

        Vector<String[]> values = new Vector<>();
        String[] temp;
        while (br.ready()) {
            line = br.readLine();
            temp = line.split("/");
            values.add(temp);
        }
        tab.setValues(values);

        return tab;
    }

    public static void deleteDatabase(String baseName) {
        File file = new File("./data/" + baseName);

        File[] listFiles = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listFiles).length; i++) {
            listFiles[i].delete();
        }
        file.delete();
    }

    public static void deleteTable(String tabName, String baseName) {
        File file = new File("./data/" + baseName + "/" + tabName + ".txt");
        file.delete();
    }
}
