var clients = [
    "459886321",
    "152993148",
    "776351026"
    ];
    
function findClient(id){
    return clients.indexOf(id) > -1;   
}


function parseDidgit($parseTree, $session, $temp){
    var text = $parseTree.text;
    if (text.match(/\d+/)){
        var number = text.match(/\d+/)[0];
        if (number.length != 9){
            $temp.lengthId = number.length
        }
        else{
            if (findClient(number)){
                $session.clientID = number
            }
        }
    }
}