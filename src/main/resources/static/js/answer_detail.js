/**

 @Name：回答详情
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
        initFollow();
        initLike();
        initList();
        initExpand();
    });
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
    function initLike(){
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
    }
    function initList(){
        var that=this;
        that.cur=1;
        that.pageSize=5;
        that.totals=Number( $("input[name='commentCount']").val());

        laypage.render({
            elem: 'answer-detail-page',
            count: that.totals,
            curr: that.cur,
            limit: that.pageSize,
            jump: function(obj, first){
                console.log(obj.curr+""+obj.limit);
                var answerId=$("input[name='answerId']").val();
                common.ajax("/question/requestMore",{
                    "limit":that.pageSize,
                    "offset":obj.curr,
                    "entityId":answerId,
                    "entityType":2,
                },function (res) {
                    if(res.code=="0"){
                        $(".js-comment-list").html("");
                        $.each(res.data,function (index, item) {
                            console.log(item);
                            var html='<div class="cont">\n' +
                                '                        <div class="img pull-left">\n' +
                                '                            <img  class="author-head-img" src="'+item.user.headUrl+'" alt="">\n' +
                                '                        </div>\n' +
                                '                        <div class="text">\n' +
                                '                            <p class="tit"><span class="name">'+item.user.name+'</span><span class="data pull-right">'+item.comment.createdDate+'</span></p>\n' +
                                '                            <p class="ct">'+item.comment.content+'</p>\n' +
                                '                        </div>\n' +
                                '                    </div>'
                            $(".js-comment-list").append(html);
                            $(window).scrollTop(0);
                        });

                    }else {
                        layer.msg("ajax请求失败");
                    }
                })
            }
        });
    }

    //start 提交
    $('.js-sub-comment').on('click', function(){
        var content=$("textarea[name='new_content']").val();
        var answer_id=$("input[name='answerId']").val();
        console.log(content+","+answer_id);
        var userId=$("input[name='global-user-id']").val();
        if(userId==""||userId==undefined){
            layer.msg("请登录后发表回答！");
            return
        }
        if(content==""||content==undefined){
            layer.msg("回答不能为空！");
            return
        }
        common.ajax("/comment/add",{
            "entityId":answer_id,
            "content":content,
            "entityType":2,
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
    function initExpand(){
        $(".off").on("click",function () {
            if($(".review-version").show){
                $(".review-version").show();
            }else {
                $(".review-version").hide();
            }
        })
    }
    //输出answer接口
    exports('answer_detail', {});
});
