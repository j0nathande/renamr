renamr
======

Search and replace keywords in filenames and file contents. It is quite similar to what maven archetypes do but fits more general purposes.


What for?
------------
When working with a template-version of application this utiltiy application can be used to start a new project based on the template-version. So every occurance of eg. the word 'template' and 'Template' can be convertet to your new application, eg. 'calculator' and 'Calculator'.


How do I build it?
------------
It's a Maven project, so just call 'mvn clean package' to generate the jar 'renamr-0.0.1-jar-with-dependencies.jar'. This contains all the required libraries (acutally only for logging) so it can be used independently.



How do I use it?
------------
The App should be called with following parameters: 

sourceFolder destinationFolder search1->replace1 search2->replace2 [and further search->replace instructions]

For example to create a new calculator application (which is copied from ~/workspace/apptemplate) and modified via the two last parameters:

java -jar renamr-0.0.1-jar-with-dependencies.jar ~/workspace/apptemplate ~/workspace/calculator "apptemplate->calculator" "Apptemplate->Calculator"
