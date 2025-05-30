This is a simple Java library for managing a live football World Cup scoreboard.

The scoreboard supports the following operations:
1. Start a new match, assuming initial score 0 – 0 and adding it the scoreboard.
   This should capture following parameters:
   a. Home team
   b. Away team
2. Update score. This should receive a pair of absolute scores: home team score and away
   team score.
3. Finish match currently in progress. This removes a match from the scoreboard.
4. Get a summary of matches in progress ordered by their total score. The matches with the
   same total score will be returned ordered by the most recently started match in the
   scoreboard. 

## Additional notes

### FootballMatch class

Since this is a simple Java library designed primarily to support a live Football World Cup scoreboard, the `FootballMatch` class is implemented as a regular class rather than an interface. If the project were to support multiple types of matches (e.g., basketball, rugby, etc.), we would introduce a `Match` interface, which different match types would implement or extend.

Given the current scope, focusing mainly on the scoreboard functionality for football matches, this design keeps things straightforward and easy to maintain.

### Team name validation

The implementation covers the most common edge cases for team names, including:

- Null or empty names
- Trimming whitespace
- Preventing identical home and away team names (case-insensitive)

Additional validation could be added in the future, such as:

- Checking for reserved words in team names
- Verifying that team names correspond to countries qualified for the World Cup tournament
- Detecting and handling unusual or special characters in team names
