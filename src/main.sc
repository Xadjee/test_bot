require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: Start
        q!: $regex</start>
        a: Добрый день! Я виртуальный помощник банка «Рога и Копыта»!
            Чем могу помочь? || htmlEnabled = true, html = "Добрый день! Я виртуальный помощник банка «Рога и Копыта»!<br>Чем могу помочь?"
        intent: /Хочу заказать карту || toState = "/Шаг2"
        event: noMatch || toState = "/Start"

    state: Шаг2
        a: Позвольте уточнить, для каких целей вам нужна карта:
            оплата в магазинах; 
            
            снятие наличных; 
            зачисление социальных выплат или пенсии;
            зачисление заработной платы;
            другое. || htmlEnabled = true, html = "Позвольте уточнить, для каких целей вам нужна карта:<br>оплата в магазинах;&nbsp;<br><ul><li>снятие наличных;&nbsp;</li><li>зачисление социальных выплат или пенсии;</li><li>зачисление заработной платы;</li><li>другое.</li></ul>"
        intent: /оплата в магазинах || toState = "./"
        intent: /снятие наличных || toState = "./"
        intent: /зачисление социальных выплат или пенсии || toState = "/Шаг3"
        intent: /зачисление заработной платы || toState = "./"
        intent: /другое || toState = "./"
        event: noMatch || toState = "/Шаг2"

    state: Шаг3
        event: noMatch || toState = "./"
        a: В соответствии с законодательством (161 ФЗ) выплаты должны осуществляться на карты национальной платежной системы "МИР".
                Банк «Рога и Копыта» предлагает клиентам карты системы (МИР) типов.
                 Народная карта МИР. Ознакомиться с условиями можно по ссылке: https://www.example.com
                Универсальная карта МИР. Ознакомиться с условиями можно по ссылке: https://www.
                example.com
        Confirmation: 
            prompt = Вы готовы заказать карту?
            agreeState = /Шаг17
            useButtons = false
            agreeButton = 
            disagreeButton = 
            disagreeState = /Вопросы?

    state: Шаг17
        a: Я предлагаю оформить заявку на сайте нашего банка: https://www.example.com
            Альтернативным способом заказа является посещение офиса.
            Благодарим за обращение в банк «Рога и Копыта»!
        go!: /Завершение

    state: Завершение
        EndSession: 
            actions = $jsapi.stopSession();

    state: Вопросы?
        Confirmation: 
            prompt = У вас остались вопросы?
            useButtons = false
            agreeButton = 
            disagreeButton = 

    state: Оператор