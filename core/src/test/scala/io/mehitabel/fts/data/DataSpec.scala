package io.mehitabel.fts
package data

import org.scalatest._

import io.mehitabel.fts.FTSGen

class DataSpec extends FlatSpec with Matchers {

  it must "Create valid Text " in new FTSGen {
    lineGen.sample.map{ l =>
      val text = Text.fromString(l)
      text.value.shouldEqual(text.value.filter(textChar.contains(_)))
    }
  }

  it must "Create a list of words from Text" in new FTSGen {
    lineGen.sample.map {t =>
      val words: Words = Words.fromText(Text.fromString(t))
      val verify = words.value.map(str => str.filter(searchChar.contains(_)))
      words.value.shouldEqual(verify)
    }
  }

  it must "index a list of words" in {
    val text = "one two three four one two three four"
    val words: Words = Words.fromText(Text.fromString(text))
    val searchText = SearchText.fromWords(words)
    searchText.value.map(_.index).shouldEqual(List(0, 1, 2, 3, 4, 5, 6, 7))
  }
}
