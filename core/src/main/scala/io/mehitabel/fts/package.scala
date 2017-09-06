package io.mehitabel
import scala.collection.immutable.Set

package object fts {

  type SearchMap = Map[String, List[Int]]

  val punctuation: Set[Char] = Set('.', ',', ';', ':')

  val searchChar: Set[Char] = (('a' to 'z') ++ ('0' to '9')).toSet

  val textChar: Set[Char] = searchChar + ' '

  val useChar: Set[Char] = ('A' to 'Z').toSet ++ textChar

  val legalChar: Set[Char] =  useChar ++ punctuation

  val isLegal: Char => Boolean = c => legalChar.contains(c)

  val substitute: Char => Char = c => if (punctuation.contains(c)) ' ' else c

  val filterMap: PartialFunction[Char, Char] = {
    case c: Char if isLegal(c) => substitute(c)
  }
  val dropChar: String => String =  _.collect(filterMap)

  val words: String => Array[String] = _.split("\\s+")
}
