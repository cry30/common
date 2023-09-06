## Overview

This project is location of commonly used codes.  It is intended to be compact,
not a substitute to 3rd party tools, but small, targeted solution to actual problems.
Code implementation maybe copied to actual project so there is no need for actual dependency.

## Using on Eclipse IDE
* You may use the Run as JUnit test to verify that it is working correctly. 
* Make sure you have configured the correct JDK in the pom.xml and in the project properties. The last version used is 17.



###




## Features

1. OneLogger - A single class development logging utility, that can be configured in 
a properties file.
  
  *Sub features*
  
  - Display package
  - Log level
  - Log Timestamp
  - Display caller method
  - Display caller class
  - Display caller line number.
  
2. Callback - A functional interface with no parameter, and no return.
3. SQLBuilder and SQL Clause Builder - An SQL statement builder for writing better
SQL codes in java.
4. MapUtil - For merging Map.
5. IO Utils
 
  *Classes*

  - File reader/writer
  - BufferedReader Iterator
  - STDIN reader
  
5. Loop Construct
  
  *Classes/Interface*

  - Enumer
  - Iter - Iterator for Array, Collections
  - IterM - Map Iterator
  - Range
  - Resource Iterator
  
6.  Utility classes

  - AnnUtil - Annotation Util.
  - Ano - Generic placeholder object, can be used for tracking return value.
  - BeanUtil
  - ObjectUtil
  - ReflectUtil
  - ResourceUtil
  - StringUtil
  

Changelog:

----------
- June 16, 2016
  - Added Project Wiki.
