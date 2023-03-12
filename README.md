My Books Crud Application

Live App URL: 

http://35.154.22.94:8080/mybooksapp-0.0.1-SNAPSHOT/

( runs on http, so couldnot interact with https as of now)

Used Stack:

1. Java 8
2. Spring boot 2.8.
3. Gradle 7.6
4. H2 as inmemory database.

Steps to Run the project.

1. Unzip the folder.
2. Import the project in any of the IDE and build the project.
3. Run the application.
4. Backend API will be up and running in port 8080

Avaialable Endpoints:
--------------------

Crud for user:
-------------

1. Get particular User by Id - GET : http://localhost:8080/user/{userId}
2. Create New User - POST : http://localhost:8080/user
3. Update particular User by Id - PUT : http://localhost:8080/user/{userId}
5. Delete the User by Id - DELETE : http://localhost:8080/user/{userId}

Tasks
-------

1. Get All tasks based on filter -
   GET : http://localhost:8080/tasks?filter=my&userId=${userId}&status=all

   filter = "my" or "all" ( "my" will get tasks assigned to userId passed whereas all will get all
   tasks )
   userId = userId (required if passed "my" as filter)
   status = "active" or "resolved" or "queued"

2. Resolve task by Id - POST : http://localhost:8080/tasks/{taskId}/resolve

Requests
--------

1. Get All Requests created by a customer - GET : http://localhost:8080/requests?userId=${userId}
2. Create requests POST: http://localhost:8080/requests
   sampleBody {
   "requestType" : "accounting",
   "createdBy"   : "1"
   }

UserAvailability Table:
----------------------

UserAvailability table is designed in way to get the availablity of the users

It stores the details until which day the expert is occupied and as well as the hours occupied in
that day

userId - Id of the expert occupiedUntil - date until the user is occupied with tasks occupiedHours -
hours the user is occupied on that particular day.



Algorithm used For Assigning Tasks to users
-------------------------------------------

1. Whenever a task is created.
2. Get the deadline of the task.
3. Get the list of users available on or before the deadline day of the task
4. If the list is empty(No users available before deadline) push it to the common queue(status as
   queued and keep it unassigned)
5. If the list has values
    1. sort the list by deadline ascending followed by available hours ascending which gives us the
       expert who is available before all other experts.
    2. If the (working hours - occupied hours) is less than the required hours for the task a. If
       the expert occupiedUntil date is equal to the deadline date, which is equivalent to the no
       users available scenario, so push it to common queue(status as queued and keep it unassigned)
       b. update the occupiedUntil to the next date and update the occupiedHours in the next day
       updatedOccupiedHours = RequiredHours - (workingHours - occupiedHours);
    3. Else update the occupiedHours updatedOccupiedHours = occupiedHours + RequiredHours;

Algorithm used For Resolving Task
-----------------------------------------

1. Whenever a task is resolved
2. Get the respective request the task is associated with.
3. Mark the task as resolved.
4. If the current task is the final task of the request.
    1. mark the request as completed
5. Else get the next follow up task to be created from the request details
6. Create a new task and Check the availability of the same expert, who did the previous task of
   this request.
7. If the user is available within the deadline of the new task, assign to the expert and mark as
   active
8. Else assign to expert and put it in queue, so expert can take in when he is free.

Note : used the same method for creating task and followup task(so checks added to assign to expert
if previous task )

General Notes:

    1. No authentication and authorization are done for the endpoints. 
    2. userId are passed as params and in body to avoid additional checks for authentication or authorization
    3. Login/Signup states maintained in a state only(On refresh it will log out)
    4. Auto generated Ids are used as the primary keys for all the entities.
    5. Endpoint responses will not have any user friendly msgs, but proper status codes sent.
    6. Accounting Request is hardcoded as string with all the tasks associated to it.
   
