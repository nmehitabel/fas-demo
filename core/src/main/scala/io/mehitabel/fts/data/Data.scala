package io.mehitabel.fts
package data

case class Text private(value: String) extends AnyVal

object Text {
  def fromString(s: String): Text = Text(dropChar(s).toLowerCase)
}

case class Words private(value: List[String]) extends AnyVal

object Words {
  def fromText(t: Text) = Words(words(t.value).toList)
}

case class IndexedWord(word: String, index: Int)

case class SearchText private(value: List[IndexedWord])

object SearchText {
  def fromWords(w: Words) = SearchText(
    w.value.zipWithIndex.map(IndexedWord.tupled)
  )
}
