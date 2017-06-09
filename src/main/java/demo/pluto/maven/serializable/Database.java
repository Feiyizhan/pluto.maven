package demo.pluto.maven.serializable;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


import demo.pluto.maven.util.MapUtil;

public class Database implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String databaseName;

    private List<String> tableNameList;
    private transient Map<String, Table> tableMap;
    private int tableCount;
    private int currentTableIndex; // 当前处理的Table下标


    public Map<String, Table> getTableMap() {
        return tableMap;
    }

    public void setTableMap(Map<String, Table> tableMap) {
        this.tableMap = tableMap;
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
        return "Database [databaseName=" + databaseName + ", tableNameList=" + tableNameList + ", tableMap=" + tableMap + ", tableCount=" + tableCount
                + ", currentTableIndex=" + currentTableIndex + "]";
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentTableIndex;
        result = prime * result + ((databaseName == null) ? 0 : databaseName.hashCode());
        result = prime * result + tableCount;
        result = prime * result + ((tableMap == null) ? 0 : tableMap.hashCode());
        result = prime * result + ((tableNameList == null) ? 0 : tableNameList.hashCode());
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
        Database other = (Database) obj;
        if (currentTableIndex != other.currentTableIndex)
            return false;
        if (databaseName == null) {
            if (other.databaseName != null)
                return false;
        } else if (!databaseName.equals(other.databaseName))
            return false;
        if (tableCount != other.tableCount)
            return false;

            
        if (tableNameList == null) {
            if (other.tableNameList != null)
                return false;
        } else if (!tableNameList.equals(other.tableNameList))
            return false;
        if(!MapUtil.compare(this.tableMap, other.tableMap)){
            return false;
        }
        return true;
    }



}
