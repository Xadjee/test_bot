patterns:
    $card = (карт*)
    $pension = (пенси*)
    $socialpayments = (соц*|детских|пособий)
    $whatisimportant = (мастеркард|виза|получ*|налич*|зарплат*)
    $cashback = (кешбек|кешбэк|кеш бек|кеш бэк|cash-back)
    $cashwithdrawal = (снимать|снял|лимит*|димит*)
    $250 = $regexp_i<^.*(250\s?000).*$>
    $500 = $regexp_i<^.*(500\s?000).*$>
    $million = $regexp_i<^.*(1\s000\s?000).*$>
    $freeservice = (мир|обслужива*|народн*|пользов*|платеж*)