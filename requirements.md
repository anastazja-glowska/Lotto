Lotto Project – Client Requirements
As a client, I want to play Lotto by submitting my chosen numbers and checking if I have won on a given draw date.

The client must provide exactly 6 numbers.

All numbers must be within the range 1–99.

Numbers must be unique (no duplicates allowed).

After submitting, the client receives information about the scheduled draw date.

The client also receives a unique ticket identifier for the draw.

The client can later verify if they have won, including details on how many numbers matched.

Winning numbers are retrieved from a remote HTTP server.

Draws take place every Saturday at 12:00 PM.

# happy path 
step 1: external service returns 6 random numbers (1,2,3,4,5,6)
step 2: system generated winning numbers for draw date: 19.11.2022 12:00
step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 16-11-2022 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)
step 4: user made GET /results/notExistingId and system returned 404(NOT_FOUND) and body with (“message”: “Not found for id: notExistingId” and “status”: “NOT_FOUND”)
step 5: 3 days and 55 minutes passed, and it is 5 minute before draw (19.11.2022 11:55)
step 6: system generated result for TicketId: sampleTicketId with draw date 19.11.2022 12:00, and saved it with 6 hits
step 7: 6 minutes passed and it is 1 minute after the draw (19.11.2022 12:01)
step 8: user made GET /results/sampleTicketId and system returned 200 (OK)