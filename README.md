An Android app that works as a personal attendance diary for students. Features so far include allowing users to create a database of their courses, and log their attendance in each course. The app displays details of each course including attendance and how short or safe the user is with regards to the minimum attendance requirement.

Each course has lectures associated with it, using which a graph is created. This graph plots the user's attendance in the course with respect to each lecture of the course. The single attendance line of the graph then branches into two, one plotting the user's future attendance if they attend all subsequent lectures, and the other plotting the user's future attendance if they miss all subsequent lectures. A RecyclerView below the graph gives more information about each lecture. Clicking on a point on the graph automatically scrolls to the respective item in the RecyclerView, and vice versa. Swiping any lecture automatically deletes it.

The option to edit any particular lecture's status is currently pending.

![alt tag](http://i65.tinypic.com/ke7m0j.jpg)
