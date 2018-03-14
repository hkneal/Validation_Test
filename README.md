# Validation_Test
Java program that validates trade input from a .csv file, reads valid broker and stock symbol input from 2 other .txt files and outputs valid and invalid order .txt files. 

# Installation
Import and execute this project from Eclipse 

1) From Eclipse select FILE->IMPORT... 
2) Expand the GENERAL tab then Select "Existing Projects into Workspace"->NEXT 
3) From the Import Projects window Select the "Select Archive File:" radio button
4) Click "Browse" and navigate to the .zip Archive File "Archive.zip"-> click Open-> click FINISH
5) Once the project has been imported into Eclipse Highlight the "Validation_Test" project and click Run->Run from the Eclipse menu.

# Output
The console window lists the location of two output .txt files, one for valid orders, and one for invalid orders.  You may need to refresh your project window (function F5 key) to see the output files in the package explorer project pane.  The files are stored in Validation_Test/Files folder.  

Two JSON output files are also generated: one with a list of valid transaction and one with a listing of all invalid transactions.

# Revision 3/14
Refractering to reduce nested loops and complexity

1) Moved order validation requirements testing into Order Class and eliminated nesting loops
2) Removed Helper Class, moved it's validation code into Validation Class
3) Changed to FileInputStream, - TODO move to BufferedInputStream 

TODO - Write to output text as part of input read process, AND/OR use Stream and Filter on valid/invalid order lists to generate the output .txt and .json files.
