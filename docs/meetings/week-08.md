| Key | Value                                                                                          |
| --- |------------------------------------------------------------------------------------------------|
| Date: | 02.04.2024                                                                                     |
| Time: | 13:45 - 14:30                                                                                  |
| Location: | DW PC Hall Cubicle 11                                                                          |
| Chair | Cristiana Vlădăreanu                                                                                     |
| Minute Taker | Maria Grouev                                                                                   |
| Attendees: | Mihail Bankov, Maria Grouev, Robin Stuffers, Cristiana Vlădăreanu, Francesco Hamar |
- Agenda Items:

    - Opening by chair (1 min)
    - Check-in: How is everyone doing? (3 min)
        - Are we done with the issues in week 7? How are we doing with issues and feature implementations?
        - Maria: finished and closed issue to edit expense but can't test it due to bugs with adding expense
        - Misho: did settle debts tasks. Included a lot of code which was not directly connected to the ui, believes the code is testable by injecting artificial components but did not want to initialize yet. 
        - Robin: started issue with web socket and long polling, didn't finish yet, but will finish in the next 2 days. Also did detailed expense, Cristina says it looks great
        - Cristina: finished edit participant stuff
        - Additional: found issues with javafx test - testfx doesn't work, should use mockito probably? We might be wasting time on things that are not that important, for testing if we test most of our other classes it should be fine because its graded as good or very good. Should at least test all server utils methods, that will give us more line coverage.
    - Approval of the agenda - Does anyone have any additions? (1 min)
      - Misho wants to mention a few things he's noticed at the end.
    - Announcements by the TA (4 min)
      - Nothing specific to announce, said he looked at the app, from what he understands we have everything working in backend but because we started front end later many features are not yet accessible. Asked if admin menu was present.
      - The admin menu is in fact present, but we should add to the read me that the person who runs the program needs to first start the server and then the admin, as the admin menu is seperate. The admin is confiugred with the same javafx, so it should still run fine.
      - We asked ta: If we use mockito, does it add line coverage? Response: yes, line coverage is there. But when you mock something, the real version is not actually called, so that part will not be tested because you are not actually running the line to save the repo, but you are running everything else.
    - Presentation of the current app to TA (5 min)
        - Show the changes and the current state of the app.
        - Misho: settle debts. If you see open debts, you get more details, then you can clear the debts. 
          - TA asks: When you click settle debts, does it look at expenses?
          - Response: Yes, it persists. How it works: we arleady implemented the settle debts extension that gives at most n-1 debts. There is no debt object in the database, instead each participant has a net debt that is calculated when an expense is created, and when you settle debts, the debts are calculated on the fly.
        - Cristina: changes to add/edit participant. Now when you go to edit participant, you get a drop down menu of the participants instead of manually entering the name of the participant. 
          - TA mentions: think about adding a back button to this screen. Also, how are we changing the title of the event? Things we need to think about, basic requirements.
        - Peter: dowload template option is new in translations
        - Robin: detailed expense view. Before it was a list of just the name of expenses, now it is a drop down with an edit button inside, that displays all information.
        - Maria: edit expense page, and added pop ups for when fields are empty or incorrect format of money.


- Talking Points:
    - Self-reflection (5 min)
        - Did everyone familiarise themselves with the assignment?
        - Try to finish it this week, next week will be really busy
          - We did not talk very long about this, it is an assignment due on Friday.
    - Websockets and Long-polling (5 min)
        - Check-in with the progress, how is this going?
        - Do we need to work on it more, should others attempt working on it?
          - Robin says: we only need to implement 1 long polling somewhere and 1 web socket method somewhere, so he doesn't know how everyone could all work on it. Everyone should understand what's going on, but working on it with 5 people would be difficult.
          - We asked TA, how deeply does everyone need to understand this and how its implemented? TA response: You can get asked, how does long polling/web sockets work and where is it implemented in the project? So make sure everyone is familiar with it.
    - Looking at the issues of coming week (10 min)
        - What are we going to do this week?
        - Is there anything from the previous weeks we still need to finish?
          - Accessibility Assignment:
            - Color Contrast - need to pick a color scheme
            - Keyboard codes - eg, esc to go back, enter to add
            - Multi-modal visualization - 3+ elements with both picture and text - similar to how peter did it in the language drop down, or cristina in the add/edit participant buttons. Note: it works, but cannot be opened in scene builder. Change it to absolute path to open in scene builder.
            - Logical navigation - is the navigation logical? Make sure we have a back and forward for every page. 
            - Pop ups for errors and confirmations - potentially make new pop up controller/class, with message, width, and length variables. 
              - Specifically need delete button confiratmion
            - Implement undo action
            - Confirmation dialogue for delete (pop up that says are you sure?)
            - After meeting: make checklist, everyone does it for the UI they worked on.
    - Action plan for the coming week (5 min)
        - Who, what, when?
        - Assign the issues in week 8.
          - Maria: Select tags for expenses and tag creation task - having some type of selection color wheel
          - Peter: Currency Linking to exchange
          - Robin: edit/delete tag page
          - Cristina: Statistics
          - Misho: debugging and extra tests
          - Everyone (for whoever finishes first): start on email invitations
    - Misho's reminders:
      - Recent events do not persist
      - Reminder: everyone has to time track their issues

- Feedback round: What went well and what can be improved next time? (3 min)
  - Everyone was willing to step up when the chair wasn't here
  - The meeting was less organized and more chaotic than usual. Was difficult to follow the agenda without the chair, and we talked about a lot of other things.
- Were time estimates accurate? If not, what did we spend more/ less time on? (1 min)
  - Relatively accurate, but we covered lots of other stuff that wasn't included as well. Also started 5 minutes late, so we ended a bit later as well.
- Any additions before end of the meeting? (2 min)
  - At this point in the project we are debugging a lot so maybe next week we should give more time in the meeting for debugging issues.
  - There was confusion at the beginning for who will be chair, but we sorted it out.
- Closure (1 min)