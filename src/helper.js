var OPENWEATHERMAP_API_KEY = $injector.api_key;

function checkWeather(units, q){
    return $http.get("https://api.openweathermap.org/data/2.5/forecast?q=${q}&units=${units}&appid=${APPID}", {
        timeout: 10000,
        query:{
            APPID: OPENWEATHERMAP_API_KEY,
            units: units,
            q: q
        }
    });
}

function getDurationUnit(unit) {
    switch(unit) {
        case 'day':
            return "день"
        case 'week':
            return "неделя"
        case 'month':
            return "месяц"
        default:
            return "день"
    }
}

function extractEntities($parseTree, $session, $client) {
    if ($parseTree._geo) {
        $session.geo = capitalize($parseTree._geo);
    }
    if ($parseTree._peopleNumber) {
        $session.peopleNumber = $parseTree._peopleNumber;
        if ($session.peopleNumber <= 0) {
            $session.peopleNumber = 'не определился(-лась)'
        }
    }
    if ($parseTree._childrenNumber) {
        $session.childrenNumber = $parseTree._childrenNumber;
        if ($session.childrenNumber < 0) {
            $session.childrenNumber = 'не определился(-лась)'
        }
    }
    if ($parseTree._moneyAmount) {
        $session.moneyAmount = $parseTree._moneyAmount;
    }
    if ($parseTree._startDate) {
        $session.startDate = moment($parseTree._startDate.value).format("DD.MM.YYYY");
    }
    if ($parseTree._duration) {
        $session.duration = $parseTree._duration.value + ' ' + $nlp.conform(getDurationUnit($parseTree._duration.unit), $parseTree._duration.value);
    }
    if ($parseTree._hotelStars) {
        $session.hotelStars = $parseTree._hotelStars;
        if ($session.hotelStars > 5) {
            $session.hotelStars = 5;
        } else if ($session.hotelStars <= 0) {
            $session.hotelStars = 0;
        }
    }
    if ($parseTree._name) {
        $client.name = capitalize($parseTree._name);
    }
    if ($parseTree._phoneNumber) {
        $client.phoneNumber = $parseTree._phoneNumber;
    }
}
