package demo.pluto.maven.serializable;

import java.io.Serializable;
import java.util.Arrays;

public class Table implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    

    private String tableName;
    private String[] fieldNames ;
    private String[][] datas;
    private int maxColumns;
    private int totalRows;
    private int currentRow;
    private int currentColumn;
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String[] getFieldNames() {
        return fieldNames;
    }
    public void setFieldNames(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }
    public String[][] getDatas() {
        return datas;
    }
    public void setDatas(String[][] datas) {
        this.datas = datas;
    }
    public int getMaxColumns() {
        return maxColumns;
    }
    public void setMaxColumns(int maxColumns) {
        this.maxColumns = maxColumns;
    }
    public int getTotalRows() {
        return totalRows;
    }
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }
    public int getCurrentRow() {
        return currentRow;
    }
    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }
    public int getCurrentColumn() {
        return currentColumn;
    }
    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
    }
    @Override
    public String toString() {
        return "Table [tableName=" + tableName + ", fieldNames=" + Arrays.toString(fieldNames) + ", datas=" + printDatas() + ", maxColumns="
                + maxColumns + ", totalRows=" + totalRows + ", currentRow=" + currentRow + ", currentColumn=" + currentColumn + "]";
    }

    public String printDatas(){
        if(datas!=null){
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append("[");
            for(int i=0;i<datas.length;i++){
                sBuilder.append(Arrays.toString(datas[i])+"," );
            }
            sBuilder.append("]");
            return sBuilder.toString();
        }else{
            return null;
        }
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentColumn;
        result = prime * result + currentRow;
        result = prime * result + Arrays.hashCode(datas);
        result = prime * result + Arrays.hashCode(fieldNames);
        result = prime * result + maxColumns;
        result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
        result = prime * result + totalRows;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Table other = (Table) obj;
        if (currentColumn != other.currentColumn)
            return false;
        if (currentRow != other.currentRow)
            return false;
        if (!Arrays.deepEquals(datas, other.datas))
            return false;
        if (!Arrays.equals(fieldNames, other.fieldNames))
            return false;
        if (maxColumns != other.maxColumns)
            return false;
        if (tableName == null) {
            if (other.tableName != null)
                return false;
        } else if (!tableName.equals(other.tableName))
            return false;
        if (totalRows != other.totalRows)
            return false;
        return true;
    }
    
    
}
