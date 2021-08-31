package nl.gn0s1s.isbn

import munit.ScalaCheckSuite
import org.scalacheck.Prop._

class IsbnSuite extends ScalaCheckSuite {
  property("accepts isbn-13s") {
    Isbn("9781617292774").isDefined
  }

  property("accepts isbn-10s") {
    Isbn("161729277X").isDefined
  }

  property("accepts sbns") {
    Isbn("340013818").isDefined
  }

  property("accepts hyphenated isbns") {
    Isbn("978-1-61729-277-4").isDefined
  }

  property("accepts amazon-style isbns") {
    Isbn("978-1617292774").isDefined
  }

  property("accepts spaces") {
    Isbn("978 1 61729 277 4").isDefined
  }

  property("considers an isbn-10 equivalent to its isbn-13") {
    Isbn("0999063502") == Isbn("9780999063507")
  }

  property("can convert an isbn-13 to an isbn-10") {
    Isbn("9780999063507").flatMap(_.toIsbn10).contains("0999063502")
  }

  property("can convert to an isbn-10 number ending in X") {
    Isbn("9781617292774").flatMap(_.toIsbn10).contains("161729277X")
  }

  property("prevents conversion to isbn-10 if not possible") {
    Isbn("9798615656972").flatMap(_.toIsbn10).isEmpty
  }

  property("can convert to an isbn-13 number ending in 0") {
    Isbn("1563892790").map(_.value).contains("9781563892790")
  }

  property("has a toIsbn13-method") {
    Isbn("9780999063507").map(_.toIsbn13).contains("9780999063507")
  }

  property("can validate isbn-13s") {
    val isbns = Seq("9780999063507", "9781617292774", "9781563894893", "9781563892790")

    isbns.forall(Isbn(_).isDefined)
  }

  property("can validate isbn-10s") {
    val isbns = Seq("0999063502", "161729277X", "1563894890", "1563892790")

    isbns.forall(Isbn(_).isDefined)
  }

  property("can invalidate isbn-13s") {
    val isbns = Seq("9780999063509", "9781617292779", "9781563894899", "9781563892799")

    isbns.forall(Isbn(_).isEmpty)
  }

  property("can invalidate isbn-10s") {
    val isbns = Seq("0999063509", "1617292779", "1563894899", "1563892799")

    isbns.forall(Isbn(_).isEmpty)
  }
}
