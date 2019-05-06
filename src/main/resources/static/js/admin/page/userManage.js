/**

 @Name：管理后台-用户管理界面首页
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

        form.on('radio(status)', function(data){
            //lay-filtert=status
            console.log(data.elem); //得到radio原始DOM对象
            console.log(data.value); //被点击的radio的value值
            initList(data.value);
        });
    });
    function initList(status){
        // var status=$("input[name='status']").val();
        if(status==undefined){
            status=$("input[name='status']").val();
        }
        table.render({
            elem: '#usersList' //指定原始表格元素选择器（推荐id选择器）
            // ,height: 315 //容器高度
            ,url: '/getUsersList' //数据接口
            ,where:{"status":status}
            ,request: {
                pageName: 'offset' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            }
            ,page:true
            ,cols:  [[ //表头
                {field: 'id', title: 'id', width:100, sort: true, fixed: 'left'},
                {field: 'name', title: '用户名', width:100, sort: true, fixed: 'left'},
                {field: 'status', title: '账号状态', width:100, sort:true, fixed: 'left',templet: function(d){
                        if(d.status=="0")
                            return "正常" ;
                        else
                            return "被禁";
                    }
                },
                {field: 'act', title: '权限操作', width:120, fixed: 'left',templet: function(d){
                    // console.log(d)
                        if(d.priLv=="0"){
                            return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-up-admin" data-user="'+d.id+'">' +
                                '升为管理员</a></div>';
                        }else if(d.priLv=="1"){
                            return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-cancel-admin" data-user="'+d.id+'">' +
                                '取消权限</a></div>';
                        }
                    }
                },
                {field: 'act', title: '状态操作', width:120, fixed: 'left',templet: function(d){
                    if(d.status=="0"){
                        return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-ban-user" data-user="'+d.id+'">' +
                            '封禁</a></div>';
                    }else if(d.status=="1"){
                        return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-reban-user" data-user="'+d.id+'">' +
                            '解禁</a></div>';
                    }


                    }
                },
                {field: 'status', title: '权限等级', width:100, sort:true , fixed: 'left',templet: function(d){
                        if(d.priLv=="0")
                            return "普通用户" ;
                        else if(d.priLv=="1")
                            return "admin";
                        else if (d.priLv=="2"){
                            return "root";
                        }
                    }
                },
                {field: 'index', title: '用户主页', width:100, fixed: 'left',templet: function(d){
                        return '<a href="/user/'+d.id+'">跳转</a>'
                    }
                }

            ]] //设置表头
            //,…… //更多参数参考右侧目录：基本参数选项
        });

    }
    function initEvent(){
        $(document).on('click','.js-up-admin',function(){
            var here=$(this);
            layer.confirm("确认提升该用户为管理员？",function (index) {

                var user_id=here.attr("data-user");
                common.ajax("/upPrivage/",{
                    "userId":user_id
                },function (result) {
                    if(result.code=='0'){
                        layer.msg("提升权限成功");
                        // window.location=location.reload()
                    }else if(result.code=="1"){
                        layer.msg("提升权限失败"+result.msg);
                    }else {
                        layer.msg("请重新登陆");
                    }
                })
                layer.close(index)
            })
        })
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
        $(document).on('click','.js-ban-user',function(){
            var here=$(this);
            var u_id=here.attr("data-user");
            console.log(u_id);
            layer.confirm("封禁该用户？",function (index) {
                common.ajax("/admin/banUser",{
                    "userId":u_id
                },function (result) {
                    console.log(result);
                    if(result.code=='0'){
                        layer.msg("封禁该用户成功");
                        // window.location=location.reload()
                    }else if(result.code=="1"){
                        layer.msg("操作失败");
                    }else {
                        layer.msg("请重新登陆");
                    }
                })
                layer.close(index)
            })

        })
        $(document).on('click','.js-reban-user',function(){
            var here=$(this);
            var u_id=here.attr("data-user");
            console.log(u_id);
            layer.confirm("封禁该用户？",function (index) {
                common.ajax("/admin/rebanUser",{
                    "userId":u_id
                },function (result) {
                    console.log(result);
                    if(result.code=='0'){
                        layer.msg("解禁该用户成功");
                        // window.location=location.reload()
                    }else if(result.code=="1"){
                        layer.msg("操作失败");
                    }else {
                        layer.msg("请重新登陆");
                    }
                })
                layer.close(index)
            })

        })
    }
    //输出接口
    exports('userManage', {});
});
