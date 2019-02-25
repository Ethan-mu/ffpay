package org.xxpay.common.util.excel.write;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WriteExcel {
	//默认样式写入
	public boolean write(Map<String, Object> param, List list)throws IOException, IllegalArgumentException, IllegalAccessException;
}
