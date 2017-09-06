package io.mehitabel.fts
package data

class SearchBody private(val wordMap: SearchMap,val resourceName: String)

object SearchBody {
  def apply(st: SearchText, rn: String = ""): SearchBody = {
    // index each word by it's position in the body
    new SearchBody(st.value.groupBy(_.word).map{case (k,v) => (k, v.map(e => e.index))}, rn)
  }
}

case class SearchTerms(words: Words) {
  // count of occurences of each term in list
  val termMap: Map[String, Int] = words.value.foldLeft(Map[String,Int]() withDefaultValue 0){
    (m,x) => m + (x -> (1 + m(x)))
  }
  // todo index each term in list so can match on sequence as well as presence
}

case class ResourceScore(resourceName: String, score: Double) {
  override def toString = s"resource : $resourceName - score $score"
}

trait Searching {
  def terms: SearchTerms
  def body: SearchBody

  private val findTerm: String => Option[Tuple2[String, List[Int]]] = term =>
    body.wordMap.get(term).map(l => (term, l))

  lazy val findTerms = terms.words.value.map(findTerm)

  //todo ensure duplicate terms are accounted for
  // currently just check for number of found matches against number of input terms as crude %age
  def termsPresent: Double = {
    val x = terms.words.value.toSet
    val y = findTerms.flatten.map{t => t._1}.toSet
    // super naive rounding probably use BigDecimal in real world
    "%.2f".format((y.size * 1.0 / x.size * 1.0) * 100.00).toDouble
  }

  def findResult = ResourceScore(body.resourceName, termsPresent)
}

case class SearchScore(terms: SearchTerms, body: SearchBody) extends Searching
