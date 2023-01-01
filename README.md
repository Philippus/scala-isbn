# Scala-isbn

[![build](https://github.com/Philippus/scala-isbn/workflows/build/badge.svg)](https://github.com/Philippus/scala-isbn/actions/workflows/scala.yml?query=workflow%3Abuild+branch%3Amain)
[![codecov](https://codecov.io/gh/Philippus/scala-isbn/branch/main/graph/badge.svg)](https://codecov.io/gh/Philippus/scala-isbn)
![Current Version](https://img.shields.io/badge/version-1.0.0-brightgreen.svg?style=flat "1.0.0")
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![license](https://img.shields.io/badge/license-MPL%202.0-blue.svg?style=flat "MPL 2.0")](LICENSE)

Scala-isbn is a library to work with ISBNs. An ISBN is an International Standard Book Number. ISBNs were 10 digits
in length up to the end of December 2006, but since 1 January 2007 they now always consist of 13 digits. ISBNs are
calculated using a specific mathematical formula and include a check digit to validate the number.
This library can convert ISBN-10s to ISBN-13s and vice versa if possible. It can also give the hyphenated versions of
the ISBN, following the [ISBN Ranges](https://www.isbn-international.org/range_file_generation). It can also split the
ISBN into its parts ('prefix element', 'registration group element', 'registrant element', 'publication element' and
'check digit'.)

Only valid SBN, ISBN-10 and ISBN-13 strings, with leniency towards spaces and hyphens, can be lifted into an ISBN
`case class`.

## Installation
Scala-isbn is published for Scala 2.13. To start using it add the following to your `build.sbt`:

```
libraryDependencies += "nl.gn0s1s" %% "scala-isbn" % "1.0.0"
```

## Example usage

```scala
import nl.gn0s1s.isbn._

val book = Isbn("9781617292774") // val book: Option[nl.gn0s1s.isbn.Isbn] = Some(Isbn(9781617292774))
val book = Isbn("abc") // val book: Option[nl.gn0s1s.isbn.Isbn] = None, 
val book = Isbn("161729277X") // val book: Option[nl.gn0s1s.isbn.Isbn] = Some(Isbn(9781617292774))

book.flatMap(_.toIsbn10) // val res1: Option[String] = Some(161729277X)
book.map(_.toIsbn13) // val res2: Option[String] = Some(9781617292774)
book.flatMap(_.toHyphenatedIsbn10) // val res3: Option[String] = Some(1-61729-277-X)
book.map(_.toHyphenatedIsbn13) // val res4: Option[String] = Some(978-1-61729-277-4)
book.map(_.toAmazonStyleHyphenatedIsbn13) // val res5: Option[String] = Some(978-1617292774)

book.map(_.prefixElement) // val res6: Option[String] = Some(978)
book.map(_.registrationGroupElement) // val res7: Option[String] = Some(1)
book.map(_.registrantElement) // val res8: Option[String] = Some(61729)
book.map(_.publicationElement) // val res9: Option[String] = Some(277)
book.map(_.checkDigit) // val res10: Option[String] = Some(4)
```

## Resources
- [International ISBN Agency](https://www.isbn-international.org/)
- [Wikipedia entry](https://en.wikipedia.org/wiki/International_Standard_Book_Number)

## License
The code is available under the [Mozilla Public License, version 2.0](LICENSE).
