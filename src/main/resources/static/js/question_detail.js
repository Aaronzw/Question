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
        var question_id=$("input[name='questionId']").val();
        var that=this;
        that.page=0;
        that.pageSize=5;
        common.ajax("/question/requestMore",{
            "limit":that.pageSize,
            "offset":that.page,
            "questionId":question_id
        },function (res) {
            // console.log(res);
            if(res.code=="0"){
                that.totals=res.totals;
                initPages(that.totals,that.page,that.pageSize)
            }
        })
        initFollow();
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
                    "questionId":question_id
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
            "questionId":question_id,
            "content":answer_content,
            "userId":userId,
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

    //输出index接口
    exports('question_detail', {});
});
