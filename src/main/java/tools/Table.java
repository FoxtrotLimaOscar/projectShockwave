package tools;

import java.io.IOException;
import java.util.ArrayList;

public class Table {
    private int fixedLength;
    private int[] sizes;
    String title;
    Enum[] align;
    //List = Zeilen/rows, Array = Spalten/columns        spalte : zeile
    private ArrayList<String[]> rows = new ArrayList<>();

    public Table(String title, String[] desc, int[] sizes, Enum[] align) throws IOException {
        this.title = title;
        this.fixedLength = desc.length;
        this.sizes = sizes;
        this.align = align;
        if(sizes.length != fixedLength) {
            throw new IOException("The sizes must be at the same length as the description length");
        } else if (align.length != fixedLength) {
            throw new IOException("The enums must be at the same length as the description length");
        } else {
            rows.add(desc);
        }
    }

    public void addColumn(String[] column) throws IOException {
        if(column.length != fixedLength) {
            throw new IOException("The added column must be at the same length as the description length");
        } else {
            rows.add(column);
        }
    }

    @Override
    public String toString() {
        String main = "";
        if (this.title != null) {
            main = this.title + "\n";
        }
        int rowLength = rows.size() + 3;
        int columnLength = (fixedLength * 2) + 1;

        for(int row = 0; row < rowLength; row++) {
            int tableRow = getTableRow(row);
            if (row != 0) {
                main += "\n";
            }
            for (int column = 0; column < columnLength; column++) {
                String insert;
                int tableColumn = getTableColumn(column);
                if ((column % 2 == 0) && (row == 0 || row == 2 || row == rowLength - 1)) {
                    insert = "+";
                } else if ((column % 2 != 0) && (row == 0 || row == 2 || row == rowLength - 1)) {
                    insert = "-" + StringTools.spacer(sizes[tableColumn] , '-') + "-";
                } else if ((column % 2 == 0) && !(row == 0 || row == 2 || row == rowLength - 1)) {
                    insert = "|";
                } else {
                    Enum realAlign;
                    if (tableRow == 0) {
                        realAlign = ALIGN.LEFT;
                    } else {
                        realAlign = align[tableColumn];
                    }
                    insert = " " + StringTools.align(rows.get(tableRow)[tableColumn], ' ', sizes[tableColumn], realAlign) + " ";
                }
                main += insert;
            }
        }
        return main;
    }

    private int getTableColumn(int column) {
        return (column - 1) / 2;
    }

    private int getTableRow(int row) {
        if (row == 1) {
            return 0;
        } else {
            return row - 2;
        }
    }

    public enum ALIGN {
        LEFT, RIGHT, CENTER,
    }
}
