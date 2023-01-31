patterns:
    $card = (карт*|ккарт*|карточк*)
    $pension = (пенси*)
    $socialpayments = (детск*|пособ*|соц*)
    $whatisimportant = (летел*|мастеркард*|виз*|зарплат*|заработ*|зп|снят*|снима*|опла*|выгод*)
    $cashback = (кеш*|кэш*|cash-back)
    $cashwithdrawal = (снимать|снял*|лимит*|димит*)
    $250 = $regexp<250\s*000>
    $500 = $regexp<500\s*000>
    $million = $regexp_i<1\s*000\s*000>
    $freeservice = (мир|обслуж*|народн*|пользов*|без платеж*)
    $particles = (конечно)
    $yes = {([$particles] (да|даа|дааа))}
    $no = {[$particles] (не|нет|нету|неа)}