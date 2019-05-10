/**

 @Name：管理后台-举报处理界面首页
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
            elem: '#undeal-list' //指定原始表格元素选择器（推荐id选择器）
            // ,height: 315 //容器高度
            ,url: '/getReportedUserList' //数据接口
            ,where:{"status":0}
            ,request: {
                pageName: 'offset' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            }
            ,page:true
            ,cols:  [[ //表头
                {field: 'report.entityType', title: '举报记录', width:100, sort: true, fixed: 'left',
                    templet: function(d){
                        return d.report.id;
                    }
                },
                {field: 'status', title: '类型', width:80, fixed: 'left',templet: function(d){
                        if(d.report.entityType=="1")
                            return "问题" ;
                        else if(d.report.entityType=="2")
                            return "回答";
                        else if(d.report.entityType=="3")
                            return "用户";
                    }
                },
                {field: 'title', title: '用户名', width:300, fixed: 'left',templet: function(d){
                    console.log(d)
                        return d.user.name
                    }
                },
                {field: 'act', title: '举报原因', width:220, fixed: 'left',templet: function(d){
                        return d.report.reason;
                    }
                },
                {field: 'act', title: '处理', width:60, fixed: 'left',templet: function(d){
                        return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-ban-user" data-report="'+d.report.id+'">' +
                            '封禁</a></div>';
                    }
                },
                {field: 'act', title: '拒绝', width:60, fixed: 'left',templet: function(d){
                        return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-refuse-report" data-report="'+d.report.id+'">' +
                            '拒绝</a></div>';
                    }
                },
                {field: 'act', title: '忽略', width:60, fixed: 'left',templet: function(d){
                        return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-ignore-report" data-report="'+d.report.id+'">' +
                            '忽略</a></div>';
                    }
                },{field: 'reportTime', title: '举报时间', width:200, fixed: 'left',templet: function(d){
                        return '<a>'+d.report.createdDate+'</a>';
                    }
                }

            ]] //设置表头
            //,…… //更多参数参考右侧目录：基本参数选项
        });
        table.render({
            elem: '#dealt-list' //指定原始表格元素选择器（推荐id选择器）
            // ,height: 315 //容器高度
            ,url: '/getReportedUserList' //数据接口
            ,where:{"status":3}
            ,request: {
                pageName: 'offset' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            }
            ,page:true
            ,cols:  [[ //表头
                {field: 'report.entityType', title: '举报记录', width:100, sort: true, fixed: 'left',
                    templet: function(d){
                        return d.report.id;
                    }
                },
                {field: 'status', title: '类型', width:80, fixed: 'left',templet: function(d){
                        if(d.report.entityType=="1")
                            return "问题" ;
                        else if(d.report.entityType=="2")
                            return "回答";
                        else if(d.report.entityType=="3")
                            return "用户";
                    }
                },
                {field: 'title', title: '用户名', width:300, fixed: 'left',templet: function(d){
                        console.log(d)
                        return '<a href="/user/'+d.user.id+'">'+d.user.name+'</a>'
                    }
                },
                {field: 'act', title: '举报原因', width:220, fixed: 'left',templet: function(d){
                        return d.report.reason;
                    }
                }
                ,{field: 'reportTime', title: '举报时间', width:200, fixed: 'left',templet: function(d){
                        return '<a>'+d.report.createdDate+'</a>';
                    }
                }
                , {field: 'reportTime', title: '处理时间', width:200, fixed: 'left',templet: function(d){
                        return '<a>'+d.report.dealDate+'</a>';
                    }
                }
                , {field: 'reportTime', title: '处理人id', width:100, fixed: 'left',templet: function(d){
                        return '<a>'+d.report.dealerId+'</a>';
                    }
                }
            ]] //设置表头
            //,…… //更多参数参考右侧目录：基本参数选项
        });
        table.render({
            elem: '#refuse-list' //指定原始表格元素选择器（推荐id选择器）
            // ,height: 315 //容器高度
            ,url: '/getReportedUserList' //数据接口
            ,where:{"status":1}
            ,request: {
                pageName: 'offset' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            }
            ,page:true
            ,cols:  [[ //表头
                {field: 'report.entityType', title: '举报记录', width:100, sort: true, fixed: 'left',
                    templet: function(d){
                        return d.report.id;
                    }
                },
                {field: 'status', title: '类型', width:80, fixed: 'left',templet: function(d){
                        if(d.report.entityType=="1")
                            return "问题" ;
                        else if(d.report.entityType=="2")
                            return "回答";
                        else if(d.report.entityType=="3")
                            return "用户";
                    }
                },
                {field: 'title', title: '用户名', width:300, fixed: 'left',templet: function(d){
                        console.log(d)
                        return d.user.name
                    }
                },
                {field: 'act', title: '举报原因', width:220, fixed: 'left',templet: function(d){
                        return d.report.reason;
                    }
                },
                {field: 'reportTime', title: '举报时间', width:200, fixed: 'left',templet: function(d){
                        return '<a>'+d.report.createdDate+'</a>';
                    }
                }

            ]] //设置表头
            //,…… //更多参数参考右侧目录：基本参数选项
        });
        table.render({
            elem: '#ignore-list' //指定原始表格元素选择器（推荐id选择器）
            // ,height: 315 //容器高度
            ,url: '/getReportedUserList' //数据接口
            ,where:{"status":2}
            ,request: {
                pageName: 'offset' //页码的参数名称，默认：page
                ,limitName: 'limit' //每页数据量的参数名，默认：limit
            }
            ,page:true
            ,cols:  [[ //表头
                {field: 'report.entityType', title: '举报记录', width:100, sort: true, fixed: 'left',
                    templet: function(d){
                        return d.report.id;
                    }
                },
                {field: 'status', title: '类型', width:80, fixed: 'left',templet: function(d){
                        if(d.report.entityType=="1")
                            return "问题" ;
                        else if(d.report.entityType=="2")
                            return "回答";
                        else if(d.report.entityType=="3")
                            return "用户";
                    }
                },
                {field: 'title', title: '用户名', width:300, fixed: 'left',templet: function(d){
                        console.log(d)
                        return d.user.name
                    }
                },
                {field: 'act', title: '举报原因', width:220, fixed: 'left',templet: function(d){
                        return d.report.reason;
                    }
                },
                {field: 'act', title: '处理', width:60, fixed: 'left',templet: function(d){
                        return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-del-ques" data-report="'+d.report.id+'">' +
                            '删除</a></div>';
                    }
                },
                {field: 'act', title: '拒绝', width:60, fixed: 'left',templet: function(d){
                        return '<div>' + '<a href="javacript:;" class="layui-btn-sm js-refuse-report" data-report="'+d.report.id+'">' +
                            '拒绝</a></div>';
                    }
                },{field: 'reportTime', title: '举报时间', width:200, fixed: 'left',templet: function(d){
                        return '<a>'+d.report.createdDate+'</a>';
                    }
                }

            ]] //设置表头
            //,…… //更多参数参考右侧目录：基本参数选项
        });
    }
    function initEvent(){
        $(document).on('click','.js-ban-user',function(){
            var here=$(this);
            var report_id=here.attr("data-report");
            console.log(report_id);
            layer.confirm("封禁该用户？",function (index) {
                common.ajax("/handleDealtReport",{
                    "reportId":report_id,
                    "resultCode":3
                },function (result) {
                    console.log(result);
                    if(result.code=='0'){
                        layer.msg("封禁该用户成功");
                        // window.location=location.reload()
                    }else if(result.code=="1"){
                        layer.msg("封禁该用户失败");
                    }else {
                        layer.msg("请重新登陆");
                    }
                })
                layer.close(index)
            })

        })
        $(document).on('click','.js-refuse-report',function(){
            var here=$(this);
            var report_id=here.attr("data-report");
            console.log(report_id);
            layer.confirm("不通过该举报申请？",function (index) {

                common.ajax("/handleDealtReport",{
                    "reportId":report_id,
                    "resultCode":1
                },function (result) {
                    console.log(result);
                    if(result.code=='0'){
                        layer.msg("处理申请成功");
                        // window.location=location.reload()
                    }else if(result.code=="1"){
                        layer.msg("处理该申请成功");
                    }else {
                        layer.msg("请重新登陆");
                    }
                })
                layer.close(index)
            })

        })
        $(document).on('click','.js-ignore-report',function(){
            var here=$(this);
            var report_id=here.attr("data-report");
            console.log(report_id);
            layer.confirm("忽略该问题？",function (index) {

                common.ajax("/handleDealtReport",{
                    "reportId":report_id,
                    "resultCode":2
                },function (result) {
                    console.log(result);
                    if(result.code=='0'){
                        layer.msg("忽略该举报成功");
                        // window.location=location.reload()
                    }else if(result.code=="1"){
                        layer.msg("忽略该举报失败");
                    }else {
                        layer.msg("请重新登陆");
                    }
                })
                layer.close(index)
            })

        })
    }
    //输出接口
    exports('reportUser', {});
});
