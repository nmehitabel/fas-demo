package io.mehitabel.fts
package file

import org.scalatest._

class DirectoryFilesSpec extends FlatSpec with Matchers {

  it must "list .txt files in directory hierarchy" in {
    val path = getClass.getResource("/filetest").getPath
    val d = new DirectoryFiles(path)
    d.textFiles.toList.map(_.name).sorted.shouldBe(List("1.txt","2.txt", "3.txt"))
    //d.textFiles.toList.shouldBe(List("1.txt","2.txt", "3.txt"))
  }

}
