package demo.pluto.maven.serializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RunClass {
    public static void main(String[] args){
        testSerializableObjectToMultiFile();
    }
    
    
    /**
     * 测试系列化对象到多个文件
     * @author A4YL9ZZ pxu3@mmm.com
     */
    public static void testSerializableObjectToMultiFile(){
        String databaseName="database";
        Database database = new Database();
        database.setDatabaseName(databaseName);
        
        List<String> tablenameList = new ArrayList<String>();
        tablenameList.add("t_account");
        tablenameList.add("t_log");
        tablenameList.add("t_data");
        database.setCurrentTableIndex(tablenameList.size());
        database.setTableCount(tablenameList.size());
        database.setTableNameList(tablenameList);
        Map<String, Table> tableMap= new HashMap<String,Table>();
        
        Table accountTable =new Table();
        accountTable.setTableName("t_account");
        String[] fieldNames = {"id","name","password","logind_date"};
        accountTable.setFieldNames(fieldNames);
        accountTable.setCurrentColumn(fieldNames.length-1);
        accountTable.setCurrentRow(2);
        accountTable.setMaxColumns(fieldNames.length);
        accountTable.setTotalRows(5);
        accountTable.setDatas(new String[fieldNames.length][100]);
        tableMap.put("t_account",accountTable);    
            
        Table logTable =new Table();
        logTable.setTableName("t_log");
        fieldNames =new String[] {"id","value","date"};
        logTable.setFieldNames(fieldNames);
        logTable.setCurrentColumn(fieldNames.length-1);
        logTable.setCurrentRow(2);
        logTable.setMaxColumns(fieldNames.length);
        logTable.setTotalRows(5);
        logTable.setDatas(new String[fieldNames.length][100]);
        tableMap.put("t_log",logTable);  
        
        
        Table dataTable =new Table();
        dataTable.setTableName("t_data");
        fieldNames =new String[] {"id","data"};
        dataTable.setFieldNames(fieldNames);
        dataTable.setCurrentColumn(fieldNames.length-1);
        dataTable.setCurrentRow(2);
        dataTable.setMaxColumns(fieldNames.length);
        dataTable.setTotalRows(5);
        dataTable.setDatas(new String[fieldNames.length][100]);
        tableMap.put("t_data",dataTable); 
        
        database.setTableMap(tableMap);
        
        String folederName="./testSerializableObjectToMultiFile";
        writeDatabaseToFolder(database,folederName);
        
        Database  database2 = readDatabaseFromFolder(folederName,databaseName);
        if(database.equals(database2)){
            System.out.println("identical");
        }else{
            System.out.println("different");
        }
        
    }
    

    /**
     * 系列化Database对象全部信息到目录<br/>
     * 对于tableList成员，每个Table独立生成一个文件。文件名为Table名称
     * @author A4YL9ZZ pxu3@mmm.com
     * @param database
     * @param folderName
     */
    public static void writeDatabaseToFolder(Database database,String folderName){
        File folder = new File(folderName);
        if(folder.isDirectory() || !folder.exists()){
            folder.mkdirs();   
        }
        File databaseFile = new File(folderName,database.getDatabaseName());
        writeObjectToFile(database,databaseFile.getPath());
        Map<String, Table> tableMap= database.getTableMap();
        List<String> tableNameList = database.getTableNameList();
        for(String tableName:tableNameList){
            File tableFile = new File(folderName,tableName);
            System.out.println(tableName);
            writeObjectToFile(tableMap.get(tableName),tableFile.getPath());
        }
        
    }
    

    /**
     * 从目录中反系列化Database对象全部信息
     * @author A4YL9ZZ pxu3@mmm.com
     * @param folderName
     * @param databaseName
     * @return
     */
    public static Database readDatabaseFromFolder(String folderName,String databaseName){
        File folder = new File(folderName);
        if(folder.isDirectory() && folder.exists()){
            File databaseFile = new File(folderName,databaseName);
            Database database = (Database)readObjectFromFile(databaseFile.getPath());
            
            Map<String, Table> tableMap= new HashMap<String,Table>();
            List<String> tableNameList = database.getTableNameList();
            for(String tableName:tableNameList){
                System.out.println(tableName);
                File tableFile = new File(folderName,tableName);
                Table table = (Table)readObjectFromFile(tableFile.getPath());
                tableMap.put(tableName, table);
            }
            database.setTableMap(tableMap);
            return database;
        }else{
            return null;
        }
        
        
    }
    
    
    /**
     * 保存对象
     * @author A4YL9ZZ pxu3@mmm.com
     * @param obj
     */
    public static void writeObjectToFile(Object obj,String fileName)
    {
        File file =new File(fileName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }
    
    
    /**
     * 读取对象
     * @author A4YL9ZZ pxu3@mmm.com
     * @param fileName
     * @return
     */
    public static Object readObjectFromFile(String fileName)
    {
        Object temp=null;
        File file =new File(fileName);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
