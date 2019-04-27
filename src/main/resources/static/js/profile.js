/**
 @Name：wenda首页
 @Author：庄巍
 */
layui.define(['element', 'form','laypage','jquery','upload','common'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,upload = layui.upload
        ,common=layui.common;
    //statr 分页
    $(function () {
        //执行实例
        var uploadInst = upload.render({
            elem: '#upload_head' //绑定元素
            ,url: '/uploadHeadPic/' //上传接口
            ,done: function(res){
                console.log(res);//上传完毕回调
                if (res.code=='0'){
                    layer.msg("上传头像成功！");
                    window.location.reload()
                }else if(res.code=='1'){
                    layer.msg("上传头像失败！");
                }else if(res.code=="-1"){
                    layer.msg("请登录后修改头像");
                }
            },error: function(){
                //请求异常回调
                layer.msg("上传失败！");
            }
        });
        initList();
        initFollowEvent();
        /*暂时不需要举报用户功能*/
        initOptions();
    });
    function initFollowEvent() {
        $(document).on('click','.js-follow',function(){
            var to_user=$("input[name='current_page_user']").val();
            if(to_user==""||to_user==undefined)
            {
                layer.msg("关注异常，请重试！");
                return
            }
            common.ajax("/followUser",{
                "userId":to_user
            },function (result) {
                console.log(result);
                if(result.code=='0'){
                    layer.msg("关注成功");
                    var cancel_btn='<a href="javascript:;" class="layui-btn layui-btn-primary js-cancel-follow fly-imActive" data-type="addFriend">取消关注</a>';
                    $(".js-replacebtn").html("");
                    $(".js-replacebtn").html(cancel_btn);
                }else if(result.code=="1"){
                    layer.msg("关注失败"+result.msg);
                }else {
                    layer.msg("请重新登陆");
                }
            })
        })
        $(document).on('click','.js-cancel-follow',function(){
            var to_user=$("input[name='current_page_user']").val();
            if(to_user==""||to_user==undefined)
            {
                layer.msg("取消关注异常，请重试！");
                return
            }
            common.ajax("/unFollowUser",{
                "userId":to_user
            },function (result) {
                console.log(result);
                if(result.code=='0'){
                    layer.msg("取关成功");
                    var followbtn='<a href="javascript:;" class="layui-btn layui-btn-primary js-follow fly-imActive" data-type="addFriend">关注</a>';
                    $(".js-replacebtn").html("");
                    $(".js-replacebtn").html(followbtn);
                }else if(result.code=="1"){
                    layer.msg("取关失败"+result.msg);
                }else {
                    layer.msg("请重新登陆");
                }
            })
        });

    }
    function initList(){
        var that=this;
        that.page_user=$("input[name='current_page_user']").val();
        that.pageSize=10;
        that.q_page=1;
        that.a_page=1;
        common.ajax("/index/requestLatestQuestions",{
            "userId":that.page_user,
            "limit":that.pageSize,
            "offset":that.q_page++,
        },function (result) {
            console.log(result)
            if(result.code=="0"){
                var total=result.totals;
                init_question_list(total,that.page-1,that.pageSize)

            }else {
                layui.msg("请求失败")
            }
        });
        common.ajax("/index/requestLatestAnswers",{
            "userId":that.page_user,
            "limit":that.pageSize,
            "offset":that.a_page++,
        },function (result) {
            console.log(result)
            if(result.code=="0"){
                var total=result.totals;
                init_answer_list(total,that.a_page-1,that.pageSize)

            }else {
                layui.msg("请求失败")
            }
        });
    }
    function init_question_list(totals,cur,pageSize){
        var that=this;
        laypage.render({
            elem: 'page-pro-ques',
            count: totals,
            curr: cur,
            limit: pageSize,
            jump: function(obj, first){
                // console.log(obj.curr+""+obj.limit);
                common.ajax("/index/requestLatestQuestions",{
                    "limit":pageSize,
                    "offset":obj.curr,
                    "userId":that.page_user,
                },function (res) {
                    if(res.code=="0"){
                        $("#new-question-list").html("");
                        if(res.totals==0){
                            var html='<div class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;">\n' +
                                '          <i style="font-size:14px;">没有发表任何求解</i>\n' +
                                '     </div>';
                            $("#new-question-list").html(html);
                            return
                        }
                        $.each(res.data,function (index, item) {
                            // console.log(item);
                            var html='<li>\n' +
                                '         <a href="/question/'+item.questionMap.question.id+'" class="jie-title"> '+item.questionMap.question.title+'</a>\n' +
                                '         <i class="pull-right">'+item.questionMap.question.createdDate+'</i>\n' +
                                '     </li>';
                            $("#new-question-list").append(html);
                        });
                    }else {
                        layer.msg("ajax请求失败");
                    }
                });
            }
        });
    }
    function init_answer_list(totals,cur,pageSize){
        var that=this;
        laypage.render({
            elem: 'page-pro-ans',
            count: totals,
            curr: cur,
            limit: pageSize,
            jump: function(obj, first){
                // console.log(obj.curr+""+obj.limit);
                common.ajax("/index/requestLatestAnswers",{
                    "limit":pageSize,
                    "offset":obj.curr,
                    "userId":that.page_user,
                },function (res) {
                    if(res.code=="0"){
                        console.log(res);
                        $("#new-answer-list").html("");
                        if(res.totals==0){
                            var html='<div class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;">\n' +
                                '          <i style="font-size:14px;">没有发表任何回答</i>\n' +
                                '     </div>';
                            $("#new-answer-list").html(html);
                            return
                        }
                        $.each(res.data,function (index, item) {
                            // console.log(item);
                            var html='<li>\n' +
                                '          <p class="jie-title" style="max-width:750px">\n' +
                                '          在<a  href="/question/'+item.questionMap.question.id+'" target="_blank">'+item.questionMap.question.title+'</a>中回答：\n' +
                                '          </p>\n' +
                                '          <div class="home-dacontent">\n' +
                                item.commentMap.comment.content+
                                '          </div>\n' +

                                '        </li>';
                            $("#new-answer-list").append(html);
                        });
                    }else {
                        layer.msg("ajax请求失败");
                    }
                })
            }
        });
    }
    function initOptions(){
        $(".js-optionMenus").on("click",function () {
            if($(".js-optionsBtn").css('display')=='none'){
                $(".js-optionsBtn").show();
            }else {
                $(".js-optionsBtn").hide();
            }
        })
        /*弹窗事件*/
        $(".js-report-user").on("click",function () {
            layer.open({
                type: 1,
                title: '举报用户',
                // maxmin: true,
                shadeClose: true, //点击遮罩关闭层
                area : ['500px' , '320px'],
                content: $(".reportUserWindow")
            });
        });
        //举报界面按钮
        $(".js-submitReportUser").on("click",function () {
            var user_id=$("input[name='reportUserId']").val();
            var reason=$("textarea[name='report_reason']").val();
            if(user_id==""||user_id==undefined){
                layer.msg("加载错误，请刷新网页后重试！");
                return
            }
            if(reason==""||reason==undefined){
                layer.msg("请输入举报理由");
                return
            }
            common.ajax("/reportUser",{
                "userId":user_id,
                "reason":reason
            },function (result) {
                console.log(result)
                if(result.code=='0'){
                    layer.msg("举报成功！",{time:3000})
                    layer.closeAll('page')
                    return
                }else if(result.code=='1'){
                    layer.msg("举报失败"+result.msg,{time:3000});
                    layer.closeAll('page')

                }else if(result.code=='999'){
                    layer.msg("未登录或登录信息失效，请重新登陆",{time:3000})
                    //关闭所有页面层
                    layer.closeAll('page')
                }
            })
        })
    }
    //输出profile接口
    exports('profile', {});
});
