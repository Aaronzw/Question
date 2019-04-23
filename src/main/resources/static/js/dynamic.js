/**

 @Name：导航栏关注动态界面
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


    $(function () {
        dynamic();
        initLike();
    });
    function dynamic(){
        var that=this;
        that.cur=1;
        that.pageSize=5;
        that.totals=Number($("input[name='totals']").val());
        laypage.render({
            elem: 'dynamic-page',
            count: that.totals,
            curr: that.cur,
            limit: that.pageSize,
            jump: function(obj, first){
                console.log(obj.curr+""+obj.limit);

                common.ajax("/dynamic/request",{
                    "limit":that.pageSize,
                    "offset":obj.curr,
                },function (res) {
                    // console.log(res);
                    if(res.code=="0"){
                        $("#followee-dynamic-list").html("");
                        $.each(res.data,function (index, Item) {
                            console.log(Item)
                            if(Item.entityType=="1"){
                                //提问动态
                                var html='<div class="item">\n' +
                                    '         <div class="item-box  layer-photos-demo1 layer-photos-demo">\n' +
                                    '             <div class="question_info">\n' +
                                    '                 <h3>\n' +
                                    '                     <a class="question_title" href="/question/'+Item.questionMap.question.id+'">\n' +
                                    Item.questionMap.question.title+
                                    '                                            </a>\n' +
                                    '                                        </h3>\n' +
                                    '                                    </div>\n' +
                                    '                                    <div class="ans_author_info">\n' +
                                    '                                        <div class="author_head pull-right">\n' +
                                    '                                            <a href="/user/'+Item.questionMap.user.id+'">\n' +
                                    '                                                <img src="'+Item.questionMap.user.headUrl+'" class="author-head-img">\n' +
                                    '                                            </a>\n' +
                                    '                                        </div>\n' +
                                    '                                        <span class="author_info"><a href="/user/'+Item.questionMap.user.id+'">'+Item.questionMap.user.name+'</a> </span>\n' +
                                    '                                        <h5 class="answer_date p">提问于：<span>'+Item.questionMap.question.createdDate+'</span></h5>\n' +
                                    '                                    </div>\n' +
                                    '\n' +
                                    '                                    <!--<p class="answer-content"></p>-->\n' +
                                    '                                </div>\n' +
                                    '                                <!--<div class="comment count" data-comment-id="">-->\n' +
                                    '                                    <!--<a href="/question/{}">评论</a>-->\n' +
                                    '                                    <!--<a href="javascript:;" class="like"><i class="layui-icon layui-icon-praise"></i><span class="js-likecount">1201</span></a>-->\n' +
                                    '                                    <!--<a href="javascript:;" class="dislike"><i class="layui-icon layui-icon-tread"></i></a>-->\n' +
                                    '                                <!--</div>-->\n' +
                                    '                            </div>';
                            }else if(Item.entityType=="2"){
                                //回答动态
                                var html='<div class="item">\n' +
                                    '                                <div class="item-box  layer-photos-demo1 layer-photos-demo">\n' +
                                    '                                    <div class="question_info" >\n' +
                                    '                                        <h3 >\n' +
                                    '                                            <a  class="question_title" href="/question/'+Item.questionMap.question.id+'">\n' +
                                    Item.questionMap.question.title+
                                    '                                            </a>\n' +
                                    '                                        </h3>\n' +
                                    '                                    </div>\n' +
                                    '                                    <div class="ans_author_info" >\n' +
                                    '                                        <div class="author_head pull-right">\n' +
                                    '                                            <a href="/user/'+Item.commentMap.user.id+'">\n' +
                                    '                                                <img src="'+Item.commentMap.user.headUrl+'" class="author-head-img">\n' +
                                    '                                            </a>\n' +
                                    '                                        </div>\n' +
                                    '                                        <span class="author_info"><a href="/user/'+Item.commentMap.user.id+'">'+Item.commentMap.user.name+'</a> </span>\n' +
                                    '                                        <h5 class="answer_date p">回答于：<span>'+Item.commentMap.comment.createdDate+'</span></h5>\n' +
                                    '                                    </div>\n' +
                                    '\n' +
                                    '                                    <p class="answer-content">'+Item.commentMap.comment.content+'</p>\n' +
                                    '                                </div>\n' +
                                    '                                <div class="comment count" data-comment-id="'+Item.commentMap.comment.id+'">\n' ;
                                if(Item.commentMap.likeStatus>0)
                                    html=html+ '<a href="javascript:;" class="like layblog-this">';
                                else
                                    html=html+ '<a href="javascript:;" class="like">';
                                html=html+'<i class="layui-icon layui-icon-praise"></i><span class="js-likecount">'+Item.commentMap.likeCount+'</span></a>\n' ;
                                if(Item.commentMap.likeStatus<0)
                                    html=html+ '<a href="javascript:;" class="dislike layblog-this">';
                                else
                                    html=html+ '<a href="javascript:;" class="dislike">';
                                html=html+'<i class="layui-icon layui-icon-tread"></i></a>\n' +
                                    '<a href="/answer/Detail?questionId='+Item.questionMap.question.id+'&&answerId='+Item.commentMap.comment.id+'" class="comment-link">\n' +
                                    '                                        <i class="layui-icon layui-icon-reply-fill"></i>\n' +
                                    '                                    </a>' +
                                    '                                </div>\n' +
                                    '                            </div>';
                            }
                            $("#followee-dynamic-list").append(html);
                        })
                    }else if(res.code=="-1"){
                        layer.msg("未登录或登录信息失效！");
                    }else {
                        layer.msg("请求数据错误"+res.msg);
                    }
                })
            }
        });
    }
    function initLike(){

    }
    $(document).on('click','.like',function(){
        var here=$(this);
        var comment_id=here.parent('.comment').attr("data-comment-id");

        // if((here.hasClass("layblog-this"))){
        //     here.removeClass('layblog-this');
        //     here.find('.js-likecount').text(Number(here.find('.js-likecount').text())-1);
        //     layer.msg('取消点赞成功', {
        //         icon: 6
        //         ,time: 1000
        //     })
        //     return ;
        // }
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
    //输出dynamic接口
    exports('dynamic', {});
});
