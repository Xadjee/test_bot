require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: ШАГ1
        q!: $regex</start>
        a: Hello

    state: ШАГ2
        q!: * $card *