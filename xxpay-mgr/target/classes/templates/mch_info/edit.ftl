<div style="margin: 15px;">
	<form class="layui-form">

		<div class="layui-form-item">
			<label class="layui-form-label">商户ID</label>
			<div class="layui-input-block">
			<#if (item.mchId)??>
				<input type="text" name="mchId" placeholder="请输入商户ID" autocomplete="off" class="layui-input"  value="${item.mchId?if_exists }" readonly="readonly">
			<#else>
                <input type="text" name="mchId" placeholder="请输入商户ID" autocomplete="off" class="layui-input"  value="${item.mchId?if_exists }">
			</#if>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">商户名称</label>
			<div class="layui-input-block">
				<input type="text" name="name" lay-verify="required" placeholder="请输入商户名称" autocomplete="off" class="layui-input" value="${item.name?if_exists }">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">商户类型</label>
			<div class="layui-input-block">
				<select name="type" lay-verify="required">
					<option value=""></option>
                    <option value="1" <#if (item.type!"") == "1">selected="selected"</#if>>平台账户</option>
                    <option value="2" <#if (item.type!"") == "2">selected="selected"</#if>>私有账户</option>
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">是否启用</label>
			<div class="layui-input-block">
				<input type="checkbox" name="state" lay-skin="switch" <#if (item.state!1) == 1>checked="checked"</#if> >
			</div>
		</div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">负责人姓名</label>
            <div class="layui-input-block">
                <input type="text" name="leaderName" placeholder="请输入负责人姓名" lay-verify="required"  class="layui-input"  value="${item.leaderName?if_exists }">
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">负责人电话</label>
            <div class="layui-input-block">
                <input type="text" name="leaderPhone" placeholder="请输入负责人电话" lay-verify="required" class="layui-input"  value="${item.leaderPhone?if_exists }">
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">办公地址</label>
            <div class="layui-input-block">
                <input type="text" name="officeAdress" placeholder="请输入办公地址" lay-verify="required"  class="layui-input"  value="${item.officeAdress?if_exists }">
            </div>
        </div>
		<div class="layui-form-item layui-form-text">
			<label class="layui-form-label">请求私钥</label>
			<div class="layui-input-block">
				<textarea name="reqKey" placeholder="请输入商户请求私钥" lay-verify="required" class="layui-textarea">${item.reqKey?if_exists }</textarea>
			</div>
		</div>
		<div class="layui-form-item layui-form-text">
            <label class="layui-form-label">响应私钥</label>
            <div class="layui-input-block">
                <textarea name="resKey" placeholder="请输入商户响应私钥" lay-verify="required" class="layui-textarea">${item.resKey?if_exists }</textarea>
            </div>
        </div>


		<input type="hidden" name="createTime" disabled="disabled" class="layui-input" value="${(item.createTime?string("yyyy-MM-dd HH:mm:ss"))!''} ">
		<input type="hidden" disabled="disabled" class="layui-input" value="${(item.updateTime?string("yyyy-MM-dd HH:mm:ss"))!''} ">
		<button lay-filter="edit" lay-submit style="display: none;"></button>
	</form>
</div>