
function back(backid,max){
    var backValue = backid;
    var decNext;
    document.getElementById("count").value = backValue+" of "+max;
    var getImage = document.getElementById('Image'+backValue).src;
    var nextValue = document.getElementsByName('nextButton')[0].id;
    decNext = nextValue - 1;
    if(decNext == 0){
        decNext = backValue;
    }
    var decBack = backValue - 1;
    if(decBack<=0)
    {
        decBack = max;
    }
    document.getElementById(backid).id = decBack;
    document.getElementsByName("nextButton")[0].id = decNext;
    document.getElementById("jqImage").src=getImage;
}
function next(nextid,max){
    var nextValue = nextid;
    var incBack;
    document.getElementById("count").value = nextValue+" of "+max;
    var getImage = document.getElementById('Image'+nextValue).src;
    var backValue = document.getElementsByName("backButton")[0].id;
    incBack = parseInt(backValue)+ 1;
    if(incBack > max){
        incBack = parseInt(nextValue) - 1 ;
        if(incBack == 0){
            incBack = 1;
        }
    }
    var incNext = parseInt(nextValue) + 1;
    if(incNext > max){
        incNext = 2;
    }
    document.getElementById(nextid).id = incNext;
    document.getElementsByName("backButton")[0].id = incBack;
    document.getElementById("jqImage").src=getImage;
}
