/**

 @Name：问题详情
 @Author：庄巍


 */
layui.define(['element', 'form','laypage','jquery','laytpl','common'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,laytpl = layui.laytpl
        ,common=layui.common
        ,layer=layui.layer;
    //statr 分页

    $(function () {
        var that=this;
        that.question_id=$("input[name='questionId']").val();
        that.page=0;
        that.pageSize=5;
        common.ajax("/question/requestMore",{
            "limit":that.pageSize,
            "offset":that.page,
            "entityId":that.question_id,
            "entityType":1
        },function (res) {
            if(res.code=="0"){
                that.totals=res.totals;
                initPages(that.totals,that.page,that.pageSize)
            }
        })
        initFollow();
        initOptions();
    })
    function initFollow() {
        $(document).on("click",".js-follow-question",function () {
            var questionId=$("input[name='questionId']").val();
            common.ajax("/followQuestion",{
                "questionId":questionId
            },function (result) {
                console.log(result);
                if(result.code=="0"){
                    layer.msg("收藏问题成功")
                    $(".js-replace-btn").html("");
                    var cancelBtn='<button class="layui-btn layui-btn-sm js-cancel-follow">\n' +
                        '                            取消收藏\n' +
                        '                        </button>';
                    $(".js-replace-btn").html(cancelBtn);
                    var cnt=$(".js-follow-cnt").html();
                    $(".js-follow-cnt").html("");
                    $(".js-follow-cnt").html(Number(cnt)+1);
                }else if(result.code=="999"){
                    layer.msg("请登录后操作");
                }else {
                    layer.msg("收藏问题失败")
                }
            })
        })
        $(document).on("click",".js-cancel-follow",function () {
            var questionId=$("input[name='questionId']").val();
            common.ajax("/unFollowQuestion",{
                "questionId":questionId
            },function (result) {
                console.log(result);
                if(result.code=="0"){
                    layer.msg("取消收藏问题成功")
                    $(".js-replace-btn").html("");
                    var cancelBtn='<button class="layui-btn layui-btn-sm js-follow-question">\n' +
                        '                            收藏问题\n' +
                        '                        </button>';
                    $(".js-replace-btn").html(cancelBtn);
                    var cnt=$(".js-follow-cnt").html();
                    $(".js-follow-cnt").html("");
                    $(".js-follow-cnt").html(Number(cnt)-1);
                }else if(result.code=="999"){
                    layer.msg("请登录后操作");

                }else {
                    layer.msg("取消操作失败")
                }
            })
        })
    }
    function initPages(totals,cur,pageSize){
        var that=this;
        laypage.render({
            elem: 'detail_page',
            count: totals,
            curr: cur,
            limit: pageSize,
            jump: function(obj, first){
                console.log(obj.curr+""+obj.limit);
                var question_id=$("input[name='questionId']").val();
                common.ajax("/question/requestMore",{
                    "limit":pageSize,
                    "offset":obj.curr,
                    "entityId":question_id,
                    "entityType":1,
                },function (res) {
                    if(res.code=="0"){
                        $(".answers-list").html("");
                        $.each(res.data,function (index, item) {
                            console.log(item);
                            var html='<div class="item" >\n' +
                                '                    <div class="item-box  layer-photos-demo1 layer-photos-demo">\n' +
                                '\n' +
                                '                        <div class="ans_author_info" >\n' +
                                '                            <div class="author_head pull-right">\n' +
                                '                                <a href="/user/'+item.user.id+'">\n' +
                                '                                    <img src="'+item.user.headUrl+'" class="author-head-img">\n' +
                                '                                </a>\n' +
                                '                            </div>\n' +
                                '                            <span class="author_info">\n' +
                                '                            <a href="/user/'+item.user.id+'">'+item.user.name+'</a>\n' +
                                '                        </span>\n' +
                                '                            <h5 class="answer_date p">回答于：<span>'+item.comment.createdDate+'</span></h5>\n' +
                                '                        </div>\n' +
                                '\n' +
                                '                        <p class="answer-content">'+item.comment.content+'</p>\n' +
                                '                    </div>\n' +
                                '                    <div class="comment count" data-comment-id="'+item.comment.id+'">\n' ;
                                if(item.liked>0)
                                    html=html+'                        <a href="javascript:;" class="like layblog-this">\n';
                                else
                                    html=html+'                        <a href="javascript:;" class="like ">\n';

                                html=html+'                            <i class="layui-icon layui-icon-praise"></i>\n' +
                                '                            <span class="js-likecount">'+item.likeCount+'</span>\n' +
                                '                        </a>\n' ;
                                if(item.liked<0)
                                    html=html+'                        <a href="javascript:;" class="dislike layblog-this">\n';
                                else
                                    html=html+'                        <a href="javascript:;" class="dislike ">\n';
                                html=html+ '                            <i class="layui-icon layui-icon-tread"></i>\n' +
                                '                        </a>\n' +
                                '<a href="/answer/Detail?questionId='+item.comment.entityId+'&&answerId='+item.comment.id+'" class="comment-link">\n' +
                                '                            <i class="layui-icon layui-icon-reply-fill"></i>\n' +
                                '<span class="js-comment-count">'+item.commentCount+'</span>'+
                                '                        </a>'+
                                '                    </div>\n' +
                                '                </div>';
                            $(".answers-list").append(html);
                            $(window).scrollTop(0);
                        });

                    }else {
                        layer.msg("ajax请求失败");
                    }
                })

            }
        });
    }
    /*点赞点踩*/
    $(document).on('click','.like',function(){
        var here=$(this);
        var comment_id=here.parent('.comment').attr("data-comment-id");

        if(!($(this).hasClass("layblog-this"))){
            common.ajax("/comment/like",{commentId:comment_id},function (res) {
                if(res.code=="0"){
                    here.addClass('layblog-this');
                    here.parent('.comment').children(".dislike").removeClass("layblog-this");
                    here.find('.js-likecount').text(Number(here.find('.js-likecount').text())+1);
                    // $.tipsBox({
                    //     obj: here,
                    //     str: "+1",
                    //     callback: function () {
                    //     }
                    // });
                    layer.msg('点赞成功', {
                        icon: 6
                        ,time: 1000
                    })
                }else {
                    layer.msg("点赞失败");
                }
            })
            return
        }
    })
    /*评论点踩*/
    $(document).on('click','.dislike',function(){
        var here=$(this);
        var comment_id=here.parent('.comment').attr("data-comment-id");
        if(!($(this).hasClass("layblog-this"))){
            common.ajax("/comment/dislike",{commentId:comment_id},function (res) {
                if(res.code=="0"){
                    here.addClass('layblog-this');
                    if(here.parent('.comment').children(".like").hasClass("layblog-this")) {
                        here.parent('.comment').children(".like").removeClass("layblog-this");
                        var likeCnt=Number(here.parent('.comment').find('.js-likecount').text());
                        here.parent('.comment').find('.js-likecount').text(likeCnt-1);
                    }
                    return
                }else {
                    layer.msg("点踩失败");
                }
            })

        }

    })

    //start 提交
    $('#sub_ans').on('click', function(){
        var answer_content=$("#ans-content").val();
        var question_id=$("input[name='questionId']").val();
        console.log(answer_content+","+question_id);
        var userId=$("input[name='global-user-id']").val();
        if(userId==""||userId==undefined){
            layer.msg("请登录后发表回答！");
            return
        }
        if(answer_content==""||answer_content==undefined){
            layer.msg("回答不能为空！");
            return
        }
        common.ajax("/comment/add",{
            "entityId":question_id,
            "content":answer_content,
            "entityType":1,
        },function (result) {
            console.log(result);
            if(result.code=="0"){
                layer.msg("发表成功");
                window.location.reload();
            }else {
                result.msg("发表失败");
            }
        })

    });

    function initOptions(){
        $(".js-optionMenus").on("click",function () {
            if($(".js-optionsBtn").css('display')=='none'){
                $(".js-optionsBtn").show();
            }else {
                $(".js-optionsBtn").hide();
            }
        })
    }
    $(".js-delete-question").on("click",function () {
        layer.confirm('确认删除该问题(不可恢复)？', function(index){
            //do something
            var question_id=$("input[name='questionId']").val();
            common.ajax("/deleteQuestion",{
                "questionId":question_id
            },function (result) {
                console.log(result);
                if(result.code=="0"){
                    layer.msg("删除成功！",{time:2000});
                }else {
                    layer.msg("删除失败",{time:2000});

                }
                layer.close(index);
            })
        });
    })

    /*弹窗事件*/
    $(".js-report-question").on("click",function () {
        layer.open({
            type: 1,
            title: '举报问题',
            // maxmin: true,
            shadeClose: true, //点击遮罩关闭层
            area : ['500px' , '320px'],
            content: $(".reportQuestionWindow")
        });
    });
    //举报界面按钮
    $(".js-submitReportQues").on("click",function () {
        var question_id=$("input[name='questionId']").val();
        var reason=$("textarea[name='report_reason']").val();
        if(question_id==""||question_id==undefined){
            layer.msg("加载错误，请刷新网页后重试！");
            return
        }
        if(reason==""||reason==undefined){
            layer.msg("请输入举报理由");
            return
        }
        common.ajax("/reportQuestion",{
            "questionId":question_id,
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
    //输出question_detail接口
    exports('question_detail', {});
});
