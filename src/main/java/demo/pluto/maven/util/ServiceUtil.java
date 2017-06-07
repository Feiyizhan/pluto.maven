package demo.pluto.maven.util;

import java.io.File;
import java.util.List;




public class ServiceUtil {
	static final String LINESEPARATOR = System.getProperty("line.separator", "\r\n"); 
	static final String FUNCTION_GETLIST ="getList";
	static final String FUNCTION_DELETE ="delete";
	static final String FUNCTION_INSERT ="insert";
	static final String FUNCTION_UPDATE ="update";
	
	public static void generateCURDService(Class cl,String saveFolder,String basePackage){
		//获取类名
		String className = cl.getSimpleName();
		//获取包名
		String servicePackage =basePackage+".service";
		
		File savePackage = new File(saveFolder,servicePackage.replace(".", "\\"));
		File saveFile = new File(savePackage,className+"Service.java");
		if(FileUtil.createDir(savePackage) && FileUtil.createFile(saveFile) ){
			StringBuilder sb = new StringBuilder();
			generateCURDService(sb,cl,servicePackage);
			FileUtil.writeFile(saveFile.getAbsolutePath(), sb.toString(), "UTF-8", false);
		}
		
	}
	
	/**
	 * 生成CRUD Service内容
	 * @param out
	 * @param cl
	 */
	public static void generateCURDService(StringBuilder out,Class cl,String servicePackage){
		//获取类名
		String className = cl.getSimpleName();
		//获取命名空间名称
		String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
		//获取Service Nmae
		String serviceName =className+"Service";
		//
		//生成Service文件头
		addLine(out, "package "+servicePackage+";");
		addLine(out, "");
//		addLine(out, "import java.sql.SQLException;");
		addLine(out, "import java.util.List;");
		addLine(out, "");
		addLine(out, "import org.springframework.stereotype.Service;");
		addLine(out, "");
		addLine(out, "import "+cl.getName()+";");
		addLine(out, "");
		addLine(out, "");
		addLine(out, "@Service");
		addLine(out, "public class "+serviceName+" extends BaseService  {");
		addLine(out, "");
		//生成查询方法
		generateGetList(out,cl);
		addLine(out, "");
		//生成insert方法
		generateInsert(out,cl);
		addLine(out, "");
		//生成删除方法
		generateDelete(out,cl);
		addLine(out, "");
		//生成删除方法
		generateUpdate(out,cl);
		addLine(out, "");
		
		//生成末尾
		addLine(out, "}");

		
	}


	
	/**
	 * 生成GetList方法
	 * @param out
	 * @param cl
	 */
	public static void generateGetList(StringBuilder out,Class cl){
		//获取类名
		String className = cl.getSimpleName();
		//获取命名空间名称
		String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
		//生成方法注释
		addLine(out, "	/**");
		addLine(out, "	* getList 方法");
		addLine(out, "	* @param "+namespace);
		addLine(out, "	* @return");
		addLine(out, "	* @throws Exception");
		addLine(out, "	*/");
		//生成方法 体
		addLine(out,"	public List<"+className+"> "+FUNCTION_GETLIST+"("+className+" "+namespace+") throws Exception{");
		addLine(out, "		return (List<"+className+">)this.getSqlMapClient().queryForList(\""+namespace+"."+FUNCTION_GETLIST+"\","+namespace+");");
		addLine(out, "	}");
		
		
	}
	
	/**
	 * 生成insert方法
	 * @param out
	 * @param cl
	 */
	public static void generateInsert(StringBuilder out,Class cl){
		//获取类名
		String className = cl.getSimpleName();
		//获取命名空间名称
		String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
		//生成方法注释
		addLine(out, "	/**");
		addLine(out, "	* insert 方法");
		addLine(out, "	* @param "+namespace);
		addLine(out, "	* @return");
		addLine(out, "	* @throws Exception");
		addLine(out, "	*/");
		//生成方法 体
		addLine(out,"	public Integer "+FUNCTION_INSERT+"("+className+" "+namespace+") throws Exception{");
		addLine(out, "		return (Integer)this.getSqlMapClient().insert(\""+namespace+"."+FUNCTION_INSERT+"\","+namespace+");");
		addLine(out, "	}");
		
		
	}
	
	/**
	 * 生成delete方法
	 * @param out
	 * @param cl
	 */
	public static void generateDelete(StringBuilder out,Class cl){
		//获取类名
		String className = cl.getSimpleName();
		//获取命名空间名称
		String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
		//生成方法注释
		addLine(out, "	/**");
		addLine(out, "	* delete 方法");
		addLine(out, "	* @param "+namespace);
		addLine(out, "	* @return");
		addLine(out, "	* @throws Exception");
		addLine(out, "	*/");
		//生成方法 体
		addLine(out,"	public Integer "+FUNCTION_DELETE+"("+className+" "+namespace+") throws Exception{");
		addLine(out, "		return (Integer)this.getSqlMapClient().delete(\""+namespace+"."+FUNCTION_DELETE+"\","+namespace+");");
		addLine(out, "	}");

	}
	
	/**
	 * 生成update方法
	 * @param out
	 * @param cl
	 */
	public static void generateUpdate(StringBuilder out,Class cl){
		//获取类名
		String className = cl.getSimpleName();
		//获取命名空间名称
		String namespace = CamelCaseUtils.UpperCamelCase2LowerCamelCase(className);
		//生成方法注释
		addLine(out, "	/**");
		addLine(out, "	* update 方法");
		addLine(out, "	* @param "+namespace);
		addLine(out, "	* @return");
		addLine(out, "	* @throws Exception");
		addLine(out, "	*/");
		//生成方法 体
		addLine(out,"	public Integer "+FUNCTION_UPDATE+"("+className+" "+namespace+") throws Exception{");
		addLine(out, "		return (Integer)this.getSqlMapClient().update(\""+namespace+"."+FUNCTION_UPDATE+"\","+namespace+");");
		addLine(out, "	}");

	}
	
	/**
	 * 增加一行到StringBuilder
	 * @param out
	 * @param val
	 */
	public static void addLine(StringBuilder out,String val){
		out.append(val);
		out.append(LINESEPARATOR);
	}
	

	public static void main(String[] args){
		String saveFolder="C:\\github\\GeneratedProject";
		String basePackage="com.mmm.fsfjos";
		String beanPackage="com.mmm.fsfjos.bean";
		String classPath="C:\\github\\FSFJOS\\workspace\\FSFJOS\\build\\classes\\";
		List<String> classList =PackageUtil.getClassName(beanPackage,classPath);
		TestClassLoader classLoader = new TestClassLoader();
		for(String name:classList){
			try {
				System.out.println(name);
				generateCURDService(classLoader.loadClass(name,classPath),saveFolder,basePackage);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
