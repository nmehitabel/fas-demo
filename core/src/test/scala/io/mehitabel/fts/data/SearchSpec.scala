package io.mehitabel.fts
package data

import org.scalatest._

class SearchSpec extends FlatSpec with Matchers {

  val text = "abc def abc ghi jkl abc mno def jkl pqr"
  val words: Words = Words.fromText(Text.fromString(text))
  val searchText = SearchText.fromWords(words)
  val searchBody = SearchBody(searchText)

  it must "create a map of keys and word indexes from text" in {
    searchBody.wordMap.shouldEqual(
      Map("abc" -> List(0, 2, 5),
          "pqr" -> List(9),
          "mno" -> List(6),
          "jkl" -> List(4, 8),
          "def" -> List(1, 7),
          "ghi" -> List(3))
    )
  }

  it must "identify absence or presence of word in search text" in {
    val terms = SearchTerms(Words.fromText(Text.fromString("jkl zzz")))
    val mySearch = SearchScore(terms, searchBody)
    mySearch.findTerms.shouldBe(List(Some(Tuple2("jkl", List(4,8))), None))
  }


}
