## KWIX
KWIX is a payroll management system that I built to solve business problems for my client _Works of Art Construction LLC_. The client has multiple employees spread across multiple jobsites. They wanted an easy way to store:

*   Employee Information
*   Payroll Information
*   Jobsite Information
*   Paystub Information

KWIX assisted my client in managing labor costs, jobsite costs, time worked per employee and across jobsites and more.

### What is KWIX LITE?

KWIX LITE is a sandbox version of KWIX and it is the repository you are currently looking at.

KWIX LITE was designed to allow guest users to play around with KWIX in a sandbox environment. 
It is essentially a public demo that I can show off to new potential clients or recruiters who may be curious to see what I have built using springboot. 
Feel free to play around, but note that some features are disabled and all data is scoped to each user's session. 
Once a user logs out or their session expires all of their data is destroyed.

KWIX is locked down and kept private to secure client data. Additionally KWIX has an entirely different security config. Many of the features of KWIX LITE have been restricted or simplified for demonstration purposes. Some of the KWIX code still exists in the KWIX LITE repo for those interested.

## Stack
- Backend: Java (Springboot)
- Frontend: html + css + vanilla js
- Templates: thymleaf
- Database: postgres
- Containerized using docker
- Both the postgresql server and the springboot application are hosted on a 3rd party hosting site

> note: KWIX LITE does not use a postgresql server. All data is cached and scoped to the user's session. This is a just a demo.

## Springboot Tools 
- JPA
- Thymeleaf
- Spring Security
- JDBC 
