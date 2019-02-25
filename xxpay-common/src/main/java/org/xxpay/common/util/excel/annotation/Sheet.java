package org.xxpay.common.util.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sheet {
	//sheet的名字
	public String sheetName() default "sheet";
	//在哪一个sheet
	public int sheetNum() default 1;
	//一个sheet存放多少条数据
	public int sheetSize() default 0;
}
