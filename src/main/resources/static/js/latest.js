/**

 @Name：wenda最新界面
 @Author：庄巍


 */
layui.define(['element', 'form','laypage','jquery','laytpl','common'],function(exports){
    var element = layui.element
        ,form = layui.form
        ,laypage = layui.laypage
        ,$ = layui.jquery
        ,laytpl = layui.laytpl
        ,common=layui.common;
    //statr 分页

    initClickMore();
    function initClickMore() {
        var that=this;
        that.ans_page=1;
        that.ques_page=1;
        that.pageSize=5;
        that.quesListHasNext=true;
        that.ansListHasNext=true;
        $(".js-more-ans").on("click",function (oEvent) {

            var oEl = $(oEvent.currentTarget);
            if(that.loading==true)
                return ;
            that.loading=true;
            fRenderMoreAns();
            that.loading=false;
            !that.ansListHasNext && oEl.hide();
        })
        $(".js-more-ques").on("click",function (oEvent) {

            var oEl = $(oEvent.currentTarget);
            if(that.loading==true)
                return ;
            that.loading=true;
            fRenderMoreQues();
            that.loading=false;
            !that.quesListHasNext && oEl.hide();
        })
    }

    function fRenderMoreAns(){
        var that=this;
        if(that.ansListHasNext==false){
            return ;
        }
        common.ajax("/index/requestLatestAnswers",{
            "userId":0,
            "limit":that.pageSize,
            "offset":that.ans_page+1,
            },function (result) {
            console.log(result)
            if(result.code=="0"){
                that.ans_page++;
                that.ansListHasNext=result.has_next;
                $.each(result.data,function (Index, Item) {
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
                        '                                        <span class="author_info"><a href="/user/">'+Item.commentMap.user.name+'</a> </span>\n' +
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
                        '                                </div>\n' +
                        '                            </div>';
                    $("#new-answer-list").append(html);
                });

            }else {
                layui.msg("请求失败")
            }


        });

    }
    function fRenderMoreQues(){
        var that=this;
        if(that.quesListHasNext==false){
            return ;
        }
        common.ajax("/index/requestLatestQuestions",{
            "userId":0,
            "limit":that.pageSize,
            "offset":that.ques_page+1,
        },function (result) {
            console.log(result)
            if(result.code=="0"){
                that.ques_page++;
                that.quesListHasNext=result.has_next;
                $.each(result.data,function (Index, Item) {
                    var html='<div class="item">\n' +
                        '                                <div class="item-box  layer-photos-demo1 layer-photos-demo">\n' +
                        '                                    <div class="question_info" >\n' +
                        '                                        <h3 >\n' +
                        '                                            <a  class="question_title" href="/question/'+Item.questionMap.question.id+'">\n' +
                        '                                                '+Item.questionMap.question.title+'\n' +
                        '                                            </a>\n' +
                        '                                        </h3>\n' +
                        '                                    </div>\n' +
                        '                                    <div class="ans_author_info" >\n' +
                        '                                        <div class="author_head pull-right">\n' +
                        '                                            <a href="/user/'+Item.questionMap.user.id+'">\n' +
                        '                                                <img src="'+Item.questionMap.user.headUrl+'" class="author-head-img">\n' +
                        '                                            </a>\n' +
                        '                                        </div>\n' +
                        '                                        <span class="author_info"><a href="/user/">'+Item.questionMap.user.name+'</a> </span>\n' +
                        '                                        <h5 class="answer_date p">提问于：<span>'+Item.questionMap.question.createdDate+'</span></h5>\n' +
                        '                                    </div>\n' +
                        '\n' +
                        '                                    <!--<p class="answer-content">$!{vo.commentMap.comment.content}</p>-->\n' +
                        '                                </div>\n' +

                        '                            </div>'
                    $("#new-question-list").append(html);
                });

            }else {
                layui.msg("请求失败")
            }


        });

    }
    //start 评论的特效
    (function ($) {
        $.extend({
            tipsBox: function (options) {
                options = $.extend({
                    obj: null,  //jq对象，要在那个html标签上显示
                    str: "+1",  //字符串，要显示的内容;也可以传一段html，如: "<b style='font-family:Microsoft YaHei;'>+1</b>"
                    startSize: "12px",  //动画开始的文字大小
                    endSize: "30px",    //动画结束的文字大小
                    interval: 600,  //动画时间间隔
                    color: "red",    //文字颜色
                    callback: function () { }    //回调函数
                }, options);

                $("body").append("<span class='num'>" + options.str + "</span>");

                var box = $(".num");
                var left = options.obj.offset().left + options.obj.width() / 2;
                var top = options.obj.offset().top - 10;
                box.css({
                    "position": "absolute",
                    "left": left + "px",
                    "top": top + "px",
                    "z-index": 9999,
                    "font-size": options.startSize,
                    "line-height": options.endSize,
                    "color": options.color
                });
                box.animate({
                    "font-size": options.endSize,
                    "opacity": "0",
                    "top": top - parseInt(options.endSize) + "px"
                }, options.interval, function () {
                    box.remove();
                    options.callback();
                });
            }
        });
    })($);

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
                    $.tipsBox({
                        obj: here,
                        str: "+1",
                        callback: function () {
                        }
                    });
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

    // $(function () {
    //
    // });

    //end 评论的特效


    // start点赞图标变身
    // $('#LAY-msg-box').on('click', '.info-img', function(){
    //     $(this).addClass('layblog-this');
    // })


    // end点赞图标变身

    //end 提交
    $('#item-btn').on('click', function(){
        var elemCont = $('#LAY-msg-content'),content = elemCont.val();
        if(content.replace(/\s/g, '') == ""){
            layer.msg('请先输入留言');
            return elemCont.focus();
        }

        var view = $('#LAY-msg-tpl').html(),
            data = {
                username: '闲心'
                ,avatar: '../res/static/images/info-img.png'
                ,praise: 0
                ,content: content
            };

        //模板渲染
        laytpl(view).render(data, function(html){
            $('#LAY-msg-box').prepend(html);
            elemCont.val('');
            layer.msg('留言成功', {
                icon: 1
            })
        });

    })

    //输出latest接口
    exports('latest', {});
});  
