package io.mehitabel.fts
package file

import better.files._

/**
* get files from directory name
* To Do use something like apache tika to properly inspect content type
* to filter for text like data
**/
class DirectoryFiles(root: String) {

  private def files(dir: File): List[File] = dir.glob("*").toList

  def listFiles(dir: File) = {
    def go(dir: File, fList: List[File]): List[File] = {
      files(dir).foldLeft(fList) { (acc: List[File], f: File) =>
        if (f.isDirectory)
          go(f, acc)
        else if (f.name.endsWith(".txt")) // TODO don't limit to one extension type
          f :: acc
        else acc
      }
    }
    go(dir, List.empty[File])
  }

  val textFiles = listFiles(File(root))
}
