Twitter-Based Product / Sales Events Recommender
========================

## General Purpose
*  Discover potential demand on products on social network (**Twitter**)
*  Provide a reciprocal platform to benefit retailers and customers

## Work Process
###Costumers
   1.  Fetch tweets from our followers 
   2.  NLP Extract desire for some product
   3.  TFIDF Classify product into one category(department)
   4.  Reply user with URL of recommended products on Amazon
   5.  User gives rating on recommended products
   6.  Do user-based recommendation based on ratings on our website

###Retailers
   1.  Retailor offers information about products and sales events
   2.  Check ratings for products from our users

## Usage
###Category Classification
   **repositery: CategoryClassification/<br>**
   1)  Calculate the TF-IDF value for each word in "total.txt" and generate the file "catTFIDF.dat" for querying:<br>
```
python createIndex_amazonCat.py stopwords.dat total.txt catTFIDF.dat
```
<br>


   2) Execute the program, type in your queries (free text, phrases), and it will rank the most similar category and return the result:<br>
```
python queryIndex_amazonCat.py stopwords.dat catTFIDF.dat total.txt
```
<br>
  

   More Information: http://www.ardendertat.com/2012/01/11/implementing-search-engines/

###Awmazon Web Service : Product Query
   **repositery: AWSquery/<br>**
Search product with the util function:
```
ArrayList<String> list = ItemSearch.getProductInfo(<product key word>, <product category>);
```
<br>
 The returned file is stored as: { {name1}, {URL1}, {name2}, {URL2}, {name3}, {URL3} }<br>  Note that you will need to fill in your own *AWS_ACCESS_KEY_ID*, *AWS_SECRET_KEY*, and *ASSOCIATE_TAG* in *AWSquwry/ItemSearch.java* to run the function. For more examples, please refer to the function call in *AWSquery/test.java*<br>

   
