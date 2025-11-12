# jako klient chcę zagrać w Lotto, podać liczby i sprawdzić, czy wygrałem danego dnia
klient podaje 6 liczb
liczby muszą być w zakresie od 1-99
liczby nie mogą się powtarzać
klient dostaje informacje o dacie losowanie
klient dostaje informacje o swoim unikalnym identyfikatorze losowanie
klient może sprawdzić, czy wygrał (dostaje informacje ile trafił liczb)
wygrane liczby powinny być pobrane ze zdalnego serwera HTTP
losowania są co soboty o 12:00


# Happy path WITHOUT FAILURES

when user input 6 numbers (1, 2, 3, 4, 5, 6) to POST /inputNumbers on date on 12-10-2022
system returns that numbers were correct in range (1-99), unique random ID (userLotteryId) and draw date 15-10-2022
system generates random winning number (1, 2, 3, 4, 5, 6) using “lotto” logic for day 15-10-2022
user wants to know if won using GET /winners/{userLotteryId}
system returns ‘won result’ to user
