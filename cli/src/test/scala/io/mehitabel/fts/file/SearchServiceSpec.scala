package io.mehitabel.fts

import io.mehitabel.fts.data._
import org.scalatest._
//import scala.concurrent.Future

// Consider making this an integration test
class SearchServiceSpec extends AsyncFlatSpec with Matchers {
  val resourceRoot = "/filetest"
  val dirname = getClass.getResource(resourceRoot).getPath

  // remove project specific resource file path from test resource
  def trimPrefix(rs: List[ResourceScore]) = {
    val fromRoot = s"${resourceRoot}.*".r
    rs.map{ r =>
      val trimmed = fromRoot.findFirstIn(r.resourceName)
      trimmed.fold(r)(t => r.copy(resourceName = t))
    }
  }

  val  searchService = new SearchService(dirname)

  val terms = "left real one spicy elephant"

  it must "find the matching terms in the list of files" in  {
    searchService.results(terms)
      .map { r =>
          trimPrefix(r).shouldBe(List(
            ResourceScore("/filetest/subd/3.txt",20.0),
            ResourceScore("/filetest/2.txt",20.0),
            ResourceScore("/filetest/1.txt",20.0)))
      }
  }
}
