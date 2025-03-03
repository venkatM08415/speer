# speer
speer application to share and maintain notes
Step 1:- update database username,password,schema and  run the application. application automatically creates required tabels once the application is stareted please create five users using step 2 

Step 2:-create 5 users with username and password please refer to the below json request for creating the users
note:- please register single user at once

{
  "username": "venkat",
  "password": "123456"
}

{
  "username": "alice",
  "password": "123456"
}

{
  "username": "guest",
  "password": "123456"
}

{
  "username": "tommy",
  "password": "123456"
}

{
  "username": "mike",
  "password": "123456"
}

step 3:- execute the below provided schema in DB

INSERT INTO note (title, content, owner_id) VALUES
('Meeting Notes', 'Discussed project timelines and deliverables.', 1),
('Important Tasks', 'Complete the report by Friday.', 2),
('Team Updates', 'New team members joined the project.', 3),
('Project Plan', 'Finalized the project plan and milestones.', 4),
('Weekly Review', 'Reviewed progress and set goals for next week.', 5),
('Client Feedback', 'Received feedback from the client on the prototype.', 1),
('Bug Fixes', 'Fixed critical bugs reported by QA team.', 2),
('Design Mockups', 'Created new design mockups for the UI.', 3),
('Code Refactor', 'Refactored legacy code for better maintainability.', 4),
('Sprint Planning', 'Planned tasks for the upcoming sprint.', 5),
('Budget Review', 'Reviewed project budget and expenses.', 1),
('Risk Assessment', 'Identified potential risks and mitigation strategies.', 2),
('Training Session', 'Conducted training on new tools for the team.', 3),
('Product Launch', 'Prepared for the upcoming product launch.', 4),
('Marketing Strategy', 'Discussed new marketing strategies.', 5),
('Customer Support', 'Resolved customer support tickets.', 1),
('Performance Review', 'Reviewed team performance and provided feedback.', 2),
('Security Audit', 'Conducted a security audit of the application.', 3),
('Database Optimization', 'Optimized database queries for better performance.', 4),
('API Documentation', 'Updated API documentation for developers.', 5);

INSERT INTO note_shared_users (note_id, users_id) VALUES
(2, 1),
(3, 2),
(4, 3),
(5, 4),
(6, 5),
(7, 1),
(8, 2),
(9, 3),
(10, 4),
(11, 5),
(12, 1),
(13, 2),
(14, 3),
(15, 4),
(16, 5),
(17, 1),
(18, 2),
(19, 3),
(20, 4),
(1, 5);


step 3:- please import the postman collection and login with any user with given url and request

http://localhost:8080/api/auth/login

{
  "username": "venkat",
  "password": "123456"
}

step 4:- once step 3 is done you will be reciving jwt_token in the response. now add this token value to the jwt_token variable in the postman global variables

step 5:- now you can access all provided API in the collection for searching notes,creating notes, retriving all notes owned and shared with user , and api for sharing a note to another user
