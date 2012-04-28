$(function(){
    $('body').on('click.data-remove', ".data-remove",function(e){
        bootbox.confirm("Are you sure you want to delete this ticket?", function(result){
            if( result ) {
                var id = $(e.currentTarget).attr("data-id");
                var form = $("<form></form>");
                form.attr({
                    action : AtsutaKatze.routes.removeTicket + id,
                    method : "POST"
                });
                form.trigger('submit');
                $("body").append(form);
                form.submit();
            }
        });
    })
});