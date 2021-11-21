/*
*
* Author: Lina Walaa
* Date: August 2021
* Course: Java Web Developer Nanodegree Program by Udacity
*
* This is the home.js file that has the used js functions on the home.html page
*
* jQuery functions
* each line sets the value of an html id to blank if the corresponding variable is null
*
*/

// For opening the note modal
function showNoteModal(noteId, noteTitle, noteDescription) {
    $('#note-id').val(noteId ? noteId : '');
    $('#note-title').val(noteTitle ? noteTitle : '');
    $('#note-description').val(noteDescription ? noteDescription : '');
    $('#noteModal').modal('show');
}

//global variable because will be accessed in the display password function and many others
let index = 0;

//when I click on the row number button = view credential button
//but for the #credentialTable tr the below line worked on the first row only
$('#credentialTable tr #credential-password-view').on("click", function (){
    let $tr = $(this).closest("tr");
    index = $("tr",$tr.closest("table")).index($tr);
    //click on the button:nth-child(index)  = on row index
    // but jquery/js starts at 0 unlike nth child that starts at 1
    //so we add index-1
    //#credential-password-view-button is hidden button and when clicked
    //the displayPassword function is
    $("#credentialTable tr #credential-password-view-button").eq(index-1).click();
});

function displayPassword(decryptedPassword, encryptedPassword, credentialid){
    let rowIndex = index;

    //if credential password is displayed
    if ($('#credentialTable tr #credential-password-input').eq(rowIndex-1).attr("type")=="text"){

        $('#credentialTable tr #credential-password-input').eq(rowIndex-1).attr("type", "password");
        $('#credentialTable tr #credential-password-input').eq(rowIndex-1).val(encryptedPassword);

        $('#credentialTable tr #credential-password-view').eq(rowIndex-1).val(credentialid).removeClass("bi bi-eye");
        $('#credentialTable tr #credential-password-view').eq(rowIndex-1).val(credentialid).addClass("bi bi-eye-slash");

        //if credential password is encrypted
    }else if($('#credentialTable tr #credential-password-input').eq(rowIndex-1).attr("type")=="password"){

        $('#credentialTable tr #credential-password-input').eq(rowIndex-1).attr("type", "text");
        $('#credentialTable tr #credential-password-input').eq(rowIndex-1).val(decryptedPassword);

        $('#credentialTable tr #credential-password-view').eq(rowIndex-1).val(credentialid).removeClass("bi bi-eye-slash");
        $('#credentialTable tr #credential-password-view').eq(rowIndex-1).val(credentialid).addClass("bi bi-eye");
    }

}

//when clicking any of the redundant delete note buttons on the note tab
//that will click the respective delete button inside the form
// $('#noteTable tr #note-delete').on("click", function(){
//     let $tr = $(this).closest("tr");
//     index = $("tr",$tr.closest("table")).index($tr);
//
//     $("#noteTable tr #note-delete-button").eq(index-1).click();
// });

//when clicking any of the redundant delete credential buttons on the credential tab
//that will click the respective delete button inside the form
// $('#credentialTable tr #credential-delete').on("click", function(){
//     let $tr = $(this).closest("tr");
//     index = $("tr",$tr.closest("table")).index($tr);
//
//     $("#credentialTable tr #credential-delete-button").eq(index-1).click();
// });

//when interacting with the file selection
$('#validatedCustomFile').change(function (){

    //if there is no file selected set label text to default value,
    // hide error message and enable upload button
    if($('#validatedCustomFile').val()==''){
        $('#fileLabel').text("Choose file");
        $('#fileError').addClass("d-none");
        $('#uploadFile').attr('disabled', false);

        //if there is a file selected
    }else{

        //if the selected file is greater than 200KB
        //set file name, display error message, and disable the upload button
        if (this.files[0].size>1024*200){
            $('#fileLabel').text(this.files[0].name);
            $('#fileError').removeClass("d-none");
            $('#uploadFile').attr('disabled', true);

            //if a valid file size is selected
            //set file name, hide error message and enable the upload button
        }else{
            $('#fileLabel').text(this.files[0].name);
            $('#fileError').addClass("d-none");
            $('#uploadFile').attr('disabled', false);

        }
    }
});

//To keep the same tab open after any action
// (after redirection from result page even after logouts)
$(function (){
    $('.nav-tabs a[data-toggle="tab"]').on('click', function (e){
        localStorage.setItem('lastTab',jQuery(this).attr('href'));
    });

    //will return a value if it exists
    var lastTab = localStorage.getItem('lastTab');
    if (lastTab){
        $('.nav-tabs a[href="'+ lastTab +'"]').click();
    }
});