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

    })


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
