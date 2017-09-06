package io.mehitabel.fts
package find

import io.mehitabel.fts.data._
import cats._
import cats.implicits._

trait Terms[F[_]] {
  def searchTerms: F[SearchTerms]
}

trait Finder[F[_]] {
  def index: F[SearchBody]

  def score(terms: SearchTerms): F[SearchScore]
}

/**
* Need separate type constructors for Applicative & Traverse as both extend map from Functor
* and need to disambiguate searchTerms map as scalac can't see that they are equivalent.
**/
class SearchAll[F[_] : Applicative, G[_]: Traverse](terms: Terms[F], finders: G[Finder[F]]) {
  def scores = terms.searchTerms.map{st =>
    finders.traverse(_.score(st))
  }
}
