package io.mehitabel.fts

import org.scalatest._
import org.scalacheck._
import Gen._
//Will need if can figure out generator rather than sample
//import Arbitrary.arbitrary

trait FTSGen {
  // restrict to ascii chars with some non alphanumeric
  val restrictChars = ('0' to 'z')
  val ws = Seq(' ', '\t')

  val restrictCharsGen: Gen[String] = Gen.chooseNum(1, 25).flatMap { n =>
    Gen.buildableOfN[String, Char](n, Gen.oneOf(restrictChars ++ ws))
  }

  val p: Gen[Char] = Gen.oneOf(punctuation.toSeq ++ ws)

  val lineGen = for {
    txt   <- listOfN(10, restrictCharsGen)
    delim <- listOfN(10, p)
  } yield txt.zip(delim).map{ case(w, e) => w + e}.mkString

}

class FTSSpec extends FlatSpec with Matchers{
  it must "sanitize input" in new FTSGen {
    // todo see if forAll can be used
    // forAll(lineGen) { w => w.flatMap {x: String =>
    //     val cleaned: String =  dropChar(x)
    //     val verify: String = cleaned.filter(useChar.contains(_))
    //     cleaned.shouldEqual(verify)
    //   }}
    // do this for the moment
    lineGen.sample.map {bar =>
      val cleaned = dropChar(bar)
      val verify: String = cleaned.filter(useChar.contains(_))
      cleaned.shouldEqual(verify)
    }
  }
}
