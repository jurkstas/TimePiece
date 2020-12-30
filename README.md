This fork
=============
I have added Lithuanian language and some changes to how candidate is generated - see code.
Also, there are some issues with running this with Eclipse and Java. As far as I understand, it works with Java 8, but 
does not work with Java 11. Don't know if it works with 9 or 10.


Original README
=============
This is just a quick hack for generating word clocks

see http://miniaturegiantspacehamster.blogspot.com/2011/03/building-word-clock-part-1-genetic.html for more details!

relevant parts of the porgram output:
check ok:	total number of strings that can be represented (e.g. "four thirty", "half past four", ...)
nok:		total number of strings that can not be represented
times ok:	from the 144 5-minutes intervals (1:05, 1:10, ...) how many can be represented?
nok:		how many times intervals can not be represented (i.e. the clock is not usable as long as there are more then 0)
