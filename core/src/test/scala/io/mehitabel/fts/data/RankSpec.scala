package io.mehitabel.fts
package data

import org.scalatest._

class RankSpec extends FlatSpec with Matchers {
  import Rank._
  val atoz = ('a' to 'z').toList
  val ztoa = atoz.reverse
  it must "rank a collection according to a unique value numeric returning function" in {
    val asI: Char => Int = _.toInt
    val asF: Char => Double = asI(_) * 1.75

    (rank(ztoa)(asI)).shouldEqual(atoz.map(c => (c, asI(c))))
    (rankInverse(atoz)(asF)).shouldEqual(ztoa.map(c => (c,asF(c))))
  }

  it must "rank a collection according to a non unique value numeric returning function" in {
    val asIDup: Char => Int = _.toInt % 10
    val ranked = (rank(ztoa)(asIDup))
    // check returned returned rank values are less than or equal to successor
    val rValues = ranked.map{case (v, r) => r}
    (rValues.zip(rValues.tail).forall{case (a, b) => a <= b}).shouldBe(true)
  }

}
