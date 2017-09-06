package io.mehitabel.fts
package data

object Rank {
  import Numeric.Implicits._

  def rank[A, B: Numeric](a: List[A])(f: A => B): List[Tuple2[A,B]] = {
    a.map{e => (e, f(e))}.sortBy{case (x, y) => y}
  }

  def rankInverse[A, B : Numeric](a: List[A])(f: A => B): List[Tuple2[A,B]] = {
    a.map{e => (e, f(e))}.sortBy{case (x, y) => -y}
  }

}
