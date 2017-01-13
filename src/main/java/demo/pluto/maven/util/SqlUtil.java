package demo.pluto.maven.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;



public class SqlUtil {

    static final String LINESEPARATOR = System.getProperty("line.separator", "\r\n");
    static final String SQL_ID_WHERE = "allFieldWhereClause";
    static final String SQL_ID_GETLIST = "getList";
    static final String SQL_ID_DELETE = "delete";
    static final String SQL_ID_INSERT = "insert";
    static final String SQL_ID_UPDATE = "update";


    /**
     * 根据Package的绝对路径，生成CRUD Sql
     * @author a4yl9zz pxu3@mmm.com
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
     * @author a4yl9zz pxu3@mmm.com
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
     * @author a4yl9zz pxu3@mmm.com
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
        //
        // 生成SqlMap文件头
        addLine(out, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        addLine(out, "<!DOCTYPE sqlMap PUBLIC \"-//ibatis.apache.org//DTD SQL Map 2.0//EN\" \"http://ibatis.apache.org/dtd/sql-map-2.dtd\" >");
        addLine(out, "<sqlMap namespace=\"" + namespace + "\">");
        // 生成typeAlias
        addLine(out, "<typeAlias alias=\"" + namespace + "\" type=\"" + packageName + "." + className + "\"/>");
        // 生成全字段where条件
        generateAllfieldWhereClause(out, cl);
        addLine(out, "");
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
        generateUpdate(out, cl);
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
        Field[] fields = cl.getDeclaredFields();
        addLine(out, "    <select id=\"" + SQL_ID_GETLIST + "\" resultClass=\"" + namespace + "\" parameterClass=\"" + namespace + "\">");
        addLine(out, "        SELECT");
        for (int i = 0; i < fields.length; i++) {

            String fieldName = CamelCaseUtils.camelCase2UnderlineName(fields[i].getName());
            String memberName = fields[i].getName();
            String type = fields[i].getType().getName();
            // 过滤掉序列化字段
            if (memberName.equalsIgnoreCase("serialVersionUID")) {
                continue;
            }

            if (!type.equalsIgnoreCase("java.util.List")) {
                if (i == fields.length) {
                    addLine(out, "            " + fieldName + " \"" + memberName + "\"");
                } else {
                    addLine(out, "            " + fieldName + " \"" + memberName + "\",");
                }
            }
        }
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
        Field[] fields = cl.getDeclaredFields();
        // 生成insert头
        addLine(out, "    <insert id=\"" + SQL_ID_INSERT + "\" parameterClass=\"" + namespace + "\">");
        addLine(out, "		INSERT INTO " + tableName);
        addLine(out, "		(");
        addLine(out, "		  <dynamic prepend=\" \">");
        for (int i = 0; i < fields.length; i++) {

            String fieldName = CamelCaseUtils.camelCase2UnderlineName(fields[i].getName());
            String memberName = fields[i].getName();
            String type = fields[i].getType().getName();
            // 过滤掉序列化字段
            if (memberName.equalsIgnoreCase("serialVersionUID")) {
                continue;
            }
            // 自增长主键不设置
            if (memberName.equalsIgnoreCase("id")) {
                continue;
            }
            if (!type.equalsIgnoreCase("java.util.List")) {
                addLine(out, "		     <isNotEmpty prepend=\", \" property=\"" + memberName + "\">" + fieldName + "</isNotEmpty>");
            }

        }
        addLine(out, "		  </dynamic>");
        addLine(out, "		) VALUES (");
        addLine(out, "		  <dynamic prepend=\" \">");
        for (int i = 0; i < fields.length; i++) {
            String memberName = fields[i].getName();
            String type = fields[i].getType().getName();
            // 过滤掉序列化字段
            if (memberName.equalsIgnoreCase("serialVersionUID")) {
                continue;
            }
            // 自增长主键不设置
            if (memberName.equalsIgnoreCase("id")) {
                continue;
            }
            if (!type.equalsIgnoreCase("java.util.List")) {
                addLine(out, "		     <isNotEmpty prepend=\", \" property=\"" + memberName + "\">#" + memberName + "#</isNotEmpty>");
            }

        }
        addLine(out, "		  </dynamic>");
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
     */
    private static void generateUpdate(StringBuilder out, Class cl) {
        // 获取类名
        String className = cl.getSimpleName();
        // 获取命名空间名称
        String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
        // 获取表名
        String tableName = "t_" + CamelCaseUtils.camelCase2UnderlineName(className);
        Field[] fields = cl.getDeclaredFields();
        addLine(out, "    <update id=\"" + SQL_ID_UPDATE + "\" parameterClass=\"" + namespace + "\">");
        addLine(out, "		UPDATE " + tableName);
        addLine(out, "		SET");
        addLine(out, "		  <dynamic prepend=\" \">");
        for (int i = 0; i < fields.length; i++) {
            String fieldName = CamelCaseUtils.camelCase2UnderlineName(fields[i].getName());
            String memberName = fields[i].getName();
            String type = fields[i].getType().getName();
            // 过滤掉序列化字段
            if (memberName.equalsIgnoreCase("serialVersionUID")) {
                continue;
            }

            // 主键不更新
            if (memberName.equalsIgnoreCase("id")) {
                continue;
            }
            if (!type.equalsIgnoreCase("java.util.List")) {
                addLine(out, "		     <isNotEmpty prepend=\", \" property=\"" + memberName + "\">" + fieldName + " = #" + memberName + "#</isNotEmpty>");
            }

        }
        addLine(out, "		  </dynamic>");
        addLine(out, "		  WHERE");
        addLine(out, "		        id = #id#");
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
