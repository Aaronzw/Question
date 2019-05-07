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
        initRecommenderQuestion();
    });
    function initRecommenderQuestion(){
        var that=this;
        that.curPage=1;
        that.pageSize=10;
        that.totals=$("input[name='rec_totals']").val();
        laypage.render({
            elem: 'rec-page',
            count: that.totals,
            curr: that.curPage,
            limit: that.pageSize,
            jump: function(obj, first){
                common.ajax("/request/recomQuestionsForUser",{
                    "limit":that.pageSize,
                    "offset":obj.curr
                },function (result) {
                    $("#new-question-list").html("");
                    $.each(result.data,function (index, item) {
                        console.log(item)
                        var html='<div class="item">\n' +
                            '                                <div class="item-box  layer-photos-demo1 layer-photos-demo">\n' +
                            '                                    <div class="question_info">\n' +
                            '                                        <h3>\n' +
                            '                                            <a class="question_title" href="/question/'+item.question.id+'">\n' +
                            item.question.title+
                            '                                            </a>\n' +
                            '                                        </h3>\n' +
                            '                                    </div>\n' +
                            '                                    <div class="ans_author_info">\n' +
                            '                                        <div class="author_head pull-right">\n' +
                            '                                            <a href="/user/'+item.user.id+'">\n' +
                            '                                                <img src="'+item.user.headUrl+'" class="author-head-img">\n' +
                            '                                            </a>\n' +
                            '                                        </div>\n' +
                            '                                        <span class="author_info"><a href="/user/'+item.user.id+'">'+item.user.name+'</a> </span>\n' +
                            '                                        <h5 class="answer_date p">提问于：<span>'+item.question.createdDate+'</span></h5>\n' +
                            '                                    </div>\n' +
                            '                                </div>\n' +
                            '\n' +
                            '                            </div>';
                        $("#new-question-list").append(html);
                    })
                })
            }
        });
    }

    //输出index接口
    exports('index', {});
});
