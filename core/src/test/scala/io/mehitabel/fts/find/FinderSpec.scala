package io.mehitabel.fts
package find

import io.mehitabel.fts.data._
import cats._
import cats.implicits._

import org.scalatest._

class SearchSpec extends FlatSpec with Matchers {

  val searchDataMap: Map[String, String] = Map(
    "first" -> "Drain and roughly chop the tinned tomatoes, then add to the pan, stirring occasionally, until sweet and rich.",
    "second" -> "Add the shellfish, cover and cook until the fish is just done and the shellfish are open. Serve in hot bowls, with a little garlic mayo, if desired."
  )

  class TestTerms(terms: String) extends Terms[Id] {
    val searchTerms: Id[SearchTerms] = SearchTerms(Words.fromText(Text.fromString(terms)))
  }

  class TestFinder(key: String) extends Finder[Id] {

    override lazy val index: Id[SearchBody] = {
      val words: Words = Words.fromText(Text.fromString(searchDataMap.getOrElse(key, "")))
      val searchText = SearchText.fromWords(words)
      val searchBody = SearchBody(searchText, key)
      searchBody
    }

    override def score(st: SearchTerms): Id[SearchScore] =
      SearchScore(st, index)
  }

  it must "return 100.0 when all terms found" in {
    val terms = new TestTerms("chop the")
    val finder = new TestFinder("first")
    val searcher = new SearchAll[Id, List](terms, List(finder))
    searcher.scores.map(_.findResult).shouldBe(List(ResourceScore("first", 100.0)))
  }

  it must "return 0.0 when no terms found" in {
    val terms = new TestTerms("ventricular arrythmia")
    val finder = new TestFinder("first")
    val searcher = new SearchAll[Id, List](terms, List(finder))
    searcher.scores.map(_.findResult).shouldBe(List(ResourceScore("first", 0.0)))
  }

  it must "return 75.0 when 3 in 4 terms found" in {
    val terms = new TestTerms("shellfish shark just piano open bowls cover serve")
    val finder = new TestFinder("second")
    val searcher = new SearchAll[Id, List](terms, List(finder))
    searcher.scores.map(_.findResult).shouldBe(List(ResourceScore("second", 75.0)))
  }

  it must "return a correct list of reults for a list of searches" in {
    val terms = new TestTerms("roughly shellfish shark just piano open bowls cover serve")
    val finder = searchDataMap.keys.map(new TestFinder(_)).toList
    val searcher = new SearchAll[Id, List](terms, finder)
    searcher.scores.map(_.findResult).shouldBe(List(ResourceScore("first", 11.11), ResourceScore("second", 66.67)))

  }
}
