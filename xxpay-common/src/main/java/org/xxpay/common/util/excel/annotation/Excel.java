package org.xxpay.common.util.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel{
	//从哪一行开始
	public int beginRow() default 1; 
	//是否自动生成序号列
	public boolean isNeedSequence() default true;
	//列表头数据，格式为  字段名：哪一列：列宽,格式为  字段名：哪一列：列宽...
	public String dataHeader() default "";
	//生成后的路径
	public String outFilePath() default "";
	//读取文件的路径
	public String inFilePath() default "";
	//是否自适应高度
	public boolean autoHeight() default false;
	//创建行的方式，add新增一行，insert插入一行
	public String createRowWay() default "add";
}
