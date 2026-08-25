# OACP Test — OACP Context

## What this app does
A test app for exercising OACP dispatch patterns and slot-filling parameter extraction.
It has a persistent counter and exposes both foreground (activity) and background (broadcast) actions.

## Capabilities
- increment_counter: Increase the counter by a specified amount (default 1). Accepts `amount` parameter.
- decrement_counter: Decrease the counter by a specified amount (default 1). Accepts `amount` parameter.
- set_counter: Set the counter to an exact value. Requires `value` parameter.
- reset_counter: Reset the counter to zero. No parameters. Destructive — asks for confirmation.
- get_counter: Read the counter value without opening the app. Background action.
- get_battery: Read device battery percentage. Background action.

## Disambiguation
- "add 5 to the counter" / "increment by 3" / "plus ten" → increment_counter (with amount)
- "subtract 2" / "minus 5" / "decrease by three" → decrement_counter (with amount)
- "set counter to 42" / "make it 100" → set_counter (with value)
- "reset" / "clear" / "go back to zero" → reset_counter
- "what's the counter" / "check the count" → get_counter
- "battery level" / "how much juice" → get_battery

## Vague command examples
- "bump it up a few" → increment_counter
- "take some off" → decrement_counter
- "what's the number at" → get_counter
- "how much power left" → get_battery
