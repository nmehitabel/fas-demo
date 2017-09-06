package io.mehitabel.fts

import io.mehitabel.fts.data._
import io.mehitabel.fts.find._
import io.mehitabel.fts.file._

//import cats._
import cats.implicits._

import better.files._
//import better.files.Dsl._
//import java.io.{File => JFile}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

class AsyncTerms(terms: String) extends Terms[Future] {
  val searchTerms: Future[SearchTerms] = Future.successful(SearchTerms(Words.fromText(Text.fromString(terms))))
}

class AsyncFinder(key: String)(implicit ec: ExecutionContext) extends Finder[Future] {

  def toFH = Future.successful(File(key))

  // Limit to size of file can reasonably slurp
  def fileContent(file: File) = Future {
    file.lines.mkString
  }

  override lazy val index: Future[SearchBody] =
    for {
      file  <- toFH
      lines <- fileContent(file)
      words <- Future{ Words.fromText(Text.fromString(lines))}
      searchText <- Future{SearchText.fromWords(words) }
    } yield SearchBody(searchText, file.toString)

  override def score(st: SearchTerms): Future[SearchScore] =
    for {
      idx <- index
    } yield SearchScore(st, idx)

}

class SearchService(rootDir: String)(implicit ec: ExecutionContext) {

  def asyncTerms(t: String) = new AsyncTerms(t)

  // todo think of better way than toing and froing between String and File
  lazy val finders = (new DirectoryFiles(rootDir)).textFiles.map(tf => new AsyncFinder(tf.toString))

  def searcher(terms: String): SearchAll[Future,List] = new SearchAll[Future, List](asyncTerms(terms), finders)

  def results(terms: String): Future[List[ResourceScore]] = searcher(terms).scores.flatMap(identity).map(_.map(_.findResult))

}
