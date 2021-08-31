package nl.gn0s1s.isbn

import munit.ScalaCheckSuite
import org.scalacheck.Prop._

class HyphenationSuite extends ScalaCheckSuite {
  property("can convert to a hyphenated isbn-13") {
    Isbn("9781617292774").map(_.toHyphenatedIsbn13).contains("978-1-61729-277-4")
  }

  property("can convert to an amazon-style hyphenated isbn-13") {
    Isbn("9781617292774").map(_.toAmazonStyleHyphenatedIsbn13).contains("978-1617292774")
  }

  property("can convert to a hyphenated isbn-10") {
    Isbn("9781617292774").flatMap(_.toHyphenatedIsbn10).contains("1-61729-277-X")
  }

  property("prevents conversion to a hyphenated isbn-10 if not possible") {
    Isbn("9798615656972").flatMap(_.toHyphenatedIsbn10).isEmpty
  }

  property("can break up an isbn-13 into parts") {
    val isbn = Isbn("9781617292774").get

    isbn.prefixElement == "978" &&
    isbn.registrationGroupElement == "1" &&
    isbn.registrantElement == "61729" &&
    isbn.publicationElement == "277" &&
    isbn.checkDigit == "4"
  }
}
