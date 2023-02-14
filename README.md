# GoodJobRay
This simple to-do manager with some helpful built-in, it's not finished yet but I'm going to develop it.

## What we have now:
  - **Home Screen.**
      - 
      - Long click on the green profile zone to choose the avatar 
      - Long click on Username to change the username. 
      - Task counts the number of tasks for the actual day.
      - Progress will count your completed goals but goals not yet implemented :( 
      - <img width="154" src="https://github.com/kekulta/GoodJobRay/blob/master/examples/home.png"/>
  - **Planner Screen.** 
     -
     - Long click on a month to change to the previous month, usual click to next month. 
     - Long click on time to create a one-hour task. Usual click on two hours one after another (i.e. click on 14:00 and after it 16:00) 
     - To create, a multi-hour task. I didn't implement a visual representation of it so it may be a little bit laggy a first sight. Or you can add any task by the top context "plus" button.
     - <img width="154" src="https://github.com/kekulta/GoodJobRay/blob/master/examples/planner.png"/>
  - **Notes Screen.**
     -
     - Tap on any note to pin/unpin. Long click to delete. 
     - Top context "plus" button to create new.
     - <img width="154" src="https://github.com/kekulta/GoodJobRay/blob/master/examples/notes.png"/>
  - **Habits Screen**
     -
     - Actually, just a placeholder :(
     - Custom view AchieveBar completed and fully functional but I didn't implement classes to store its state.
     - Will be added soon!
## What we will have soon: 
  - Fixes.
    -
    - There are a lot of TODOs right now and will be more in the future, some examples:
      - Incorrect behavior with system dark theme.
      - Incorrect navbar color on some Samsungs.
      - Some phones' white navigation buttons can't be seen on the white navbar.
      - In my custom ProfileView functionality that should catch clicks on the avatar zone doesn't work at all, currently changed on long click.
      - et cetera, et cetera
  - Implement all Functionality.
    -
    - There are a lot of buttons that just do nothing right now. And a bunch of screens that have already been designed but not implemented yet.
  - New features!
    - All data currently stored in Room. Implement cloud saves.
    - Implement users before it.
  - Improving architecture and performance.
    -
    - This is mostly an educational project so it is really far from perfect. But I'm constantly learning and refactoring A LOT.
