/**

 @Name：管理后台-权限管理界面首页
 @Author：庄巍
 */
layui.define(['element', 'form','laypage','jquery','laytpl','common','table'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,laytpl = layui.laytpl
        ,common=layui.common
        ,layer=layui.layer
        ,table=layui.table;
    $(function () {
        initList();
        initEvent();
    });
    function initList(){
        table.render({
            elem: '#admin-list' //指定原始表格元素选择器（推荐id选择器）
            // ,height: 315 //容器高度
            ,url: '/getAdminsList/' //数据接口
            // ,where:{"limit":10,"offset":1}
            ,request: {
                pageName: 'offset' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            }
            ,page:true
            ,cols:  [[ //表头
                {field: 'id', title: 'id', width:100, sort: true, fixed: 'left'},
                {field: 'name', title: '用户名', width:100, sort: true, fixed: 'left'},
                {field: 'status', title: '账号状态', width:100, fixed: 'left',templet: function(d){
                        if(d.status=="0")
                            return "正常" ;
                        else
                            return "异常";
                    }
                },
                {field: 'act', title: '操作', width:120, fixed: 'left',templet: function(d){
                        return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-cancel-admin" data-user="'+d.id+'">' +
                            '取消权限</a></div>';
                    }
                },
                {field: 'status', title: '权限等级', width:100, fixed: 'left',templet: function(d){
                        if(d.priLv=="1")
                            return "admin" ;
                        else if(d.priLv=="2")
                            return "root";
                    }
                }

            ]] //设置表头
            //,…… //更多参数参考右侧目录：基本参数选项
        });
        table.render({
            elem: '#root-list' //指定原始表格元素选择器（推荐id选择器）
            // ,height: 315 //容器高度
            ,url: '/getRootsList/' //数据接口
            // ,where:{"limit":10,"offset":1}
            ,request: {
                pageName: 'offset' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            }
            ,page:true
            ,cols:  [[ //表头
                {field: 'id', title: 'id', width:100, sort: true, fixed: 'left'},
                {field: 'name', title: '用户名', width:100, sort: true, fixed: 'left'},
                {field: 'status', title: '账号状态', width:100, fixed: 'left',templet: function(d){
                        if(d.status=="0")
                            return "正常" ;
                        else
                            return "异常";
                    }
                },
                {field: 'status', title: '权限等级', width:100, fixed: 'left',templet: function(d){
                        if(d.priLv=="1")
                            return "admin" ;
                        else if(d.priLv=="2")
                            return "root";
                    }
                }

            ]]
        });
    }
    function initEvent(){
        $(document).on('click','.js-cancel-admin',function(){
            var here=$(this);
            var admin_id=here.attr("data-user");
            common.ajax("/downPrivage/",{
                "userId":admin_id
            },function (result) {
                if(result.code=='0'){
                    layer.msg("取消权限成功");
                    // window.location=location.reload()
                }else if(result.code=="1"){
                    layer.msg("操作失败"+result.msg);
                }else {
                    layer.msg("请重新登陆");
                }
            })

        })
    }
    //输出接口
    exports('administratorManage', {});
});
