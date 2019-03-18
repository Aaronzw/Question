/**

 @Name：wenda首页
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
    })
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
                                if(item.liked>0)
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

    $(document).on('click','.like',function(){
        var here=$(this);
        var comment_id=here.parent('.comment').attr("data-comment-id");

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

    });

    //输出index接口
    exports('question_detail', {});
});
