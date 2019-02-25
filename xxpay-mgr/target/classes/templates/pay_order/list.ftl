<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>支付订单</title>
		<link rel="stylesheet" href="../plugins/layui/css/layui.css" media="all" />
		<link rel="stylesheet" href="../css/global.css" media="all">
		<link rel="stylesheet" href="../plugins/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="../css/table.css" />
        <script src="../plugins/laydate/laydate.js"></script>
    </head>

	<body>
		<div class="admin-main">

            <blockquote class="layui-elem-quote">
                <div class="layui-btn layui-btn-small" id=""></div>
                <div class="layui-form" style="float:right;">
                    <div class="layui-form-item" style="margin:0;">
                        <label class="layui-form-label">支付单号</label>
                        <div class="layui-input-inline">
                            <input type="text" name="payOrderId" id="payOrderId" autocomplete="off" class="layui-input">
                        </div>
						<label class="layui-form-label">商户ID</label>
                        <div class="layui-input-inline">
                            <input type="text" name="mchId" id="mchId" autocomplete="off" class="layui-input">
                        </div>
                        </br>
                        <label class="layui-form-label">请求流水号</label>
                        <div class="layui-input-inline">
                            <input type="text" name="mchOrderNo" id="mchOrderNo" autocomplete="off" class="layui-input" width="50px">
                        </div>
						<label class="layui-form-label">支付渠道流水号</label>
                        <div class="layui-input-inline">
                            <input type="text" name="channelOrderNo" id="channelOrderNo" autocomplete="off" class="layui-input">
                        </div>
                        <label class="layui-form-label">日期范围</label>
                        <div class="layui-input-inline">
                            <div class="mt40">
                                <input type="text" name="createTime" id="createTime" autocomplete="off" class="demo-input">
                            </div>
                        </div>
                        </br>
                        <label class="layui-form-label">订单状态</label>
                        <div class="layui-input-inline">
                                <select name="status" id="status" lay-search="">
                                <option value="-99">所有状态</option>
                                <option value="0" >订单生成</option>
                                <option value="1" >支付中</option>
                                <option value="2" >支付成功</option>
                                <option value="3" >处理完成</option>
                            </select>
                        </div>
                        <div class="layui-form-mid layui-word-aux" style="padding:0;">
                            <button id="search" lay-filter="search" class="layui-btn" lay-submit><i class="fa fa-search" aria-hidden="true"></i> 查询</button>
                            <button id="export" lay-filter="search" class="layui-btn" lay-submit><i class="fa fa-search" aria-hidden="true"></i> 导出</button>
                        </div>
                    </div>
                </div>
            </blockquote>

			<fieldset class="layui-elem-field">
				<legend>订单列表</legend>
				<div class="layui-field-box layui-form">
					<table class="layui-table admin-table">
						<thead>
							<tr>
								<th style="width: 30px;"><input type="checkbox" lay-filter="allselector" lay-skin="primary"></th>
                                <th>支付单号</th>
                                <th>商户ID</th>
                                <th>请求流水号</th>
                                <th>支付渠道订单号</th>
                                <th>渠道ID</th>
                                <th>金额(元)</th>
                                <th>状态</th>
                                <th>创建时间</th>
                                <th>操作</th>
							</tr>
						</thead>
						<tbody id="content">
						</tbody>
					</table>
				</div>
			</fieldset>
			<div class="admin-table-page">
				<div id="paged" class="page">
				</div>
			</div>
		</div>
		<!--模板-->
		<script type="text/html" id="tpl">
			{{# layui.each(d.list, function(index, item){ }}
			<tr>
				<td><input type="checkbox" lay-skin="primary"></td>
                <td>{{ item.payOrderId }}</td>
                <td>{{ item.mchId }}</td>
                <td>{{ item.mchOrderNo }}</td>
                <td>{{ item.channelOrderNo }}</td>
                <td>{{ item.channelId }}</td>
                <td>{{ item.amount }}</td>
                <td>
                    {{# if(item.status === 0){ }} <span style="color: black">订单生成</span> {{#  } }}
                    {{# if(item.status === 1){ }} <span style="color: blue">支付中</span> {{#  } }}
                    {{# if(item.status === 2){ }} <span style="color: green">支付成功</span> {{#  } }}
                    {{# if(item.status === 3){ }} <span style="color: orange">处理完成</span> {{#  } }}
                </td>
                <td>{{ item.createTime }}</td>
				<td>
					<a href="javascript:;" data-id="{{ item.payOrderId }}" data-opt="view" class="layui-btn layui-btn-normal layui-btn-mini">订单详情</a>
					<#--<a href="javascript:;" data-id="{{ item.payOrderId }}" data-opt="refund" class="layui-btn layui-btn-mini">发起退款</a>-->
					<!--<a href="javascript:;" data-id="{{ item.mchId }}" data-opt="del" class="layui-btn layui-btn-danger layui-btn-mini">删除</a>-->
				</td>
			</tr>
			{{# }); }}
		</script>
		<script type="text/javascript" src="../plugins/layui/layui.js"></script>
		<script>
			layui.config({
				base: '../js/'
			});

			layui.use(['paging', 'form'], function() {
				var $ = layui.jquery,
					paging = layui.paging(),
					layerTips = parent.layer === undefined ? layui.layer : parent.layer, //获取父窗口的layer对象
					layer = layui.layer, //获取当前窗口的layer对象
					form = layui.form();

                paging.init({
                    openWait: true,
					url: '/pay_order/list?v=' + new Date().getTime(), //地址
					elem: '#content', //内容容器
					params: { //发送到服务端的参数
					},
					type: 'GET',
					tempElem: '#tpl', //模块容器
					pageConfig: { //分页参数配置
						elem: '#paged', //分页容器
						pageSize: 10 //分页大小
					},
					success: function() { //渲染成功的回调
						//alert('渲染成功');
					},
					fail: function(msg) { //获取数据失败的回调
						//alert('获取数据失败')
					},
					complate: function() { //完成的回调
						//alert('处理完成');
						//重新渲染复选框
						form.render('checkbox');
						form.on('checkbox(allselector)', function(data) {
							var elem = data.elem;

							$('#content').children('tr').each(function() {
								var $that = $(this);
								//全选或反选
								$that.children('td').eq(0).children('input[type=checkbox]')[0].checked = elem.checked;
								form.render('checkbox');
							});
						});

                        //绑定所有预览按钮事件
                        $('#content').children('tr').each(function() {
                            var $that = $(this);
                            $that.children('td:last-child').children('a[data-opt=view]').on('click', function() {
                                viewForm($(this).data('id'));
                            });
                        });

						//绑定所有编辑按钮事件						
						$('#content').children('tr').each(function() {
                            var $that = $(this);
                            $that.children('td:last-child').children('a[data-opt=refund]').on('click', function() {
                                layer.confirm('确定发起退款么？', {
                                    btn: ['退款','放弃'] //按钮
                                }, function(){
                                    layer.msg('功能还再开发中...', {icon: 1});
                                }, function(){
                                });
                            });
						});

                        //绑定所有删除按钮事件
                        $('#content').children('tr').each(function() {
                            var $that = $(this);
                            $that.children('td:last-child').children('a[data-opt=del]').on('click', function() {
                                layer.msg($(this).data('id'));
                            });
                        });

					},
				});
				//获取所有选择的列
				$('#getSelected').on('click', function() {
					var names = '';
					$('#content').children('tr').each(function() {
						var $that = $(this);
						var $cbx = $that.children('td').eq(0).children('input[type=checkbox]')[0].checked;
						if($cbx) {
							var n = $that.children('td:last-child').children('a[data-opt=edit]').data('name');
							names += n + ',';
						}
					});
					layer.msg('你选择的名称有：' + names);
				});

				$('#search').on('click', function() {
					var mchId = $("#mchId").val();
					var payOrderId = $("#payOrderId").val();
                    var status = $("#status").val();
                    var mchOrderNo = $("#mchOrderNo").val();
                    var channelOrderNo = $("#channelOrderNo").val();
                    var createTime = $("#createTime").val();
                    paging.get({
                        "mchId": mchId,
						"payOrderId":payOrderId,
						"status":status,
						"mchOrderNo":mchOrderNo,
						"channelOrderNo":channelOrderNo,
						"param1":createTime,
                        "v":new Date().getTime()
                    });
				});

				var addBoxIndex = -1;

				$('#import').on('click', function() {
					var that = this;
					var index = layer.tips('只想提示地精准些', that, { tips: [1, 'white'] });
					$('#layui-layer' + index).children('div.layui-layer-content').css('color', '#000000');
				});

				//订单导出
                $('#export').on('click', function() {
                    doExport();
                });
                function doExport() {
                    var mchId = $("#mchId").val();
                    var payOrderId = $("#payOrderId").val();
                    var status = $("#status").val();
                    var mchOrderNo = $("#mchOrderNo").val();
                    var channelOrderNo = $("#channelOrderNo").val();
                    var createTime = $("#createTime").val();

                    window.location.href="../pay_order/export?mchId="+mchId+"&payOrderId="+payOrderId+"&status="+status+
                            "&mchOrderNo="+mchOrderNo+"&channelOrderNo="+channelOrderNo+"&param1="+createTime;
                    }

				function viewForm(id) {
                    //本表单通过ajax加载 --以模板的形式，当然你也可以直接写在页面上读取
                    $.get('/pay_order/view.html?payOrderId=' + id, null, function(form) {
                        addBoxIndex = layer.open({
                            type: 1,
                            title: '订单详情',
                            content: form,

                            shade: false,
                            offset: ['100px', '30%'],
                            area: ['600px', '550px'],
                            zIndex: 19950924,
                            maxmin: false,

                            full: function(elem) {
                                var win = window.top === window.self ? window : parent.window;
                                $(win).on('resize', function() {
                                    var $this = $(this);
                                    elem.width($this.width()).height($this.height()).css({
                                        top: 0,
                                        left: 0
                                    });
                                    elem.children('div.layui-layer-content').height($this.height() - 95);
                                });
                            },
                            end: function() {
                                addBoxIndex = -1;
                            }
                        });
                        layer.full(addBoxIndex);
                    });
				}
			});
		</script>
        <script>
            lay('#version').html('-v'+ laydate.v);

            //执行一个laydate实例
            //常规用法
            laydate.render({
                elem: '#test1' //指定元素
            });

            //日期范围
            laydate.render({
                elem: '#createTime'
                ,range: true
            });

            //年月范围
            laydate.render({
                elem: '#test8'
                ,type: 'month'
                ,range: true
            });
            //时间范围
            laydate.render({
                elem: '#test9'
                ,type: 'time'
                ,range: true
            });
            //日期时间范围
            laydate.render({
                elem: '#test10'
                ,type: 'datetime'
                ,range: true
            });
            //墨绿主题
            laydate.render({
                elem: '#test29'
                ,theme: 'molv'
            });
            //自定义颜色
            laydate.render({
                elem: '#test30'
                ,theme: '#393D49'
            });
            //格子主题
            laydate.render({
                elem: '#test31'
                ,theme: 'grid'
            });

        </script>
        <style>
            body{padding: 20px;}
            .demo-input{padding-left: 10px; height: 38px; min-width: 100px; line-height: 38px; border: 1px solid #e6e6e6;  background-color: #fff;  border-radius: 2px;}

        </style>
	</body>

</html>