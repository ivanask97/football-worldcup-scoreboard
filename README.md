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

## Features:

Scoreboard: Start, update,finish football matches and generating the summary of all the ongoing matches
Update scores for ongoing matches
Sorting Matches on the scoreboard: Matches are primarly sorted by the total score. The match with higher total score is ranking higher on the scoreboard.
In case of a tie, than the second criteria is to check which football match started most recently on the scoreboard
Prevention of a conflict: Teams can't play multiple matches simultaneously, there can not be two matches with the same teams just reversed
Validation: Validation for team names and scores
Summary Generation: Get formatted match summaries as a list


## FootballMatch Class
Represents a single football match with:
 - Home and away team names
 - Current scores for both teams
 - Total score calculation


## Scoreboard Class
### Core Methods

#### startMatch(String homeTeam, String awayTeam)
Starts a new match between two teams.
 - Both teams must not be currently playing
 - Team names are case-insensitive and whitespace is trimmed
 - Teams cannot play against themselves
 Throws:
 - IllegalStateException - If match already exists or teams are active in another match
 - IllegalArgumentException - If team names are invalid

#### updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore)
Updates the score for an ongoing match.
 - Scores must be between 0-100
 - Match must already exist
Throws:
 - IllegalStateException - If match doesn't exist
 - IllegalArgumentException - If scores are invalid

#### finishMatch(String homeTeam, String awayTeam)
Ends a match and removes it from the scoreboard.
Throws:
 - IllegalStateException - If match doesn't exist

#### getSummary()
Returns a sorted list of all ongoing matches.
Sorting Rules:
 - Total Score (highest first)
 - Recency (if there is a tie, the match that was most recently added is shown first in summary)


## Use case example
 - initialize the scoreboard
Scoreboard scoreboard = new Scoreboard();
 - Start multiple matches
 scoreboard.startMatch("Team A", "Team B");
 scoreboard.startMatch("Team C", "Team D");
 scoreboard.startMatch("Team E", "Team F");
 - update with total scores
 scoreboard.updateScore("Team A", "Team B", 3, 3);
 scoreboard.updateScore("Team C", "Team D", 1, 2);
 scoreboard.updateScore("Team E", "Team F", 2, 4);
 - finishing the ongoing match
 scoreboard.finishMatch("Team E", "Team F");
 - get the summary of ongoing matches
 List<String> summary = scoreboard.getSummary();

### Validation rules
Team Names
 - Cannot be null or empty
 - Whitespace will be automatically trimmed ("Arsenal    " = "Arsenal")
 - Case-insensitive comparison ("arsenal" = "Arsenal")
 -Teams cannot play against themselves
Scores
 - Must be non-negative (≥ 0)
 - Must be realistic (< 100)
 - Updated scores replace previous scores completely
Match States
 - Teams can only participate in one match at a time
 - Matches must be started before they can be updated
 - Matches must exist before they can be finished

### Requirements
 - Java 8 or higher
 - No external dependencies

## Additional notes

Since this is a simple Java library designed primarily to support a live Football World Cup scoreboard, the `FootballMatch` class is implemented as a regular class rather than an interface or abstract class. If the project were to support multiple types of matches (e.g., basketball, rugby, etc.), we would introduce a `Match` interface or Match as abstract class, which different match types would implement or extend.

Given the current scope, focusing mainly on the scoreboard functionality for football matches, this design keeps things straightforward and easy to maintain.

In further development, there would also be a `Team` class, where each team would be represented and classified. However, for simplicity, this is not currently implemented.

For the `Scoreboard` class, an additional interface could be introduced to define all the necessary methods that a `Scoreboard` implementation should support. This would allow flexibility to implement different types of scoreboards (e.g., in-memory or remote storage) while adhering to a consistent contract.


### Team name validation

The implementation covers the most common edge cases for team names, including:

- Null or empty names
- Trimming whitespace
- Preventing identical home and away team names (case-insensitive)

Additional validation could be added in the future, such as:

- Checking for reserved words in team names
- Verifying that team names correspond to countries qualified for the World Cup tournament
- Detecting and handling unusual or special characters in team names
