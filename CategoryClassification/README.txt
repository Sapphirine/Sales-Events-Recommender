====================README====================
Environment: Python 2.7
File:
queryIndex_amazonCat.py
createIndex_amazonCat.py
porterStemmer.py
stopwords.dat
total.txt (pre-processed into specific format by parsing)

Usage (command line):
1) python createIndex_amazonCat.py stopwords.dat total.txt catTFIDF.dat
-- calculate the TF-IDF value for each word in "total.txt" and generate the file "catTFIDF.dat" for querying

2) python queryIndex_amazonCat.py stopwords.dat catTFIDF.dat total.txt
-- execute the program, type in your queries (free text, phrases), and it will rank the most similar category and return the result

More Information: http://www.ardendertat.com/2012/01/11/implementing-search-engines/
