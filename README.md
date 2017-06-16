Author: Peter Zhou

Introduction

A Java program that retrieve and sorts all metropolitan statistical areas (MSAs) in the United States
by wettest population during May of 2015. The population wetness of an MSA is calculated as the number 
of people in the MSA times the amount of rain received. 

The brief idea is to build two HashMaps. One map contains the MSA name and MSA, the MAS has a set of County. 
The other map contains the County and  the average precipitation amount during 2015 May inside this county.  
Then calculate the Wetness with the  total population of MSA and the average precipitation amount of MSA.  
Since the different states may have same county name, the program use the combination of county name and 
the Abbreviation of state name to identity a unique county in the U.S.




Prerequisite
Java(TM) SE Runtime Environment 1.8.0
Gradle 2.13

Instruction:

// clone the code from github
git clone <the github link>

// build the program
gradle build 

//The argument need 4 text files arguments, which locates at src/java/resource folder
//they are configured in gradle run task. 
//to run the program
gradle run

// check the results 
vim output.txt or cat output.txt



 



