Demo Program For Text Search

Build and Run

Project uses
  Java  8
  Scala 2.12.3
  Sbt   0.13.6


The project can be built with sbt using the assembly plugin from the
project directory fts-demo

sbt assembly

This will create a runnable jar file in
./target/scala-2.12/fts-assembly-0.1.0.jar

which can be executed as follows supplying the full pathname to the java command

java -jar ./target/scala-2.12/fts-assembly-0.1.0.jar <directory_path>

where <directory_path> is the directory with .txt files to search

The user can then enter sets of terms (space separated) to search for.
An empty line of input will terminate the acceptance of term sets and start the
searches.

e.g. User will see at command line

Enter sets of terms to search for on each line, with empty line to finish:

Then can enter terms

one two three<return>
mary had a little lamb<return>
<return>

Where the last return of an empty line will start the SearchTerms

If necessary adjust the heap space with the -Xmx option on the command line.

Assumptions

1. Not particularly geared for performance
   1.1 Files read into memory limiting size of corpus to be searched
   1.2 Futures are just using default execution context
   1.3 No benchmarking or instrumentation to measure performance

2. Search method is primitive at the moment
   1. Not tested on Windows
   2. Only looking for ".txt" files within directory substructure
   3. Non alphanumeric characters stripped out
   (replaced with spaces to preserve wordlike boundary)
   4. after input is scrubbed just look for exact matches and count number of
   successes against number of search terms.
   5. Only considering for ascii english characters


Possible Improvements

Performance
1. Need benchmarking of some kind before should attempt
2. More efficient storage of search corpus e.g. Patricia Trie
3. Tuning of EC (e.g. separate ec for blocking io reading from file)
4. Try different Future implementation e.g. Twitter, Monix Task or FS2 task
5. Could try using Akka Actors for concurrency
6. Possibly stream input from files to update search structure

Searching
1. Stemming of search corpus for fuzzy search, plurals
2. Consider adjacency and order of found terms with original search
3. Quoted terms for absolute match
4. Have multiple search strategies that can be run in parallel and results
compared to choose most appropriate match type.
5. Use resource uri so don't just limit to text file on disk
(e.g for Amazon S3 or GCloud)
