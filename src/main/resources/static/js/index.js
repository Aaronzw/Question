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

    initClickMore();
    function initClickMore() {
        var that=this;
        that.page=1;
        that.pageSize=5;
        that.listHasNext=true;
        $(".js-index-more").on("click",function (oEvent) {

            var oEl = $(oEvent.currentTarget);
            // var sAttName = 'data-load';
            // // 正在请求数据中，忽略点击事件
            // if (oEl.attr(sAttName) === '1') {
            //     return;
            // }
            // // 增加标记，避免请求过程中的频繁点击
            // oEl.attr(sAttName, '1');
            if(that.loading==true)
                return ;
            that.loading=true;
            fRenderMore();
            !that.listHasNext && oEl.hide();
        })
    }
    function fRenderMore(){
        var that=this;
        if(that.listHasNext==false){
            return ;
        }
        that.loading=false;
        common.ajax("/index/requestLatestAnswers",{
            "userId":0,
            "limit":that.pageSize,
            "offset":that.page+1,
            },function (result) {
            console.log(result)
            that.page++;
            that.listHasNext=result.has_next;
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

    function niceIn(prop){
        prop.find('i').addClass('niceIn');
        setTimeout(function(){
            prop.find('i').removeClass('niceIn');
        },1000);
    }

    $(function () {
        $(".like").on('click',function () {

            if(!($(this).hasClass("layblog-this"))){
                // this.text = '已赞';
                $(this).addClass('layblog-this');
                $(this).parent('.comment').children(".dislike").removeClass("layblog-this");
                $.tipsBox({
                    obj: $(this),
                    str: "+1",
                    callback: function () {
                    }
                });
                niceIn($(this));
                layer.msg('点赞成功', {
                    icon: 6
                    ,time: 1000
                })
            }
        });
        $(".dislike").on('click',function () {

            if(!($(this).hasClass("layblog-this"))){
                $(this).addClass('layblog-this');
                $(this).parent('.comment').children(".like").removeClass("layblog-this");
                // $.tipsBox({
                //     obj: $(this),
                //     str: "+1",
                //     callback: function () {
                //     }
                // });
                // niceIn($(this));
                // layer.msg('点赞成功', {
                //     icon: 6
                //     ,time: 1000
                // })
            }
        });
    });

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

    //输出test接口
    exports('index', {});
});  
