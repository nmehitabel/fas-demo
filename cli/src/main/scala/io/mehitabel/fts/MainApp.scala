package io.mehitabel.fts

import scala.concurrent.Await
import scala.concurrent.duration._

object MainApp extends App {
  // not this ec in real world
  import scala.concurrent.ExecutionContext.Implicits.global

  if (args.length == 0) {
    println("Error must provide directory name to search")
    System.exit(-1)
  }
  val dirname = args(0)

  // todo testable version of this
  val termSearches: List[String] = {
    println("Enter sets of terms to search for on each line, with empty line to finish:\n")
    Iterator.continually(scala.io.StdIn.readLine())
                       .takeWhile(Option(_).fold(false)(_.nonEmpty))
                       .toList
  }

  val searchService = new SearchService(dirname)

  termSearches.map { t =>
    println(s"\n\nSearching for [$t]")
    val r = Await.result(searchService.results(t), Duration.Inf)
    val fs = r.filter{_.score > 0.0}
              .sortBy(-_.score)
              .take(10)
    if (fs.isEmpty) println("No Matches found")
    else fs.foreach(println)
  }

}
