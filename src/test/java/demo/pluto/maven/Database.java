package demo.pluto.maven;

import java.io.Serializable;
import java.util.List;

public class Database implements Serializable  {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String databaseName; 
    
    private List<String> tableNameList;
    private List<Table> tableList;
    private int tableCount;
    private int currentTableIndex; //当前处理的Table下标
    

    public List<Table> getTableList() {
        return tableList;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    
    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    
    public List<String> getTableNameList() {
        return tableNameList;
    }

    public void setTableNameList(List<String> tableNameList) {
        this.tableNameList = tableNameList;
    }

    
    public int getCurrentTableIndex() {
        return currentTableIndex;
    }

    public void setCurrentTableIndex(int currentTableIndex) {
        this.currentTableIndex = currentTableIndex;
    }

    @Override
    public String toString() {
        return "Database [databaseName=" + databaseName + ", tableNameList=" + tableNameList + ", tableList=" + tableList + ", tableCount=" + tableCount
                + ", currentTableIndex=" + currentTableIndex + "]";
    }

    
    
    
    
}
