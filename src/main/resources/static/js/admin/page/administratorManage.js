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
    });
    function initList(){
        table.render({
            elem: '#admin-list' //指定原始表格元素选择器（推荐id选择器）
            ,height: 315 //容器高度
            ,url: '/getAdminsList/' //数据接口
            ,where:{"limit":10,"offset":1}
            ,request: {
                pageName: 'offset' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            }
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
                }

            ]] //设置表头
            //,…… //更多参数参考右侧目录：基本参数选项
        });

    }
    function initEvent(){

    }
    //输出接口
    exports('administratorManage', {});
});
