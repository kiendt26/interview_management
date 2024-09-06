$('document').ready(function(){

    var $file = $('#file-input'),
        $label = $file.next('label'),
        $labelText = $label.find('span'),
        $labelRemove = $('i.remove'),
        labelDefault = $labelText.text();

    // on file change
    $file.on('change', function(event){
        var fileName = $file.val().split( '\\' ).pop();
        if( fileName ){
            console.log($file)
            $labelText.text(fileName);
            $labelRemove.show();
        }else{
            $labelText.text(labelDefault);
            $labelRemove.hide();
        }
    });

    // Remove file
    $labelRemove.on('click', function(event){
        $file.val("");
        $labelText.text(labelDefault);
        $labelRemove.hide();
        console.log($file)
    });
})