
package wordCount

import zio._
import zio.console._
import java.io.{BufferedReader, File, FileInputStream, InputStreamReader}

import zio._
import zio.console._
import scala.io.Source

object wordCountFolderAsZioEnv extends zio.App {
  
  
  def run(args: List[String]) =
    myAppLogic.exitCode

  val myAppLogic =
    for {
      _ <- putStrLn(
        "Hello! What folder path do you want to word count? please enter full folder path")
      fullPath <- getStrLn
      files <- getFolderFiles(fullPath)
      count <- ZIO.collectAllParN(3)(files.map(countPerFile(_)))
      _ <- putStrLn(s"found ${count.sum} in all files")
    } yield count.sum

  def getFolderFiles(path: String): Task[List[String]] =
    for {
      file <- ZIO.effect(new File(path))
    } yield file.listFiles().filter(_.isFile).map(_.getAbsolutePath).toList

  def countPerFile(path: String) =
    for {
      contents <- readFileAsString(path)
      count <- countWords(contents)
      _ <- putStrLn(s"found ${count} words")
    } yield count

  def countWords(str: String): UIO[Int] = UIO(str.split(" ").length)

  def readFileAsString(path: String): Task[String] = {
    var string = ""
    var strLine: String = null

    Task(new BufferedReader(new InputStreamReader(new FileInputStream(path))))
      .bracket(inputStream => UIO(println("closing")) *> UIO(inputStream.close)) {
        br =>
        {
          while ({
            ({ strLine = br.readLine; strLine }) != null
          }) { // Print the content on the console
            string += strLine
          }
          Task(string)
        }
      }
  }
}
