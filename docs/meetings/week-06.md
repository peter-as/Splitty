| Key | Value                                                                                             |
| --- |---------------------------------------------------------------------------------------------------|
| Date: | 19.03.2024                                                                                        |
| Time: | 13:45 - 14:30                                                                                     |
| Location: | DW PC Hall Cubicle 11                                                                             |
| Chair | Robin Stuffers                                                                                    |
| Minute Taker | Cristiana Vlădăreanu                                                                                        |
| Attendees: | Peter Aszalós, Mihail Bankov, Maria Grouev, Robin Stuffers, Cristiana Vlădăreanu, Francesco Hamar |
- Agenda Items:

    - Opening by chair (1 min)
    - Check -in: How is everyone doing? (3 min)
        - How are we doing on the issues all of us are working on?
        - Did everyone meet the basic knockout criteria?
        - How was the work load in the past 2 weeks?
    - Approval of the agenda - Does anyone have any additions? (1 min)
    - Announcements by the TA (4 min)
    - Presentation of the current app to TA (5 min)
        - Everyone shows what they individually contributed, not in depth, but for example ui we made.


- Talking Points:
    - Code contributions and code reviews feedback (5 min)
        - What did we get positive feedback on?
        - What could we have done better?
        - How are we going to improve in the future?
    - Tasks and planning feedback (5 min)
        - What did we get positive feedback on?
        - What could we have done better?
        - How are we going to improve in the future?
    - Accessibility assignment (10 min)
      - What colors and styles should we use for the program?
      - Options to switch styles?
      - Shortcuts?
      - How will we set up general rules for design of the ui, navigation, errors?
    - Looking at the issues of coming week (8 min)
        - Anyone have preference for a certain issue?
        - Double-checking milestone for this week is achievable, does something need to change?
    - Action plan for the coming week (3 min)
        - Who, what, when?
        - Maybe look at code contributions summative in the future?


- Feedback round: What went well and what can be improved next time? (3 min)
- Were time estimates accurate? if not where did we spend more / less time on? (1 min)
- Any additions before end of the meeting? (2 min)
- Closure (1 min)


**Minute taker notes:**

_- Opening by Chair and Check-in_
- Everyone was feeling positive about the week, however most of us still had some work to finish/debug and some other features to implement.

_- Approval of the agenda_ 
- We agreed to additionally discuss the feedback we received on the Technology assignment, which had just come out, and address a few questions about the Language Switch

_- Announcements by the TA_

- A bunch of assignments were graded so we should really look into the feedback, see how we've done and discuss

- If we receive one Insufficient on a single grading rubric then the whole assignment will be marked as Insufficient, but we shouldn't worry very much about that because, for now, it's formative grading

- Instead, we should look at what we can improve, read the grading rubric very carefully and prepare based on that criteria so we can have a good grading on the summative assignments
- Soon we will start receiving feedback on our actual application and the features we have implemented/ we are yet to implement

_- Presentation of the current app to TA_

- We went around the table showing what everyone has worked on

- We had some technical difficulties with running the application because of synchronization between the local and the remote repo and also a few 'Unsupported Java' errors some of us had been struggling with, but eventually we got everything to work

- We started with showing the UI on the admin side, with the Login and Event Overview menu, then we showed the Start Screen on the client side with language switch, and also the basic UI for client Event Overview and Add/Edit Expense


_- Talking Points:_


- Code contributions and code reviews feedback

    -  We can improve on 'Reviewability' by adding more extensive descriptions to our MRs
    - We can improve on 'Code Reviews' by asking more specific questions about the code a person wrote and included in a MR; every comment should be responded to and it should start a discussion
    - We decided that an MR should not be merged unless comments have been responded to and a comprehensive discussion took place; also, we agreed that we want minimum 2 approvals/MR, and out TA will change that in GitLab as well
    - We agreed on creating a new Discord channel for announcing MRs, in order to make sure that they are discussed, approved and merged in a timely manner


- Tasks and planning feedback
    - We can improve on 'Assignment Tracking' - we received Insufficient because we had not assigned any issues to any developers at the time of grading
    - We now have decided to assign all/ the most part of our issues at the beginning of each week, to make sure the work load is balanced
    - Some issues were assigned to later weeks but we started working on parts of them earlier; we have decided that in these cases, we will split the original issue into smaller ones and add the smaller issues to the Milestone of the week when it was implemented
    
- Accessibility assignment
    - For now, besides the Tags and Statistics, we have decided to keep everything else in the app in black and white
    - We should make sure that when we use colors, they have contrast and they are accessible for visually impaired users as well
    - We had the idea of having an extra option for switching/ increasing the contrast of colors
    - We need to implement keyboard shortcuts and navigation so the app can also be used without a mouse/ touchpad
    - We will display error messages with either a pop-up or some red error text
    - Near the end of the project, if we have extra time, we can add different color pallets and extra design elements to the app

- Looking at the issues of coming week
    - We read through the issues we had already planned for this week and agreed on sticking to them, with an extra addition of a new issue for a debug
       
- Action plan for the coming week
    - We assigned all the issues between ourselves, as outlined below
        - Cris: Add participant + Edit/remove Participant
        - Maria: Add new expense - Expense page + Edit expense
        - Mihail: Fix rounding when splitting expense + make new issue for debugging/changing Participant Controller
        - Peter: User managing events + Event (title) creation and editing
        - Robin: Add new expense - Overview page + Expense list


_- Feedback round_
- The pacing of the meeting was good, we had more topics to discuss this time and a lot of feedback to take into consideration, so the meeting was longer than usual but we got through every topic before the end

- The order of talking points was good, starting with the feedback and then continuing with the task distribution felt natural

- Organization was good, even better than usual

_- Were time estimates accurate?_
- Time estimates were good, even though discussing the feedback took a bit longer than expected

- Showing the app to our TA took a bit longer because of technical issues, we should make sure that everything works smoothly for next time

_- Any additions before end of the meeting?_
- Remember to add @Service classes in the Controllers, in order to follow the grading rubric in the Technology assignment a bit better
