### Changes from PA03 to PA04

- Changed Coordinate from a class to a record: CoordJson was similar in structure to our Coordinate
  class, so to make it
  easier and concise, we "combined" the two.
- Updated "setUp" such that it correctly places ship randomly, without overlap, to be more
  competitive for the competition.
- Added methods to ship that get its starting coordinate and its direction. Used in the new class,
  ship adapter, such that we can correctly convert a ship into a ShipJson.
- Updated driver such that it can handle multiple types of games based on the arguments passed in.
- Added AutoGameController which implements GameController, corresponding to an AI game, to control
  the interactions with the server and model.
- Added corresponding Jsons to help with server communication.
- Updated the ArtificialPlayerBoard's addShots to be based on a hunt and target strategy to be more
  competitive for the competition.
- Simplified how models were set up to improve readability and reduce classes' reliance on each
  other. 