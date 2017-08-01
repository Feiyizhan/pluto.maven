package demo.pluto.maven.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;



public class SqlUtil {

    
    static final String LINESEPARATOR = System.getProperty("line.separator", "\r\n");
    static final String SQL_ID_WHERE = "allFieldWhereClause";
    static final String SQL_ID_FIELDLIST_SELECT = "selectAllFields";
    static final String SQL_ID_FIELDLIST_UPDATEANDINSERT = "insertAllFields";
    static final String SQL_ID_MEMBERLIST_UPDATEANDINSERT = "insertMemberList";
    static final String SQL_ID_GETLIST = "getList";
    static final String SQL_ID_DELETE = "delete";
    static final String SQL_ID_INSERT = "insert";
    static final String SQL_ID_UPDATE = "update";
    

    /**
     * 根据Package的绝对路径，生成CRUD Sql
     * @author a4yl9zz 
     * @param packageName
     * @param saveFolder
     */
    public static void generateCRUDSqlByPackage(String packageName, String classPath,String saveFolder) {
        List<String> classList = PackageUtil.getClassName(packageName,classPath);
        TestClassLoader classLoader = new TestClassLoader();
        for (String name : classList) {
            try {
                System.out.println(name);
                
                generateCRUDSql(classLoader.loadClass(name,classPath), saveFolder);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据Class文件绝对存放路径，生成CRUD Sql
     * 
     * @author a4yl9zz 
     * @param classPath
     * @param saveFolder
     * @throws ClassNotFoundException
     */
    public static void generateCRUDSql(String classPath, String saveFolder) throws ClassNotFoundException {
        generateCRUDSql(SqlUtil.class.getClassLoader().loadClass(classPath), saveFolder);
    }

    /**
     * 根据指定的BeanClass生成CRUD SQL
     * 
     * @author a4yl9zz 
     * @param cl
     * @param saveFolder
     */
    public static void generateCRUDSql(Class cl, String saveFolder) {
        // 获取类名
        String className = cl.getSimpleName();
        // 获取包名
        String packageName = cl.getPackage().getName() + ".sqlmap";

        File savePackage = new File(saveFolder, packageName.replace(".", "\\"));
        File saveFile = new File(savePackage, className + ".xml");
        if (FileUtil.createDir(savePackage) && FileUtil.createFile(saveFile)) {
            StringBuilder sb = new StringBuilder();
            generateCRUDSql(sb, cl);
            FileUtil.writeFile(saveFile.getAbsolutePath(), sb.toString(), "UTF-8", false);
        }

    }

    /**
     * 生成CRUD标准SQL
     * 
     * @param out
     * @param cl
     */
    public static void generateCRUDSql(StringBuilder out, Class cl) {
        // 获取类名
        String className = cl.getSimpleName();
        // 获取包名
        String packageName = cl.getPackage().getName();
        // 获取命名空间名称
        String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
        
        //获取非List，非序列化字段的成员字段对象列表
        List<FieldBean> allfieldBeanList = getFieldBeanList(cl);
        List<FieldBean> fieldBeanList = getFieldBeanListIgnoreId(cl);
        
        // 生成SqlMap文件头
        addLine(out, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        addLine(out, "<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" \"http://ibatis.apache.org/dtd/sql-map-2.dtd\" >");
        addLine(out, "<sqlMap namespace=\"" + namespace + "\">");
        // 生成typeAlias
        addLine(out, "<typeAlias alias=\"" + namespace + "\" type=\"" + packageName + "." + className + "\"/>");
        // 生成全字段where条件
        generateAllfieldWhereClause(out, cl);
        addLine(out, "");
        //生成全字段sql id
        generateAllFieldListSqlId(out,allfieldBeanList,fieldBeanList);
        
        // 生成查询SQL
        generateGetList(out, cl);
        addLine(out, "");
        // 生成insertSQL
        generateInsert(out, cl);
        addLine(out, "");
        // 生成删除SQL
        generateDelete(out, cl);
        addLine(out, "");
        // 生成update SQL
        generateUpdate(out, cl,fieldBeanList);
        addLine(out, "");
        // 生成SqlMap文件尾
        addLine(out, "</sqlMap>");

    }


    /**
     * 生成全字段查询条件
     * 
     * @param out
     * @param cl
     */
    public static void generateAllfieldWhereClause(StringBuilder out, Class cl) {

        Field[] fields = cl.getDeclaredFields();
        addLine(out, "   <sql id=\"" + SQL_ID_WHERE + "\">");
        addLine(out, "		<dynamic prepend=\" where \">");
        for (int i = 0; i < fields.length; i++) {

            String fieldName = CamelCaseUtils.camelCase2UnderlineName(fields[i].getName());
            String memberName = fields[i].getName();
            String type = fields[i].getType().getName();
            // 过滤掉序列化字段
            if (memberName.equalsIgnoreCase("serialVersionUID")) {
                continue;
            }

            addLine(out, "			<isNotEmpty prepend=\"AND \" property=\"" + memberName + "\">");
            if (type.equalsIgnoreCase("java.util.List")) {
                addLine(out, "				" + fieldName + " IN");
                addLine(out, "	            <iterate  property=\"" + memberName + "\" conjunction=\",\" open=\"(\" close=\")\">");
                addLine(out, "					#" + memberName + "[]#");
                addLine(out, "	            </iterate>");
            } else {
                addLine(out, "	            " + fieldName + " = #" + memberName + "#");
            }
            addLine(out, "			</isNotEmpty>");

        }
        addLine(out, "		</dynamic>");
        addLine(out, "	</sql>");
    }
    
    /**
     * 生产查询和修改的字段SQL。
     * @author a4yl9zz 
     * @param out
     * @param allfieldBeanList
     * @param fieldBeanList
     */
    public static void generateAllFieldListSqlId(StringBuilder out,List<FieldBean> allfieldBeanList,List<FieldBean> fieldBeanList){
        
        

        StringBuilder selectSb  = new StringBuilder();
        addLine(selectSb, "   <sql id=\"" + SQL_ID_FIELDLIST_SELECT + "\">");
        
        StringBuilder updateAndInsertSb  = new StringBuilder();
        addLine(updateAndInsertSb, "   <sql id=\"" + SQL_ID_FIELDLIST_UPDATEANDINSERT + "\">");
        
        StringBuilder memberInsertSb  = new StringBuilder();
        addLine(memberInsertSb, "   <sql id=\"" + SQL_ID_MEMBERLIST_UPDATEANDINSERT + "\">");
        
        for (int i = 0; i < allfieldBeanList.size()-1; i++) {
            FieldBean fb = allfieldBeanList.get(i);
            addLine(selectSb, "           "+fb.getFieldName() +" "+ CamelCaseUtils.underlineName2LowerCamelCase(fb.getFieldName())+",");
        }
        //追加最后一个字段。
        addLine(selectSb, "           "+allfieldBeanList.get(allfieldBeanList.size()-1).getFieldName() +" "+ CamelCaseUtils.underlineName2LowerCamelCase(allfieldBeanList.get(allfieldBeanList.size()-1).getFieldName()));
        addLine(selectSb, "  </sql>");
        //合并到主SQL
        addLine(out, selectSb.toString());
        
        for (int i = 0; i < fieldBeanList.size()-1; i++) {
            FieldBean fb = fieldBeanList.get(i);
            addLine(updateAndInsertSb, "           "+fb.getFieldName() + ",");
            addLine(memberInsertSb, "           "+fb.getMemberName() + ",");
        }
        
        
        //合并
        addLine(updateAndInsertSb, "           "+fieldBeanList.get(fieldBeanList.size()-1).getFieldName() );
        addLine(out, updateAndInsertSb.toString());
        addLine(out, "  </sql>");
        
        
        //合并Member
        addLine(memberInsertSb, "           "+fieldBeanList.get(fieldBeanList.size()-1).getMemberName() );
        addLine(out, memberInsertSb.toString());
        addLine(out, "  </sql>");
        
    }

    /**
     * 生成getListSQL
     * 
     * @param out
     * @param cl
     */
    public static void generateGetList(StringBuilder out, Class cl) {
        // 获取类名
        String className = cl.getSimpleName();
        // 获取命名空间名称
        String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
        // 获取表名
        String tableName = "t_" + CamelCaseUtils.camelCase2UnderlineName(className);

        addLine(out, "    <select id=\"" + SQL_ID_GETLIST + "\" resultClass=\"" + namespace + "\" parameterClass=\"" + namespace + "\">");
        addLine(out, "        SELECT");
        addLine(out, "            <include refid=\""+SQL_ID_FIELDLIST_SELECT+"\" />");
        addLine(out, "        FROM");
        addLine(out, "            " + tableName);
        addLine(out, "        <isParameterPresent>");
        addLine(out, "			<include refid=\"" + SQL_ID_WHERE + "\" />");
        addLine(out, "		</isParameterPresent>");
        addLine(out, "    </select>");
    }


    /**
     * 生成insert SQL
     * 
     * @param out
     * @param cl
     */
    public static void generateInsert(StringBuilder out, Class cl) {
        // 获取类名
        String className = cl.getSimpleName();
        // 获取命名空间名称
        String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
        // 获取表名
        String tableName = "t_" + CamelCaseUtils.camelCase2UnderlineName(className);
        // 生成insert头
        addLine(out, "    <insert id=\"" + SQL_ID_INSERT + "\" parameterClass=\"" + namespace + "\">");
        addLine(out, "		INSERT INTO " + tableName);
        addLine(out, "		(");
        addLine(out, "           <include refid=\""+SQL_ID_FIELDLIST_UPDATEANDINSERT+"\" />");
        addLine(out, "		) VALUES (");
        addLine(out, "           <include refid=\""+SQL_ID_MEMBERLIST_UPDATEANDINSERT+"\" />");
        addLine(out, "		) ");
        addLine(out, "        <selectKey resultClass=\"java.lang.Integer\" keyProperty=\"id\" > ");
        addLine(out, "		   SELECT @@IDENTITY AS id ");
        addLine(out, "		</selectKey>  ");
        addLine(out, "    </insert>");
    }


    /**
     * 生成delete SQL
     * 
     * @param out
     * @param cl
     */
    public static void generateDelete(StringBuilder out, Class cl) {
        // 获取类名
        String className = cl.getSimpleName();
        // 获取命名空间名称
        String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
        // 获取表名
        String tableName = "t_" + CamelCaseUtils.camelCase2UnderlineName(className);
        // 生成insert头
        addLine(out, "    <delete id=\"" + SQL_ID_DELETE + "\" parameterClass=\"" + namespace + "\">");
        addLine(out, "		DELETE FROM " + tableName);
        addLine(out, "       <isParameterPresent>");
        addLine(out, "		    <include refid=\"" + SQL_ID_WHERE + "\" />");
        addLine(out, "		</isParameterPresent>");
        addLine(out, "    </delete>");
    }



    /**
     * 生成update 语句
     * 
     * @param out
     * @param cl
     * @param fieldBeanList
     */
    private static void generateUpdate(StringBuilder out, Class cl,List<FieldBean> fieldBeanList) {
        // 获取类名
        String className = cl.getSimpleName();
        // 获取命名空间名称
        String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
        // 获取表名
        String tableName = "t_" + CamelCaseUtils.camelCase2UnderlineName(className);
        addLine(out, "    <update id=\"" + SQL_ID_UPDATE + "\" parameterClass=\"" + namespace + "\">");
        addLine(out, "		UPDATE " + tableName);
        addLine(out, "		SET");
        for (int i = 0; i < fieldBeanList.size()-1; i++) {
          addLine(out, "		     " + fieldBeanList.get(i).getFieldName() + " = #" + fieldBeanList.get(i).getMemberName() + "#,");
        }
        addLine(out, "           " + fieldBeanList.get(fieldBeanList.size()-1).getFieldName() + " = #" + fieldBeanList.get(fieldBeanList.size()-1).getMemberName() + "#,");
        addLine(out, "        WHERE");
        addLine(out, "             id = #id#");
        addLine(out, "    </update>");

    }

    /**
     * 增加一行到StringBuilder
     * 
     * @param out
     * @param val
     */
    public static void addLine(StringBuilder out, String val) {
        out.append(val);
        out.append(LINESEPARATOR);
    }

    
    /**
     * 获取Class中有效成员字段列表。
     * @author a4yl9zz 
     * @param cl
     * @return
     */
    public static List<FieldBean> getFieldBeanList(Class cl){
        
        List<FieldBean> fieldBeanList = new ArrayList<FieldBean>();
        Field[] fields = cl.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            

            
            String fieldName = CamelCaseUtils.camelCase2UnderlineName(fields[i].getName());
            String memberName = fields[i].getName();
            String type = fields[i].getType().getName();
            // 过滤掉序列化字段
            if (memberName.equalsIgnoreCase("serialVersionUID")) {
                continue;
            }
            if (!type.equalsIgnoreCase("java.util.List")) {
                FieldBean fb = new FieldBean();
                fb.setFieldName(fieldName);
                fb.setMemberName(memberName);
                fb.setType(type);
                fieldBeanList.add(fb);
            }
        }
        
        return fieldBeanList;
        
    }
    
    /**
     * 获取Class中有效成员字段列表,排除掉ID。
     * @author a4yl9zz 
     * @param cl
     * @return
     */
    public static List<FieldBean> getFieldBeanListIgnoreId(Class cl){
        
        List<FieldBean> fieldBeanList = new ArrayList<FieldBean>();
        Field[] fields = cl.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            

            
            String fieldName = CamelCaseUtils.camelCase2UnderlineName(fields[i].getName());
            String memberName = fields[i].getName();
            String type = fields[i].getType().getName();
            // 过滤掉序列化字段
            if (memberName.equalsIgnoreCase("serialVersionUID")) {
                continue;
            }
            // 过滤掉序列化字段
            if (memberName.equalsIgnoreCase("id")) {
                continue;
            }
            if (!type.equalsIgnoreCase("java.util.List")) {
                FieldBean fb = new FieldBean();
                fb.setFieldName(fieldName);
                fb.setMemberName(memberName);
                fb.setType(type);
                fieldBeanList.add(fb);
            }
        }
        
        return fieldBeanList;
        
    }
    
    
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        // Field[] fields = WarrantyBiddingProduct.class.getDeclaredFields();
        // for (int i = 0; i < fields.length; i++) {
        // System.out.println("成员变量" + i + "类型 : " + fields[i].getType().getName());
        // System.out.println("成员变量" + i + "变量名: " + fields[i].getName());
        //
        //
        // }
        // StringBuilder sb = new StringBuilder();
        // generateCRUDSql(sb,WarrantyBiddingProduct.class);
        // System.out.println(sb.toString());

        // System.out.println(WarrantyBiddingProduct.class.getSimpleName());
        String saveFolder = "C:\\github\\GeneratedProject";
        String packageName = "com.mmm.fsfjos.bean";
//        List<String> classList = PackageUtil.getClassName(packageName);
//        for (String name : classList) {
//            try {
//                System.out.println(name);
//                generateCRUDSql(SqlUtil.class.getClassLoader().loadClass(name), saveFolder);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        generateCRUDSqlByPackage(packageName,"C:\\github\\FSFJOS\\workspace\\FSFJOS\\build\\classes\\",saveFolder);
        // generateCRUDSql(WarrantyBiddingProduct.class,saveFolder);

    }

}

class FieldBean{
    String fieldName;
    String memberName ;
    String type ;
    
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    
}
