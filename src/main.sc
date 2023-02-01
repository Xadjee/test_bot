require: requirements.sc
  
init:
  $mail.debug(true);

theme: / 
    state: Start
        q!: $regex</start>
        script:
            $jsapi.startSession()
        if: $client.name
            a: Здравствуйте, {{capitalize($client.name)}}! Я бот туристической компании «Just Tour». Умею подсказывать погоду, а также бронировать туры в места с подходящими условиями 🌴
        else:
            a: Здравствуйте! Я бот туристической компании «Just Tour». Умею подсказывать погоду, а также бронировать туры в места с подходящими условиями 🌴
        a: Хотите сначала узнать метеоусловия или уже готовы забронировать тур?
        buttons:
            "Узнать погоду"
            "Забронировать тур"

    state: Weather
        q!: * {($weather|$conditions) * [@mystem.geo] * [@duckling.date]} *
        script:
            if ($parseTree["mystem.geo"]) { //если в $parseTree была сущность гео, заполняем слот сессии соответствующим значением
                $session.geo = capitalize($parseTree["mystem.geo"][0].value);
            }
            if ($parseTree["duckling.date"]) { //по аналогии с гео заполняем слот даты
                $session.date = moment($parseTree["duckling.date"][0].value.value).format("DD.MM.YYYY, HH:mm");
                var time_diff = moment($session.date, "DD.MM.YYYY, HH:mm").diff(moment($jsapi.currentTime()), 'hours'); //считаем разницу в днях между текущим моментом и указанным пользователем
                if (time_diff < 0) { //метод апи https://openweathermap.org/forecast5 не умеет смотреть в прошлое, укажем маркер того, что дата в прошлом
                    $session.dateInPast = true;
                } else if (time_diff > 120) { //тот же метод бесплатно отдает только прогноз на 5 дней, проверим разницу в часах
                    $session.dateDiff = true;
                }
            }
        if: $session.geo && $session.date
            if: $session.dateInPast
                script:
                    $session.dateInPast = undefined;
                a: Погоду в прошлом я подсказывать не умею, только на ближайшее время. Попробуйте написать в ответ, например, «завтра»
            elseif: $session.dateDiff
                script:
                    $session.dateDiff = undefined;
                a: К сожалению, мои возможности в предсказании погоды ограничиваются 5 днями. Давайте выберем дату в этом промежутке?
            else:
                go!: /CheckWeather
        elseif: $session.geo
            a: С местом понятно - {{$session.geo}}. А за какую дату вы хотите узнать погоду?
        elseif: $session.date
            a: {{$session.date}} погода в городе... Подождите, вы не указали место! Напишете в ответ?
        else:
            a: Подскажите, в каком месте и на какую дату вас интересует погода?

        state: CityAndDate
            q: [$oneWord] {@mystem.geo * @duckling.date} [$oneWord]
            q: [$oneWord] {@mystem.geo * @duckling.date} [$oneWord] || fromState = /CheckWeather
            go!: ..

        state: City
            q: [$oneWord] @mystem.geo [$oneWord]
            q: [$oneWord] @mystem.geo [$oneWord] || fromState = /CheckWeather
            go!: ..

        state: Date
            q: [$oneWord] @duckling.date [$oneWord]
            q: [$oneWord] @duckling.date [$oneWord] || fromState = /CheckWeather
            go!: ..

    state: CheckWeather
        script:
            checkWeather("metric", $session.geo).then(function (res) {
                if (res && res.list) {
                    var closest = Infinity;
                    res.list.forEach(function(d) { //находим ближайший к указанному пользователем момент времени из полученного прогноза
                       var date = moment(d.dt_txt);
                       if (date.isAfter(moment($session.date, "DD.MM.YYYY, HH:mm")) && (date < moment(closest) || date < closest)) {
                          closest = d;
                       }
                    });
                    if (!closest || !closest.main) {
                        closest = res.list[res.list.length - 1];
                    }
                    var temperature = Math.round(closest.main.temp),
                        sign = temperature > 0 ? '+' : '';
                    if (temperature < -10) {
                        $reactions.answer($session.date + " в " + capitalize($nlp.inflect($session.geo, "loct")) + " очень холодно: " + temperature + "°C. Точно хотите такую поездку?");
                    } else if (temperature > 25) {
                        $reactions.answer($session.date + " в " + capitalize($nlp.inflect($session.geo, "loct")) + " настоящая жара, целых " + sign + temperature + "°C, нравятся такие условия?");
                    } else {
                        $reactions.answer($session.date + " в " + capitalize($nlp.inflect($session.geo, "loct")) + " хорошая погода, около " + sign + temperature + "°C. Климат умеренный, поедем?");
                    }
                } else {
                    $reactions.answer("Кажется, погоду сейчас проверить не получится. А вы точно хотите поехать в " + capitalize($nlp.inflect($session.geo, "accs")) + "?");
                }
            }).catch(function (err) {
                $reactions.answer("Кажется, погоду сейчас проверить не получится. А вы точно хотите поехать в " + capitalize($nlp.inflect($session.geo, "accs")) + "?");
            });
        buttons:
            "Да"
            "Нет"

        state: Yes
            q: [$oneWord] ($yes|поехал*|поеду|поедем) [$oneWord]
            go!: /Tour/TourRequest

        state: No
            q: [$oneWord] $no [поехал*|поеду|поедем] [$oneWord]
            script:
                $session.date = undefined;
                $session.geo = undefined;
            a: Не подошло, так и знал! Давайте подберем вам что-нибудь другое, пишите свои варианты в ответ.
            go: /Weather

    state: NoMatch || noContext = true
        event!: noMatch
        random:
            a: Не смог разобрать :( Попробуете сказать иначе?
            a: Простите, я не понял вас. Давайте попробуем еще раз?
            a: Извините, я не понял. Попробуйте сформулировать по-другому

    state: Greeting
        q!: * $hello *
        if: $client.name
            a: Здравствуйте, {{capitalize($client.name)}}! Я бот туристической компании «Just Tour». Умею подсказывать погоду, а также бронировать туры в места с подходящими условиями 🌴
        else:
            a: Здравствуйте! Я бот туристической компании «Just Tour». Умею подсказывать погоду, а также бронировать туры в места с подходящими условиями 🌴
        buttons:
            "Узнать погоду"
            "Забронировать тур"

    state: Goodbye
        intent!: /bye
        script:
            $jsapi.stopSession();
        random:
            a: Всего доброго!
            a: До свидания!
