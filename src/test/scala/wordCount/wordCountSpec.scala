import zio.test.Assertion._
import zio.test._

object WordCountFolderSpec extends DefaultRunnableSpec {
  override def spec =
    suite("WordCountSpec")(
      test("count words properly") {
        assert(1)(equalTo(2))
      }
    )
}
